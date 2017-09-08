package contribe_project;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleInterface {
	private Scanner scanner = new Scanner(System.in);
	private static final String regex_menu = "\\b[1-9]\\b|\\b[Qq]\\b";
	private static final String regex_utf8 = "([^\\u0000-\\u0040\\u005B-\\u0060\\u007B-\\u00BF\\u02B0-\\u036F\\u00D7\\u00F7\\u2000-\\u2BFF])+";
	private static final String regex_cart = "(\\b[1-9][\" \"][1-9]\\b)|(\\b[rR]\\b)";
	private static final String regex_bigdecimal = "^\\d{0,6}(\\.\\d{0,2})?$";
	private static final String regex_digit = "\\b[0-9]{1,2}\\b";
	private StoreManager store;
	private Matcher matcher;

	public void start(StoreManager store) {
		this.store = store;
		print(Msg.PAGE_DOWN, Msg.SPLASH_SCREEN, Msg.ADMIN, Msg.CLIENT, Msg.SCREEN_PROMPT);
		menuSelect(0);
	}

	public void menuSelect(int hierarchyToken) {
		int input = menuUserInput();

		switch (hierarchyToken) {
		case 0: /* Splash screen */
			if (input == 1) {
				initAdmin();
			} else if (input == 2) {
				initClient();
			} else if (input > 2) {
				start(store);
			}
			break;

		case 1: /* Client level 1 */
			if (input == 1) {
				searchBooks(hierarchyToken);
			} else if (input == 2) {
				viewCart();
			} else if (input == 3) {
				start(store);
			} else if (input == -1) {
				exit();
			}
			break;

		case 2: /* Client level 2 */
			if (input == 1) {
				addToCart(editCart());
			} else if (input == 2) {
				searchBooks(hierarchyToken);
			} else if (input == 3) {
				initClient();
			} else if (input == 4 && !store.getCart().getCartBooks().isEmpty()) {
				showCartCheckout();
			} else if (input == -1) {
				exit();
			}
			break;

		case 3: /* Client level 3 */
			if (input == 1) {
				buyBooksInCart();
			} else if (input == 2) {
				emptyCartReturnToSearch();
			} else if (input == 3) {
				searchBooks(1);
			} else if (input == -1) {
				exit();
			}
			break;

		case 9: /* Admin level 1 */
			if (input == 1) {
				addBook();
			} else if (input == 2) {
				viewAllBooks();
			} else if (input == 3) {
				start(store);
			} else if (input == -1) {
				exit();
			}
			break;		
		}
	}

	private void buyBooksInCart() {
		Book booksInCart[] = store.getCart().cartListToArray(); 
		printReceipt(store.buy(booksInCart), booksInCart);
	}

	private void initClient() {
		print(Msg.PAGE_DOWN, Msg.SEARCH_BOOKS, Msg.VIEW_CART, Msg.RETURN, Msg.SCREEN_PROMPT);
		menuSelect(1);
	}

	private void searchBooks(int token) {
		if (store.getSearchedBookList() == null && token == 1
				|| token == 2) {
			print(Msg.PAGE_DOWN, Msg.SEARCH_BOOKS_PROMPT);
			store.setSearchedBookList(searchBooksInput());
		}
		print(Msg.PAGE_DOWN);
		displayBooks(store.getSearchedBookList());
		print(Msg.ADD_BOOKS_TO_CART, Msg.RETURN_TO_SEARCH, Msg.RETURN);
		if (!store.getCart().getCartBooks().isEmpty())
			print(Msg.CART_CHECKOUT);
		print(Msg.SCREEN_PROMPT);
		menuSelect(2);
	}

	private void emptyCartReturnToSearch() {
		store.getCart().clearCart();
		searchBooks(1);
	}

	private void addToCart(int[] bookSelection) {
		print(Msg.PAGE_DOWN);
		int selectedIndex = bookSelection[0];
		int wantedQuantity = bookSelection[1];
		Book book = store.getSearchedBookList()[selectedIndex];
		store.getCart().addToCart(book, wantedQuantity);
		showCartCheckout();
	}

	private void viewCart() {
		print(Msg.PAGE_DOWN);
		print(Msg.CART_CONTENT);
		displayCartContent();
		print(Msg.RETURN_ANY_KEY_PRESS);
		Scanner localScanner = new Scanner(System.in);
		if (localScanner.hasNextLine()) {
			initClient();
		}
		localScanner.close();
	}

	private int[] editCart() {
		if (store.getSearchedBookList() == null) {
			return null;
		} else {
			print(Msg.PAGE_DOWN);
			displayBooks(store.getSearchedBookList());
			print(Msg.ADD_REMOVE_BOOKS_TO_CART_PROMPT);
			int input[] = cartInput();
			return input;
		}
	}

	private void showCartCheckout() {
		print(Msg.PAGE_DOWN);
		displayCartContent();
		print(Msg.BUY_AND_EXIT, Msg.DISCARD_AND_RETURN, Msg.SAVE_AND_RETURN, Msg.SCREEN_PROMPT);
		menuSelect(3);
	}

	private void initAdmin() {
		print(Msg.PAGE_DOWN, Msg.ADMIN_ADD_BOOKS, Msg.ADMIN_VIEW_BOOKS, Msg.RETURN, Msg.SCREEN_PROMPT);
		menuSelect(9);
	}

	private void addBook() {
		print(Msg.PAGE_DOWN, Msg.ADMIN_TITLE_PROMPT);
		String title = checkStringInput(1);
		print(Msg.PAGE_DOWN, Msg.ADMIN_AUTHOR_PROMPT);
		String author = checkStringInput(1);
		print(Msg.PAGE_DOWN, Msg.ADMIN_PRICE_PROMPT);
		DecimalFormat nf = store.createNumberFormat();
		String temp = checkStringInput(2);
		BigDecimal price = (BigDecimal) nf.parse(temp.toString(), new ParsePosition(0));
		print(Msg.PAGE_DOWN, Msg.ADMIN_QTY_PROMPT);
		int qty = Integer.parseInt(checkStringInput(3)); 
		Book book = new Book(title, author, price);
		store.add(book, qty);
		print(Msg.PAGE_DOWN);
		displayBooks(store.list(null));
		print(Msg.RETURN_ANY_KEY_PRESS);
		Scanner localScanner = new Scanner(System.in);
		if (localScanner.hasNextLine()) {
			initAdmin();
		}
		localScanner.close();
	}

	private void viewAllBooks() {
		print(Msg.PAGE_DOWN);
		displayBooks(store.list(null));
		print(Msg.RETURN_ANY_KEY_PRESS);
		Scanner localScanner = new Scanner(System.in);
		if (localScanner.hasNextLine()) {
			initAdmin();
		}
		localScanner.close();
	}

	/*
	 * 
	 * IO methods for reading and printing data.
	 * 
	 */

	private int menuUserInput() {
		while (true) {
			String s = scanner.nextLine();
			matcher = Pattern.compile(regex_menu).matcher(s);
			if (matcher.find()) {
				if (s.toLowerCase().equals("q")) {
					return -1;
				}
				return (Integer) Integer.parseInt(s);
			} else {
				print(Msg.BASE_INPUT_ERROR);
			}
		}
	}

	private Book[] searchBooksInput() {
		while (true) {
			String searchString = scanner.nextLine();
			matcher = Pattern.compile(regex_utf8).matcher(searchString);
			if (matcher.find()) {
				if (searchString.toLowerCase().equals("r")) {
					initClient();
					return null;
				} else if (searchString.toLowerCase().equals("all")) {
					return store.list(null);
				} else {
					return store.list(searchString);
				}
			} else {
				print(Msg.SEARCH_BOOKS_INPUT_ERROR);
			}
		}
	}

	private String checkStringInput(int i) {
		String regex = "";
		if (i == 1)
			regex = regex_utf8;
		if (i == 2)
			regex = regex_bigdecimal;
		if (i == 3)
			regex = regex_digit; 
		while (true) {
			String string = scanner.nextLine();
			matcher = Pattern.compile(regex).matcher(string);
			if (matcher.find()) {
				if (string.toLowerCase().equals("r")) {
					initAdmin();
					return null;
				}
				return string;
			} else {
				print(Msg.BASE_INPUT_ERROR);
			}
		}
	}

	private int[] cartInput() {
		int[] intArray;
		while (true) {
			String searchString = scanner.nextLine();
			matcher = Pattern.compile(regex_cart).matcher(searchString);
			if (matcher.find()) {
				if (searchString.toLowerCase().equals("r")) {
					searchBooks(1);
					return null;
				}
				String[] strArray = searchString.split(" ");
				intArray = new int[2];
				intArray[0] = Integer.parseInt(strArray[0]) - 1;
				intArray[1] = Integer.parseInt(strArray[1]);
				return intArray;
			} else {
				print(Msg.CART_INPUT_ERROR);
			}
		}
	}

	public void print(Msg... msg) {
		for (Msg m : msg)
			System.out.print(m);
	}

	public void exit() {
		print(Msg.SYSTEM_EXIT);
		scanner.close();
		System.exit(0);
	}

	private String getTabSize(int delimiter, int strlen) {
		int dist;
		dist = delimiter - strlen;
		String str = "";
		for (int i = 0; i < dist; i++) {
			str = str.concat(" ");
		}
		return str;
	}

	public void displaySelectionErrors(int index) {
		System.out.print("\n ## Book selection " + (index + 1) + " doesn't exist in the index. Selection(s) could not be added.\n\n");
	}

	private void displayBooks(Book[] books) {
		Arrays.sort(books, (a, b) -> a.getTitle().compareTo(b.getTitle()));
		if (books.length == 0) {
			print(Msg.SEARCH_BOOKS_NO_RESULTS);
		} else {
			print(Msg.SEARCH_RESULTS);
			for (int i = 0; i < books.length; i++) {
				System.out.print("[" + (i + 1) + ". [Title: " + books[i].getTitle() + ", Author: " + books[i].getAuthor() + "] "
						+ getTabSize(50, books[i].getTitle().length() + books[i].getAuthor().length())
						+ "] - Price: " + books[i].getPrice()
						+ getTabSize(10, books[i].getPrice().precision())
						+ " - Qty: " + store.getStock().get(books[i].hashCode()) + "\n");
			}
		}
	}

	private void displayCartContent() {
		Map<Integer, Book> books = store.getCart().getCartBooks();
		Map<Integer, Integer> quantity = store.getCart().getCartQuantity();
		if (books.size() == 0) {
			print(Msg.NO_CART_CONTENT);
		} else {
			for (Book b : books.values()) {
				System.out.print("[Title: " + b.getTitle() + ", Author: " + b.getAuthor()
						+ "]" + getTabSize(50, b.getTitle().length() + b.getAuthor().length())
						+ " - Price: " + b.getPrice()
						+ getTabSize(10, b.getPrice().precision())
						+ " - Qty: " + quantity.get(b.hashCode()) + "\n");
			}
		}
		if (!books.isEmpty()) {
			DecimalFormat df = new DecimalFormat("#,###.00");
			System.out.println("--Total sum: " + df.format(store.getCart().calculateTotalPrice()) + "\n");
		}
	}

	private void printReceipt(int receipt[], Book[] booksInCart) {
		print(Msg.PAGE_DOWN);
		List<Book> notInStock = new ArrayList<>();
		for (int i = 0; i < receipt.length; i++) {
			if (receipt[i] == 1) {
				store.getCart().removeFromCart(booksInCart[i]);
				notInStock.add(booksInCart[i]);
			}
		}
		System.out.print("\nYou bought the following books:\n");
		displayCartContent();
		for (Book book : notInStock) {
			System.out.print("\nThe following books were not in stock per requested quantity and was not bought:\n" +
					"[Title: " + book.getTitle() + ", Author: " + book.getAuthor()
					+ "]" + getTabSize(50, book.getTitle().length() + book.getAuthor().length())
					+ "\n\n");
		}
		exit();
	}
}
