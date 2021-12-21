package org.abigtomato.nebula.designpatterns.iterator;

/**
 * @author abigtomato
 */
public class Main {

    public static void main(String[] args) {
        BookShelf bookShelf = new BookShelf(4);
        bookShelf.appendBook(new Book("1234"))
                .appendBook(new Book("1235"))
                .appendBook(new Book("1236"))
                .appendBook(new Book("1237"));
        Iterator<Book> iterator = bookShelf.iterator();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            System.out.println("book = " + book);
        }
    }
}
