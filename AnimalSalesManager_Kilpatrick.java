import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Kilpatrick's shared farm-animal manager. Sales receipts are kept by SalesManager. */
public class AnimalSalesManager_Kilpatrick {
    private final List<Animal_Kilpatrick> animals = new ArrayList<>();
    public List<Animal_Kilpatrick> getAllAnimals() { return Collections.unmodifiableList(animals); }
    public Animal_Kilpatrick addAnimal(String species, String breed, int age, long price, int qty) {
        int id = animals.isEmpty() ? 1 : animals.get(animals.size() - 1).getId() + 1;
        Animal_Kilpatrick animal = new Animal_Kilpatrick(id, species, breed, age, price, qty);
        animals.add(animal);
        return animal;
    }
    void restore(Animal_Kilpatrick animal) {
        if (findById(animal.getId()) != null || (!animals.isEmpty() && animal.getId() <= animals.get(animals.size()-1).getId()))
            throw new IllegalArgumentException("Invalid or duplicate animal ID.");
        animals.add(animal);
    }
    public Animal_Kilpatrick findById(int id) {
        for (Animal_Kilpatrick animal : animals) if (animal.getId() == id) return animal;
        return null;
    }
}
