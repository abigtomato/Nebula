package org.abigtomato.nebula.netty.rpc;

/**
 * @author abigtomato
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String message) {
        return "Hello " + message;
    }
}
