package com.funtime.grpc.interceptor;

import io.grpc.stub.StreamObserver;

/**
 * Called when gRPC request is received...
 */

public interface GrpcBeforeInterceptor {
    void before(Object request, StreamObserver streamObserver);
}
