package com.funtime.grpc.logging;

import io.grpc.*;
import io.grpc.ServerCall.Listener;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@GRpcGlobalInterceptor
public class GrpcLoggingServerInterceptor implements ServerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger("grpc");
    private static final int logDataLen = 50;
    private static final Context.Key<String> requestId = Context.key("request_id");

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            final Metadata requestHeaders,
            ServerCallHandler<ReqT, RespT> next) {

        final Context context = Context.current().withValue(requestId, UUID.randomUUID().toString());

        Listener<ReqT> nextListener = next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            private StringBuilder responseData;

            @Override
            public void sendMessage(RespT message) {
                Context previousContext = context.attach();
                try {
                    super.sendMessage(message);
                    if (logger.isInfoEnabled()) {
                        responseData.append("Body:\n");
                        responseData.append(message.toString());

                        logger.info(responseData.toString());
                    }
                } finally {
                    context.detach(previousContext);
                }
            }

            @Override
            public void sendHeaders(Metadata headers) {
                Context previousContext = context.attach();
                try {
                    super.sendHeaders(headers);
                    if (logger.isInfoEnabled()) {
                        responseData = new StringBuilder(logDataLen);
                        responseData.append("[gRPC response] <= requestId: ");
                        responseData.append(requestId.get());
                        responseData.append("\n");
                        responseData.append("Headers:\n");
                        responseData.append(headers.toString());
                        responseData.append("\n");
                    }
                } finally {
                    context.detach(previousContext);
                }
            }
        }, requestHeaders);

        return new Listener<ReqT>() {
            @Override
            public void onMessage(ReqT message) {
                Context previousContext = context.attach();
                try {
                    nextListener.onMessage(message);

                    if (logger.isInfoEnabled()) {
                        String requestData = "[gRPC request] => requestId:" + requestId.get() + "\n" +
                                "Headers:\n" +
                                requestHeaders.toString() +
                                "\n" +
                                "Body:\n" +
                                message.toString();

                        logger.info(requestData);
                    }
                } finally {
                    context.detach(previousContext);
                }
            }

            @Override
            public void onHalfClose() {
                Context previousContext = context.attach();
                try {
                    nextListener.onHalfClose();
                } finally {
                    context.detach(previousContext);
                }
            }

            @Override
            public void onCancel() {
                Context previousContext = context.attach();
                try {
                    nextListener.onCancel();
                } finally {
                    context.detach(previousContext);
                }
            }

            @Override
            public void onComplete() {
                Context previousContext = context.attach();
                try {
                    nextListener.onComplete();
                } finally {
                    context.detach(previousContext);
                }
            }

            @Override
            public void onReady() {
                Context previousContext = context.attach();
                try {
                    nextListener.onReady();
                } finally {
                    context.detach(previousContext);
                }
            }
        };
    }
}