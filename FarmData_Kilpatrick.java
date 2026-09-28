import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** One shared business state for every tab. New AI-assisted integration in Kilpatrick's files. */
public class FarmData_Kilpatrick {
    public final InventoryManager_Kilpatrick inventory = new InventoryManager_Kilpatrick();
    public final AnimalSalesManager_Kilpatrick animals = new AnimalSalesManager_Kilpatrick();
    public final BreederSalesManager_Kilpatrick breeders = new BreederSalesManager_Kilpatrick();
    public final ServiceManager_Kilpatrick services = new ServiceManager_Kilpatrick();
    public final SalesManager_Kilpatrick sales = new SalesManager_Kilpatrick();
    public final List<Payment_Kilpatrick> payments = new ArrayList<>();

    public static FarmData_Kilpatrick demo() {
        FarmData_Kilpatrick data = new FarmData_Kilpatrick();
        data.inventory.addItem("Chicken Feed 25lb", "Feed", 1899, 40, 10);
        data.inventory.addItem("Rabbit Pellets 10lb", "Feed", 1249, 25, 8);
        data.inventory.addItem("Small Animal Bedding", "Bedding", 999, 30, 10);
        data.inventory.addItem("Poultry Waterer", "Supplies", 1450, 12, 5);
        data.inventory.addItem("Leash - Medium", "Supplies", 1125, 15, 5);
        data.animals.addAnimal("Chicken", "Rhode Island Red", 8, 1500, 10);
        data.animals.addAnimal("Duck", "Pekin", 6, 1800, 6);
        data.animals.addAnimal("Rabbit", "Holland Lop", 10, 3500, 4);
        data.animals.addAnimal("Hamster", "Syrian", 4, 1200, 8);
        return data;
    }
    private void payment(String type, int ref, String customer, long amount, String method) {
        int id = payments.isEmpty() ? 1 : payments.get(payments.size()-1).id + 1;
        payments.add(new Payment_Kilpatrick(id, type, ref, customer, amount, method, LocalDateTime.now()));
    }
    private void salePayment(StoreSale_Kilpatrick sale) {
        payment(sale.getType(), sale.getId(), sale.getCustomer(), sale.getTotalCents(), sale.getMethod());
    }
    public StoreSale_Kilpatrick checkout(Map<Integer, Integer> cart, String customer, String method) {
        StoreSale_Kilpatrick sale = sales.checkout(inventory, cart, customer, method);
        salePayment(sale);
        return sale;
    }
    public StoreSale_Kilpatrick sellFarmAnimal(int id, int qty, String customer, String method) {
        Validation_Kilpatrick.method(method);
        customer = Validation_Kilpatrick.customer(customer);
        Animal_Kilpatrick animal = animals.findById(id);
        if (animal == null) throw new IllegalArgumentException("Select an animal.");
        if (qty <= 0 || qty > animal.getQuantityAvailable()) throw new IllegalArgumentException("Enter a positive quantity no greater than the available stock.");
        StoreSale_Kilpatrick sale = sales.record("Farm animal", customer, method,
                Arrays.asList(new SaleLineItem_Kilpatrick(id, animal.getBreed() + " " + animal.getSpecies(), qty, animal.getPriceCents())));
        animal.sell(qty);
        salePayment(sale);
        return sale;
    }
    public StoreSale_Kilpatrick sellBreederAnimal(int id, String customer, String method) {
        Validation_Kilpatrick.method(method);
        customer = Validation_Kilpatrick.customer(customer);
        BreederAnimal_Kilpatrick animal = breeders.findById(id);
        if (animal == null || animal.isSold()) throw new IllegalArgumentException("Select an available breeder animal. Sold animals cannot be sold twice.");
        StoreSale_Kilpatrick sale = sales.record("Breeder", customer, method,
                Arrays.asList(new SaleLineItem_Kilpatrick(id, animal.getBreed() + " " + animal.getAnimalType(), 1, animal.getPriceCents())));
        breeders.sellById(id);
        salePayment(sale);
        return sale;
    }
    public void payService(int id, long amount, String method) {
        Validation_Kilpatrick.method(method);
        Appointment_Kilpatrick a = services.findById(id);
        if (a == null) throw new IllegalArgumentException("Select an appointment.");
        a.receive(amount);
        payment("Service", id, a.getCustomerName(), amount, method);
    }
    public void cancelService(int id, String refundMethod) {
        Validation_Kilpatrick.method(refundMethod);
        Appointment_Kilpatrick a = services.findById(id);
        if (a == null || !a.getStatus().equals("Scheduled")) throw new IllegalArgumentException("Only scheduled appointments can be cancelled.");
        long refund = a.getPaidCents();
        a.refundForCancellation();
        a.markCancelled();
        if (refund > 0) payment("Service refund", id, a.getCustomerName(), -refund, refundMethod);
    }
    public long serviceCollected() {
        long total = 0;
        for (Payment_Kilpatrick p : payments) if (p.type.startsWith("Service")) total += p.amountCents;
        return total;
    }
    public long totalCollected() {
        long total = 0;
        for (Payment_Kilpatrick p : payments) total += p.amountCents;
        return total;
    }
}
