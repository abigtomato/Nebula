package org.abigtomato.nebula.thread.juc.t05;

/**
 * 内部类实现单例模式
 */
public class Test_02 {

    private Test_02() { }

    private static class Inner {
        private static Test_02 t = new Test_02();
    }

    public static Test_02 geInstance() {
        return Inner.t;
    }

}
