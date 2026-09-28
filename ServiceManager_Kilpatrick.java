import java.time.LocalDateTime;

/** Extension of Johnson's manager for the shared app. One service station is assumed. */
public class ServiceManager_Kilpatrick extends ServiceManager_Johnson {
    public Appointment_Kilpatrick findById(int id) {
        for (ServiceAppointment_Johnson appt : getAllAppointments()) {
            Appointment_Kilpatrick a = (Appointment_Kilpatrick)appt;
            if (a.getId() == id) return a;
        }
        return null;
    }
    public Appointment_Kilpatrick book(String name, String phone, String animal, String service,
            LocalDateTime start, int duration, long cost) {
        if (!start.isAfter(LocalDateTime.now())) throw new IllegalArgumentException("Choose a future appointment date and time.");
        int size = getAllAppointments().size();
        int id = size == 0 ? 1 : ((Appointment_Kilpatrick)getAllAppointments().get(size - 1)).getId() + 1;
        Appointment_Kilpatrick a = new Appointment_Kilpatrick(id, name, phone, animal, service, start, duration, cost);
        scheduleAppointment(a);
        return a;
    }
    @Override public void scheduleAppointment(ServiceAppointment_Johnson appt) {
        if (!(appt instanceof Appointment_Kilpatrick)) throw new IllegalArgumentException("An appointment ID is required.");
        Appointment_Kilpatrick a = (Appointment_Kilpatrick)appt;
        if (findById(a.getId()) != null) throw new IllegalArgumentException("Duplicate appointment ID.");
        if (!getAllAppointments().isEmpty() && a.getId() <= ((Appointment_Kilpatrick)getAllAppointments().get(getAllAppointments().size()-1)).getId())
            throw new IllegalArgumentException("Appointment IDs must be in order.");
        if (!a.getStatus().equals("Cancelled")) checkSlot(a.getStart(), a.getDurationMinutes(), a.getId());
        super.scheduleAppointment(appt);
    }
    public void checkSlot(LocalDateTime start, int duration, int ignoredId) {
        if (duration < 5 || duration > 480) throw new IllegalArgumentException("Duration must be 5 to 480 minutes.");
        LocalDateTime end = start.plusMinutes(duration);
        for (ServiceAppointment_Johnson appt : getAllAppointments()) {
            Appointment_Kilpatrick a = (Appointment_Kilpatrick)appt;
            if (a.getId() != ignoredId && !a.getStatus().equals("Cancelled")
                    && start.isBefore(a.getEnd()) && end.isAfter(a.getStart()))
                throw new IllegalArgumentException("This time overlaps appointment #" + a.getId() + ". Choose another time.");
        }
    }
    public void reschedule(int id, LocalDateTime start) {
        Appointment_Kilpatrick a = findById(id);
        if (a == null) throw new IllegalArgumentException("Select an appointment.");
        if (!start.isAfter(LocalDateTime.now())) throw new IllegalArgumentException("Choose a future date and time.");
        checkSlot(start, a.getDurationMinutes(), id);
        a.reschedule(start);
    }
    public long outstandingCents() {
        long total = 0;
        for (ServiceAppointment_Johnson appt : getAllAppointments()) total += ((Appointment_Kilpatrick)appt).getBalanceCents();
        return total;
    }
    @Override public double getOutstandingBalance() { return outstandingCents() / 100.0; }
    @Override public double getTotalRevenueCollected() {
        long total = 0;
        for (ServiceAppointment_Johnson appt : getAllAppointments()) total += ((Appointment_Kilpatrick)appt).getPaidCents();
        return total / 100.0;
    }
}
