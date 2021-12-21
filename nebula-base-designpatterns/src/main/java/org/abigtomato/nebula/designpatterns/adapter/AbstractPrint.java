package org.abigtomato.nebula.designpatterns.adapter;

/**
 * @author abigtomato
 */
public abstract class AbstractPrint {

    /**
     * 弱化打印
     */
    abstract void printWeak();

    /**
     * 强化打印
     */
    abstract void printStrong();
}
