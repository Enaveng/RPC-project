package com.enaveng.rpc.fault.retry;

import com.enaveng.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略--不进行重试
 */
public class NoRetryStrategy implements RetryStrategy {
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
