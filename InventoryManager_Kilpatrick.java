import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Shared inventory, based on Kilpatrick's submitted InventoryManager. */
public class InventoryManager_Kilpatrick {
    private final List<InventoryItem_Kilpatrick> items = new ArrayList<>();
    public List<InventoryItem_Kilpatrick> getAllItems() { return Collections.unmodifiableList(items); }
    public InventoryItem_Kilpatrick addItem(String name, String category, long price, int qty, int reorder) {
        int id = items.isEmpty() ? 1 : items.get(items.size() - 1).getId() + 1;
        InventoryItem_Kilpatrick item = new InventoryItem_Kilpatrick(id, name, category, price, qty, reorder);
        items.add(item);
        return item;
    }
    void restore(InventoryItem_Kilpatrick item) {
        if (findById(item.getId()) != null) throw new IllegalArgumentException("Duplicate product ID.");
        if (!items.isEmpty() && item.getId() <= items.get(items.size()-1).getId())
            throw new IllegalArgumentException("Product IDs must be in order.");
        items.add(item);
    }
    public InventoryItem_Kilpatrick findById(int id) {
        for (InventoryItem_Kilpatrick item : items) if (item.getId() == id) return item;
        return null;
    }
    public List<InventoryItem_Kilpatrick> getLowStockItems() {
        List<InventoryItem_Kilpatrick> low = new ArrayList<>();
        for (InventoryItem_Kilpatrick item : items) if (item.isLowStock()) low.add(item);
        return low;
    }
}
