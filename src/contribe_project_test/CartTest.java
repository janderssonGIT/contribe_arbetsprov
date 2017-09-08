package contribe_project_test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import contribe_project.Book;
import contribe_project.Cart;

public class CartTest {
	
	private Map<Integer, Book> cartBooks = new HashMap<Integer, Book>();
	private Map<Integer, Integer> cartQuantity = new HashMap<Integer, Integer>();
	private Cart cart;
	
	@Before
	public void init() {
		cart = new Cart();
	}

	@Test
	public void should_return_array_of_books_from_map() {
		Book a = new Book("TitleA", "AuthorA", new BigDecimal(50));
		Book b = new Book("TitleB", "AuthorB", new BigDecimal(25));
		cartBooks.put(a.hashCode(), a);
		cartBooks.put(b.hashCode(), b);
		cart.setCartBooks(cartBooks);
		Book books1[] = cart.cartListToArray();
		Book books2[] = {b, a};
		Assert.assertArrayEquals(books1, books2);
	}
	
	@Test
	public void return_total_price_of_cart_book_content() throws Exception {
		Book a = new Book("TitleA", "AuthorA", new BigDecimal(50));
		Book b = new Book("TitleB", "AuthorB", new BigDecimal(25));
		cartBooks.put(a.hashCode(), a);
		cartBooks.put(b.hashCode(), b);
		cart.setCartBooks(cartBooks);
		cartQuantity.put(a.hashCode(), 1);
		cartQuantity.put(b.hashCode(), 2);
		cart.setCartQuantity(cartQuantity);
		BigDecimal result = cart.calculateTotalPrice();
		BigDecimal comp = new BigDecimal(100).setScale(2);
		Assert.assertEquals(comp, result);
	}
	
	@Test
	public void add_book_to_cart() throws Exception {
		Book a = new Book("TitleA", "AuthorA", new BigDecimal(50));
		cart.addToCart(a, 3);
		Book a1 = cart.getCartBooks().get(a.hashCode());
		int i = cart.getCartQuantity().get(a.hashCode());
		assertEquals(a1, a);
		assertEquals(i, 3);
	}
	
	@Test
	public void remove_book_from_cart() throws Exception {
		Book a = new Book("TitleA", "AuthorA", new BigDecimal(50));
		cart.addToCart(a, 3);
		Book a1 = cart.getCartBooks().get(a.hashCode());
		assertEquals(a, a1); 
		cart.removeFromCart(a);
		Book result = cart.getCartBooks().get(a.hashCode());
		assertEquals(null, result); 
	}
	
	@Test
	public void clear_all_books_from_cart() throws Exception {
		Book a = new Book("TitleA", "AuthorA", new BigDecimal(50));
		Book b = new Book("TitleB", "AuthorB", new BigDecimal(25));
		cart.addToCart(a, 3);
		cart.addToCart(b, 2);
		Book a1 = cart.getCartBooks().get(a.hashCode());
		Book b1 = cart.getCartBooks().get(b.hashCode());
		assertEquals(a, a1); 
		assertEquals(b, b1); 
		cart.clearCart();
		Book book[] = cart.cartListToArray();
		Book book1[] = {};
		assertArrayEquals(book1, book);
	}
}
