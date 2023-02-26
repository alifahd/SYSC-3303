import java.util.ArrayList;
import java.util.Arrays;

/**
 * Chefs who ask for ingredients from the table.
 * 
 * @author Ali Fahd
 * @version 1.00
 */
public class Chef implements Runnable {
	private Table table;
	private String ingredient;
	
	/**
	 * Constructor for Chef class
     * 
     * @param table that it shares with agent.
     * @param ingredient that the chef has infinite amounts of
     */
	public Chef(Table table, String ingredient) {
  		this.table = table;	//table it shares with agent
		this.ingredient = ingredient; //main ingredient that the chef has infinite amounts of
	}
	
	/**
	 * Runs the chef thread
	 */
	public void run() {
		if(table.areNeeded(ingredient)) {// checks if it needs the ingredients on the table
			table.get();
			System.out.println("Chef with " + ingredient + " has eaten the sandwich");// updates console
		}
	}

}
