import java.util.ArrayList;
import java.util.Arrays;
/**
 * Ingredients are placed by the agent and picked up by the chefs.
 * 
 * @author Ali Fahd
 * @version 1.00
 */
public class Table
{
    private static ArrayList<String> contents = new ArrayList<String>(); // contents
    private boolean empty = true; // empty?
    
    /**
     * Puts ingredients on the table.  This method returns when
     * the ingredients have been put onto the table.
     * 
     * @param item The ingredients to be put on the table.
     */
    public synchronized void put(ArrayList<String> ingredients) {
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }
        contents = ingredients;
        empty = false;
        notifyAll();
    }
    
    /**
     * Gets ingredients from the table.  This method returns once the
     * ingredients have been removed from the table.
     * 
     * @return The object taken from the box.
     */
    public synchronized ArrayList<String> get() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients = contents;
        contents = null;
        empty = true;
        notifyAll();
        return ingredients;
    }
    
    /**.
     * 
     * @return The status of empty on the table.
     */    
    public synchronized boolean isEmpty() {
    	return empty;
    }
    
    /**
     * Gets ingredients from the table.
     * 
     * @return String of ingredients on table.
     */
    public synchronized String onTable() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
    	String listString = String.join(", ", contents);
        notifyAll();
    	return listString;
    }
    
    /**
     * Gets ingredients from the table.
     * 
     * @param chefIngredient The ingredient that the chef would have calling this method.
     * @return boolean of whether or not the chef needs the other 2 ingredients.
     */
    public synchronized boolean areNeeded(String chefIngredient) {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                return false;
            }
        }
    	if(contents.contains(chefIngredient)) {
            notifyAll();
        	return false;    			
    	}
        notifyAll();
    	return true;
    }
}
