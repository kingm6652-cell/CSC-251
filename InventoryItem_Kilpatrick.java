/** Kilpatrick's product model, reworked with validation and stable saved IDs. */
public class InventoryItem_Kilpatrick {
    private final int id;
    private String name;
    private String category;
    private long priceCents;
    private int quantityOnHand;
    private int reorderLevel;

    public InventoryItem_Kilpatrick(int id, String name, String category, long priceCents, int quantity, int reorder) {
        if (id < 1) throw new IllegalArgumentException("Invalid product ID.");
        this.id = id;
        update(name, category, priceCents, quantity, reorder);
    }
    public void update(String name, String category, long price, int quantity, int reorder) {
        String validName = Validation_Kilpatrick.required(name, "Product name");
        String validCategory = Validation_Kilpatrick.required(category, "Category");
        Validation_Kilpatrick.cents(Validation_Kilpatrick.dollars(price));
        Validation_Kilpatrick.nonnegative(quantity, "Quantity");
        Validation_Kilpatrick.nonnegative(reorder, "Reorder level");
        this.name = validName;
        this.category = validCategory;
        this.priceCents = price;
        this.quantityOnHand = quantity;
        this.reorderLevel = reorder;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public long getPriceCents() { return priceCents; }
    public double getUnitPrice() { return priceCents / 100.0; }
    public int getQuantityOnHand() { return quantityOnHand; }
    public int getReorderLevel() { return reorderLevel; }
    public boolean isLowStock() { return quantityOnHand <= reorderLevel; }
    public boolean sell(int quantity) {
        if (quantity <= 0 || quantity > quantityOnHand) return false;
        quantityOnHand -= quantity;
        return true;
    }
    public void restock(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Restock quantity must be positive.");
        quantityOnHand = Validation_Kilpatrick.nonnegative(Math.addExact(quantityOnHand, quantity), "Stock");
    }
    @Override public String toString() {
        return "#" + id + "  " + name + "  (" + Validation_Kilpatrick.money(priceCents) + "; " + quantityOnHand + " available)";
    }
}
