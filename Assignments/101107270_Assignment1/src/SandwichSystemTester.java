/**
 *
 * Make sandwiches with a Table, Agent and Chefs.
 * 
 * @author Ali Fahd
 * 
 */

class SandwichSystemTester {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Table table = new Table(); // shared by agent and chefs
		Thread agent, breadChef, pbChef, jellyChef;

		// Initializing agent and chef threads
		agent = new Thread(new Agent(table), "Agent");
		breadChef = new Thread(new Chef(table, "Bread"), "Bread Chef");
		pbChef = new Thread(new Chef(table, "Peanut Butter"), "Peanut Butter Chef");
		jellyChef = new Thread(new Chef(table, "Jelly"), "Jelly Chef");

		//running threads
		agent.start();
		breadChef.start();
		pbChef.start();
		jellyChef.start();
	}
}