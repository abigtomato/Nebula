package org.abigtomato.nebula.designpatterns.template;

/**
 * 模板抽象类
 *
 * @author abigtomato
 */
public abstract class AbstractDisplay {

    /**
     * 打开
     */
    public abstract void open();

    /**
     * 打印
     */
    public abstract void print();

    /**
     * 关闭
     */
    public abstract void close();

    /**
     * 模板方法
     */
    public final void display() {
        open();
        for (int i = 0; i < 5; i++) {
            print();
        }
        close();
    }
}
