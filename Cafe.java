import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class Cafe extends JPanel {
    // images
    public static final String IMG_FILE_ONE = "files/EmptyCafe.png";
    public static final String IMG_FILE_TWO = "files/CoffeeMaker.png";
    public static final String IMG_FILE_THREE = "files/OneCup.png";
    public static final String IMG_FILE_FOUR = "files/TwoCups.png";
    public static final String IMG_FILE_FIVE = "files/ThreeCups.png";
    public static final String IMG_FILE_SIX = "files/Ready.png";
    public static final String IMG_FILE_SEVEN = "files/Cup.png";
    public static final String IMG_FILE_EIGHT = "files/SidePanel.png";
    public static final String IMG_FILE_NINE = "files/CupForSidePanel.png";
    
    // dimensions and game info
    public static final int MAX_TABLES = 3;
    public static final int MAX_FOODS = 3;
    public static final int CAFE_WIDTH = 780;
    public static final int CAFE_HEIGHT = 800;
   
    // private fields
    private List<Customer> line;
    private List<Customer> seated; 
    private int[][] tables;
    private Table tableOne;
    private Table tableTwo;
    private Table tableThree;
    private Customer customerOne;
    private Customer customerTwo;
    private Customer customerThree;
    private Customer customerFour;
    private Customer customerFive;
    private Customer toBeSeated;
    private Customer justServed;
    private MouseListener listener;
    private Timer customerTimer;
    private Timer cupTimer;
    private Timer gameTimer;
    private int time;
    private boolean canSeat = false; 
    private boolean canServe = false;
    private boolean canMake = true;
    
    // JLabels
    private JLabel status; // Current game status text
    private JLabel timeLeft; 

    // BufferedImages
    private static BufferedImage img1;
    private static BufferedImage img2;
    private static BufferedImage img3;
    private static BufferedImage img4;
    private static BufferedImage img5;
    private static BufferedImage img6;
    private static BufferedImage img7;
    private static BufferedImage img8;
    private static BufferedImage img9;
    
    /**
     * Constructs a {@code Cafe}. Initializes JLabel fields, reads images, and starts 45 second 
     * timer for the game.
     * 
     * @param status JLabel for the current status of the game (running, won, lost) 
     * @param timeLeft JLabel for displaying how much time the user has left to win the game
     */
    public Cafe(JLabel status, JLabel timeLeft) {
        this.status = status;
        this.timeLeft = timeLeft;
        this.time = 45; // user has 45 seconds to win
        
        // overall game timer, started in the reset method
        gameTimer = new Timer(1000, new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                // update the timeLeft JLabel every second 
                timeLeft.setText("" + (--time));
                
                // check if the user has won or lost
                isGameOver();
            }
        });
        
        try {
            if (img1 == null) {
                img1 = ImageIO.read(new File(IMG_FILE_ONE));
            }
            
            if (img2 == null) {
                img2 = ImageIO.read(new File(IMG_FILE_TWO));
            }
            
            if (img3 == null) {
                img3 = ImageIO.read(new File(IMG_FILE_THREE));
            }
            
            if (img4 == null) {
                img4 = ImageIO.read(new File(IMG_FILE_FOUR));
            }
            
            if (img5 == null) {
                img5 = ImageIO.read(new File(IMG_FILE_FIVE));
            }
            
            if (img6 == null) {
                img6 = ImageIO.read(new File(IMG_FILE_SIX));
            }
            
            if (img7 == null) {
                img7 = ImageIO.read(new File(IMG_FILE_SEVEN));
            }
            
            if (img8 == null) {
                img8 = ImageIO.read(new File(IMG_FILE_EIGHT));
            }
            
            if (img9 == null) {
                img9 = ImageIO.read(new File(IMG_FILE_NINE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    } 
    
    /**
     * Resets the initial state of the game. Also used to start the game.
     */
    public void reset() {       
        if (listener != null) {
            // if the game is reset before being won or lost
            this.removeMouseListener(listener);
        } else {
            this.listener = new Mouse();
        }
        
        // allows for user interaction
        this.addMouseListener(this.listener);
        
        // initialize fields
        line = new LinkedList<Customer>();
        seated = new LinkedList<Customer>(); 
        tables = new int[MAX_TABLES][MAX_FOODS];
        
        customerOne = new Customer(1, 130, 600, 1);
        customerTwo = new Customer(3, 230, 600, 2);
        customerThree = new Customer(2, 330, 600, 1);
        customerFour = new Customer(2, 430, 600, 3);
        customerFive = new Customer(1, 530, 600, 2);
        
        tableOne = new Table(1, 303, 416);
        tableTwo = new Table(2, 409, 353);
        tableThree = new Table(3, 512, 290);
        
        canSeat = false; 
        canServe = false;
        canMake = true;
        toBeSeated = null; 
        justServed = null;
        time = 45;
        
        // fill tables array 
        for (int i = 0; i < MAX_TABLES; i++) {
            for (int j = 0; j < MAX_FOODS; j++) {
                tables[i][j] = 0;
            }
        }
        
        // add customers to the line
        line.add(customerOne);
        line.add(customerTwo);
        line.add(customerThree);
        line.add(customerFour);
        line.add(customerFive);
        
        // start overall timer and set text on JLabels 
        gameTimer.start();
        status.setText("Running...");
        timeLeft.setText("" + time);
                
        repaint();
    }
    
    /**
     * Draws the playing area and its components. Updates fields as needed to ensure correct images
     * are displayed.
     * 
     * @param g Graphics context
     */
    public void paint(Graphics g) {  
        g.drawImage(img1, 0, 0, null); // background
        g.drawImage(img2, 235, 235, 70, 70, null); // coffee maker
        g.drawImage(img8, 650, 475, 100, 150, null); // side panel
        
        // update fields to allow for correct images 
        if (canSeat) { 
            seated.add(toBeSeated);
            
            canSeat = false;
            toBeSeated = null;
        }
        
        // draw customer in side panel if customer was selected from line
        if (toBeSeated != null) {
            toBeSeated.setXPos(675);
            toBeSeated.setYPos(490);
            toBeSeated.draw(g, 50, 70);
        }
        
        // draw cup in side panel if a cup was picked up and the user can serve a customer
        if (canServe) {
            g.drawImage(img9, 675, 565, 50, 50, null);
        }
        
        // draw the line of customers 
        for (Customer c : line) {
            c.draw(g, Customer.WIDTH, Customer.HEIGHT);
        }  
        
        // customer to be removed from the game state 
        Customer toBeRemoved = null; 
        
        // seated customers
        for (Customer c : seated) {
            int numFoodsLeft = 0;

            for (int i = 0; i < MAX_FOODS; i++) {
                if (tables[c.getTableNumber() - 1][i] == 1) {
                    ++numFoodsLeft;
                }
            }
            
            // update images above customers
            if (numFoodsLeft == 1) {
                g.drawImage(img3, c.getXPos() + 10, c.getYPos() - 90, 80, 100, null);
            } else if (numFoodsLeft == 2) {
                g.drawImage(img4, c.getXPos() + 10, c.getYPos() - 90, 80, 100, null);
            } else if (numFoodsLeft == 3) {
                g.drawImage(img5, c.getXPos() + 10, c.getYPos() - 90, 80, 100, null);
            } else if (numFoodsLeft == 0) {
                // draw check mark above customer
                g.drawImage(img6, c.getXPos() + 10, c.getYPos() - 90, 80, 100, null);
                toBeRemoved = c;
                
                // display the customer for half a second before removing them
                customerTimer = new Timer(500, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        customerTimer.stop();
                    }
                });
                
                customerTimer.start(); 
            } 
            
            // draw seated customer
            c.draw(g, Customer.WIDTH, Customer.HEIGHT);
        }
        
        if (toBeRemoved != null) {
            removeCustomer(toBeRemoved);
        }
        
        // draw tables
        tableOne.draw(g);
        tableTwo.draw(g);
        tableThree.draw(g);
        
        // draw check mark above coffee maker when coffee can be picked up to serve
        if (!canMake) {
            g.drawImage(img6, 235, 145, 80, 100, null);
        } 
        
        // draw image of a cup on the table of a customer that was just served
        if (!canServe && justServed != null) {
            int tableNumber = justServed.getTableNumber();
            int xPos = 0;
            int yPos = 0;
            
            if (tableNumber == 1) {
                xPos = tableOne.getXPos();
                yPos = tableOne.getYPos();
            } else if (tableNumber == 2) {
                xPos = tableTwo.getXPos();
                yPos = tableTwo.getYPos();
            } else if (tableNumber == 3) {
                xPos = tableThree.getXPos();
                yPos = tableThree.getYPos();
            }
            
            g.drawImage(img7, xPos + 15, yPos - 10, 50, 50, null);
            
            // image is painted over after half a second
            cupTimer = new Timer(500, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cupTimer.stop();
                    repaint();
                    justServed = null;
                }
            });
            
            cupTimer.start();
        }
    }
    
    /**
     * Removes a customer from the line by updating the appropriate fields. 
     */
    public void removeFromLine() {
        // remove the first customer in line
        toBeSeated = line.remove(0);
        
        // shift over the remaining customers 
        for (Customer c : line) {
            c.setXPos(c.getXPos() - 60);
        }

        repaint();  
    }
    
    /**
     * Seat the given customer at the given table.
     * 
     * @param table Table where the customer should be seated 
     * @param toBeSeated Customer that will be seated at a table
     */
    public void addToTable(Table table, Customer toBeSeated) {
        if (table.getCustomer() == null) { // check if table is empty
            // update tables array
            for (int i = 0; i < MAX_FOODS; i++) {
                if (i < toBeSeated.getNumFoods()) {
                    tables[table.getTableNumber() - 1][i] = 1; // food 
                } else {
                    tables[table.getTableNumber() - 1][i] = 0; // no food 
                }
            }
            
            // set new position of the customer 
            toBeSeated.setXPos(table.getXPos() - 30);
            toBeSeated.setYPos(table.getYPos() - 60);

            repaint();
            canSeat = true; 
            
            // update that the table is no longer empty
            table.setCustomer(toBeSeated);
            
            // update the customer's table number 
            toBeSeated.setTableNumber(table.getTableNumber());
        }
    }
    
    /**
     * Serves the given customer by updating fields. 
     * 
     * @param toServe Customer being served
     */
    public void serve(Customer toServe) {        
        int tableNumber = toServe.getTableNumber();
        
        // update tables field
        for (int i = 0; i < MAX_FOODS - 1; i++) {
            // one or two cups left
            if (tables[tableNumber - 1][i] == 1 && tables[tableNumber - 1][i + 1] == 0) { 
                tables[tableNumber - 1][i] = 0;
            } else if (i == 1 && tables[tableNumber - 1][i + 1] == 1) { // three cups left
                tables[tableNumber - 1][i + 1] = 0;
            }
        }
        
        justServed = toServe; // to be used for drawing the cup on table image 
        canServe = false; 
        repaint();
    }
    
    /**
     * Removes the given customer from the game. Called when the customer's number of foods reaches
     * 0.
     * 
     * @param remove Customer being removed
     */
    public void removeCustomer(Customer remove) {
        int tableNumber = remove.getTableNumber();
                
        // the customer's table becomes empty 
        if (tableNumber == 1) {
            tableOne.setCustomer(null);
        } else if (tableNumber == 2) {
            tableTwo.setCustomer(null);
        } else if (tableNumber == 3) {
            tableThree.setCustomer(null);
        }
        
        // customer is no longer seated 
        seated.remove(remove);
    }
    
    /**
     * Checks if the user has won or lost the game. 
     */
    public void isGameOver() {
        /* in both conditions, JLabel text is updated, the game timer is stopped, and the mouse
         * listener is removed to ensure the user cannot keep interacting with the game 
         */
        if (seated.isEmpty() && line.isEmpty() && toBeSeated == null) { // winning condition
            status.setText("You won!");
            gameTimer.stop();
            this.removeMouseListener(listener);
        } else if (time == 0) { // losing condition, time ran out
            status.setText("You lost...");
            gameTimer.stop();
            this.removeMouseListener(listener);
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(CAFE_WIDTH, CAFE_HEIGHT);
    }
    
    // inner class for user mouse interactions 
    private class Mouse extends MouseAdapter implements MouseListener {    
        @Override
        public void mouseClicked(MouseEvent arg) {
            // position of the mouse click
            int xPos = arg.getX();
            int yPos = arg.getY(); 
            
            // removing the first customer in line
            if (!line.isEmpty() && toBeSeated == null) { 
                if (xPos >= line.get(0).getXPos() && 
                        xPos <= line.get(0).getXPos() + Customer.WIDTH && 
                        yPos >= line.get(0).getYPos() && 
                        yPos <= line.get(0).getYPos() + Customer.HEIGHT) { 
                    removeFromLine();
                }
            }
            
            // seat a customer at a table if a customer was removed from the line
            if (toBeSeated != null) {  
                if (xPos >= tableOne.getXPos() && xPos <= tableOne.getXPos() + Table.SIZE && 
                        yPos >= tableOne.getYPos() &&  yPos <= tableOne.getYPos() + Table.SIZE) { 
                    addToTable(tableOne, toBeSeated);
                } else if (xPos >= tableTwo.getXPos() &&  
                        xPos <= tableTwo.getXPos() + Table.SIZE && yPos >= tableTwo.getYPos() && 
                        yPos <= tableTwo.getYPos() + Table.SIZE) {
                    addToTable(tableTwo, toBeSeated);
                } else if (xPos >= tableThree.getXPos() && xPos <= tableThree.getXPos() + Table.SIZE
                        && yPos >= tableThree.getYPos() && 
                        yPos <= tableThree.getYPos() + Table.SIZE) {
                    addToTable(tableThree, toBeSeated);
                }
            } 
            
            // serve a table
            if (canServe && !canSeat) { // prevents seating and serving in one click
                if (tableOne.getCustomer() != null && xPos >= tableOne.getXPos() && 
                        xPos <= tableOne.getXPos() + Table.SIZE && yPos >= tableOne.getYPos() && 
                        yPos <= tableOne.getYPos() + Table.SIZE) { 
                    serve(tableOne.getCustomer());
                } else if (tableTwo.getCustomer() != null && xPos >= tableTwo.getXPos() &&  
                        xPos <= tableTwo.getXPos() + Table.SIZE && yPos >= tableTwo.getYPos() &&
                        yPos <= tableTwo.getYPos() + Table.SIZE) {
                    serve(tableTwo.getCustomer());
                } else if (tableThree.getCustomer() != null && xPos >= tableThree.getXPos() &&  
                        xPos <= tableThree.getXPos() + Table.SIZE && yPos >= tableThree.getYPos() &&
                        yPos <= tableThree.getYPos() + Table.SIZE) {
                    serve(tableThree.getCustomer());
                }
            }
            
            // clicking the coffee maker
            if (xPos >= 235 && xPos <= 305 && yPos >= 235 && yPos <= 305) {
                if (canMake) { // make coffee
                    canMake = false;
                    repaint(); 
                } else if (!canMake) { // pick up coffee
                    canServe = true;
                    canMake = true;
                    repaint();
                }
            }
        }
    }
    

    
    // getters for testing 
    /**
     * Returns a copy of the tables array.
     * 
     * @return 2-D int array that is a copy of the tables field
     */
    public int[][] getTables() {
        int[][] copy = new int[MAX_TABLES][MAX_FOODS]; 
        
        for (int i = 0; i < MAX_TABLES; i++) {
            for (int j = 0; j < MAX_FOODS; j++) {
                copy[i][j] = tables[i][j];
            }
        }
        
    /*    int[][] matrix = new int[300][2];
        int[] beta = new int[2];
        int[] product = new int[300];
        int sum = 0;
        int c = 0;
        int total = 0;
        
        while (c < 300) {
            for (int i = c; i < matrix.length; i++) {
                for (int j = 0; j < beta.length; j++) {
                    sum += matrix[i][j] * beta[j];
                }
            }
            
            product[c] = sum;
            sum = 0;
            c++;
        }
        
        for (int i = 0; i < product.length; i++) {
            total += product[i];
        } */
        
        return copy;
    }
}
