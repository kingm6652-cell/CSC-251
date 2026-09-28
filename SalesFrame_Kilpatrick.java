import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/** Kilpatrick's store-sales screen expanded to a multi-item cart and saved receipts. */
public class SalesFrame_Kilpatrick extends JPanel {
    private final FarmController_Kilpatrick controller;
    private final JComboBox<InventoryItem_Kilpatrick> product = new JComboBox<>();
    private final JSpinner quantity = new JSpinner(new SpinnerNumberModel(1,1,1000000,1));
    private final JTable table = Ui_Kilpatrick.table("Product ID","Product","Quantity","Unit price","Line total");
    private final Map<Integer,Integer> cart = new LinkedHashMap<>();
    private final JTextField customer = new JTextField(18);
    private final JComboBox<String> method = Ui_Kilpatrick.methods();
    private final JLabel total = new JLabel("Total: $0.00");
    public SalesFrame_Kilpatrick(FarmController_Kilpatrick controller) {
        super(new BorderLayout(0,14)); this.controller=controller;
        setBorder(new EmptyBorder(20,20,20,20));
        JPanel top=new JPanel(new BorderLayout(0,12));
        top.add(Ui_Kilpatrick.heading("Store Sales","Add products to a cart. Stock changes only after you confirm payment received."),BorderLayout.NORTH);
        JPanel picker=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        product.setPreferredSize(new Dimension(510,32)); product.setName("Product"); quantity.setName("Quantity");
        picker.add(product); picker.add(new JLabel("Qty:")); picker.add(quantity);
        picker.add(Ui_Kilpatrick.button("Add to cart",this::addToCart));
        top.add(picker,BorderLayout.SOUTH); add(top,BorderLayout.NORTH);
        add(Ui_Kilpatrick.scroll(table),BorderLayout.CENTER);
        JPanel bottom=new JPanel(new GridLayout(3,1,0,10));
        JPanel cartButtons=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        cartButtons.add(Ui_Kilpatrick.button("Remove cart line",()->{cart.remove(Ui_Kilpatrick.selectedId(table));refreshCart();}));
        cartButtons.add(Ui_Kilpatrick.button("Clear cart",()->{cart.clear();refreshCart();}));
        bottom.add(cartButtons);
        JPanel pay=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        pay.add(new JLabel("Customer (optional):")); pay.add(customer);
        pay.add(new JLabel("Payment received by:")); pay.add(method);
        pay.add(Ui_Kilpatrick.button("Checkout / record payment",this::checkout));
        bottom.add(pay);
        total.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20)); total.setForeground(Ui_Kilpatrick.GREEN);
        bottom.add(total);add(bottom,BorderLayout.SOUTH);
        controller.onChange(this::refresh); refresh();
    }
    public void refresh() {
        InventoryItem_Kilpatrick selected=(InventoryItem_Kilpatrick)product.getSelectedItem();
        int id=selected==null?-1:selected.getId();
        product.removeAllItems();
        for(InventoryItem_Kilpatrick item:controller.data().inventory.getAllItems()) {
            product.addItem(item);
            if(item.getId()==id) product.setSelectedItem(item);
        }
        refreshCart();
    }
    private void addToCart() {
        InventoryItem_Kilpatrick p=(InventoryItem_Kilpatrick)product.getSelectedItem();
        if(p==null)throw new IllegalArgumentException("Add a product in Inventory first.");
        try { quantity.commitEdit(); }
        catch(java.text.ParseException ex){throw new IllegalArgumentException("Enter a whole-number quantity.");}
        int qty=(Integer)quantity.getValue();
        int combined=cart.getOrDefault(p.getId(),0)+qty;
        if(combined>p.getQuantityOnHand())throw new IllegalArgumentException("Only "+p.getQuantityOnHand()+" available, including items already in the cart.");
        cart.put(p.getId(),combined);refreshCart();
    }
    private long cartTotal() {
        long sum=0;
        for(Map.Entry<Integer,Integer> line:cart.entrySet()) {
            InventoryItem_Kilpatrick p=controller.data().inventory.findById(line.getKey());
            if(p!=null)sum+=p.getPriceCents()*line.getValue();
        }
        return sum;
    }
    private void refreshCart() {
        Ui_Kilpatrick.model(table).setRowCount(0);
        for(Map.Entry<Integer,Integer> line:cart.entrySet()) {
            InventoryItem_Kilpatrick p=controller.data().inventory.findById(line.getKey());
            if(p!=null)Ui_Kilpatrick.model(table).addRow(new Object[]{p.getId(),p.getName(),line.getValue(),
                    Validation_Kilpatrick.money(p.getPriceCents()),Validation_Kilpatrick.money(p.getPriceCents()*line.getValue())});
        }
        total.setText("Total: "+Validation_Kilpatrick.money(cartTotal())+"   |   Tax is included in price");
    }
    private void checkout() {
        if(cart.isEmpty())throw new IllegalArgumentException("Add products to the cart first.");
        if(!Ui_Kilpatrick.confirm(this,"Record "+Validation_Kilpatrick.money(cartTotal())+" received by "+method.getSelectedItem()+" and finish this sale?"))return;
        StoreSale_Kilpatrick[] result=new StoreSale_Kilpatrick[1];
        controller.commit(data->result[0]=data.checkout(cart,customer.getText(),(String)method.getSelectedItem()));
        cart.clear(); customer.setText(""); refreshCart();
        Ui_Kilpatrick.message(this,"Receipt saved",result[0].getReceipt());
    }
    public boolean hasCart() { return !cart.isEmpty(); }
}
