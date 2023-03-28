package io.github.bullettooth.example.grpcservice;

import io.github.bullettooth.example.api.grpc.v1.HelloRequest;
import io.github.bullettooth.example.api.grpc.v1.HelloResponse;
import io.github.bullettooth.example.api.grpc.v1.ReactorExampleServiceGrpc;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ExampleServiceImpl extends ReactorExampleServiceGrpc.ExampleServiceImplBase {
    @Override
    public Mono<HelloResponse> hello(Mono<HelloRequest> request) {
        return request.map(it -> HelloResponse.newBuilder()
            .setMessage("Hello, " + it.getName())
            .build()
        );
    }
}
