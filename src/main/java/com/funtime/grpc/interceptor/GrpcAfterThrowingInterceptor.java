package com.funtime.grpc.interceptor;

import io.grpc.stub.StreamObserver;

/**
 * Called when gRPC request is received and an exception...
 */

public interface GrpcAfterThrowingInterceptor {
    void afterThrowing(Object request, StreamObserver streamObserver, Exception exception);
}
