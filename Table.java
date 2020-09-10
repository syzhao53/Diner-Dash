import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class Table extends JComponent {
    // image and dimensions
    public static final String IMG_FILE = "files/Table.png";
    public static final int SIZE = 100;
    
    // private fields
    private int tableNumber;
    private int xPos;
    private int yPos;
    private Customer customer;
    
    private static BufferedImage img;
    
    /**
     * Constructs a {@code Table} and initializes private fields. Also reads image.
     */
    public Table(int tableNumber, int xPos, int yPos) {
        this.tableNumber = tableNumber;
        this.xPos = xPos;
        this.yPos = yPos;
        this.customer = null; // starts null since table is empty
        
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    /**
     * Draws the table.
     *
     * @param g Graphics context
     */
    public void draw(Graphics g) {
        g.drawImage(img, this.xPos, this.yPos, SIZE, SIZE, null);
    }
    
    /**
     * Returns the table's assigned number.
     * 
     * @return int representing the table number
     */
    public int getTableNumber() {
        return this.tableNumber;
    }
    
    /**
     * Returns the x position of the table
     * 
     * @return int representing the x position of the table 
     */
    public int getXPos() {
        return this.xPos;
    }
    
    /**
     * Returns the y position of the table
     * 
     * @return int representing the y position of the table 
     */
    public int getYPos() {
        return this.yPos;
    }
    
    /**
     * Returns the customer seated at the table
     * 
     * @return Customer representing the customer seated at the table, null if table is empty
     */
    public Customer getCustomer() {
        return this.customer;
    }
    
    /**
     * Sets the customer field
     * 
     * @param newCustomer Customer representing a new customer seated at the table
     */
    public void setCustomer(Customer newCustomer) {
        this.customer = newCustomer;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(SIZE, SIZE);
    }
}
