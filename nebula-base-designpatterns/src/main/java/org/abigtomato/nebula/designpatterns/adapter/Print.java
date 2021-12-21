package org.abigtomato.nebula.designpatterns.adapter;

/**
 * 需求
 *
 * @author abigtomato
 */
public interface Print {

    /**
     * 弱化打印
     */
    void printWeak();

    /**
     * 强化打印
     */
    void printStrong();
}
