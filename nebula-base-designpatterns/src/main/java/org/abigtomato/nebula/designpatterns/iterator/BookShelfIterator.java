package org.abigtomato.nebula.designpatterns.iterator;

/**
 * @author abigtomato
 */
public class BookShelfIterator implements Iterator<Book> {

    private final BookShelf bookShelf;
    private int index;

    public BookShelfIterator(BookShelf bookShelf) {
        this.bookShelf = bookShelf;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return this.index < bookShelf.getLength();
    }

    @Override
    public Book next() {
        return bookShelf.getBookAt(index++);
    }
}
