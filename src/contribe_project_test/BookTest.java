package contribe_project_test;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import org.junit.Test;
import contribe_project.Book;

public class BookTest {

	@Test
	public void test_Equal_fail() {
		Book book1 = new Book();
		book1.setAuthor("Jon Snow");
		book1.setTitle("Knowing nothing and survival: Wildling 101");
		book1.setPrice(new BigDecimal("0.50"));
		Book book2 = new Book();
		book2.setAuthor("Jon Spoiler");
		book2.setTitle("Knowing nothing and survival: Wildling 101");
		book2.setPrice(new BigDecimal("0.50"));
		boolean result = book1.equals(book2);
		assertEquals(false, result);
	}
	
	@Test
	public void test_equal_success() {
		Book book1 = new Book();
		book1.setAuthor("Jon Snow");
		book1.setTitle("Knowing nothing and survival: Wildling 101");
		book1.setPrice(new BigDecimal("0.50"));
		Book book2 = new Book();
		book2.setAuthor("Jon Snow");
		book2.setTitle("Knowing nothing and survival: Wildling 101");
		book2.setPrice(new BigDecimal("0.50"));
		boolean result = book1.equals(book2);
		assertEquals(true, result);
	}
	
	@Test
	public void test_hash_code_fail() {
		Book book1 = new Book();
		book1.setAuthor("Jon Snow");
		book1.setTitle("Knowing nothing and survival: Wildling 101");
		book1.setPrice(new BigDecimal("0.50"));
		Book book2 = new Book();
		book2.setAuthor("Jon Spoiler");
		book2.setTitle("Knowing nothing and survival: Wildling 101");
		book2.setPrice(new BigDecimal("0.50"));
		boolean result = book1.hashCode() == book2.hashCode();
		assertEquals(false, result);
	}
	
	@Test
	public void test_hash_code_success() {
		Book book1 = new Book();
		book1.setAuthor("Jon Snow");
		book1.setTitle("Knowing nothing and survival: Wildling 101");
		book1.setPrice(new BigDecimal("0.50"));
		Book book2 = new Book();
		book2.setAuthor("Jon Snow");
		book2.setTitle("Knowing nothing and survival: Wildling 101");
		book2.setPrice(new BigDecimal("0.50"));
		boolean result = book1.hashCode() == book2.hashCode();
		assertEquals(true, result);
	}
}
