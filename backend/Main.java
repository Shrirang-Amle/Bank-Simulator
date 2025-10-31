package mainapp;

import config.DBConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import api.JerseyConfig;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

public class Main {
    public static final String BASE_URI = "http://localhost:8080/bank-simulator/";

    /**
     * Starts Grizzly HTTP server.
     * 
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        final ResourceConfig rc = new JerseyConfig();
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, SQLException {
        DBConfig dbConfig = DBConfig.getInstance();
        dbConfig.createAllTables();
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.shutdownNow();
    }
}
