import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PizzaGUIFrame extends JFrame {
    // Crust options
    private JRadioButton thinCrust, regularCrust, deepDish;
    private ButtonGroup crustGroup;

    // Size options
    private JComboBox<String> sizeBox;

    // Toppings
    private JCheckBox[] toppings;
    private String[] toppingNames = {
            "Pepperoni", "Mushrooms", "Onions", "Bacon",
            "Pineapple", "Jalapenos"
    };

    // Display area
    private JTextArea receiptArea;

    // Buttons
    private JButton orderButton, clearButton, quitButton;

    public PizzaGUIFrame() {
        setTitle("Pizza Order Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLayout(new BorderLayout(10, 10));

        // Create panels
        JPanel crustPanel = createCrustPanel();
        JPanel sizePanel = createSizePanel();
        JPanel toppingPanel = createToppingPanel();
        JPanel displayPanel = createDisplayPanel();
        JPanel buttonPanel = createButtonPanel();

        // Add to layout
        JPanel topPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        topPanel.add(crustPanel);
        topPanel.add(sizePanel);
        topPanel.add(toppingPanel);

        add(topPanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createCrustPanel() {
        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDish = new JRadioButton("Deep-Dish");

        crustGroup = new ButtonGroup();
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDish);

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setBorder(new TitledBorder("Crust Type"));
        panel.add(thinCrust);
        panel.add(regularCrust);
        panel.add(deepDish);

        return panel;
    }

    private JPanel createSizePanel() {
        String[] sizes = {"Small - $8", "Medium - $12", "Large - $16", "Super - $20"};
        sizeBox = new JComboBox<>(sizes);
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Pizza Size"));
        panel.add(sizeBox);
        return panel;
    }

    private JPanel createToppingPanel() {
        toppings = new JCheckBox[toppingNames.length];
        JPanel panel = new JPanel(new GridLayout(toppingNames.length, 1));
        panel.setBorder(new TitledBorder("Toppings ($1 each)"));

        for (int i = 0; i < toppingNames.length; i++) {
            toppings[i] = new JCheckBox(toppingNames[i]);
            panel.add(toppings[i]);
        }

        return panel;
    }

    private JPanel createDisplayPanel() {
        receiptArea = new JTextArea(12, 50);
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        scrollPane.setBorder(new TitledBorder("Order Receipt"));

        JPanel panel = new JPanel();
        panel.add(scrollPane);
        return panel;
    }

    private JPanel createButtonPanel() {
        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");

        orderButton.addActionListener(new OrderListener());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> quitApp());

        JPanel panel = new JPanel();
        panel.add(orderButton);
        panel.add(clearButton);
        panel.add(quitButton);

        return panel;
    }

    private class OrderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String crust = getSelectedCrust();
            String size = (String) sizeBox.getSelectedItem();
            if (crust == null) {
                JOptionPane.showMessageDialog(PizzaGUIFrame.this, "Please select a crust type.");
                return;
            }

            double basePrice = getBasePrice(size);
            StringBuilder receipt = new StringBuilder();
            receipt.append("=========================================\n");
            receipt.append("Type of Crust & Size\t\tPrice\n");
            receipt.append("-----------------------------------------\n");
            receipt.append(crust + " - " + size + "\t\n");

            double toppingsCost = 0;
            receipt.append("\nToppings:\n");
            for (JCheckBox topping : toppings) {
                if (topping.isSelected()) {
                    receipt.append(topping.getText()).append("\t$1.00\n");
                    toppingsCost += 1.00;
                }
            }

            double subtotal = basePrice + toppingsCost;
            double tax = subtotal * 0.07;
            double total = subtotal + tax;

            receipt.append("\n-----------------------------------------\n");
            receipt.append(String.format("Sub-total:\t\t$%.2f\n", subtotal));
            receipt.append(String.format("Tax (7%%):\t\t$%.2f\n", tax));
            receipt.append("-----------------------------------------\n");
            receipt.append(String.format("Total:\t\t\t$%.2f\n", total));
            receipt.append("\n=========================================\n");

            receiptArea.setText(receipt.toString());
        }
    }

    private String getSelectedCrust() {
        if (thinCrust.isSelected()) return "Thin Crust";
        if (regularCrust.isSelected()) return "Regular Crust";
        if (deepDish.isSelected()) return "Deep-Dish";
        return null;
    }

    private double getBasePrice(String size) {
        if (size.contains("Small")) return 8.00;
        if (size.contains("Medium")) return 12.00;
        if (size.contains("Large")) return 16.00;
        if (size.contains("Super")) return 20.00;
        return 0;
    }

    private void clearForm() {
        crustGroup.clearSelection();
        sizeBox.setSelectedIndex(0);
        for (JCheckBox topping : toppings) {
            topping.setSelected(false);
        }
        receiptArea.setText("");
    }

    private void quitApp() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to quit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
