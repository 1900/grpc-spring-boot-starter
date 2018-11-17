package com.funtime.grpc.interceptor;

import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

public class GrpcBeforeInterceptorImpl implements GrpcBeforeInterceptor {

    @Autowired
    private Validator validator;

    public void before(Object request, StreamObserver streamObserver) {
        Set<ConstraintViolation<Object>> constraintViolations =
                validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation error1 = constraintViolations.iterator().next();
            String errorMsg = error1.getRootBeanClass().getSimpleName() + "."
                    + error1.getPropertyPath().toString() + " "
                    + error1.getMessage() + ", input: \""
                    + error1.getInvalidValue() + "\"";
            throw new IllegalArgumentException(errorMsg);
        }
    }
}
