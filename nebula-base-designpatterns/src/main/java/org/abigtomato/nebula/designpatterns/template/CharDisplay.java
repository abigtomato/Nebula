package org.abigtomato.nebula.designpatterns.template;

/**
 * 字符型具体类
 *
 * @author abigtomato
 */
public class CharDisplay extends AbstractDisplay {

    private final char ch;

    public CharDisplay(char ch) {
        this.ch = ch;
    }

    @Override
    public void open() {
        System.out.print("<<");
    }

    @Override
    public void print() {
        System.out.print(this.ch);
    }

    @Override
    public void close() {
        System.out.print(">>");
    }
}
