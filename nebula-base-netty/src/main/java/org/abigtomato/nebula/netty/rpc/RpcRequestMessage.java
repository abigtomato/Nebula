package org.abigtomato.nebula.netty.rpc;

import lombok.*;

/**
 * @author abigtomato
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequestMessage extends Message {

    private String interfaceName;

    private String methodName;

    private Class<?> returnType;

    private Class<?>[] parameterTypes;

    private Object[] parameterValue;
}
