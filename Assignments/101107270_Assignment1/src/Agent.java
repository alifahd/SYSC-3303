import java.util.ArrayList;
import java.util.Random;

/**
 * 
 */

/**
 * @author Ali Fahd
 *
 */
public class Agent implements Runnable {
	private Table table;// table it shares with chefs
  	private String[] arr={"Bread", "Peanut Butter", "Jelly"};// possible ingredients agent can put on table.
  	
  	/**
	 * Constructor for Agent class
     * 
     * @param table that it shares with chefs
     */
	public Agent(Table table) {
  		this.table = table;
	}

	/**
	 * Generates a random number that can be used to access a value in arr.
     * 
     * @return a random number corresponding to a value in arr.
     */
	public int getRandomNumber() {
      	Random r=new Random();       
      	int randomNumber=r.nextInt(arr.length);
      	return randomNumber;
	}
	
	/**
	 * Generates a list of ingredients the agent has chosen.
     * 
     * @return Arraylist of ingredients agent has chosen.
     */  	
	public ArrayList<String> getIngredients() {
        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.add("");
        ingredients.add("");
        ingredients.set(0, arr[getRandomNumber()]);
        do {
            ingredients.set(1, arr[getRandomNumber()]);
        }while(ingredients.get(0) == ingredients.get(1));// loop as long as ingredients are not the same.
      	return ingredients;
	}
	


	/**
	 * Runs the agent thread
	 */
	public void run() {
		for (int i = 1; i <= 10; i++) {
			System.out.println("Sandwich "+ i);
			table.put(getIngredients());
			System.out.println("Agent has put "+ table.onTable()+" on the table.");// updates console
			//pause until next sandwich
			try {
				Thread.sleep(1700);
			} catch (InterruptedException e) {
			}
		}
	}
}
