import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        // Top-level frame in which game components live
        final JFrame frame = new JFrame("DINER DASH");
        frame.setLocation(300, 300);
        
        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        final JLabel timeLeft = new JLabel("45"); // for displaying overall game timer
        status_panel.add(status);
        status_panel.add(timeLeft);

        // instructions shown when game is run, before the game is displayed
        JOptionPane instructions = new JOptionPane();
        String text = "Welcome to Diner Dash, the game where you seat and serve customers at a "
                + "diner!\nHere's how to play:\nClick the first customer in line (left side). Next,"
                + " click an empty table where you would like to seat them. The image displayed "
                + "above the customer will show how many coffees they want.\nClick the coffee maker"
                + " to make coffee. Click again to pick up the coffee, and then click the table of "
                + "the customer you would like to serve. Customers you click from the line and "
                + "coffee you pick up will show in the side panel.\nIf you can serve all of the "
                + "customers before time runs out, you win!\n\n\nNote: In this version of Diner "
                + "Dash, food does not burn and customers do not get angry and leave. You can only "
                + "pick up one cup of coffee at a time, and you can only seat one customer at a "
                + "time.\nYou can continue serving customers even if you click a customer to seat.";
        JOptionPane.showConfirmDialog(instructions, text, "INSTRUCTIONS", 
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        // Main playing area
        final Cafe cafe = new Cafe(status, timeLeft);
        frame.add(cafe, BorderLayout.CENTER);

        // Reset button
        final JPanel reset_panel = new JPanel();
        frame.add(reset_panel, BorderLayout.NORTH);
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cafe.reset();
            }
        });
        reset_panel.add(reset);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        // Start game
        cafe.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}