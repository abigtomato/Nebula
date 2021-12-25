package org.abigtomato.nebula.designpatterns.factory;

/**
 * @author abigtomato
 */
public class Main {

    public static void main(String[] args) {
        ProductFactory factory = new IDCardFactory();
        Product produce1 = factory.produce("233");
        produce1.use();
        Product produce2 = factory.produce("666");
        produce2.use();
    }
}
