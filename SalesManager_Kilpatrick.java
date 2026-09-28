import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** Kilpatrick's sales logic, extended to a cart that validates BEFORE reducing stock. */
public class SalesManager_Kilpatrick {
    private final List<StoreSale_Kilpatrick> history = new ArrayList<>();
    public List<StoreSale_Kilpatrick> getSalesHistory() { return Collections.unmodifiableList(history); }
    public StoreSale_Kilpatrick checkout(InventoryManager_Kilpatrick inventory, Map<Integer, Integer> cart,
            String customer, String method) {
        Validation_Kilpatrick.method(method);
        Validation_Kilpatrick.customer(customer);
        if (cart.isEmpty()) throw new IllegalArgumentException("Add at least one item to the cart.");
        List<SaleLineItem_Kilpatrick> lines = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            InventoryItem_Kilpatrick item = inventory.findById(entry.getKey());
            int qty = entry.getValue();
            if (item == null || qty <= 0) throw new IllegalArgumentException("Invalid item or quantity.");
            if (qty > item.getQuantityOnHand()) throw new IllegalArgumentException("Only " + item.getQuantityOnHand() + " of " + item.getName() + " available.");
            lines.add(new SaleLineItem_Kilpatrick(item.getId(), item.getName(), qty, item.getPriceCents()));
        }
        StoreSale_Kilpatrick sale = record("Store", customer, method, lines);
        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) inventory.findById(entry.getKey()).sell(entry.getValue());
        return sale;
    }
    public StoreSale_Kilpatrick record(String type, String customer, String method, List<SaleLineItem_Kilpatrick> lines) {
        int id = history.isEmpty() ? 1 : history.get(history.size()-1).getId() + 1;
        StoreSale_Kilpatrick sale = new StoreSale_Kilpatrick(id, type, customer, method, LocalDateTime.now(), lines);
        history.add(sale);
        return sale;
    }
    void restore(StoreSale_Kilpatrick sale) {
        if (!history.isEmpty() && sale.getId() <= history.get(history.size()-1).getId())
            throw new IllegalArgumentException("Invalid sale IDs.");
        history.add(sale);
    }
    public long total(String type) {
        long sum = 0;
        for (StoreSale_Kilpatrick sale : history) if (type == null || type.equals(sale.getType())) sum += sale.getTotalCents();
        return sum;
    }
}
