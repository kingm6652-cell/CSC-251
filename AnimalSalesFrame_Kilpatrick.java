import javax.swing.*;

/** Replaces Kilpatrick's farm-animal placeholder with listing, restocking, and sales. */
public class AnimalSalesFrame_Kilpatrick extends TablePanel_Kilpatrick {
    public AnimalSalesFrame_Kilpatrick(FarmController_Kilpatrick controller) {
        super(controller,"Farm Animals","Each listing tracks a batch of similar farm-raised animals. Breeder animals have their own tab.",
                "ID","Species","Breed","Age (weeks)","Price each","Available");
        actions.add(Ui_Kilpatrick.button("Add listing",()->edit(false)));
        actions.add(Ui_Kilpatrick.button("Edit listing",()->edit(true)));
        actions.add(Ui_Kilpatrick.button("Restock animals",this::restock));
        actions.add(Ui_Kilpatrick.button("Sell selected",this::sell));
        refresh();
    }
    public void refresh() {
        Ui_Kilpatrick.model(table).setRowCount(0); int count=0;
        for (Animal_Kilpatrick a : controller.data().animals.getAllAnimals()) {
            Ui_Kilpatrick.model(table).addRow(new Object[]{a.getId(),a.getSpecies(),a.getBreed(),a.getAgeInWeeks(),
                    Validation_Kilpatrick.money(a.getPriceCents()),a.getQuantityAvailable()});
            count+=a.getQuantityAvailable();
        }
        summary.setText(count+" animals available | Farm-animal sales: "+Validation_Kilpatrick.money(controller.data().sales.total("Farm animal")));
    }
    private void edit(boolean editing) {
        int id=editing?Ui_Kilpatrick.selectedId(table):-1;
        Animal_Kilpatrick a=editing?controller.data().animals.findById(id):null;
        Ui_Kilpatrick.Form f=new Ui_Kilpatrick.Form();
        JTextField species=f.text("Species",a==null?"":a.getSpecies());
        JTextField breed=f.text("Breed",a==null?"":a.getBreed());
        JTextField age=f.text("Age in weeks",a==null?"0":""+a.getAgeInWeeks());
        JTextField price=f.text("Price each ($)",a==null?"0.00":Validation_Kilpatrick.dollars(a.getPriceCents()));
        JTextField qty=f.text("Quantity available",a==null?"1":""+a.getQuantityAvailable());
        if (!f.show(this,editing?"Edit animal listing #"+id:"Add farm-animal listing")) return;
        int weeks=Validation_Kilpatrick.whole(age.getText(),"Age",0);
        int quantity=Validation_Kilpatrick.whole(qty.getText(),"Quantity",0);
        long cents=Validation_Kilpatrick.cents(price.getText());
        controller.commit(data->{
            if(editing) data.animals.findById(id).update(species.getText(),breed.getText(),weeks,cents,quantity);
            else data.animals.addAnimal(species.getText(),breed.getText(),weeks,cents,quantity);
        });
    }
    private void restock() {
        int id=Ui_Kilpatrick.selectedId(table);
        Ui_Kilpatrick.Form f=new Ui_Kilpatrick.Form(); JTextField qty=f.text("Quantity to add","1");
        if(!f.show(this,"Restock listing #"+id))return;
        int amount=Validation_Kilpatrick.whole(qty.getText(),"Quantity",1);
        controller.commit(data->data.animals.findById(id).addStock(amount));
    }
    private void sell() {
        int id=Ui_Kilpatrick.selectedId(table);
        Animal_Kilpatrick a=controller.data().animals.findById(id);
        if(!a.isAvailable())throw new IllegalArgumentException("This listing is sold out.");
        Ui_Kilpatrick.Form f=new Ui_Kilpatrick.Form();
        f.add("Animal",new JLabel(a.getBreed()+" "+a.getSpecies()));
        f.add("Unit price",new JLabel(Validation_Kilpatrick.money(a.getPriceCents())));
        JTextField qty=f.text("Quantity","1"); JTextField customer=f.text("Customer (optional)","");
        JComboBox<String> method=Ui_Kilpatrick.methods();f.add("Payment received by",method);
        if(!f.show(this,"Record farm-animal sale and payment"))return;
        int quantity=Validation_Kilpatrick.whole(qty.getText(),"Quantity",1);
        StoreSale_Kilpatrick[] result=new StoreSale_Kilpatrick[1];
        controller.commit(data->result[0]=data.sellFarmAnimal(id,quantity,customer.getText(),(String)method.getSelectedItem()));
        Ui_Kilpatrick.message(this,"Sale saved",result[0].getReceipt());
    }
}
