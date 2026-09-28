import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Based on Kilpatrick's StoreSale; now reused for store, farm, and breeder sales. */
public class StoreSale_Kilpatrick {
    private final int id;
    private final String type;
    private final String customer;
    private final String method;
    private final LocalDateTime dateTime;
    private final List<SaleLineItem_Kilpatrick> lines;
    public StoreSale_Kilpatrick(int id, String type, String customer, String method,
            LocalDateTime dateTime, List<SaleLineItem_Kilpatrick> lines) {
        if (id < 1 || lines.isEmpty()) throw new IllegalArgumentException("Sale requires an ID and at least one item.");
        if (!type.equals("Store") && !type.equals("Farm animal") && !type.equals("Breeder"))
            throw new IllegalArgumentException("Invalid sale type.");
        this.id = id; this.type = type; this.customer = Validation_Kilpatrick.customer(customer);
        this.method = Validation_Kilpatrick.method(method); this.dateTime = dateTime;
        this.lines = new ArrayList<>(lines);
    }
    public int getId() { return id; }
    public String getType() { return type; }
    public String getCustomer() { return customer; }
    public String getMethod() { return method; }
    public LocalDateTime getSaleDateTime() { return dateTime; }
    public List<SaleLineItem_Kilpatrick> getLineItems() { return Collections.unmodifiableList(lines); }
    public long getTotalCents() {
        long total = 0;
        for (SaleLineItem_Kilpatrick line : lines) total = Math.addExact(total, line.getLineTotalCents());
        return total;
    }
    public String getReceipt() {
        StringBuilder text = new StringBuilder("Barnyard Supply & Services\n");
        text.append(type).append(" sale #").append(id).append("\n")
            .append(dateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")))
            .append("\nCustomer: ").append(customer).append("\n\n");
        for (SaleLineItem_Kilpatrick line : lines) text.append(line).append("\n");
        return text.append("\nTOTAL PAID: ").append(Validation_Kilpatrick.money(getTotalCents()))
                .append("\nPayment recorded: ").append(method)
                .append("\nTax is included in pricing\n").toString();
    }
}
