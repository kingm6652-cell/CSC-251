import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** New integration dashboard: separates revenue, unpaid fees, and breeder gross margin. */
public class OverviewPanel_Kilpatrick extends JPanel {
    private final FarmController_Kilpatrick controller;
    private final JLabel[] values=new JLabel[6];
    private final JTextArea notes=new JTextArea();
    public OverviewPanel_Kilpatrick(FarmController_Kilpatrick controller) {
        super(new BorderLayout(0,18));this.controller=controller;
        setBorder(new EmptyBorder(22,22,22,22));
        add(Ui_Kilpatrick.heading("Overview of Stock and Sales","Inventory, sales, animal care, and local breeder resales."),BorderLayout.NORTH);
        JPanel center=new JPanel(new BorderLayout(0,20));
        JPanel cards=new JPanel(new GridLayout(2,3,14,14));
        String[] labels={"Net Payments Received","Store Sales","Farm-Animal Sales","Breeder Sales","Service Payments (net)","Unpaid Service Fees"};
        for(int i=0;i<values.length;i++) {
            JPanel card=new JPanel(new BorderLayout(0,10));
            card.setBackground(Color.WHITE);card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(216,224,214)),new EmptyBorder(18,18,18,18)));
            card.add(new JLabel(labels[i]),BorderLayout.NORTH);
            values[i]=new JLabel("$0.00");values[i].setFont(new Font(Font.SANS_SERIF,Font.BOLD,26));values[i].setForeground(Ui_Kilpatrick.GREEN);
            card.add(values[i],BorderLayout.CENTER);cards.add(card);
        }
        center.add(cards,BorderLayout.NORTH);
        notes.setEditable(false);notes.setLineWrap(true);notes.setWrapStyleWord(true);
        notes.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,15));notes.setMargin(new Insets(18,18,18,18));
        center.add(new JScrollPane(notes),BorderLayout.CENTER);add(center,BorderLayout.CENTER);
        JLabel footer=new JLabel("Changes save automatically");
        footer.setForeground(new Color(83,96,85));add(footer,BorderLayout.SOUTH);
        controller.onChange(this::refresh);refresh();
    }
    public void refresh() {
        FarmData_Kilpatrick d=controller.data();
        long[] numbers={d.totalCollected(),d.sales.total("Store"),d.sales.total("Farm animal"),d.sales.total("Breeder"),d.serviceCollected(),d.services.outstandingCents()};
        for(int i=0;i<values.length;i++)values[i].setText(Validation_Kilpatrick.money(numbers[i]));
        StringBuilder text=new StringBuilder("AT A GLANCE\n\n");
        text.append(d.inventory.getLowStockItems().size()).append(" low-stock product alerts\n")
            .append(d.services.getUpcomingAppointments().size()).append(" appointments still scheduled\n")
            .append(d.breeders.getAvailableAnimals().size()).append(" breeder animals available\n")
            .append("Sold breeder animals' gross margin: ").append(Validation_Kilpatrick.money(d.breeders.getProfitCents()))
            .append(" (sale price minus purchase cost; excludes other expenses)\n\n")
            .append("START HERE\n\n1. Add or update products in Inventory; use Store sales to checkout.\n")
            .append("2. Book animal-care appointments in Services; record partial or full payments in Payments.\n")
            .append("3. Manage farm-raised animals and specialty breeder animals in their separate tabs.\n")
            .append("4. Reopen receipts or export payment and item-sale reports in Payments.\n\n");
        notes.setText(text.toString());notes.setCaretPosition(0);
    }
}
