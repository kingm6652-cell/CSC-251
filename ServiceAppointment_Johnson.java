/**
 * ServiceAppointment_Johnson.java
 *
 * A "model" class for ONE appointment - things like nail trims,
 * basic checkups, vaccinations, etc. that we offer as a service.
 *
 * Just holds data, no GUI code here.
 */
public class ServiceAppointment_Johnson {

    // ----- Fields -----
    private String customerName;
    private String phoneNumber;
    private String animalType;   // example: "Dog", "Chicken", "Rabbit"
    private String serviceType;  // example: "Nail Trim", "Vaccination"
    private String date;         // simple text date, example "09/20/2026"
    private String time;         // simple text time, example "2:00 PM"
    private double cost;
    private boolean paid;
    private String status;       // "Scheduled", "Completed", or "Cancelled"

    public ServiceAppointment_Johnson(String customerName, String phoneNumber, String animalType,
                               String serviceType, String date, String time, double cost) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.animalType = animalType;
        this.serviceType = serviceType;
        this.date = date;
        this.time = time;
        this.cost = cost;
        this.paid = false;              // nobody has paid yet when it's first scheduled
        this.status = "Scheduled";      // every new appointment starts as "Scheduled"
    }

    // ----- Getters -----
    public String getCustomerName() { return customerName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAnimalType() { return animalType; }
    public String getServiceType() { return serviceType; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public double getCost() { return cost; }
    public boolean isPaid() { return paid; }
    public String getStatus() { return status; }

    // ----- Methods with logic -----

    /**
     * Marks the appointment completed, but only if it wasn't already
     * cancelled. Returns true/false so the caller knows what happened.
     */
    public boolean markCompleted() {
        if (status.equals("Cancelled")) {
            return false; // can't complete something that was cancelled
        }
        status = "Completed";
        return true;
    }

    public boolean markCancelled() {
        if (status.equals("Completed")) {
            return false; // can't cancel something already finished
        }
        status = "Cancelled";
        return true;
    }

    /**
     * Marks payment received. Returns false if it was already paid,
     * so the GUI can tell the user instead of just doing it silently.
     */
    public boolean markPaid() {
        if (paid) {
            return false;
        }
        paid = true;
        return true;
    }

    @Override
    public String toString() {
        String paidText = paid ? "Paid" : "Unpaid";
        return String.format("%s - %s (%s) | %s at %s | $%.2f | %s | %s",
                customerName, serviceType, animalType, date, time, cost, status, paidText);
    }
}
