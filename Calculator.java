import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame {
    private JTextField display;
    private StringBuilder input;
    private double result = 0;
    private String operation = "";
    private boolean startNewNumber = true;

    public Calculator() {
        setTitle("Calculator");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        input = new StringBuilder();
        
        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Display panel
        display = new JTextField();
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setText("0");
        mainPanel.add(display, BorderLayout.NORTH);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));
        
        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C", "←", "%", "√"
        };
        
        for (String buttonLabel : buttons) {
            JButton button = new JButton(buttonLabel);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.setFocusable(false);
            
            // Color operators differently
            if (buttonLabel.matches("[/\\*+\\-=√%]") || buttonLabel.equals("C")) {
                button.setBackground(new Color(255, 153, 0));
                button.setForeground(Color.WHITE);
            } else if (buttonLabel.equals("←")) {
                button.setBackground(new Color(200, 200, 200));
            }
            
            button.addActionListener(new ButtonClickListener(buttonLabel));
            buttonPanel.add(button);
        }
        
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }
    
    private class ButtonClickListener implements ActionListener {
        private String buttonLabel;
        
        public ButtonClickListener(String label) {
            this.buttonLabel = label;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttonLabel.matches("\\d")) {
                // Number button
                if (startNewNumber) {
                    input.setLength(0);
                    startNewNumber = false;
                }
                input.append(buttonLabel);
                display.setText(input.toString());
            } 
            else if (buttonLabel.equals(".")) {
                // Decimal point
                if (startNewNumber) {
                    input.setLength(0);
                    input.append("0");
                    startNewNumber = false;
                }
                if (!input.toString().contains(".")) {
                    input.append(".");
                    display.setText(input.toString());
                }
            } 
            else if (buttonLabel.matches("[/\\*+\\-]")) {
                // Operation button
                if (!input.toString().isEmpty()) {
                    if (!operation.isEmpty()) {
                        calculate();
                    } else {
                        result = Double.parseDouble(input.toString());
                    }
                    operation = buttonLabel;
                    startNewNumber = true;
                }
            } 
            else if (buttonLabel.equals("=")) {
                // Equals button
                if (!operation.isEmpty() && !input.toString().isEmpty()) {
                    calculate();
                    operation = "";
                    startNewNumber = true;
                }
            } 
            else if (buttonLabel.equals("C")) {
                // Clear button
                input.setLength(0);
                result = 0;
                operation = "";
                startNewNumber = true;
                display.setText("0");
            } 
            else if (buttonLabel.equals("←")) {
                // Backspace button
                if (input.length() > 0) {
                    input.deleteCharAt(input.length() - 1);
                    display.setText(input.toString().isEmpty() ? "0" : input.toString());
                }
            } 
            else if (buttonLabel.equals("√")) {
                // Square root button
                if (!input.toString().isEmpty()) {
                    double num = Double.parseDouble(input.toString());
                    double sqrt = Math.sqrt(num);
                    display.setText(String.valueOf(sqrt));
                    input.setLength(0);
                    input.append(sqrt);
                    startNewNumber = true;
                }
            } 
            else if (buttonLabel.equals("%")) {
                // Percentage button
                if (!input.toString().isEmpty()) {
                    double num = Double.parseDouble(input.toString());
                    double percentage = num / 100;
                    display.setText(String.valueOf(percentage));
                    input.setLength(0);
                    input.append(percentage);
                    startNewNumber = true;
                }
            }
        }
    }
    
    private void calculate() {
        if (input.toString().isEmpty()) return;
        
        double secondNum = Double.parseDouble(input.toString());
        double calcResult = 0;
        
        switch (operation) {
            case "+":
                calcResult = result + secondNum;
                break;
            case "-":
                calcResult = result - secondNum;
                break;
            case "*":
                calcResult = result * secondNum;
                break;
            case "/":
                if (secondNum != 0) {
                    calcResult = result / secondNum;
                } else {
                    display.setText("Error: Division by 0");
                    input.setLength(0);
                    result = 0;
                    return;
                }
                break;
        }
        
        // Format result to avoid too many decimal places
        if (calcResult == (long) calcResult) {
            display.setText(String.valueOf((long) calcResult));
        } else {
            display.setText(String.valueOf(calcResult));
        }
        
        input.setLength(0);
        input.append(calcResult);
        result = calcResult;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator());
    }
}