package io.github.bullettooth.example.grpcservice;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class GrpcServiceApplication {

    @Bean(destroyMethod = "shutdownNow")
    public Server grpcServer(List<BindableService> services) throws IOException {
        NettyServerBuilder builder = NettyServerBuilder.forPort(8080);
        services.forEach(builder::addService);
        return builder.build().start();
    }

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(GrpcServiceApplication.class, args);
        Server server = context.getBean(Server.class);
        server.awaitTermination();
    }

}
