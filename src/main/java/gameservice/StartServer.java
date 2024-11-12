package gameservice;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

import java.net.InetAddress;

public class StartServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        String host = "Local IPv4";
        int port = Port num;

        Server server = ServerBuilder.forPort(port)
                .addService(new StubServer())
                .build();

        InetAddress inetAddress = InetAddress.getByName(host);
        System.out.println("Server started at " + inetAddress.getHostAddress() + ":" + port);

        server.start();
        server.awaitTermination();
    }
}
