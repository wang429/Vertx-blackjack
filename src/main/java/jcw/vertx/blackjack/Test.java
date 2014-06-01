package jcw.vertx.blackjack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.vertx.java.core.Handler;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

/**
 * This is unused
 */
public class Test extends Verticle {
    @Override
    public void start() {
        RouteMatcher matcher = new RouteMatcher();
        // very simple routematcher, that matches a single path, simulates
        // minimal webserver
        matcher.get("/web/:req", new Handler<HttpServerRequest>() {

            @Override
            public void handle(HttpServerRequest req) {
                MultiMap params = req.params();
                if (params.contains("req")) {
                    File f = new File("src/main/resources/web/" + params.get("req"));
                    try {
                        // get the data from the filesystem and output to
                        // response
                        String data = new String(Files.readAllBytes(f.toPath()));
                        req.response().setStatusCode(200);
                        req.response().putHeader("Content-Length", Integer.toString(data.length()));
                        req.response().write(data);
                        req.response().end();
                    } catch (IOException e) {
                        // assume file not found, so send 404
                        req.response().setStatusCode(404);
                        req.response().end();
                    }
                }
            }
        });
    }

}
