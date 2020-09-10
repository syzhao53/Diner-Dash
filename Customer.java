import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class Customer extends JComponent {
    // images and dimensions
    public static final String IMG_FILE_ONE = "files/Customer1.png";
    public static final String IMG_FILE_TWO = "files/Customer2.png";
    public static final String IMG_FILE_THREE = "files/Customer3.png";
    public static final int WIDTH = 100;
    public static final int HEIGHT = 140;
    
    // private fields
    private int numFoods;
    private int tableNumber;
    private int xPos;
    private int yPos;
    private int imageNumber;
    
    // BufferedImages
    private static BufferedImage img1;
    private static BufferedImage img2;
    private static BufferedImage img3;
    
    /**
     * Constructs a {@code Customer} and initializes private fields. Also reads images.
     */
    public Customer(int numFoods, int xPos, int yPos, int imageNumber) {
        this.numFoods = numFoods;
        this.xPos = xPos;
        this.yPos = yPos;
        this.imageNumber = imageNumber;
        this.tableNumber = 0; // since no table has been assigned yet
        
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
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    /**
     * Draws the customer. Selects the image based on the imageNumber that was initalized by the
     * constructor.
     *
     * @param g Graphics context
     * @param width Width of the image
     * @param height Height of the image 
     */
    public void draw(Graphics g, int width, int height) {
        if (imageNumber == 1) {
            g.drawImage(img1, this.xPos, this.yPos, width, height, null);
        } else if (imageNumber == 2) {
            g.drawImage(img2, this.xPos, this.yPos, width, height, null);
        } else if (imageNumber == 3) {
            g.drawImage(img3, this.xPos, this.yPos, width, height, null);
        }
    }
    
    /**
     * Returns the number of foods a customer has ordered.
     * 
     * @return int representing the number of foods a customer has ordered
     */
    public int getNumFoods() {
        return this.numFoods;
    }
   
    /**
     * Returns the x position of the customer
     * 
     * @return int representing the x position of the customer 
     */
    public int getXPos() {
        return this.xPos;
    }
    
    /**
     * Returns the y position of the customer
     * 
     * @return int representing the y position of the customer 
     */
    public int getYPos() {
        return this.yPos;
    }
    
    /**
     * Returns the number of the table where the customer is seated.
     * 
     * @return int representing the customer's table number
     */
    public int getTableNumber() {
        return tableNumber;
    }
    
    /**
     * Sets the xPos field to the new given position 
     * 
     * @param newXPos int representing the new x position 
     */
    public void setXPos(int newXPos) {
        this.xPos = newXPos;
    }
    
    /**
     * Sets the yPos field to the new given position 
     * 
     * @param newYPos int representing the new y position 
     */
    public void setYPos(int newYPos) {
        this.yPos = newYPos;
    }
    
    /**
     * Sets the tableNumber field to the new given number. Used when seating customers.
     * 
     * @param newNum int representing the new table number
     */
    public void setTableNumber(int newNum) {
        tableNumber = newNum;
    } 
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
}
