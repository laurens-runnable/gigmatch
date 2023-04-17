package nl.runnable.gigmatch.website;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.NonBlocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/vacancies")
@Produces("text/html")
public class VacanciesResource {

    @Inject
    Template vacancies;

    @GET
    @NonBlocking
    public TemplateInstance index() {
        return vacancies.data("title", "Gigmatch");
    }

}
