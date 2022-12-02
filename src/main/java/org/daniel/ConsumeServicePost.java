package org.daniel;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.client.WebClient;

public interface ConsumeServicePost<T> {
    public Uni<T> consumeServicePost(WebClient client);
}
