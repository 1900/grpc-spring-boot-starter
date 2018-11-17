package com.funtime.grpc.autoconfigure;

import com.funtime.grpc.interceptor.*;
import com.funtime.grpc.logging.GrpcLoggingServerInterceptor;
import org.lognet.springboot.grpc.GRpcServerRunner;
import org.lognet.springboot.grpc.autoconfigure.GRpcAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.validation.Validator;

@ConditionalOnBean(GRpcServerRunner.class)
@AutoConfigureAfter(GRpcAutoConfiguration.class)
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class GrpcAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(Validator.class)
    public GrpcBeforeInterceptor grpcBeforeInterceptor() {
        return new GrpcBeforeInterceptorImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public GrpcAfterReturningInterceptor grpcAfterReturningInterceptor() {
        return new GrpcAfterReturningInterceptorImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public GrpcAfterThrowingInterceptor grpcAfterThrowingInterceptor() {
        return new GrpcAfterThrowingInterceptorImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public GrpcAspect grpcAspect() {
        return new GrpcAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public GrpcLoggingServerInterceptor grpcLoggingServerInterceptor() {
        return new GrpcLoggingServerInterceptor();
    }
}
