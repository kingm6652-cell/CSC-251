import java.time.LocalDateTime;

/** New integration record. Records payments received, not an online payment processor. */
public class Payment_Kilpatrick {
    public final int id;
    public final String type;
    public final int referenceId;
    public final String customer;
    public final long amountCents;
    public final String method;
    public final LocalDateTime dateTime;
    public Payment_Kilpatrick(int id, String type, int referenceId, String customer, long amount,
            String method, LocalDateTime dateTime) {
        if (id < 1 || referenceId < 1) throw new IllegalArgumentException("Invalid payment ID.");
        if (!type.equals("Store") && !type.equals("Farm animal") && !type.equals("Breeder")
                && !type.equals("Service") && !type.equals("Service refund"))
            throw new IllegalArgumentException("Invalid payment type.");
        if ((type.equals("Service refund") && amount >= 0) || (!type.equals("Service refund") && amount < 0))
            throw new IllegalArgumentException("Invalid payment amount.");
        this.id = id; this.type = type; this.referenceId = referenceId;
        this.customer = Validation_Kilpatrick.customer(customer); this.amountCents = amount;
        this.method = Validation_Kilpatrick.method(method); this.dateTime = dateTime;
    }
}
