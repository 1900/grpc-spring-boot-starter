package com.funtime.grpc.interceptor;

import com.funtime.grpc.exception.BusinessException;
import com.funtime.grpc.exception.UnAuthorizedException;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class GrpcAfterThrowingInterceptorImpl implements GrpcAfterThrowingInterceptor {
    private Metadata.Key<byte[]> codeKey = Metadata.Key.of("code-bin", Metadata.BINARY_BYTE_MARSHALLER);
    private Metadata.Key<byte[]> messageKey = Metadata.Key.of("msg-bin", Metadata.BINARY_BYTE_MARSHALLER);

    public void afterThrowing(Object request, StreamObserver streamObserver, Exception exception) {
        if (exception instanceof UnAuthorizedException) {
            exception = new StatusRuntimeException(Status.UNAUTHENTICATED);
        } else if (exception instanceof IllegalArgumentException) {
            Metadata trailers = new Metadata();
            trailers.put(codeKey, "400".getBytes());
            trailers.put(messageKey, exception.getMessage().getBytes());
            exception = new StatusRuntimeException(Status.INVALID_ARGUMENT, trailers);
        } else if (exception instanceof BusinessException) {
            BusinessException businessException = (BusinessException) exception;
            Metadata trailers = new Metadata();
            trailers.put(codeKey, businessException.getCode().getBytes());
            trailers.put(messageKey, businessException.getMessage().getBytes());
            exception = new StatusRuntimeException(Status.INTERNAL, trailers);
        } else {
            exception = new StatusRuntimeException(Status.UNKNOWN);
        }
        streamObserver.onError(exception);
    }
}
