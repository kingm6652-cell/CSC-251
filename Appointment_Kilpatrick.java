import java.time.LocalDateTime;

/** Inherits Johnson's customer/service/status model; adds ID, duration, and partial payments. */
public class Appointment_Kilpatrick extends ServiceAppointment_Johnson {
    private final int id;
    private LocalDateTime start;
    private final int durationMinutes;
    private final long costCents;
    private long paidCents;
    public Appointment_Kilpatrick(int id, String customer, String phone, String animal, String service,
            LocalDateTime start, int duration, long cost) {
        super(Validation_Kilpatrick.required(customer, "Customer name"),
              Validation_Kilpatrick.required(phone, "Phone/contact"),
              Validation_Kilpatrick.required(animal, "Animal type"),
              Validation_Kilpatrick.required(service, "Service"),
              start.format(Validation_Kilpatrick.DATE), start.format(Validation_Kilpatrick.TIME), cost / 100.0);
        if (id < 1 || duration < 5 || duration > 480) throw new IllegalArgumentException("Duration must be 5 to 480 minutes.");
        Validation_Kilpatrick.cents(Validation_Kilpatrick.dollars(cost));
        this.id = id; this.start = start; this.durationMinutes = duration; this.costCents = cost;
    }
    public int getId() { return id; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return start.plusMinutes(durationMinutes); }
    public int getDurationMinutes() { return durationMinutes; }
    public long getCostCents() { return costCents; }
    public long getPaidCents() { return paidCents; }
    public long getBalanceCents() { return getStatus().equals("Cancelled") ? 0 : costCents - paidCents; }
    @Override public String getDate() { return start.format(Validation_Kilpatrick.DATE); }
    @Override public String getTime() { return start.format(Validation_Kilpatrick.TIME); }
    @Override public boolean isPaid() { return !getStatus().equals("Cancelled") && paidCents == costCents; }
    public void receive(long amount) {
        if (getStatus().equals("Cancelled")) throw new IllegalArgumentException("A cancelled appointment cannot receive payment.");
        if (amount <= 0 || amount > getBalanceCents()) throw new IllegalArgumentException("Payment must be positive and no more than the outstanding balance.");
        paidCents += amount;
        if (paidCents == costCents) super.markPaid();
    }
    @Override public boolean markPaid() {
        if (isPaid() || getStatus().equals("Cancelled")) return false;
        receive(getBalanceCents());
        return true;
    }
    @Override public boolean markCompleted() {
        if (!getStatus().equals("Scheduled")) return false;
        return super.markCompleted();
    }
    @Override public boolean markCancelled() {
        if (!getStatus().equals("Scheduled") || paidCents > 0) return false;
        return super.markCancelled();
    }
    void refundForCancellation() {
        if (!getStatus().equals("Scheduled")) throw new IllegalArgumentException("Only scheduled appointments can be cancelled.");
        paidCents = 0;
    }
    void reschedule(LocalDateTime value) {
        if (!getStatus().equals("Scheduled")) throw new IllegalArgumentException("Only scheduled appointments can be moved.");
        start = value;
    }
    @Override public String toString() {
        return "#" + id + " " + getCustomerName() + " | " + getServiceType() + " | " + getDate() + " " + getTime()
                + " | balance " + Validation_Kilpatrick.money(getBalanceCents());
    }
}
