/**
 * Breeder.java
 *
 * This is a "model" class. It just holds the data for ONE animal that
 * we bought from a local breeder to resell in the store (things like
 * a specialty rabbit breed, a fancy chicken, etc.).
 *
 * It does not print anything or open any windows - it just stores info.
 */
public class Breeder {

    // ----- Fields (the data we track for each breeder animal) -----
    private String breederName;   // name of the local breeder we bought from
    private String contactInfo;   // phone or email for the breeder
    private String animalType;    // example: "Rabbit", "Chicken", "Duck"
    private String breed;         // example: "Holland Lop", "Silkie"
    private double cost;          // what WE paid the breeder
    private double price;         // what WE are selling it for
    private String dateAcquired;  // simple text date, example "09/15/2026"
    private boolean sold;         // true if this animal has been sold already

    // ----- Constructor -----
    public Breeder(String breederName, String contactInfo, String animalType,
                    String breed, double cost, double price, String dateAcquired) {
        this.breederName = breederName;
        this.contactInfo = contactInfo;
        this.animalType = animalType;
        this.breed = breed;
        this.cost = cost;
        this.price = price;
        this.dateAcquired = dateAcquired;
        this.sold = false; // brand new animals start out NOT sold
    }

    // ----- Getters -----
    public String getBreederName() { return breederName; }
    public String getContactInfo() { return contactInfo; }
    public String getAnimalType() { return animalType; }
    public String getBreed() { return breed; }
    public double getCost() { return cost; }
    public double getPrice() { return price; }
    public String getDateAcquired() { return dateAcquired; }
    public boolean isSold() { return sold; }

    // ----- Setters (only the ones we actually need to change later) -----
    public void setPrice(double price) { this.price = price; }

    // ----- Simple methods with logic -----

    /**
     * Marks this animal as sold. Returns false if it was already sold,
     * so the Manager/Frame can warn the user instead of double-selling it.
     */
    public boolean markAsSold() {
        if (sold) {
            return false; // already sold, can't sell it again
        }
        sold = true;
        return true;
    }

    /**
     * How much profit we make on this one animal once it sells.
     */
    public double getProfit() {
        return price - cost;
    }

    /**
     * This controls how the animal looks when displayed in a list.
     */
    @Override
    public String toString() {
        String status = sold ? "SOLD" : "Available";
        return String.format("%s (%s) from %s | Cost: $%.2f | Price: $%.2f | %s",
                animalType, breed, breederName, cost, price, status);
    }
}
