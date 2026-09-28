import java.util.ArrayList;

/**
 * BreederManager.java
 *
 * This class manages the FULL LIST of Breeder animals. It handles
 * all the "logic" - adding new ones, selling them, removing them,
 * and doing simple math like total profit.
 *
 * The Frame (GUI) class will call these methods instead of dealing
 * with the ArrayList directly. This keeps the GUI code simple.
 */
public class BreederManager {

    // The list that holds every breeder animal we've ever brought in
    private ArrayList<Breeder> breederAnimals;

    public BreederManager() {
        breederAnimals = new ArrayList<>();
    }

    // ----- Add -----
    public void addBreederAnimal(Breeder animal) {
        breederAnimals.add(animal);
    }

    // ----- Remove -----
    /**
     * Removes an animal by its position in the list.
     * Returns true if it worked, false if the index was bad.
     */
    public boolean removeBreederAnimal(int index) {
        if (index < 0 || index >= breederAnimals.size()) {
            return false; // out of range, do nothing
        }
        breederAnimals.remove(index);
        return true;
    }

    // ----- Sell -----
    /**
     * Marks the animal at this index as sold.
     * Returns a String message explaining what happened, so the GUI
     * can show it directly in a JOptionPane.
     */
    public String sellBreederAnimal(int index) {
        if (index < 0 || index >= breederAnimals.size()) {
            return "Please select a valid animal from the list first.";
        }

        Breeder animal = breederAnimals.get(index);

        if (animal.isSold()) {
            return animal.getAnimalType() + " has already been sold.";
        } else {
            animal.markAsSold();
            return animal.getAnimalType() + " (" + animal.getBreed() + ") marked as SOLD!";
        }
    }

    // ----- Getters used by the GUI -----
    public ArrayList<Breeder> getAllAnimals() {
        return breederAnimals;
    }

    /**
     * Returns only the animals that have NOT been sold yet.
     */
    public ArrayList<Breeder> getAvailableAnimals() {
        ArrayList<Breeder> available = new ArrayList<>();
        for (Breeder animal : breederAnimals) {
            if (!animal.isSold()) {
                available.add(animal);
            }
        }
        return available;
    }

    /**
     * Adds up the profit (price - cost) for every animal that HAS sold.
     */
    public double getTotalProfit() {
        double total = 0.0;
        for (Breeder animal : breederAnimals) {
            if (animal.isSold()) {
                total += animal.getProfit();
            }
        }
        return total;
    }

    public int getTotalCount() {
        return breederAnimals.size();
    }
}
