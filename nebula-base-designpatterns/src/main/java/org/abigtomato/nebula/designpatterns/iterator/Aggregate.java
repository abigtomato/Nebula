package org.abigtomato.nebula.designpatterns.iterator;

/**
 * @author abigtomato
 */
public interface Aggregate<E> {

    /**
     * 生成一个用于遍历集合的迭代器
     *
     * @return iterator
     */
    Iterator<E> iterator();
}
