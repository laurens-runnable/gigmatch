package nl.runnable.gigmatch.website;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.NonBlocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/")
@Produces("text/html")
public class HomeResource {

    @Inject
    Template home;

    @GET
    @NonBlocking
    public TemplateInstance index() {
        return home.data("title", "Gigmatch");
    }

}
