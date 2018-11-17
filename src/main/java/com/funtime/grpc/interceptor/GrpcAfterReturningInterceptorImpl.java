package com.funtime.grpc.interceptor;

import io.grpc.stub.StreamObserver;

public class GrpcAfterReturningInterceptorImpl implements GrpcAfterReturningInterceptor {
    public void afterReturning(Object request, StreamObserver streamObserver) {
        streamObserver.onCompleted();
    }
}
