import java.text.DecimalFormat;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class gui implements ActionListener{

    private static final int WINDOW_WIDTH= 285;//pixels
    private static final int WINDOW_HEIGHT= 510;//pixels
    private static final int FIELD_WIDTH= 20;//characters
    private static final int AREA_WIDTH= 40;//characters
    private static ArrayList<String> id = new ArrayList<>();
    private static ArrayList<String> p = new ArrayList<>();
    private static ArrayList<String> title = new ArrayList<>();
    private static ArrayList<String> confirmed = new ArrayList<>();
    private static int t = 1;
    private static double subtotal = 0;
    private static double orderTotal = 0;
    private static double taxTotal = 0;


    //layout
    private static final FlowLayout LAYOUT_STYLE=   new FlowLayout();

    //popup box
    private JFrame window= new JFrame("E-Store");



    //labels to add to container
    private JLabel numItems= new JLabel("Enter number of items");
    private JTextField numItemsText= new JTextField(FIELD_WIDTH);

    private JLabel bookID = new JLabel("Enter Book ID for item #1");
    private JTextField bookIDField = new JTextField(FIELD_WIDTH);

    private JLabel quantity = new JLabel("Enter quantity of item #1");
    private JTextField quantityField = new JTextField(FIELD_WIDTH);

    private JLabel info = new JLabel("Book info");
    private JTextField infoField = new JTextField(FIELD_WIDTH);

    private JLabel price = new JLabel("Subtotal");
    private JTextField priceField = new JTextField(FIELD_WIDTH);

    private JButton processButton = new JButton("Process Item #1");
    private JButton confirmButton = new JButton("Confirm Item #1");
    private JButton viewButton = new JButton("View Order");
    private JButton finishButton = new JButton("Finish Order");
    private JButton newOrder = new JButton("New Order");
    private JButton exit = new JButton("Exit");



    //constructor
    public gui() {
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //add components to the container
        Container c = window.getContentPane();
        c.setLayout(LAYOUT_STYLE);

        processButton.addActionListener(this);
        confirmButton.addActionListener(this);
        viewButton.addActionListener(this);
        newOrder.addActionListener(this);
        exit.addActionListener(this);
        finishButton.addActionListener(this);

        c.add(numItems);
        c.add(numItemsText);
        c.add(bookID);
        c.add(bookIDField);
        c.add(quantity);
        c.add(quantityField);
        c.add(info);
        c.add(infoField);
        c.add(price);
        c.add(priceField);
        c.add(processButton);
        c.add(confirmButton);
        c.add(viewButton);
        c.add(finishButton);
        c.add(newOrder);
        c.add(exit);

        confirmButton.setEnabled(false);
        viewButton.setEnabled(false);
        finishButton.setEnabled(false);
        window.setVisible(true);
    }

    //putting the actions to each button
    @Override
    public void actionPerformed(ActionEvent e) {
        int quant = 0;
        double disc = 0;
        double disc1 = 0;
        double disc2 = 0;
        double pri = 0;
        double pric = 0;
        String output = "";
        DecimalFormat df = new DecimalFormat("#.##");


        if(e.getSource() == processButton) {
            //preventing a crash if quantity is null
            if(!Objects.equals(quantityField.getText(), "")) {
                quant = Integer.parseInt(quantityField.getText());
            }

            if(quant < 4)
            {
                disc = 0;
                disc1 = 0;
            }
            else if(quant >= 5 && quant <= 9)
            {
                disc = 10;
                disc1 = .1;
            }
            else if (quant >= 10 && quant <= 14)
            {
                disc = 15;
                disc1 = .15;
            }

            //going through all of the database
            for(int i = 0; i < id.size(); i++) {
                if(bookIDField.getText().equals(id.get(i))) {
                    priceField.setText(p.get(i));

                    //price of book
                    pri = Double.parseDouble(p.get(i));
                    disc2 = (pri * quant) * disc1;
                    pric = (pri * quant) - disc2;

                    subtotal += pric;
                    priceField.setText("$" + df.format(subtotal));
                    infoField.setText(id.get(i) + title.get(i) + " $" + pri + " " + quant + " " + disc + "% $" + df.format(pric));
                }
            }
            if(infoField.getText().equals("")) {

                JOptionPane.showMessageDialog(window, "Book ID: " + bookIDField.getText() + " not found");
            }
            confirmButton.setEnabled(true);
            processButton.setEnabled(false);
        }
        else if(e.getSource() == confirmButton) {
                confirmed.add(infoField.getText());
                JOptionPane.showMessageDialog(window, "Item #" + t + " confirmed");

                t++;
                processButton.setText("Process Item #" + t);
                confirmButton.setText("Confirm Item #" + t);
                quantity.setText("Enter quantity of item #" + t);
                bookID.setText("Enter Book ID for item #" + t);
                info.setText("Book info");

                //reset everything
                bookIDField.setText("");
                quantityField.setText("");
                infoField.setText("");

                //disable buttons
                processButton.setEnabled(true);
                viewButton.setEnabled(true);
                finishButton.setEnabled(true);
                confirmButton.setEnabled(false);
        }
        else if(e.getSource() == viewButton){
            for(int i = 0; i < confirmed.size(); i++)
            {
                output += i+1 + ". " + confirmed.get(i) + "\n";
            }
                JOptionPane.showMessageDialog(window, output);
        }
        else if(e.getSource() == newOrder) {
            t = 1;
           processButton.setText("Process Item #1");
           confirmButton.setText("Confirm Item #1");
           quantity.setText("Enter quantity of item #1");
           bookID.setText("Enter Book ID for item #1");
           info.setText("Book info");

           //reset everything
           numItemsText.setText("");
           bookIDField.setText("");
           quantityField.setText("");
           infoField.setText("");
           priceField.setText("");
           confirmed.clear();

           //button reset
            processButton.setEnabled(true);
           confirmButton.setEnabled(false);
           viewButton.setEnabled(false);
           finishButton.setEnabled(false);

        }
        else if(e.getSource() == finishButton) {

            output = "";
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            output += "Date: " + formatter.format(date) + "PM EST\n";
            output += "Number of line items: " + numItemsText.getText() + "\n";
            output += "Item# / ID / Title / Price / Qty / Disc% / Subtotal:\n";
            for(int i = 0; i < confirmed.size(); i++)
            {
                output += i+1 + ". " + confirmed.get(i) + "\n";
            }
            output += "Order Subtotal: $" + df.format(subtotal) + "\n";
            output += "Tax rate: 6%\n";
            taxTotal = subtotal * .06;
            orderTotal = taxTotal + subtotal;
            output += "Tax amount: $" + df.format(taxTotal) + "\n";
            output += "Order total: $" + df.format(orderTotal) + "\n";
            output += "Thanks for shopping!";
            JOptionPane.showMessageDialog(window, output);

            }
        else if(e.getSource() == exit) {
            System.exit(0);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        int i = 0;
        gui gui = new gui();
        File file = new File("src/inventory.txt");
        Scanner sc = new Scanner(file);
        String str;


        while (sc.hasNextLine()) {
            str = sc.nextLine();
            String arr[] = str.split(",");
            id.add(arr[0]);
            title.add(arr[1]);
            p.add(arr[2]);
        }
    }
}
