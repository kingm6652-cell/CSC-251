import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.regex.Pattern;

/** Small reusable Swing helpers so the section classes stay readable. */
public final class Ui_Kilpatrick {
    public static final Color GREEN = new Color(32, 73, 54);
    public static final Color PAPER = new Color(245, 247, 242);
    private Ui_Kilpatrick() { }
    public static JButton button(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setName(text);
        button.addActionListener(e -> {
            try { action.run(); }
            catch (IllegalArgumentException | ArithmeticException ex) { error(button, ex.getMessage()); }
        });
        return button;
    }
    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Please check", JOptionPane.WARNING_MESSAGE);
    }
    public static void message(Component parent, String title, String text) {
        JTextArea area = new JTextArea(text, 16, 65);
        area.setEditable(false); area.setLineWrap(true); area.setWrapStyleWord(true);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setCaretPosition(0);
        JOptionPane.showMessageDialog(parent, new JScrollPane(area), title, JOptionPane.INFORMATION_MESSAGE);
    }
    public static boolean confirm(Component parent, String text) {
        return JOptionPane.showConfirmDialog(parent, text, "Confirm", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
    }
    public static JComboBox<String> methods() { return new JComboBox<>(new String[]{"Cash", "Card", "Check"}); }
    public static JTable table(String... columns) {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
            @Override public Class<?> getColumnClass(int col) {
                return getRowCount() == 0 || getValueAt(0,col) == null ? Object.class : getValueAt(0,col).getClass();
            }
        };
        JTable table = new JTable(model) {
            @Override public String getToolTipText(java.awt.event.MouseEvent event) {
                int row=rowAtPoint(event.getPoint()), col=columnAtPoint(event.getPoint());
                return row < 0 || col < 0 ? null : String.valueOf(getValueAt(row,col));
            }
        };
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(30);
        table.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        table.setSelectionBackground(new Color(213, 231, 219));
        table.setSelectionForeground(Color.BLACK);
        DefaultTableCellRenderer cells=new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t,Object value,boolean selected,boolean focus,int row,int col) {
                super.getTableCellRendererComponent(t,value,selected,focus,row,col);
                setBorder(new EmptyBorder(0,8,0,8));
                String text=String.valueOf(value);
                setHorizontalAlignment(value instanceof Number || text.startsWith("$") || text.startsWith("-$") ? SwingConstants.RIGHT : SwingConstants.LEFT);
                if(!selected)setBackground(row%2==0?Color.WHITE:new Color(245,248,244));
                return this;
            }
        };
        table.setDefaultRenderer(Object.class,cells);
        table.setDefaultRenderer(Number.class,cells);
        for (int i=0;i<columns.length;i++) {
            String name=columns[i];
            int width=120;
            if(name.equals("ID")||name.endsWith(" ID"))width=65;
            if(name.equals("Product")||name.equals("Service")||name.equals("Item"))width=205;
            if(name.equals("Customer")||name.equals("Contact")||name.equals("Breed"))width=150;
            if(name.equals("Minutes")||name.equals("Time")||name.equals("Fee")||name.equals("Paid")||name.equals("Balance"))width=85;
            table.getColumnModel().getColumn(i).setPreferredWidth(width);
            table.getColumnModel().getColumn(i).setMinWidth(40);
        }
        return table;
    }
    public static JScrollPane scroll(JTable table) {
        JScrollPane pane=new JScrollPane(table);
        // Set explicitly as well as Swing's normal addNotify behavior, for off-screen rendering.
        pane.setColumnHeaderView(table.getTableHeader());
        return pane;
    }
    public static DefaultTableModel model(JTable table) { return (DefaultTableModel)table.getModel(); }
    public static int selectedId(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) throw new IllegalArgumentException("Select a row in the table first.");
        return ((Number)table.getModel().getValueAt(table.convertRowIndexToModel(row), 0)).intValue();
    }
    public static JPanel search(JTable table) {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        JTextField field = new JTextField(); field.setName("Search");
        panel.add(new JLabel("Search:"), BorderLayout.WEST); panel.add(field, BorderLayout.CENTER);
        field.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                @SuppressWarnings("unchecked") TableRowSorter<DefaultTableModel> sorter =
                        (TableRowSorter<DefaultTableModel>)table.getRowSorter();
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(field.getText().trim())));
            }
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
        });
        return panel;
    }
    public static JPanel heading(String title, String subtitle) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 5));
        JLabel heading = new JLabel(title); heading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 23));
        heading.setForeground(GREEN); panel.add(heading); panel.add(new JLabel(subtitle));
        panel.setBorder(new EmptyBorder(0, 0, 12, 0));
        return panel;
    }
    public static class Form {
        private final JPanel panel = new JPanel(new GridLayout(0,2,10,9));
        public JTextField text(String label, String value) {
            JTextField field = new JTextField(value, 23); add(label, field); return field;
        }
        public void add(String label, JComponent field) {
            JLabel text = new JLabel(label); text.setLabelFor(field);
            field.setName(label); panel.add(text); panel.add(field);
        }
        public boolean show(Component parent, String title) {
            return JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION;
        }
    }
    public static void payService(Component parent, FarmController_Kilpatrick controller, int id) {
        Appointment_Kilpatrick a = controller.data().services.findById(id);
        if (a == null || a.getBalanceCents() <= 0) throw new IllegalArgumentException("This appointment has no outstanding balance.");
        Form form = new Form();
        form.add("Appointment", new JLabel("#" + id + " - " + a.getCustomerName()));
        JTextField amount = form.text("Amount received ($)", Validation_Kilpatrick.dollars(a.getBalanceCents()));
        JComboBox<String> method = methods(); form.add("Payment method", method);
        if (!form.show(parent, "Record service payment")) return;
        long cents = Validation_Kilpatrick.cents(amount.getText());
        controller.commit(data -> data.payService(id, cents, (String)method.getSelectedItem()));
    }
}
