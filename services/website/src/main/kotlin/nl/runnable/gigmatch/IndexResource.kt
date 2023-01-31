package nl.runnable.gigmatch

import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import javax.inject.Inject
import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Path("/")
class IndexResource {

    @Inject
    lateinit var index: Template

    @GET
    @Produces(MediaType.TEXT_HTML)
    fun get(@DefaultValue("person") @QueryParam("name") name: String): TemplateInstance =
        index.data("name", name)

}
