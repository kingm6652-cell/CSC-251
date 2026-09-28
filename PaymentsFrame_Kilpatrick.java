import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

/** Kilpatrick's payment screen now shares Johnson-backed appointments and the full ledger. */
public class PaymentsFrame_Kilpatrick extends JPanel {
    private final FarmController_Kilpatrick controller;
    private final JTable balances=Ui_Kilpatrick.table("ID","Customer","Service","Date","Time","Fee","Paid","Balance","Status");
    private final JTable ledger=Ui_Kilpatrick.table("Payment ID","Type","Reference ID","Customer","Amount","Method","Date/time");
    private final JLabel summary=new JLabel(" ");
    public PaymentsFrame_Kilpatrick(FarmController_Kilpatrick controller) {
        super(new BorderLayout(0,12));this.controller=controller;
        setBorder(new EmptyBorder(20,20,20,20));
        add(Ui_Kilpatrick.heading("Payments & Receipts","Track money received and refunds. This app records payments; it does not charge cards."),BorderLayout.NORTH);
        JTabbedPane tabs=new JTabbedPane();
        JPanel unpaid=new JPanel(new BorderLayout(0,10));
        unpaid.add(Ui_Kilpatrick.search(balances),BorderLayout.NORTH);unpaid.add(Ui_Kilpatrick.scroll(balances),BorderLayout.CENTER);
        JPanel payButtons=new JPanel(new FlowLayout(FlowLayout.LEFT));
        payButtons.add(Ui_Kilpatrick.button("Pay selected appointment",()->Ui_Kilpatrick.payService(this,controller,Ui_Kilpatrick.selectedId(balances))));
        payButtons.add(new JLabel("Only appointments with a balance are listed."));unpaid.add(payButtons,BorderLayout.SOUTH);
        tabs.addTab("Service balances",unpaid);
        JPanel history=new JPanel(new BorderLayout(0,10));
        history.add(Ui_Kilpatrick.search(ledger),BorderLayout.NORTH);history.add(Ui_Kilpatrick.scroll(ledger),BorderLayout.CENTER);
        JPanel historyButtons=new JPanel(new FlowLayout(FlowLayout.LEFT));
        historyButtons.add(Ui_Kilpatrick.button("View receipt",this::receipt));
        historyButtons.add(Ui_Kilpatrick.button("Export payments CSV",()->export(false)));
        historyButtons.add(Ui_Kilpatrick.button("Export sold items CSV",()->export(true)));
        history.add(historyButtons,BorderLayout.SOUTH);tabs.addTab("All payments / receipts",history);
        add(tabs,BorderLayout.CENTER);add(summary,BorderLayout.SOUTH);
        controller.onChange(this::refresh);refresh();
    }
    public void refresh() {
        Ui_Kilpatrick.model(balances).setRowCount(0);Ui_Kilpatrick.model(ledger).setRowCount(0);
        for(ServiceAppointment_Johnson original:controller.data().services.getAllAppointments()) {
            Appointment_Kilpatrick a=(Appointment_Kilpatrick)original;
            if(a.getBalanceCents()>0)Ui_Kilpatrick.model(balances).addRow(new Object[]{a.getId(),a.getCustomerName(),a.getServiceType(),a.getDate(),
                    a.getTime(),Validation_Kilpatrick.money(a.getCostCents()),Validation_Kilpatrick.money(a.getPaidCents()),
                    Validation_Kilpatrick.money(a.getBalanceCents()),a.getStatus()});
        }
        for(Payment_Kilpatrick p:controller.data().payments)Ui_Kilpatrick.model(ledger).addRow(new Object[]{p.id,p.type,p.referenceId,p.customer,
                Validation_Kilpatrick.money(p.amountCents),p.method,p.dateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"))});
        summary.setText("Net payments received: "+Validation_Kilpatrick.money(controller.data().totalCollected())+
                "   |   Service balances: "+Validation_Kilpatrick.money(controller.data().services.outstandingCents()));
    }
    private void receipt() {
        int id=Ui_Kilpatrick.selectedId(ledger);
        for(Payment_Kilpatrick p:controller.data().payments) if(p.id==id) {
            if(!p.type.startsWith("Service")) {
                for(StoreSale_Kilpatrick sale:controller.data().sales.getSalesHistory()) if(sale.getId()==p.referenceId) {
                    Ui_Kilpatrick.message(this,"Sale receipt",sale.getReceipt());return;
                }
            } else {
                Appointment_Kilpatrick a=controller.data().services.findById(p.referenceId);
                Ui_Kilpatrick.message(this,"Service payment receipt","Barnyard Supply & Services\nPayment #"+p.id+" | "+p.type+
                        "\nAppointment #"+p.referenceId+"\nCustomer: "+p.customer+"\nService: "+a.getServiceType()+
                        "\nAmount: "+Validation_Kilpatrick.money(p.amountCents)+"\nMethod: "+p.method+"\nRecorded: "+p.dateTime+
                        "\nCurrent appointment status: "+a.getStatus()+"\nCurrent balance: "+Validation_Kilpatrick.money(a.getBalanceCents()));return;
            }
        }
    }
    private void export(boolean items) {
        JFileChooser chooser=new JFileChooser();
        chooser.setSelectedFile(new java.io.File(items?"sold_items.csv":"payments.csv"));
        if(chooser.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)return;
        Path path=chooser.getSelectedFile().toPath().toAbsolutePath();
        if(!path.toString().toLowerCase(java.util.Locale.ROOT).endsWith(".csv"))path=path.resolveSibling(path.getFileName()+".csv");
        if(Files.exists(path)&&!Ui_Kilpatrick.confirm(this,"Replace the existing export " + path.getFileName()+"?"))return;
        try {
            if(items)ReportExporter_Kilpatrick.sales(path,controller.data());else ReportExporter_Kilpatrick.payments(path,controller.data());
            JOptionPane.showMessageDialog(this,"Report saved to:\n"+path);
        } catch(IOException ex){throw new IllegalArgumentException("Report could not be saved: "+ex.getMessage());}
    }
    public JTable getBalanceTable(){return balances;}
    public JTable getLedgerTable(){return ledger;}
}
