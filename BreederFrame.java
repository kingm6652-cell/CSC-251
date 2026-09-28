import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * BreederFrame.java
 *
 * This is the GUI window for the "Local Breeder Sales" section.
 * It lets the user:
 *   - see the list of specialty animals bought from local breeders
 *   - add a new animal
 *   - mark an animal as sold
 *   - remove an animal
 *   - view total profit made from breeder animals
 *
 * All the actual data/logic lives in BreederManager. This class just
 * connects buttons/text fields to that manager.
 */
public class BreederFrame extends JFrame {

    // The manager that holds all the data and logic
    private BreederManager manager;

    // GUI components we need to reference in more than one method
    private DefaultListModel<Breeder> listModel;
    private JList<Breeder> animalList;

    private JTextField breederNameField;
    private JTextField contactField;
    private JTextField animalTypeField;
    private JTextField breedField;
    private JTextField costField;
    private JTextField priceField;
    private JTextField dateField;

    public BreederFrame() {
        manager = new BreederManager();

        setTitle("Local Breeder Sales");
        setSize(650, 500);
        setLocationRelativeTo(null); // opens centered on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // don't kill the whole app

        // ----- Top: form for entering a new breeder animal -----
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Breeder Animal"));

        breederNameField = new JTextField();
        contactField = new JTextField();
        animalTypeField = new JTextField();
        breedField = new JTextField();
        costField = new JTextField();
        priceField = new JTextField();
        dateField = new JTextField();

        formPanel.add(new JLabel("Breeder Name:"));
        formPanel.add(breederNameField);
        formPanel.add(new JLabel("Breeder Contact:"));
        formPanel.add(contactField);
        formPanel.add(new JLabel("Animal Type (e.g. Rabbit):"));
        formPanel.add(animalTypeField);
        formPanel.add(new JLabel("Breed:"));
        formPanel.add(breedField);
        formPanel.add(new JLabel("Cost ($ we paid):"));
        formPanel.add(costField);
        formPanel.add(new JLabel("Sale Price ($):"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Date Acquired (MM/DD/YYYY):"));
        formPanel.add(dateField);

        // ----- Middle: list of all breeder animals -----
        listModel = new DefaultListModel<>();
        animalList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(animalList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Breeder Animals"));

        // ----- Bottom: buttons -----
        JButton addButton = new JButton("Add Animal");
        JButton sellButton = new JButton("Mark as Sold");
        JButton removeButton = new JButton("Remove");
        JButton profitButton = new JButton("View Total Profit");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(sellButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(profitButton);

        // ----- Hook up button actions -----
        addButton.addActionListener(this::handleAddAnimal);
        sellButton.addActionListener(this::handleSellAnimal);
        removeButton.addActionListener(this::handleRemoveAnimal);
        profitButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        String.format("Total profit from breeder animals: $%.2f", manager.getTotalProfit()),
                        "Total Profit", JOptionPane.INFORMATION_MESSAGE));

        // ----- Put it all together -----
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Reads the form fields, checks them, and adds a new Breeder animal.
     */
    private void handleAddAnimal(ActionEvent e) {
        String breederName = breederNameField.getText().trim();
        String contact = contactField.getText().trim();
        String animalType = animalTypeField.getText().trim();
        String breed = breedField.getText().trim();
        String date = dateField.getText().trim();

        // Decision structure: make sure required text fields aren't empty
        if (breederName.isEmpty() || animalType.isEmpty() || breed.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Breeder Name, Animal Type, and Breed are required.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Decision structure: make sure cost/price are actually numbers
        double cost;
        double price;
        try {
            cost = Double.parseDouble(costField.getText().trim());
            price = Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Cost and Price must be valid numbers (example: 25.00).",
                    "Invalid Number", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Breeder newAnimal = new Breeder(breederName, contact, animalType, breed, cost, price, date);
        manager.addBreederAnimal(newAnimal);
        refreshList();
        clearForm();

        JOptionPane.showMessageDialog(this, "Animal added successfully!");
    }

    /**
     * Marks the currently selected animal in the list as sold.
     */
    private void handleSellAnimal(ActionEvent e) {
        int index = animalList.getSelectedIndex();
        String message = manager.sellBreederAnimal(index);
        JOptionPane.showMessageDialog(this, message);
        refreshList();
    }

    /**
     * Removes the currently selected animal from the list.
     */
    private void handleRemoveAnimal(ActionEvent e) {
        int index = animalList.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Please select an animal to remove.");
            return;
        }

        boolean removed = manager.removeBreederAnimal(index);
        if (removed) {
            refreshList();
        }
    }

    /**
     * Clears out the list on screen and rebuilds it from the manager's data.
     */
    private void refreshList() {
        listModel.clear();
        for (Breeder animal : manager.getAllAnimals()) {
            listModel.addElement(animal);
        }
    }

    /**
     * Empties the input fields after a successful add.
     */
    private void clearForm() {
        breederNameField.setText("");
        contactField.setText("");
        animalTypeField.setText("");
        breedField.setText("");
        costField.setText("");
        priceField.setText("");
        dateField.setText("");
    }

    // Lets you run/test just this window by itself while building it
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BreederFrame frame = new BreederFrame();
            frame.setVisible(true);
        });
    }
}
