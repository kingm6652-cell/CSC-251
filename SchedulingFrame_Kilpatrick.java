import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Completes Kilpatrick's scheduling placeholder using Johnson's appointment model/manager. */
public class SchedulingFrame_Kilpatrick extends TablePanel_Kilpatrick {
    public SchedulingFrame_Kilpatrick(FarmController_Kilpatrick controller) {
        super(controller,"Service Scheduling","One service station. Times use a 24-hour clock; overlapping appointments are blocked.",
                "ID","Customer","Contact","Animal","Service","Date","Time","Minutes","Fee","Status");
        actions.add(Ui_Kilpatrick.button("Schedule service",this::schedule));
        actions.add(Ui_Kilpatrick.button("Reschedule",this::reschedule));
        actions.add(Ui_Kilpatrick.button("Complete",this::complete));
        actions.add(Ui_Kilpatrick.button("Cancel / refund",this::cancel));
        actions.add(Ui_Kilpatrick.button("Record service payment",()->Ui_Kilpatrick.payService(this,controller,Ui_Kilpatrick.selectedId(table))));
        refresh();
    }
    public void refresh() {
        Ui_Kilpatrick.model(table).setRowCount(0);
        for(ServiceAppointment_Johnson original:controller.data().services.getAllAppointments()) {
            Appointment_Kilpatrick a=(Appointment_Kilpatrick)original;
            Ui_Kilpatrick.model(table).addRow(new Object[]{a.getId(),a.getCustomerName(),a.getPhoneNumber(),a.getAnimalType(),
                    a.getServiceType(),a.getDate(),a.getTime(),a.getDurationMinutes(),Validation_Kilpatrick.money(a.getCostCents()),a.getStatus()});
        }
        summary.setText(controller.data().services.getUpcomingAppointments().size()+" scheduled | Unpaid service balances: "+
                Validation_Kilpatrick.money(controller.data().services.outstandingCents()));
    }
    private void schedule() {
        Ui_Kilpatrick.Form f=new Ui_Kilpatrick.Form();
        JTextField customer=f.text("Customer name","");
        JTextField phone=f.text("Phone/contact","");
        JTextField animal=f.text("Animal type / name","");
        JComboBox<ServiceType_Kilpatrick> service=new JComboBox<>(ServiceType_Kilpatrick.catalog());
        f.add("Service",service);
        JTextField date=f.text("Date (MM/DD/YYYY)",LocalDate.now().plusDays(1).format(Validation_Kilpatrick.DATE));
        JTextField time=f.text("Time (HH:mm, 24-hour)","09:00");
        JTextField duration=f.text("Duration (5-480 min)","15");
        JTextField fee=f.text("Fee ($)","15.00");
        service.addActionListener(e->{
            ServiceType_Kilpatrick s=(ServiceType_Kilpatrick)service.getSelectedItem();
            duration.setText(""+s.getMinutes());fee.setText(Validation_Kilpatrick.dollars(s.getPriceCents()));
        });
        if(!f.show(this,"Schedule animal-care service"))return;
        LocalDateTime start=LocalDateTime.of(Validation_Kilpatrick.date(date.getText()),Validation_Kilpatrick.time(time.getText()));
        int minutes=Validation_Kilpatrick.whole(duration.getText(),"Duration",5);
        long cents=Validation_Kilpatrick.cents(fee.getText());
        ServiceType_Kilpatrick chosen=(ServiceType_Kilpatrick)service.getSelectedItem();
        controller.commit(data->data.services.book(customer.getText(),phone.getText(),animal.getText(),chosen.getName(),start,minutes,cents));
    }
    private void reschedule() {
        int id=Ui_Kilpatrick.selectedId(table);
        Appointment_Kilpatrick a=controller.data().services.findById(id);
        if(!a.getStatus().equals("Scheduled"))throw new IllegalArgumentException("Only scheduled appointments can be moved.");
        Ui_Kilpatrick.Form f=new Ui_Kilpatrick.Form();
        JTextField date=f.text("Date (MM/DD/YYYY)",a.getDate());
        JTextField time=f.text("Time (HH:mm, 24-hour)",a.getTime());
        if(!f.show(this,"Reschedule appointment #"+id))return;
        LocalDateTime start=LocalDateTime.of(Validation_Kilpatrick.date(date.getText()),Validation_Kilpatrick.time(time.getText()));
        controller.commit(data->data.services.reschedule(id,start));
    }
    private void complete() {
        int id=Ui_Kilpatrick.selectedId(table);
        if(!controller.data().services.findById(id).getStatus().equals("Scheduled"))throw new IllegalArgumentException("Only scheduled appointments can be completed.");
        if(!Ui_Kilpatrick.confirm(this,"Mark appointment #"+id+" completed? This does not record a payment."))return;
        controller.commit(data->{
            if(!data.services.findById(id).markCompleted())throw new IllegalArgumentException("This appointment cannot be completed.");
        });
    }
    private void cancel() {
        int id=Ui_Kilpatrick.selectedId(table);
        Appointment_Kilpatrick a=controller.data().services.findById(id);
        if(!a.getStatus().equals("Scheduled"))throw new IllegalArgumentException("Only scheduled appointments can be cancelled.");
        String refundMethod="Cash";
        if(a.getPaidCents()>0) {
            Ui_Kilpatrick.Form f=new Ui_Kilpatrick.Form();
            f.add("Refund required",new JLabel(Validation_Kilpatrick.money(a.getPaidCents())));
            JComboBox<String> method=Ui_Kilpatrick.methods();f.add("Actual refund method",method);
            if(!f.show(this,"Confirm refund issued, then cancel #"+id))return;
            refundMethod=(String)method.getSelectedItem();
        } else if(!Ui_Kilpatrick.confirm(this,"Cancel appointment #"+id+"? No refund is due."))return;
        final String chosen=refundMethod;
        controller.commit(data->data.cancelService(id,chosen));
    }
}
