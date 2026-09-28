/** Kilpatrick's service catalog model, with appointment length added. Demo fees only. */
public class ServiceType_Kilpatrick {
    private final String name;
    private final long price;
    private final int minutes;
    public ServiceType_Kilpatrick(String name, long price, int minutes) {
        this.name = name; this.price = price; this.minutes = minutes;
    }
    public String getName() { return name; }
    public long getPriceCents() { return price; }
    public int getMinutes() { return minutes; }
    @Override public String toString() { return name; }
    public static ServiceType_Kilpatrick[] catalog() {
        return new ServiceType_Kilpatrick[] {
            new ServiceType_Kilpatrick("Nail trim", 1500, 15),
            new ServiceType_Kilpatrick("Grooming", 4000, 60),
            new ServiceType_Kilpatrick("Basic vet visit", 4500, 30),
            new ServiceType_Kilpatrick("Vaccination appointment", 3000, 30),
            new ServiceType_Kilpatrick("Other animal-care service", 2500, 30)
        };
    }
}
