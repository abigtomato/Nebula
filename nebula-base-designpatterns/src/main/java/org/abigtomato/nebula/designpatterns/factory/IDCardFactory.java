package org.abigtomato.nebula.designpatterns.factory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author abigtomato
 */
public class IDCardFactory extends AbstractProductFactory{

    private final List<String> owners = new ArrayList<>();

    @Override
    public Product buildProduct(String owner) {
        return new IDCard(owner);
    }

    @Override
    public void registerProduct(Product product) {
        owners.add(((IDCard) product).getOwner());
    }

    public List<String> getOwners() {
        return owners;
    }
}
