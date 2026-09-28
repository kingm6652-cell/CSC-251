import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * ServiceFrame_Johnson.java
 *
 * GUI window for the "Service Scheduling" section. Lets the user:
 *   - schedule a new appointment
 *   - view all appointments
 *   - mark an appointment completed or cancelled
 *   - mark an appointment as paid
 *   - check total revenue collected and outstanding balance
 *
 * All data/logic lives in ServiceManager_Johnson - this class just connects
 * the buttons/fields to it.
 */
public class ServiceFrame_Johnson extends JFrame {

    private ServiceManager_Johnson manager;

    private DefaultListModel<ServiceAppointment_Johnson> listModel;
    private JList<ServiceAppointment_Johnson> appointmentList;

    private JTextField nameField;
    private JTextField phoneField;
    private JTextField animalTypeField;
    private JTextField serviceTypeField;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField costField;

    public ServiceFrame_Johnson() {
        manager = new ServiceManager_Johnson();

        setTitle("Service Scheduling");
        setSize(650, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ----- Top: form for scheduling a new appointment -----
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Schedule Appointment"));

        nameField = new JTextField();
        phoneField = new JTextField();
        animalTypeField = new JTextField();
        serviceTypeField = new JTextField();
        dateField = new JTextField();
        timeField = new JTextField();
        costField = new JTextField();

        formPanel.add(new JLabel("Customer Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Animal Type:"));
        formPanel.add(animalTypeField);
        formPanel.add(new JLabel("Service Type (e.g. Nail Trim):"));
        formPanel.add(serviceTypeField);
        formPanel.add(new JLabel("Date (MM/DD/YYYY):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Time (e.g. 2:00 PM):"));
        formPanel.add(timeField);
        formPanel.add(new JLabel("Cost ($):"));
        formPanel.add(costField);

        // ----- Middle: list of all appointments -----
        listModel = new DefaultListModel<>();
        appointmentList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(appointmentList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Appointments"));

        // ----- Bottom: buttons -----
        JButton scheduleButton = new JButton("Schedule");
        JButton completeButton = new JButton("Mark Completed");
        JButton cancelButton = new JButton("Cancel");
        JButton paidButton = new JButton("Mark Paid");
        JButton summaryButton = new JButton("Payment Summary");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(scheduleButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(paidButton);
        buttonPanel.add(summaryButton);

        // ----- Hook up button actions -----
        scheduleButton.addActionListener(this::handleSchedule);
        completeButton.addActionListener(e -> handleStatusChange(manager::completeAppointment));
        cancelButton.addActionListener(e -> handleStatusChange(manager::cancelAppointment));
        paidButton.addActionListener(e -> handleStatusChange(manager::markPaid));
        summaryButton.addActionListener(this::handleSummary);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Reads the form, validates it, and schedules a new appointment.
     */
    private void handleSchedule(ActionEvent e) {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String animalType = animalTypeField.getText().trim();
        String serviceType = serviceTypeField.getText().trim();
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();

        // Decision structure: required fields can't be blank
        if (name.isEmpty() || animalType.isEmpty() || serviceType.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Customer Name, Animal Type, Service Type, and Date are required.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Decision structure: cost has to be a real number
        double cost;
        try {
            cost = Double.parseDouble(costField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Cost must be a valid number (example: 35.00).",
                    "Invalid Number", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ServiceAppointment_Johnson appt = new ServiceAppointment_Johnson(name, phone, animalType, serviceType, date, time, cost);
        manager.scheduleAppointment(appt);
        refreshList();
        clearForm();

        JOptionPane.showMessageDialog(this, "Appointment scheduled for " + name + "!");
    }

    /**
     * Shared helper for Complete/Cancel/Mark Paid buttons - they all work
     * the same way: get the selected index, call a manager method on it,
     * and show whatever message comes back.
     */
    private interface AppointmentAction {
        String apply(int index);
    }

    private void handleStatusChange(AppointmentAction action) {
        int index = appointmentList.getSelectedIndex();
        String message = action.apply(index);
        JOptionPane.showMessageDialog(this, message);
        refreshList();
    }

    /**
     * Shows total revenue collected vs. what's still outstanding.
     */
    private void handleSummary(ActionEvent e) {
        double collected = manager.getTotalRevenueCollected();
        double outstanding = manager.getOutstandingBalance();

        String summary = String.format(
                "Total Revenue Collected: $%.2f%nOutstanding Balance: $%.2f",
                collected, outstanding);

        JOptionPane.showMessageDialog(this, summary, "Payment Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshList() {
        listModel.clear();
        for (ServiceAppointment_Johnson appt : manager.getAllAppointments()) {
            listModel.addElement(appt);
        }
    }

    private void clearForm() {
        nameField.setText("");
        phoneField.setText("");
        animalTypeField.setText("");
        serviceTypeField.setText("");
        dateField.setText("");
        timeField.setText("");
        costField.setText("");
    }

    // Lets you run/test just this window by itself while building it
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServiceFrame_Johnson frame = new ServiceFrame_Johnson();
            frame.setVisible(true);
        });
    }
}
