package org.abigtomato.nebula.designpatterns.iterator;

/**
 * @author abigtomato
 */
public class BookShelf implements Aggregate<Book> {

    private final Book[] books;
    private int last = 0;

    public BookShelf(int maxSize) {
        this.books = new Book[maxSize];
    }

    public Book getBookAt(int index) {
        return this.books[index];
    }

    public BookShelf appendBook(Book book) {
        this.books[last] = book;
        last++;
        return this;
    }

    public int getLength() {
        return this.last;
    }

    @Override
    public Iterator<Book> iterator() {
        return new BookShelfIterator(this);
    }
}
