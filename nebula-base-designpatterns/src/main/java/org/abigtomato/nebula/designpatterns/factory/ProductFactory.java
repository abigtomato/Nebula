package org.abigtomato.nebula.designpatterns.factory;

/**
 * @author abigtomato
 */
public interface ProductFactory {

    /**
     * 制作
     *
     * @param owner 所有者
     * @return 产品
     */
    Product buildProduct(String owner);

    /**
     * 注册
     *
     * @param product 产品
     */
    void registerProduct(Product product);

    /**
     * 生产
     *
     * @param owner 所有者
     * @return 产品
     */
    Product produce(String owner);
}
