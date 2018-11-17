package com.funtime.grpc.interceptor;

import io.grpc.stub.StreamObserver;

/**
 * Called when gRPC responds is successfully...
 */

public interface GrpcAfterReturningInterceptor {
    void afterReturning(Object request, StreamObserver streamObserver);
}
