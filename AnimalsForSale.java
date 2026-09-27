import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class AnimalsForSale {
    private final List<Animal> animals = new ArrayList<>();

    public static void main(String[] args) {
        AnimalsForSale store = new AnimalsForSale();
        store.loadAnimals();
        SwingUtilities.invokeLater(store::showWindow);
    }

    private void loadAnimals() {
        animals.add(new Animal("AN-1001", "Daisy", "Dairy Cow", 3, 1850.00, true));
        animals.add(new Animal("AN-1002", "Buster", "Beef Calf", 1, 975.00, true));
        animals.add(new Animal("AN-1003", "Mabel", "Goat", 2, 325.00, true));
        animals.add(new Animal("AN-1004", "Copper", "Quarter Horse", 6, 4200.00, true));
        animals.add(new Animal("AN-1005", "Penny", "Mini Pig", 1, 275.00, false));
        animals.add(new Animal("AN-1006", "Ranger", "Border Collie", 4, 850.00, true));
        animals.add(new Animal("AN-1007", "Rosie", "Sheep", 2, 460.00, true));
        animals.add(new Animal("AN-1008", "Sunny", "Chicken", 1, 18.50, true));
        animals.add(new Animal("AN-1009", "Pebble", "Duck", 1, 24.75, false));
        animals.add(new Animal("AN-1010", "Scout", "Llama", 3, 1250.00, true));
        animals.add(new Animal("AN-1011", "Waffles", "Rabbit", 1, 95.00, true));
        animals.add(new Animal("AN-1012", "Jasper", "Alpaca", 2, 1600.00, true));
    }

    private void showWindow() {
        JFrame frame = new JFrame("Animals for Sale - Tractor Supply Store");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(980, 620);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(255, 249, 240));

        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(new Color(255, 201, 168));
        bannerPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel titleLabel = new JLabel("Animal for Sale");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(93, 53, 31));
        bannerPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel subtitleLabel = new JLabel("Warm hearts. Happy homes. Animals ready for a second chance.");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(74, 46, 33));
        bannerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        JTextField searchField = new JTextField(20);
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(217, 146, 111), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));

        JComboBox<String> typeFilter = new JComboBox<>(new String[] {
                "All Types", "Dairy Cow", "Beef Calf", "Goat", "Quarter Horse", "Mini Pig",
                "Border Collie", "Sheep", "Chicken", "Duck", "Llama", "Rabbit", "Alpaca"
        });
        typeFilter.setBackground(Color.WHITE);
        typeFilter.setForeground(new Color(70, 52, 39));

        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[] {"ID", "Name", "Type", "Age", "Price", "Availability"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.setGridColor(new Color(238, 209, 179));
        table.setSelectionBackground(new Color(255, 196, 160));
        table.setSelectionForeground(new Color(70, 52, 39));
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setBackground(new Color(162, 92, 62));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 5) {
                    String status = value.toString();
                    if ("Available".equals(status)) {
                        c.setBackground(new Color(214, 245, 214));
                        c.setForeground(new Color(18, 96, 42));
                    } else {
                        c.setBackground(new Color(255, 224, 224));
                        c.setForeground(new Color(121, 24, 24));
                    }
                } else {
                    c.setBackground(isSelected ? new Color(255, 196, 160) : Color.WHITE);
                    c.setForeground(new Color(50, 39, 29));
                }
                return c;
            }
        });

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(249, 229, 210));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        filterPanel.add(new JLabel("Search: "));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Animal type: "));
        filterPanel.add(typeFilter);

        JPanel featuredPanel = new JPanel(new GridLayout(1, 3, 12, 12));
        featuredPanel.setBackground(new Color(255, 249, 240));
        featuredPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        for (String id : new String[] {"AN-1001", "AN-1004", "AN-1010"}) {
            Animal animal = findAnimalById(id);
            if (animal != null) {
                featuredPanel.add(createFeaturedPetCard(animal));
            }
        }

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 249, 240));
        topPanel.add(bannerPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.SOUTH);

        JButton detailsButton = new JButton("View Details");
        detailsButton.setBackground(new Color(110, 68, 42));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        detailsButton.addActionListener(event -> showAnimalDetails(frame, table));

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionsPanel.setBackground(new Color(255, 249, 240));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));
        actionsPanel.add(detailsButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(featuredPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        scrollPane.getViewport().setBackground(Color.WHITE);
        frame.add(scrollPane, BorderLayout.SOUTH);
        frame.add(actionsPanel, BorderLayout.EAST);

        Runnable refreshTable = () -> refreshTable(
                tableModel, searchField.getText(), typeFilter.getSelectedItem().toString());
        searchField.addActionListener(event -> refreshTable.run());
        typeFilter.addActionListener(event -> refreshTable.run());

        refreshTable.run();
        frame.setVisible(true);
    }

    private Animal findAnimalById(String id) {
        for (Animal animal : animals) {
            if (animal.getId().equals(id)) {
                return animal;
            }
        }
        return null;
    }

    private JPanel createFeaturedPetCard(Animal animal) {
        JPanel card = new JPanel(new BorderLayout(6, 6));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(232, 190, 152), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel nameLabel = new JLabel(animal.getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setForeground(new Color(93, 53, 31));

        JLabel typeLabel = new JLabel(animal.getType());
        typeLabel.setForeground(new Color(109, 84, 71));

        JLabel badgeLabel = new JLabel(animal.isAvailable() ? "Available" : "Recently Reserved");
        badgeLabel.setOpaque(true);
        badgeLabel.setBackground(animal.isAvailable() ? new Color(213, 243, 214) : new Color(255, 220, 220));
        badgeLabel.setForeground(animal.isAvailable() ? new Color(12, 95, 40) : new Color(123, 31, 31));
        badgeLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        card.add(nameLabel, BorderLayout.NORTH);
        card.add(typeLabel, BorderLayout.CENTER);
        card.add(badgeLabel, BorderLayout.SOUTH);
        return card;
    }

    private void showAnimalDetails(JFrame parent, JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(parent, "Select an animal first to view details.",
                    "No Animal Selected", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String id = table.getValueAt(selectedRow, 0).toString();
        Animal selected = findAnimalById(id);
        if (selected == null) {
            return;
        }

        JFrame detailsFrame = new JFrame("Adoption Details - " + selected.getName());
        detailsFrame.setSize(420, 260);
        detailsFrame.setLocationRelativeTo(parent);
        detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(new Color(255, 249, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel(selected.getName() + " is looking for a home");
        heading.setFont(new Font("SansSerif", Font.BOLD, 22));
        heading.setForeground(new Color(93, 53, 31));

        JLabel info = new JLabel(
                "<html><b>ID:</b> " + selected.getId() + "<br>"
                        + "<b>Type:</b> " + selected.getType() + "<br>"
                        + "<b>Age:</b> " + selected.getAge() + " year(s)<br>"
                        + "<b>Price:</b> $" + String.format("%.2f", selected.getPrice()) + "<br>"
                        + "<b>Status:</b> " + (selected.isAvailable() ? "Available for adoption" : "Pending adoption")
                        + "</html>");
        info.setForeground(new Color(74, 46, 33));

        JLabel statusBadge = new JLabel(selected.isAvailable() ? "Available" : "Reserved");
        statusBadge.setOpaque(true);
        statusBadge.setBackground(selected.isAvailable() ? new Color(213, 243, 214) : new Color(255, 220, 220));
        statusBadge.setForeground(selected.isAvailable() ? new Color(12, 95, 40) : new Color(123, 31, 31));
        statusBadge.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        panel.add(heading, BorderLayout.NORTH);
        panel.add(info, BorderLayout.CENTER);
        panel.add(statusBadge, BorderLayout.SOUTH);
        detailsFrame.add(panel);
        detailsFrame.setVisible(true);
    }

    private void refreshTable(DefaultTableModel tableModel, String searchText, String selectedType) {
        String searchTerm = searchText.trim().toLowerCase();
        tableModel.setRowCount(0);

        for (Animal animal : animals) {
            boolean matchesSearch = animal.matches(searchTerm);
            boolean matchesType = selectedType.equals("All Types") || animal.getType().equals(selectedType);
            if (matchesSearch && matchesType) {
                tableModel.addRow(new Object[] {
                        animal.getId(), animal.getName(), animal.getType(),
                        animal.getAge() + " year(s)", String.format("$%.2f", animal.getPrice()),
                        animal.isAvailable() ? "Available" : "Sold"
                });
            }
        }
    }

    private static class Animal {
        private final String id;
        private final String name;
        private final String type;
        private final int age;
        private final double price;
        private final boolean available;

        Animal(String id, String name, String type, int age, double price, boolean available) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.age = age;
            this.price = price;
            this.available = available;
        }

        boolean matches(String searchTerm) {
            return id.toLowerCase().contains(searchTerm)
                    || name.toLowerCase().contains(searchTerm)
                    || type.toLowerCase().contains(searchTerm);
        }

        String getId() {
            return id;
        }

        String getName() {
            return name;
        }

        String getType() {
            return type;
        }

        int getAge() {
            return age;
        }

        double getPrice() {
            return price;
        }

        boolean isAvailable() {
            return available;
        }
    }
}