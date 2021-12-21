package org.abigtomato.nebula.designpatterns.iterator;

/**
 * @author abigtomato
 */
public interface Iterator<E> {

    /**
     * 判断下一个元素是否存在
     *
     * @return 是否
     */
    boolean hasNext();

    /**
     * 获取下一个元素
     *
     * @return 元素
     */
    E next();
}
