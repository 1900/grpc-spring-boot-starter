package com.funtime.grpc.exception;

public class UnAuthorizedException extends BusinessException {
    public UnAuthorizedException() {
        super("403", "UnAuthorizedException");
    }
}
