package contribe_project;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class StoreManager implements BookList {
	private static final String URL = "https://raw.githubusercontent.com/contribe/contribe/dev/bookstoredata/bookstoredata.txt";
	private Cart cart = new Cart(new HashMap<Integer, Book>(), new HashMap<Integer, Integer>());
	private Map<Integer, Book> books = new HashMap<Integer, Book>();
	private Map<Integer, Integer> stock = new HashMap<Integer, Integer>();
	private Locale locale = Locale.getDefault();
	private Book[] searchedBookList;

	public void load() {
		String bookData = retrieveStream();
		assembleBookList(bookData); 
	}

	//public for test (or reflection could be used..)
	public void assembleBookList(String data) {
		DecimalFormat nf = createNumberFormat();
		String entries[] = data.split("\\n");
		for (String s : entries) {
			String bookString[] = s.split(";");
			Book book = new Book(bookString[0].toString(), bookString[1].toString(),
					(BigDecimal) nf.parse(bookString[2].toString().replaceAll(",",""), new ParsePosition(0)));
			books.put(book.hashCode(), book);
			stock.put(book.hashCode(), (Integer) Integer.parseInt(bookString[3].toString()));
		}
	}

	private String retrieveStream() {
		InputStream in = null;
		try {
			in = new URL(URL).openStream();
		} catch (IOException e) {
			System.err.println("Failed to open stream, data couldn't be retrieved in: " + this);
		}
		return convertStreamToString(in);
	}

	//public for test (or reflection could be used..)
	public String convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is, "utf-8");
		s.useLocale(Locale.getDefault());
		try {
			s.useDelimiter("\\A");
			return s.next();
		} finally {
			s.close();
		}
	}

	@Override
	public Book[] list(String searchString) {
		List<Book> list = new ArrayList<Book>();
		for (Book book : getBooks().values()) {
			if (searchString == null) {
				list.add(book);
			} else if (book.getTitle().toLowerCase(locale).startsWith(searchString.toLowerCase(locale))
					|| book.getAuthor().toLowerCase(locale).startsWith(searchString.toLowerCase(locale))) {
				list.add(book);
			}
		}
		Book[] bookList = new Book[list.size()];
		bookList = list.toArray(bookList);
		return bookList;
	}

	@Override
	public boolean add(Book book, int quantity) {
		getBooks().put(book.hashCode(), book);
		getStock().put(book.hashCode(), quantity);
		return true;
	}

	@Override
	public int[] buy(Book... bookList) {
		int receipt[] = new int[bookList.length];
		for (int i = 0; i < bookList.length; i++) {
			if (books.containsKey(bookList[i].hashCode())) {
				int currQty = stock.get(bookList[i].hashCode());
				int wantedQty = getCart().getCartQuantity().get(bookList[i].hashCode());
				if (currQty - wantedQty >= 0) { 
					getStock().put(bookList[i].hashCode(), currQty - wantedQty);
					receipt[i] = Msg.OK.getCode();
				} else {
					receipt[i] = Msg.NOT_IN_STOCK.getCode();
				}
			} else {
				receipt[i] = Msg.DOES_NOT_EXIST.getCode();
			}
		}
		return receipt;
	}
	
	public DecimalFormat createNumberFormat() {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
		symbols.setGroupingSeparator(',');
		symbols.setDecimalSeparator('.');
		String pattern = "#,##0.0#";
		DecimalFormat nf = new DecimalFormat(pattern, symbols);
		nf.setParseBigDecimal(true);
		return nf;
	}

	public Book[] getSearchedBookList() {
		return searchedBookList;
	}

	public void setSearchedBookList(Book[] searchedBookList) {
		this.searchedBookList = searchedBookList;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Map<Integer, Integer> getStock() {
		return stock;
	}

	public void setStock(Map<Integer, Integer> stock) {
		this.stock = stock;
	}

	public Map<Integer, Book> getBooks() {
		return books;
	}

	public void setBooks(Map<Integer, Book> books) {
		this.books = books;
	}
}
