package com.funtime.grpc.autoconfigure;

import com.funtime.grpc.interceptor.GrpcAfterReturningInterceptor;
import com.funtime.grpc.interceptor.GrpcAfterThrowingInterceptor;
import com.funtime.grpc.interceptor.GrpcBeforeInterceptor;
import io.grpc.stub.StreamObserver;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class GrpcAspect {
    @Autowired
    private GrpcBeforeInterceptor grpcBeforeInterceptor;

    @Autowired
    private GrpcAfterReturningInterceptor grpcAfterReturningInterceptor;

    @Autowired
    private GrpcAfterThrowingInterceptor grpcAfterThrowingInterceptor;

    @Pointcut("@within(org.lognet.springboot.grpc.GRpcService)")
    public void grpcBean() {}

    @Pointcut(value = "args(request, streamObserver))", argNames = "request,streamObserver")
    public void grpcMethod(Object request, StreamObserver streamObserver) {
    }

    @Before(value = "grpcBean() && grpcMethod(request, streamObserver)", argNames = "request,streamObserver")
    public void before(Object request, StreamObserver streamObserver) {
        grpcBeforeInterceptor.before(request, streamObserver);
    }

    @AfterReturning(pointcut = "grpcBean() && grpcMethod(request, streamObserver)", argNames = "request,streamObserver")
    public void onComplete(Object request, StreamObserver streamObserver) {
        grpcAfterReturningInterceptor.afterReturning(request, streamObserver);
    }

    @AfterThrowing(pointcut = "grpcBean() && grpcMethod(request, streamObserver)", throwing = "ex", argNames = "request,streamObserver,ex")
    public void onError(Object request, StreamObserver streamObserver, Exception ex) {
        grpcAfterThrowingInterceptor.afterThrowing(request, streamObserver, ex);
    }
}