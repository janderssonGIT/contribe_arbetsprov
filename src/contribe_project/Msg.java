package contribe_project;

public enum Msg {
	/*
	 * Errors
	 */
	BASE_INPUT_ERROR("### Errorneous input, try again. ###\n"),
	CART_INPUT_ERROR("### Errorneous cart input, try again. ###\n"),
	SEARCH_BOOKS_INPUT_ERROR("### Errorneous search term, try again. ###\n"),
	/*
	 * Status
	 */
	OK(0),
	NOT_IN_STOCK(1),
	DOES_NOT_EXIST(2),
	/*
	 * Messages
	 */
	SYSTEM_EXIT("<System exit>"),
	SPLASH_SCREEN("\tBookstore simulator 2017!\n\n"), 
	SCREEN_PROMPT("\nSelect menu option by entering a selected number, enter 'Q' to quit:\n> "),
	ADMIN("1. Admin.\n"), 
	CLIENT("2. Client.\n"),
	VIEW_CART("2. View Cart.\n"),
	RETURN("3. Return.\n"),
	RETURN_ANY_KEY_PRESS("[Enter any key to return.]\n>"),
	RETURN_TO_SEARCH("2. Search again.\n"),
	SEARCH_BOOKS("1. Search for books.\n"),
	SEARCH_BOOKS_NO_RESULTS("< No results found >\n"),
	PAGE_DOWN("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"),
	ADD_BOOKS_TO_CART("\n1. Add books to cart.\n"),
	ADD_REMOVE_BOOKS_TO_CART_PROMPT("\nSelect books by entering the list number and the amount you wish to add to your cart, "
			+ "separated by a blanksteg.\nFor example: '5 2'. Hit 'Enter' to submit. Enter 'R' to return:\n> "),
	SEARCH_BOOKS_PROMPT("\nSearch for book Titles and Authors by entering a keyword, hit 'Enter' to submit. Enter 'all' to list all books. Enter 'r' to return:\n> "),
	REMOVE_BOOKS_FROM_CART("2. Remove books from cart.\n"),
	CART_CONTENT("Your cart contains the following:\n\n"),
	BUY_AND_EXIT("\n1. Buy.\n"),
	DISCARD_AND_RETURN("2. Remove selections and return to search.\n"),
	SAVE_AND_RETURN("3. Keep selections and return to search.\n"),
	ADMIN_ADD_BOOKS("1. Add books to store.\n"),
	ADMIN_VIEW_BOOKS("2. View all currently available books in store.\n"),
	CART_CHECKOUT("4. Go to checkout.\n"), 
	ADMIN_TITLE_PROMPT("Enter book title, hit 'Enter' to submit. Enter 'R' to return.\n>"),
	ADMIN_AUTHOR_PROMPT("Enter book author, hit 'Enter' to submit. Enter 'R' to return.\n>"),
	ADMIN_PRICE_PROMPT("Enter book price with max 1 decimal fraction number, hit 'Enter' to submit. Enter 'R' to return.\n>"),
	ADMIN_QTY_PROMPT("Enter book quantity in single digit, hit 'Enter' to submit. Enter 'R' to return.\n>"), 
	NO_CART_CONTENT("No cart content found.\n"), 
	SEARCH_RESULTS("Your search results:\n");

	private String cons;
	private int code;

	private Msg(String cons) {
		this.cons = cons;
	}
	
	private Msg(int code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return cons;
	}
	
	public int getCode() {
		return code;
	}
}
