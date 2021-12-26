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
public class RpcResponseMessage extends Message {

    private Object returnValue;

    private Exception exceptionValue;
}
