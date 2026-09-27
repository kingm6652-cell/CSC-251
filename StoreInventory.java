import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class StoreInventory {
    private static final int LOW_STOCK_LIMIT = 5;

    private final Map<String, Product> products = new LinkedHashMap<>();

    public static void main(String[] args) {
        StoreInventory inventory = new StoreInventory();
        inventory.loadSampleProducts();
        SwingUtilities.invokeLater(inventory::showWindow);
    }

    private void loadSampleProducts() {
        addProduct(new Product("TS-1001", "50 lb. All-Purpose Feed", "Livestock", 24.99, 12));
        addProduct(new Product("TS-1002", "Heavy-Duty Garden Hose", "Lawn and Garden", 34.99, 8));
        addProduct(new Product("TS-1003", "LED Work Light", "Tools", 19.99, 4));
        addProduct(new Product("TS-1004", "Steel Fence T-Post", "Fencing", 6.49, 45));
        addProduct(new Product("TS-1005", "Water-Resistant Work Boots", "Clothing", 79.99, 3));
        addProduct(new Product("TS-1006", "Horse Feed Pellets", "Livestock", 28.50, 10));
        addProduct(new Product("TS-1007", "Wheelbarrow", "Lawn and Garden", 119.99, 6));
        addProduct(new Product("TS-1008", "Bolt Cutter", "Tools", 29.95, 7));
        addProduct(new Product("TS-1009", "Wooden Fence Gate", "Fencing", 89.00, 2));
        addProduct(new Product("TS-1010", "Thermal Work Gloves", "Clothing", 24.75, 15));
        addProduct(new Product("TS-1011", "Chicken Coop Bedding", "Livestock", 16.25, 18));
        addProduct(new Product("TS-1012", "Garden Spade", "Lawn and Garden", 18.99, 11));
    }

    private void addProduct(Product product) {
        products.put(product.getSku(), product);
    }

    private void showWindow() {
        JFrame frame = new JFrame("Tractor Supply Store Inventory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 560);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(239, 248, 236));

        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(new Color(165, 214, 138));
        bannerPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel titleLabel = new JLabel("Farm Supply Inventory");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(23, 80, 30));
        bannerPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel subtitleLabel = new JLabel("Healthy stock for a thriving farm.");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(34, 82, 31));
        bannerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        JTextField searchField = new JTextField(22);
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(81, 153, 77), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));

        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[] {"SKU", "Product", "Category", "Price", "Quantity"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(28);
        table.setGridColor(new Color(205, 227, 192));
        table.setSelectionBackground(new Color(204, 236, 194));
        table.setSelectionForeground(new Color(18, 56, 26));
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setBackground(new Color(30, 110, 60));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(isSelected ? new Color(204, 236, 194) : Color.WHITE);
                c.setForeground(new Color(28, 52, 34));
                return c;
            }
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(232, 245, 228));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);

        JButton addButton = new JButton("Add Product");
        JButton receiveButton = new JButton("Receive Stock");
        JButton sellButton = new JButton("Sell Item");
        JButton lowStockButton = new JButton("Low Stock");
        JButton showAllButton = new JButton("Show All");

        addButton.setBackground(new Color(220, 245, 214));
        addButton.setForeground(Color.BLACK);
        receiveButton.setBackground(new Color(210, 238, 199));
        receiveButton.setForeground(Color.BLACK);
        sellButton.setBackground(new Color(196, 230, 185));
        sellButton.setForeground(Color.BLACK);
        lowStockButton.setBackground(new Color(185, 222, 174));
        lowStockButton.setForeground(Color.BLACK);
        showAllButton.setBackground(new Color(172, 214, 160));
        showAllButton.setForeground(Color.BLACK);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(new Color(243, 249, 240));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));
        actionPanel.add(addButton);
        actionPanel.add(receiveButton);
        actionPanel.add(sellButton);
        actionPanel.add(lowStockButton);
        actionPanel.add(showAllButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(239, 248, 236));
        topPanel.add(bannerPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(actionPanel, BorderLayout.SOUTH);

        frame.add(topPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));
        scrollPane.getViewport().setBackground(Color.WHITE);
        frame.add(scrollPane, BorderLayout.CENTER);

        Runnable refreshAll = () -> refreshTable(tableModel, searchField.getText(), false);
        searchField.addActionListener(event -> refreshAll.run());
        addButton.addActionListener(event -> addProductDialog(frame, refreshAll));
        receiveButton.addActionListener(event -> changeSelectedStock(frame, table, true, refreshAll));
        sellButton.addActionListener(event -> changeSelectedStock(frame, table, false, refreshAll));
        lowStockButton.addActionListener(event -> refreshTable(tableModel, searchField.getText(), true));
        showAllButton.addActionListener(event -> refreshAll.run());

        refreshAll.run();
        frame.setVisible(true);
    }

    private void refreshTable(DefaultTableModel tableModel, String searchText, boolean lowStockOnly) {
        String searchTerm = searchText.trim().toLowerCase();
        tableModel.setRowCount(0);
        for (Product product : products.values()) {
            if (product.matches(searchTerm)
                    && (!lowStockOnly || product.getQuantity() <= LOW_STOCK_LIMIT)) {
                tableModel.addRow(new Object[] {
                        product.getSku(), product.getName(), product.getCategory(),
                        String.format("$%.2f", product.getPrice()), product.getQuantity()
                });
            }
        }
    }

    private void addProductDialog(JFrame frame, Runnable refreshAll) {
        JTextField skuField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField quantityField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("SKU:"));
        panel.add(skuField);
        panel.add(new JLabel("Product name:"));
        panel.add(nameField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Product",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String sku = skuField.getText().trim().toUpperCase();
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());
            if (sku.isEmpty() || name.isEmpty() || category.isEmpty() || price < 0 || quantity < 0) {
                throw new IllegalArgumentException();
            }
            if (products.containsKey(sku)) {
                JOptionPane.showMessageDialog(frame, "That SKU already exists.", "Cannot Add Product",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            addProduct(new Product(sku, name, category, price, quantity));
            refreshAll.run();
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(frame, "Enter valid values for every field.", "Invalid Product",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changeSelectedStock(JFrame frame, JTable table, boolean receivingStock, Runnable refreshAll) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame, "Select a product first.", "No Product Selected",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String sku = table.getValueAt(selectedRow, 0).toString();
        Product product = products.get(sku);
        String action = receivingStock ? "received" : "sold";
        String input = JOptionPane.showInputDialog(frame,
                "How many units were " + action + "?", "Update Stock", JOptionPane.QUESTION_MESSAGE);
        if (input == null) {
            return;
        }

        try {
            int quantity = Integer.parseInt(input.trim());
            if (quantity < 0 || (!receivingStock && quantity > product.getQuantity())) {
                throw new IllegalArgumentException();
            }
            product.adjustQuantity(receivingStock ? quantity : -quantity);
            refreshAll.run();
        } catch (IllegalArgumentException exception) {
            String message = receivingStock
                    ? "Enter a non-negative whole number."
                    : "Enter a valid quantity no greater than " + product.getQuantity() + ".";
            JOptionPane.showMessageDialog(frame, message, "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class Product {
        private final String sku;
        private final String name;
        private final String category;
        private final double price;
        private int quantity;

        Product(String sku, String name, String category, double price, int quantity) {
            this.sku = sku;
            this.name = name;
            this.category = category;
            this.price = price;
            this.quantity = quantity;
        }

        boolean matches(String searchTerm) {
            return sku.toLowerCase().contains(searchTerm)
                    || name.toLowerCase().contains(searchTerm)
                    || category.toLowerCase().contains(searchTerm);
        }

        void adjustQuantity(int amount) {
            quantity += amount;
        }

        String getSku() {
            return sku;
        }

        String getName() {
            return name;
        }

        String getCategory() {
            return category;
        }

        double getPrice() {
            return price;
        }

        int getQuantity() {
            return quantity;
        }

        @Override
        public String toString() {
            return String.format("%-10s %-30s %-18s $%9.2f %10d", sku, name, category, price, quantity);
        }
    }
}