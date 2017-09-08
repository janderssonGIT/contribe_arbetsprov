package contribe_project;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Cart {
	private Map<Integer, Book> cartBooks = new HashMap<Integer, Book>();
	private Map<Integer, Integer> cartQuantity = new HashMap<Integer, Integer>();
	
	public Cart() {
		
	}
	
	public Cart(HashMap<Integer, Book> cartBooks, HashMap<Integer, Integer> cartQuantity) {
		this.cartBooks = cartBooks;
		this.cartQuantity = cartQuantity;
	}

	public Map<Integer, Book> getCartBooks() {
		return cartBooks;
	}

	public void setCartBooks(Map<Integer, Book> cartBooks) {
		this.cartBooks = cartBooks;
	}

	public Map<Integer, Integer> getCartQuantity() {
		return cartQuantity;
	}

	public void setCartQuantity(Map<Integer, Integer> cartQuantity) {
		this.cartQuantity = cartQuantity;
	}
	
	public Book[] cartListToArray() {
		Book books[] = cartBooks.values().toArray(new Book[cartBooks.values().size()]);
		return books;
	}
	
	public BigDecimal calculateTotalPrice() {
		BigDecimal itemCost  = BigDecimal.ZERO;
	    BigDecimal totalCost = BigDecimal.ZERO;    
	    for(Entry<Integer,Book> entry : cartBooks.entrySet()){
	    	itemCost = 	entry.getValue().getPrice().multiply(new BigDecimal(cartQuantity.get(entry.getKey())));
	    	totalCost = totalCost.add(itemCost);
	    }
		return totalCost;
	}
	
	public void addToCart(Book book, int quantity) {
		cartBooks.put(book.hashCode(), book);
		cartQuantity.put(book.hashCode(), quantity);
	}
	
	public void removeFromCart(Book book) {
		cartBooks.remove(book.hashCode());
		cartQuantity.remove(book.hashCode());
	}
	
	public void clearCart() {
		cartBooks.clear();
		cartQuantity.clear();
	}
}
