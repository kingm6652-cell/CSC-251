/** Kilpatrick's receipt line. Values are snapshots, unaffected by later price edits. */
public class SaleLineItem_Kilpatrick {
    private final int itemId;
    private final String itemName;
    private final int quantitySold;
    private final long unitPriceCents;
    public SaleLineItem_Kilpatrick(int id, String name, int qty, long price) {
        if (id < 1 || qty <= 0) throw new IllegalArgumentException("Invalid sale line.");
        Validation_Kilpatrick.cents(Validation_Kilpatrick.dollars(price));
        itemId = id; itemName = Validation_Kilpatrick.required(name, "Item name");
        quantitySold = qty; unitPriceCents = price;
    }
    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public int getQuantitySold() { return quantitySold; }
    public long getUnitPriceCents() { return unitPriceCents; }
    public long getLineTotalCents() { return Math.multiplyExact(unitPriceCents, quantitySold); }
    @Override public String toString() {
        return itemName + " x" + quantitySold + " @ " + Validation_Kilpatrick.money(unitPriceCents)
                + " = " + Validation_Kilpatrick.money(getLineTotalCents());
    }
}
