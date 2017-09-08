package contribe_project;

public class Main {
	
	public static void main(String[] args) {
		new Main().run();
	}
	
	public void run() {
		StoreManager store = new StoreManager();
		ConsoleInterface ui = new ConsoleInterface();
		store.load();
		ui.start(store);
	}
}
