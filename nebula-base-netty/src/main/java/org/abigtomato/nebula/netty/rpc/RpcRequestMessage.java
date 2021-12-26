package org.abigtomato.nebula.netty.rpc;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author abigtomato
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RpcRequestMessage extends Message {

    private String interfaceName;

    private String methodName;

    private Class<?> returnType;

    private Class<?>[] parameterTypes;

    private Object[] parameterValue;
}
