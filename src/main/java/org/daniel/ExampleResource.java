package org.daniel;

import com.fasterxml.jackson.databind.JsonNode;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.smallrye.mutiny.tuples.Tuple3;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.codec.BodyCodec;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/hello")
public class ExampleResource implements ConsumeServicePost {

    public static final String PROGRAMMING_QUOTE = "https://api.chucknorris.io/jokes/random";
    public static final String CHUCK_NORRIS_QUOTE = "https://api.chucknorris.io/jokes/random";
    public static final String CHUCK_NORRIS_QUOTE_ERROR = "https://api.chucknorris.io/jokes/ran";

    private final Vertx vertx;
    private final WebClient client;

    @Inject
    public ExampleResource(Vertx vertx) {
        this.vertx = vertx;
        this.client = WebClient.create(vertx);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello() {
        // Combine the result of our 2 Unis in a tuple
        Map<String, Object> response = new HashMap<>();
        Tuple3<ResponseChuckNorris, ResponseChuckNorris, ResponseChuckNorris> tuple = Uni.combine().all()
                .unis(getProgrammingQuote(client), getChuckNorrisQuote(client), consumeServicePost(client))
                .asTuple().await().indefinitely();
        System.out.println(response);
        response.put("response1", tuple.getItem1());
        response.put("response2", tuple.getItem2());
        response.put("response3", tuple.getItem3());
        return Response.ok(response).build();
    }

    private static Uni<ResponseChuckNorris> getProgrammingQuote(WebClient client) {
        return (Uni<ResponseChuckNorris>) client.getAbs(PROGRAMMING_QUOTE)
                .as(BodyCodec.jsonObject())
                .send()
                .onItem()
                .transform(r -> r.body().mapTo(ResponseChuckNorris.class));
    }

    private static Uni<ResponseChuckNorris> getChuckNorrisQuote(WebClient client) {
        return client.getAbs(CHUCK_NORRIS_QUOTE)
                .as(BodyCodec.jsonObject())
                .send()
                .onItem().transform(r -> r.body().mapTo(ResponseChuckNorris.class));
    }


    @Override
    public Uni<ResponseChuckNorris> consumeServicePost(WebClient client) {
        return client.getAbs(CHUCK_NORRIS_QUOTE)
                .as(BodyCodec.jsonObject())
                .send()
                .onItem().transform(r -> r.body().mapTo(ResponseChuckNorris.class));
    }
}