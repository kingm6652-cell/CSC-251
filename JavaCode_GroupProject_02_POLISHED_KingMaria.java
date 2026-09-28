import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class GroupProject_2 extends JFrame {
    private final AppModel model = new AppModel();
    private final JTabbedPane tabs = new JTabbedPane();

    public GroupProject_2() {
        super("Barnyard Supply & Services");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 820);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(60, 110, 75));
        top.setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel title = new JLabel("Barnyard Supply & Services");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        top.add(title, BorderLayout.WEST);

        JButton help = new JButton("Help");
        help.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Overview - displays general store information\n" +
                "Inventory - tracks stock levels\n" +
                "Store Sales - checkout and cart management\n" +
                "Services - schedule appointments\n" +
                "Payments - payment history and balances\n" +
                "Farm Animals - available animal sales\n" +
                "Local Breeders - breeder inventory"));
        top.add(help, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        tabs.addTab("Overview", new OverviewPanel(model));
        tabs.addTab("Inventory", new InventoryPanel(model));
        tabs.addTab("Store Sales", new SalesPanel(model));
        tabs.addTab("Services", new ServicesPanel(model));
        tabs.addTab("Payments", new PaymentsPanel(model));
        tabs.addTab("Farm Animals", new AnimalsPanel(model));
        tabs.addTab("Local Breeders", new BreedersPanel(model));
        add(tabs, BorderLayout.CENTER);

        JLabel status = new JLabel("Auto-save is on | Demo data loaded");
        status.setBorder(new EmptyBorder(8, 12, 8, 12));
        status.setOpaque(true);
        status.setBackground(new Color(245, 239, 232));
        add(status, BorderLayout.SOUTH);
    }

    private static String money(double amount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }

    private static class AppModel {
        final List<Item> inventory = new ArrayList<>();
        final List<Animal> animals = new ArrayList<>();
        final List<Breeder> breeders = new ArrayList<>();
        final List<Appointment> appointments = new ArrayList<>();
        final List<Sale> sales = new ArrayList<>();
        final List<Payment> payments = new ArrayList<>();
        final List<CartItem> cart = new ArrayList<>();

        AppModel() {
            inventory.add(new Item("INV-101", "Hay Bale", "Feed", 18.50, 42, 12));
            inventory.add(new Item("INV-102", "Chicken Feed", "Feed", 14.75, 18, 10));
            inventory.add(new Item("INV-103", "Horse Treats", "Supplies", 9.99, 26, 8));
            inventory.add(new Item("INV-104", "Brush Set", "Grooming", 22.00, 9, 10));
            inventory.add(new Item("INV-105", "Poultry Nest", "Livestock", 38.50, 7, 6));
            inventory.add(new Item("INV-106", "Water Trough", "Equipment", 65.00, 11, 5));

            animals.add(new Animal("AN-1001", "Daisy", "Dairy Cow", 3, 1850.00, true));
            animals.add(new Animal("AN-1002", "Buster", "Beef Calf", 1, 975.00, true));
            animals.add(new Animal("AN-1003", "Mabel", "Goat", 2, 325.00, true));
            animals.add(new Animal("AN-1004", "Copper", "Quarter Horse", 6, 4200.00, true));
            animals.add(new Animal("AN-1005", "Penny", "Mini Pig", 1, 275.00, false));
            animals.add(new Animal("AN-1006", "Ranger", "Border Collie", 4, 850.00, true));

            breeders.add(new Breeder("BR-2001", "Rosemary", "Alpaca", 2, 1600.00, true));
            breeders.add(new Breeder("BR-2002", "Sky", "Llama", 1, 1250.00, true));
            breeders.add(new Breeder("BR-2003", "Poppy", "Goat", 3, 500.00, false));
            breeders.add(new Breeder("BR-2004", "Juniper", "Quarter Horse", 2, 3700.00, true));

            appointments.add(new Appointment(101, "Alicia M.", "555-1001", "Daisy", "Hoof Trim", "2026-09-28", "09:00", 45, 90.00, "Scheduled"));
            appointments.add(new Appointment(102, "Mason T.", "555-1002", "Penny", "Vaccination", "2026-09-29", "11:00", 30, 44.00, "Completed"));
            appointments.add(new Appointment(103, "Lena O.", "555-1003", "Ranger", "Grooming", "2026-09-30", "14:30", 60, 65.00, "Scheduled"));

            sales.add(new Sale(301, "Cash sale", 125.50, "2026-09-24"));
            sales.add(new Sale(302, "Walk-in", 365.75, "2026-09-25"));

            payments.add(new Payment(401, "Service payment", 101, "Alicia M.", 45.00, "Cash", "2026-09-27 08:30"));
            payments.add(new Payment(402, "Store sale", 301, "Cash sale", 125.50, "Card", "2026-09-24 15:00"));
        }

        Item findItem(String id) {
            for (Item item : inventory) {
                if (item.id.equals(id)) return item;
            }
            return null;
        }

        double totalSales() {
            double total = 0;
            for (Sale sale : sales) total += sale.total;
            return total;
        }

        double totalCollected() {
            double total = 0;
            for (Payment p : payments) total += p.amount;
            return total;
        }

        double outstandingServiceFees() {
            double total = 0;
            for (Appointment a : appointments) {
                if ("Scheduled".equals(a.status)) total += a.fee;
            }
            return total;
        }

        double servicePayments() {
            double total = 0;
            for (Payment p : payments) {
                if (p.type.contains("Service")) total += p.amount;
            }
            return total;
        }
    }

    private static class Item {
        final String id, name, category;
        double price;
        int stock, reorderLevel;

        Item(String id, String name, String category, double price, int stock, int reorderLevel) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.price = price;
            this.stock = stock;
            this.reorderLevel = reorderLevel;
        }
    }

    private static class Animal {
        final String id, name, type;
        final int age;
        final double price;
        final boolean available;

        Animal(String id, String name, String type, int age, double price, boolean available) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.age = age;
            this.price = price;
            this.available = available;
        }
    }

    private static class Breeder {
        final String id, name, breed;
        final int quantity;
        final double price;
        final boolean available;

        Breeder(String id, String name, String breed, int quantity, double price, boolean available) {
            this.id = id;
            this.name = name;
            this.breed = breed;
            this.quantity = quantity;
            this.price = price;
            this.available = available;
        }
    }

    private static class Appointment {
        final int id;
        final String customer, phone, animal, service, date, time, status;
        final int duration;
        final double fee;

        Appointment(int id, String customer, String phone, String animal, String service, String date, String time, int duration, double fee, String status) {
            this.id = id;
            this.customer = customer;
            this.phone = phone;
            this.animal = animal;
            this.service = service;
            this.date = date;
            this.time = time;
            this.duration = duration;
            this.fee = fee;
            this.status = status;
        }
    }

    private static class Sale {
        final int id;
        final String customer, date;
        final double total;

        Sale(int id, String customer, double total, String date) {
            this.id = id;
            this.customer = customer;
            this.total = total;
            this.date = date;
        }
    }

    private static class Payment {
        final int id, referenceId;
        final String type, customer, method, dateTime;
        final double amount;

        Payment(int id, String type, int referenceId, String customer, double amount, String method, String dateTime) {
            this.id = id;
            this.referenceId = referenceId;
            this.type = type;
            this.customer = customer;
            this.amount = amount;
            this.method = method;
            this.dateTime = dateTime;
        }
    }

    private static class CartItem {
        final String id, name;
        final double price;
        int qty;

        CartItem(String id, String name, double price, int qty) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.qty = qty;
        }
    }

    private static class OverviewPanel extends JPanel {
        private final AppModel model;

        OverviewPanel(AppModel model) {
            this.model = model;
            setLayout(new BorderLayout(12, 18));
            setBorder(new EmptyBorder(22, 22, 22, 22));
            setBackground(new Color(248, 245, 240));

            JLabel heading = new JLabel("Overview of Stock and Sales");
            heading.setFont(new Font("SansSerif", Font.BOLD, 26));
            add(heading, BorderLayout.NORTH);

            JPanel cards = new JPanel(new GridLayout(2, 3, 14, 14));
            cards.setOpaque(false);

            String[] labels = {
                    "Net Payments Received",
                    "Store Sales",
                    "Farm-Animal Sales",
                    "Breeder Sales",
                    "Service Payments (net)",
                    "Unpaid Service Fees"
            };

            double[] values = {
                    model.totalCollected(),
                    model.totalSales(),
                    model.animals.stream().filter(a -> a.available).mapToDouble(a -> a.price).sum() * 0.12,
                    model.breeders.stream().filter(b -> b.available).mapToDouble(b -> b.price).sum() * 0.08,
                    model.servicePayments(),
                    model.outstandingServiceFees()
            };

            for (int i = 0; i < labels.length; i++) {
                JPanel card = new JPanel(new BorderLayout(8, 8));
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(208, 208, 208)),
                        new EmptyBorder(18, 18, 18, 18)));

                JLabel label = new JLabel(labels[i]);
                label.setForeground(new Color(90, 90, 90));
                card.add(label, BorderLayout.NORTH);

                JLabel value = new JLabel(money(values[i]));
                value.setFont(new Font("SansSerif", Font.BOLD, 26));
                value.setForeground(new Color(47, 107, 66));
                card.add(value, BorderLayout.CENTER);
                cards.add(card);
            }

            JTextArea notes = new JTextArea();
            notes.setEditable(false);
            notes.setLineWrap(true);
            notes.setWrapStyleWord(true);
            notes.setFont(new Font("SansSerif", Font.PLAIN, 16));
            notes.setBackground(new Color(252, 249, 246));
            notes.setBorder(new EmptyBorder(18, 18, 18, 18));
            notes.setText(
                    "AT A GLANCE\n\n" +
                    "1 low-stock product alerts\n" +
                    "2 appointments still scheduled\n" +
                    "3 breeder animals available\n" +
                    "Sold breeder animals' gross margin: " + money(0.22 * model.breeders.stream().filter(b -> !b.available).mapToDouble(b -> b.price).sum()) + "\n\n" +
                    "START HERE\n\n1. Add or update products in Inventory.\n" +
                    "2. Book animal-care appointments in Services.\n" +
                    "3. Manage farm-raised animals and breeder animals in their tabs.\n"
            );

            add(cards, BorderLayout.CENTER);
            add(new JScrollPane(notes), BorderLayout.SOUTH);
        }
    }

    private static class InventoryPanel extends JPanel {
        private final AppModel model;
        private final DefaultTableModel tableModel;
        private final JTable table;

        InventoryPanel(AppModel model) {
            this.model = model;
            setLayout(new BorderLayout(12, 12));
            setBorder(new EmptyBorder(18, 18, 18, 18));
            setBackground(new Color(248, 245, 240));

            JLabel subtitle = new JLabel("Select a product to edit or restock. Sales update this stock automatically.");
            subtitle.setForeground(new Color(90, 90, 90));
            add(subtitle, BorderLayout.NORTH);

            tableModel = new DefaultTableModel(new Object[]{"ID", "Product", "Category", "Unit Price", "On Hand", "Reorder at", "Stock Status"}, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
            table = new JTable(tableModel);
            JScrollPane scroll = new JScrollPane(table);

            JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton addBtn = new JButton("Add product");
            JButton restockBtn = new JButton("Restock selected");
            controls.add(addBtn);
            controls.add(restockBtn);
            add(controls, BorderLayout.SOUTH);

            add(scroll, BorderLayout.CENTER);
            refreshTable();

            addBtn.addActionListener(e -> {
                String name = JOptionPane.showInputDialog(this, "Product name");
                if (name == null || name.trim().isEmpty()) return;
                String category = JOptionPane.showInputDialog(this, "Category", "Feed");
                if (category == null || category.trim().isEmpty()) category = "Feed";
                String priceText = JOptionPane.showInputDialog(this, "Unit price ($)", "0.00");
                double price = priceText == null ? 0 : Double.parseDouble(priceText);
                String qtyText = JOptionPane.showInputDialog(this, "Quantity on hand", "0");
                int qty = qtyText == null ? 0 : Integer.parseInt(qtyText);
                String reorderText = JOptionPane.showInputDialog(this, "Reorder level", "5");
                int reorder = reorderText == null ? 5 : Integer.parseInt(reorderText);
                model.inventory.add(new Item("INV-" + (100 + model.inventory.size()), name, category, price, qty, reorder));
                refreshTable();
            });

            restockBtn.addActionListener(e -> {
                int selected = table.getSelectedRow();
                if (selected < 0) {
                    JOptionPane.showMessageDialog(this, "Select a product first.");
                    return;
                }
                String id = table.getValueAt(selected, 0).toString();
                Item item = model.findItem(id);
                String qtyText = JOptionPane.showInputDialog(this, "Quantity to add", "1");
                if (qtyText == null) return;
                item.stock += Integer.parseInt(qtyText);
                refreshTable();
            });
        }

        private void refreshTable() {
            tableModel.setRowCount(0);
            for (Item item : model.inventory) {
                String status = item.stock <= item.reorderLevel ? "LOW STOCK" : "In stock";
                tableModel.addRow(new Object[]{item.id, item.name, item.category, money(item.price), item.stock, item.reorderLevel, status});
            }
        }
    }

    private static class SalesPanel extends JPanel {
        private final AppModel model;
        private final DefaultTableModel cartModel;
        private final JTable cartTable;
        private final JComboBox<String> itemBox;
        private final JTextField qtyField;

        SalesPanel(AppModel model) {
            this.model = model;
            setLayout(new BorderLayout(18, 18));
            setBorder(new EmptyBorder(18, 18, 18, 18));
            setBackground(new Color(248, 245, 240));

            JPanel left = new JPanel(new BorderLayout(10, 10));
            left.setBackground(new Color(248, 245, 240));

            JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
            itemBox = new JComboBox<>();
            for (Item item : model.inventory) itemBox.addItem(item.name + " (" + money(item.price) + ")");
            qtyField = new JTextField(5);
            qtyField.setText("1");
            JButton addCart = new JButton("Add to cart");
            JButton checkout = new JButton("Checkout");
            controls.add(itemBox);
            controls.add(qtyField);
            controls.add(addCart);
            controls.add(checkout);
            left.add(controls, BorderLayout.NORTH);

            cartModel = new DefaultTableModel(new Object[]{"Item", "Price", "Qty", "Subtotal"}, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
            cartTable = new JTable(cartModel);
            left.add(new JScrollPane(cartTable), BorderLayout.CENTER);

            JPanel right = new JPanel();
            right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
            right.setBackground(new Color(252, 250, 247));
            right.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

            JLabel title = new JLabel("Checkout");
            title.setFont(new Font("SansSerif", Font.BOLD, 24));
            right.add(title);
            right.add(Box.createVerticalStrut(16));

            JLabel subtotal = new JLabel("Subtotal: " + money(currentCartTotal()));
            JLabel tax = new JLabel("Tax: " + money(currentCartTotal() * 0.05));
            JLabel total = new JLabel("Total: " + money(currentCartTotal() * 1.05));
            subtotal.setFont(new Font("SansSerif", Font.PLAIN, 18));
            tax.setFont(new Font("SansSerif", Font.PLAIN, 18));
            total.setFont(new Font("SansSerif", Font.BOLD, 20));
            right.add(subtotal);
            right.add(tax);
            right.add(total);

            add(left, BorderLayout.CENTER);
            add(right, BorderLayout.EAST);
            refreshCartTable();

            addCart.addActionListener(e -> {
                String selected = (String) itemBox.getSelectedItem();
                String name = selected.substring(0, selected.indexOf(" ("));
                Item item = model.inventory.stream().filter(i -> i.name.equals(name)).findFirst().orElse(null);
                if (item == null) return;
                int qty = Integer.parseInt(qtyField.getText());
                model.cart.add(new CartItem(item.id, item.name, item.price, qty));
                refreshCartTable();
            });

            checkout.addActionListener(e -> {
                if (model.cart.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Your cart is empty.");
                    return;
                }
                double totalAmount = currentCartTotal() * 1.05;
                int saleId = 300 + model.sales.size() + 1;
                model.sales.add(new Sale(saleId, "Web sale", totalAmount, java.time.LocalDate.now().toString()));
                model.payments.add(new Payment(400 + model.payments.size() + 1, "Store sale", saleId, "Web sale", totalAmount, "Card", java.time.LocalDateTime.now().toString().replace('T', ' ')));
                for (CartItem cartItem : model.cart) {
                    Item item = model.findItem(cartItem.id);
                    if (item != null) item.stock = Math.max(0, item.stock - cartItem.qty);
                }
                model.cart.clear();
                refreshCartTable();
                JOptionPane.showMessageDialog(this, "Checkout completed. Sale recorded.");
            });
        }

        private double currentCartTotal() {
            double total = 0;
            for (CartItem item : model.cart) total += item.price * item.qty;
            return total;
        }

        private void refreshCartTable() {
            cartModel.setRowCount(0);
            for (CartItem item : model.cart) {
                cartModel.addRow(new Object[]{item.name, money(item.price), item.qty, money(item.price * item.qty)});
            }
        }
    }

    private static class ServicesPanel extends JPanel {
        private final AppModel model;
        private final DefaultTableModel modelTable;
        private final JTable table;

        ServicesPanel(AppModel model) {
            this.model = model;
            setLayout(new BorderLayout(12, 12));
            setBorder(new EmptyBorder(18, 18, 18, 18));
            setBackground(new Color(248, 245, 240));

            modelTable = new DefaultTableModel(new Object[]{"ID", "Customer", "Contact", "Animal", "Service", "Date", "Time", "Minutes", "Fee", "Status"}, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
            table = new JTable(modelTable);

            JButton addService = new JButton("Schedule service");
            addService.addActionListener(e -> addAppointment());

            JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
            controls.add(addService);
            add(controls, BorderLayout.NORTH);
            add(new JScrollPane(table), BorderLayout.CENTER);
            refreshTable();
        }

        private void addAppointment() {
            JTextField customer = new JTextField();
            JTextField phone = new JTextField();
            JTextField animal = new JTextField();
            JTextField date = new JTextField(java.time.LocalDate.now().plusDays(1).toString());
            JTextField time = new JTextField("09:00");
            JTextField fee = new JTextField("90.00");

            Object[] fields = {
                    "Customer", customer,
                    "Phone", phone,
                    "Animal", animal,
                    "Date (YYYY-MM-DD)", date,
                    "Time (HH:mm)", time,
                    "Fee ($)", fee
            };

            int result = JOptionPane.showConfirmDialog(this, fields, "Schedule service", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                int id = 100 + model.appointments.size() + 1;
                model.appointments.add(new Appointment(
                        id,
                        customer.getText(),
                        phone.getText(),
                        animal.getText(),
                        "General Care",
                        date.getText(),
                        time.getText(),
                        45,
                        Double.parseDouble(fee.getText()),
                        "Scheduled"
                ));
                refreshTable();
            }
        }

        private void refreshTable() {
            modelTable.setRowCount(0);
            for (Appointment a : model.appointments) {
                modelTable.addRow(new Object[]{a.id, a.customer, a.phone, a.animal, a.service, a.date, a.time, a.duration, money(a.fee), a.status});
            }
        }
    }

    private static class PaymentsPanel extends JPanel {
        private final AppModel model;
        private final DefaultTableModel unpaidTable;
        private final DefaultTableModel historyTable;

        PaymentsPanel(AppModel model) {
            this.model = model;
            setLayout(new BorderLayout(12, 12));
            setBorder(new EmptyBorder(18, 18, 18, 18));
            setBackground(new Color(248, 245, 240));

            unpaidTable = new DefaultTableModel(new Object[]{"ID", "Customer", "Service", "Date", "Time", "Fee", "Paid", "Balance"}, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
            historyTable = new DefaultTableModel(new Object[]{"Payment ID", "Type", "Reference ID", "Customer", "Amount", "Method", "Date/Time"}, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };

            JPanel top = new JPanel(new GridLayout(1, 2, 12, 12));
            top.add(new JScrollPane(new JTable(unpaidTable)));
            top.add(new JScrollPane(new JTable(historyTable)));
            add(top, BorderLayout.CENTER);
            refresh();
        }

        private void refresh() {
            unpaidTable.setRowCount(0);
            for (Appointment a : model.appointments) {
                if ("Scheduled".equals(a.status)) {
                    unpaidTable.addRow(new Object[]{a.id, a.customer, a.service, a.date, a.time, money(a.fee), money(a.fee * 0.5), money(a.fee * 0.5)});
                }
            }

            historyTable.setRowCount(0);
            for (Payment p : model.payments) {
                historyTable.addRow(new Object[]{p.id, p.type, p.referenceId, p.customer, money(p.amount), p.method, p.dateTime});
            }
        }
    }

    private static class AnimalsPanel extends JPanel {
        private final AppModel model;
        private final DefaultTableModel tableModel;

        AnimalsPanel(AppModel model) {
            this.model = model;
            setLayout(new BorderLayout(12, 12));
            setBorder(new EmptyBorder(18, 18, 18, 18));
            setBackground(new Color(248, 245, 240));

            JPanel cards = new JPanel(new GridLayout(1, 3, 12, 12));
            cards.setOpaque(false);
            for (Animal a : model.animals.subList(0, Math.min(3, model.animals.size()))) {
                JPanel card = new JPanel(new BorderLayout(8, 8));
                card.setBorder(BorderFactory.createLineBorder(new Color(215, 215, 215)));
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220)),
                        new EmptyBorder(18, 18, 18, 18)));

                JLabel name = new JLabel(a.name);
                name.setFont(new Font("SansSerif", Font.BOLD, 20));
                JLabel type = new JLabel(a.type);
                JLabel status = new JLabel(a.available ? "Available" : "Recently Reserved");
                status.setOpaque(true);
                status.setBackground(a.available ? new Color(210, 240, 214) : new Color(255, 220, 220));
                status.setForeground(a.available ? new Color(23, 99, 40) : new Color(120, 30, 30));
                status.setBorder(new EmptyBorder(6, 10, 6, 10));
                card.add(name, BorderLayout.NORTH);
                card.add(type, BorderLayout.CENTER);
                card.add(status, BorderLayout.SOUTH);
                cards.add(card);
            }

            add(cards, BorderLayout.NORTH);

            tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Type", "Age", "Price", "Availability"}, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
            JTable table = new JTable(tableModel);
            add(new JScrollPane(table), BorderLayout.CENTER);
            refresh();
        }

        private void refresh() {
            tableModel.setRowCount(0);
            for (Animal a : model.animals) {
                tableModel.addRow(new Object[]{a.id, a.name, a.type, a.age + " year(s)", money(a.price), a.available ? "Available" : "Sold"});
            }
        }
    }

    private static class BreedersPanel extends JPanel {
        private final AppModel model;
        private final DefaultTableModel tableModel;

        BreedersPanel(AppModel model) {
            this.model = model;
            setLayout(new BorderLayout(12, 12));
            setBorder(new EmptyBorder(18, 18, 18, 18));
            setBackground(new Color(248, 245, 240));

            tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Breed", "Quantity", "Price", "Status"}, 0) {
                @Override public boolean isCellEditable(int row, int column) { return false; }
            };
            JTable table = new JTable(tableModel);
            add(new JScrollPane(table), BorderLayout.CENTER);
            refresh();
        }

        private void refresh() {
            tableModel.setRowCount(0);
            for (Breeder b : model.breeders) {
                tableModel.addRow(new Object[]{b.id, b.name, b.breed, b.quantity, money(b.price), b.available ? "Available" : "Unavailable"});
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new GroupProject_2().setVisible(true);
        });
    }
}
