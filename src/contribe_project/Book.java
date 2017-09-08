package contribe_project;

import java.math.BigDecimal;
import java.util.Objects;

public class Book {
	private String title;
	private String author;
	private BigDecimal price;
	
	public Book() {
		
	}

	public Book(String title, String author, BigDecimal price) {
		this.title = title;
		this.author = author;
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public BigDecimal getPrice() {
		return price.setScale(2);
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, author, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Book) {
			Book right = (Book) obj;
			return Objects.equals(title, right.title) && Objects.equals(author, right.author)
					&& Objects.equals(price, right.price);
		}
		return false;
	}
}
