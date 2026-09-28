import java.util.ArrayList;

/**
 * ServiceManager.java
 *
 * Manages the FULL LIST of ServiceAppointments. Handles scheduling,
 * cancelling, completing, marking payments, and simple totals.
 *
 * The Frame (GUI) class calls these methods instead of touching the
 * ArrayList directly.
 */
public class ServiceManager {

    private ArrayList<ServiceAppointment> appointments;

    public ServiceManager() {
        appointments = new ArrayList<>();
    }

    // ----- Add -----
    public void scheduleAppointment(ServiceAppointment appt) {
        appointments.add(appt);
    }

    // ----- Cancel -----
    public String cancelAppointment(int index) {
        if (index < 0 || index >= appointments.size()) {
            return "Please select a valid appointment first.";
        }
        ServiceAppointment appt = appointments.get(index);
        boolean success = appt.markCancelled();
        if (success) {
            return "Appointment for " + appt.getCustomerName() + " has been cancelled.";
        } else {
            return "That appointment is already completed and can't be cancelled.";
        }
    }

    // ----- Complete -----
    public String completeAppointment(int index) {
        if (index < 0 || index >= appointments.size()) {
            return "Please select a valid appointment first.";
        }
        ServiceAppointment appt = appointments.get(index);
        boolean success = appt.markCompleted();
        if (success) {
            return "Appointment for " + appt.getCustomerName() + " marked as completed.";
        } else {
            return "That appointment was cancelled and can't be completed.";
        }
    }

    // ----- Payment -----
    public String markPaid(int index) {
        if (index < 0 || index >= appointments.size()) {
            return "Please select a valid appointment first.";
        }
        ServiceAppointment appt = appointments.get(index);
        boolean success = appt.markPaid();
        if (success) {
            return "Payment of $" + String.format("%.2f", appt.getCost()) + " recorded for "
                    + appt.getCustomerName() + ".";
        } else {
            return appt.getCustomerName() + " has already paid.";
        }
    }

    // ----- Getters used by the GUI -----
    public ArrayList<ServiceAppointment> getAllAppointments() {
        return appointments;
    }

    /**
     * Only appointments that are still "Scheduled" (not done, not cancelled).
     */
    public ArrayList<ServiceAppointment> getUpcomingAppointments() {
        ArrayList<ServiceAppointment> upcoming = new ArrayList<>();
        for (ServiceAppointment appt : appointments) {
            if (appt.getStatus().equals("Scheduled")) {
                upcoming.add(appt);
            }
        }
        return upcoming;
    }

    /**
     * Adds up money collected from appointments that have been paid.
     */
    public double getTotalRevenueCollected() {
        double total = 0.0;
        for (ServiceAppointment appt : appointments) {
            if (appt.isPaid()) {
                total += appt.getCost();
            }
        }
        return total;
    }

    /**
     * Adds up money we're still owed (completed or scheduled, but unpaid).
     */
    public double getOutstandingBalance() {
        double total = 0.0;
        for (ServiceAppointment appt : appointments) {
            if (!appt.isPaid() && !appt.getStatus().equals("Cancelled")) {
                total += appt.getCost();
            }
        }
        return total;
    }
}
