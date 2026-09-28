import javax.swing.*;

/** Replaces Kilpatrick's placeholder. A JPanel section inside the shared JFrame. */
public class InventoryFrame_Kilpatrick extends TablePanel_Kilpatrick {
    public InventoryFrame_Kilpatrick(FarmController_Kilpatrick controller) {
        super(controller, "Store Inventory", "Select a product to edit or restock. Sales update this stock automatically.",
                "ID", "Product", "Category", "Unit Price", "On Hand", "Reorder at", "Stock Status");
        actions.add(Ui_Kilpatrick.button("Add product", () -> edit(false)));
        actions.add(Ui_Kilpatrick.button("Edit selected", () -> edit(true)));
        actions.add(Ui_Kilpatrick.button("Restock selected", this::restock));
        actions.add(Ui_Kilpatrick.button("Low-stock report", () -> {
            StringBuilder text = new StringBuilder();
            for (InventoryItem_Kilpatrick p : controller.data().inventory.getLowStockItems()) text.append(p).append("\n");
            Ui_Kilpatrick.message(this,"Low-stock report",text.length() == 0 ? "No products are low on stock." : text.toString());
        }));
        refresh();
    }
    public void refresh() {
        Ui_Kilpatrick.model(table).setRowCount(0);
        for (InventoryItem_Kilpatrick p : controller.data().inventory.getAllItems())
            Ui_Kilpatrick.model(table).addRow(new Object[]{p.getId(),p.getName(),p.getCategory(),Validation_Kilpatrick.money(p.getPriceCents()),
                    p.getQuantityOnHand(),p.getReorderLevel(),p.isLowStock()?"LOW STOCK":"In stock"});
        table.getColumnModel().getColumn(1).setPreferredWidth(240);
        summary.setText(table.getRowCount()+" products | " + controller.data().inventory.getLowStockItems().size()+" low-stock alerts");
    }
    private void edit(boolean editing) {
        int id = editing ? Ui_Kilpatrick.selectedId(table) : -1;
        InventoryItem_Kilpatrick p = editing ? controller.data().inventory.findById(id) : null;
        Ui_Kilpatrick.Form f = new Ui_Kilpatrick.Form();
        JTextField name=f.text("Product name",p==null?"":p.getName());
        JTextField category=f.text("Category",p==null?"Feed":p.getCategory());
        JTextField price=f.text("Unit price ($)",p==null?"0.00":Validation_Kilpatrick.dollars(p.getPriceCents()));
        JTextField qty=f.text("Quantity on hand",p==null?"0":""+p.getQuantityOnHand());
        JTextField reorder=f.text("Reorder level",p==null?"5":""+p.getReorderLevel());
        if (!f.show(this, editing?"Edit product #"+id:"Add product")) return;
        long cents=Validation_Kilpatrick.cents(price.getText());
        int quantity=Validation_Kilpatrick.whole(qty.getText(),"Quantity",0);
        int level=Validation_Kilpatrick.whole(reorder.getText(),"Reorder level",0);
        controller.commit(data -> {
            if (editing) data.inventory.findById(id).update(name.getText(),category.getText(),cents,quantity,level);
            else data.inventory.addItem(name.getText(),category.getText(),cents,quantity,level);
        });
    }
    private void restock() {
        int id=Ui_Kilpatrick.selectedId(table);
        Ui_Kilpatrick.Form f=new Ui_Kilpatrick.Form();
        JTextField qty=f.text("Quantity to add","1");
        if (!f.show(this,"Restock product #"+id)) return;
        int amount=Validation_Kilpatrick.whole(qty.getText(),"Quantity",1);
        controller.commit(data -> data.inventory.findById(id).restock(amount));
    }
}
