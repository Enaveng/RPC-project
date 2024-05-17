package com.enaveng.rpc.fault.retry;

import com.enaveng.rpc.model.RpcResponse;
import com.github.rholder.retry.*;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 重试策略--固定时间间隔
 */
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy {

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class) //表示只要发生异常就进行重试
                .withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS)) //每次重试时间等待3秒
                .withStopStrategy(StopStrategies.stopAfterAttempt(3)) //执行三次之后停止重试(注:首次执行+最多重试2次)
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("重试次数为" + attempt.getAttemptNumber());
                    }
                }).build();
        return retryer.call(callable);
    }
}
