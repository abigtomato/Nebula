package org.abigtomato.nebula.designpatterns.factory;

/**
 * @author abigtomato
 */
public abstract class AbstractProductFactory implements ProductFactory {

    @Override
    public Product produce(String owner) {
        Product product = buildProduct(owner);
        registerProduct(product);
        return product;
    }
}
