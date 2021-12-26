package org.abigtomato.nebula.netty.rpc;

/**
 * @author abigtomato
 */
public interface HelloService {

    /**
     * sayHello
     *
     * @param message message
     * @return hello message
     */
    String sayHello(String message);
}
