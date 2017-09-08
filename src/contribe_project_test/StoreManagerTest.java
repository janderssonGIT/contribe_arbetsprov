package contribe_project_test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import contribe_project.Book;
import contribe_project.StoreManager;

@RunWith(PowerMockRunner.class)
public class StoreManagerTest {
	private StoreManager store;
	private Map<Integer, Book> books = new HashMap<Integer, Book>();
	private Map<Integer, Integer> stock = new HashMap<Integer, Integer>();

	@Test
	public void retrieve_string_resource_from_web() throws Exception {
		store.load();
		InputStream anyInputStream = new ByteArrayInputStream("test data".getBytes());
		URL url = PowerMockito.mock(URL.class);
		PowerMockito.whenNew(URL.class).withParameterTypes(String.class).withArguments(Mockito.anyString()).thenReturn(url);
		Mockito.when(url.openStream()).thenReturn(anyInputStream);
		Assert.assertEquals(store.convertStreamToString(anyInputStream), "test data");
	}

	@Test
	public void stream_should_return_equal_context_as_string() throws Exception {
		InputStream anyInputStream = new ByteArrayInputStream("test data".getBytes());
		Assert.assertEquals(store.convertStreamToString(anyInputStream), "test data");
	}

	@Test
	public void raw_text_should_be_arranged_into_map() throws Exception {
		DecimalFormat nf = store.createNumberFormat();
		Book a = new Book("Random Sales", "Cunning Bastard", (BigDecimal) nf.parse(("999.00"), new ParsePosition(0)));
		Book b = new Book("Random Sales", "Cunning Bastard", (BigDecimal) nf.parse(("499.50"), new ParsePosition(0)));
		Book c = new Book("Desired", "Rich Bloke", (BigDecimal) nf.parse(("564.50"), new ParsePosition(0)));
		String bookData = "Random Sales;Cunning Bastard;999.00;20\n" +
		"Random Sales;Cunning Bastard;499.50;3\n" +
		"Desired;Rich Bloke;564.50;0\n";
		store.assembleBookList(bookData);
		books = store.getBooks();
		assertTrue(books.get(a.hashCode()).equals(a));
		assertTrue(books.get(b.hashCode()).equals(b));
		assertTrue(books.get(c.hashCode()).equals(c));
		assertEquals(store.getStock().get(a.hashCode()).intValue(), 20);
		assertEquals(store.getStock().get(b.hashCode()).intValue(), 3);
		assertEquals(store.getStock().get(c.hashCode()).intValue(), 0);
	}
	
	@Test
	public void search_string_should_return_proper_results() throws Exception {
		DecimalFormat nf = store.createNumberFormat();
		Book a = new Book("Generic Title", "Cunning Bastard", (BigDecimal) nf.parse(("999.00"), new ParsePosition(0)));
		Book b = new Book("Random Sales", "Generic Author", (BigDecimal) nf.parse(("499.50"), new ParsePosition(0)));
		Book c = new Book("Desired", "Rich Bloke", (BigDecimal) nf.parse(("564.50"), new ParsePosition(0)));
		store.add(a, 1);
		store.add(b, 1);
		store.add(c, 1);
		Book searchedBooks[] = store.list("Ge");
		assertTrue(searchedBooks[0].equals(a));
		assertTrue(searchedBooks[1].equals(b));
	}
	
	@Test
	public void verify_books_added_to_library() throws Exception {
		DecimalFormat nf = store.createNumberFormat();
		Book a = new Book("Generic Title", "Cunning Bastard", (BigDecimal) nf.parse(("999.00"), new ParsePosition(0)));
		Book b = new Book("Random Sales", "Generic Author", (BigDecimal) nf.parse(("499.50"), new ParsePosition(0)));
		Book c = new Book("Desired", "Rich Bloke", (BigDecimal) nf.parse(("564.50"), new ParsePosition(0)));
		store.add(a, 1);
		store.add(b, 1);
		store.add(c, 1);
		assertTrue(store.getBooks().containsKey(a.hashCode()) &&
				store.getBooks().containsKey(b.hashCode()) &&
				store.getBooks().containsKey(c.hashCode()));
	}
	
	@Test
	public void wanted_books_should_return_proper_codes() throws Exception {
		DecimalFormat nf = store.createNumberFormat();
		Book a = new Book("Generic Title", "Cunning Bastard", (BigDecimal) nf.parse(("999.00"), new ParsePosition(0)));
		Book b = new Book("Random Sales", "Generic Author", (BigDecimal) nf.parse(("499.50"), new ParsePosition(0)));
		Book c = new Book("Desired", "Rich Bloke", (BigDecimal) nf.parse(("564.50"), new ParsePosition(0)));
		store.add(a, 1);
		store.add(b, 5);
		store.add(c, 2);
		Book a1 = new Book("Generic Title", "Cunning Bastard", (BigDecimal) nf.parse(("999.00"), new ParsePosition(0)));
		Book b1 = new Book("Random Sales", "Generic Author", (BigDecimal) nf.parse(("499.50"), new ParsePosition(0)));
		Book c1 = new Book("This Book Does not exist", "Null author", (BigDecimal) nf.parse(("564.50"), new ParsePosition(0)));
		books.put(a1.hashCode(), a1);
		books.put(b1.hashCode(), b1);
		books.put(c1.hashCode(), c1);
		stock.put(a1.hashCode(), 2);
		stock.put(b1.hashCode(), 5);
		stock.put(c1.hashCode(), 1);
		store.getCart().setCartBooks(books);
		store.getCart().setCartQuantity(stock);
		Book booksInCart[] = store.getCart().cartListToArray();
		int receipt[] = store.buy(booksInCart);
		assertTrue(receipt[0] == 1);
		assertTrue(receipt[1] == 2);
		assertTrue(receipt[2] == 0);	
	}
}
