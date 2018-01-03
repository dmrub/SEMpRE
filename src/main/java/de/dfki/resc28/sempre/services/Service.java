package de.dfki.resc28.sempre.services;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;

import de.dfki.resc28.LDrawParser.LDrawLexer;
import de.dfki.resc28.LDrawParser.LDrawParser;
import de.dfki.resc28.igraphstore.Constants;
import de.dfki.resc28.sempre.listener.SceneGraphListener;
import de.dfki.resc28.igraphstore.util.ProxyConfigurator;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Variant;
import org.apache.http.impl.client.CloseableHttpClient;

@Path("")
public class Service {

    @Context
    HttpServletRequest fRequest;
    @Context
    protected ServletContext fContext;
    @Context
    protected HttpHeaders fRequestHeaders;
    @Context
    protected UriInfo fRequestUrl;

    @GET
    @Path("/")
    @Produces({MediaType.TEXT_HTML})
    public String index(@Context UriInfo uriInfo) {
        return "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "    <title>SEMpRE Service</title>\n"
                + "    <style>\n"
                + "        table {\n"
                + "            font-family: arial, sans-serif;\n"
                + "            border-collapse: collapse;\n"
                + "            width: 100%;\n"
                + "        }\n"
                + "\n"
                + "        td, th {\n"
                + "            border: 1px solid #dddddd;\n"
                + "            text-align: left;\n"
                + "            padding: 8px;\n"
                + "        }\n"
                + "\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "<h2>SEMpRE Service</h2>\n"
                + "<p><b>Service Base URI:</b><pre>" + uriInfo.getAbsolutePath() + "</pre></p>\n"
                + "<table>\n"
                + "    <caption><h3>Available REST API</h3></caption>\n"
                + "    <tr>\n"
                + "        <th>Path</th>\n"
                + "        <th>Description</th>\n"
                + "    </tr><tr><td>/models?uri=<b><i>ldrUri</i></b></td><td>Fetch LDR model from specified URI and convert to RDF Scene Graph</td></tr>\n"
                + "</table>\n"
                + "</body>\n"
                + "</html>";
    }

    @GET
    @Path("models")
    @Produces({Constants.CT_APPLICATION_JSON_LD, Constants.CT_APPLICATION_NQUADS, Constants.CT_APPLICATION_NTRIPLES, Constants.CT_APPLICATION_RDF_JSON, Constants.CT_APPLICATION_RDFXML, Constants.CT_APPLICATION_TRIX, Constants.CT_APPLICATION_XTURTLE, Constants.CT_TEXT_N3, Constants.CT_TEXT_TRIG, Constants.CT_TEXT_TURTLE})
    public Response addModel(@QueryParam("uri") String ldrUri, @Context Request request) throws IOException {

        // negotiate data format
        final List<Variant> defaultVariant = Variant.mediaTypes(MediaType.valueOf(Constants.CT_TEXT_TURTLE)).build();
        Variant bestVariant = request.selectVariant(defaultVariant);
        if (bestVariant == null) {
            final List<Variant> reqVariants = Variant.mediaTypes(
                    MediaType.valueOf(Constants.CT_APPLICATION_JSON_LD),
                    MediaType.valueOf(Constants.CT_APPLICATION_NQUADS),
                    MediaType.valueOf(Constants.CT_APPLICATION_NTRIPLES),
                    MediaType.valueOf(Constants.CT_APPLICATION_RDF_JSON),
                    MediaType.valueOf(Constants.CT_APPLICATION_RDFXML),
                    MediaType.valueOf(Constants.CT_APPLICATION_TRIX),
                    MediaType.valueOf(Constants.CT_APPLICATION_XTURTLE),
                    MediaType.valueOf(Constants.CT_TEXT_N3),
                    MediaType.valueOf(Constants.CT_TEXT_TRIG),
                    MediaType.valueOf(Constants.CT_TEXT_TURTLE)
            ).build();

            bestVariant = request.selectVariant(reqVariants);
            if (bestVariant == null) {
                /* Based on results, the optimal uriResponse variant can not be determined from the list given.  */
                return Response.notAcceptable(reqVariants).build();
            }
        }
        final MediaType responseMediaType = bestVariant.getMediaType();

        //try
        //{
        CloseableHttpClient http = ProxyConfigurator.createHttpClient();
        HttpGet uriRequest = new HttpGet(ldrUri);

        uriRequest.setHeader("Accept", "text/plain");			// TODO: set accept-header from inputFormat!
        HttpResponse uriResponse = (HttpResponse) http.execute(uriRequest);

        if (uriResponse.getStatusLine().getStatusCode() == 200) {
            DataInputStream input = new DataInputStream(new BufferedInputStream(uriResponse.getEntity().getContent()));

            SceneGraphListener sceneGraphListener = new SceneGraphListener(fRequestUrl.getRequestUri().toString());

            LDrawLexer lexer = new LDrawLexer(new ANTLRInputStream(input));
            LDrawParser parser = new LDrawParser(new CommonTokenStream(lexer));
            ParseTreeWalker walker = new ParseTreeWalker();
            ParseTree tree = parser.file();
            walker.walk(sceneGraphListener, tree);

            final Model sceneGraphModel = sceneGraphListener.getModel();
            final String responseMediaTypeStr = responseMediaType.getType() + "/" + responseMediaType.getSubtype();
            StreamingOutput out = new StreamingOutput() {
                public void write(OutputStream output) throws IOException, WebApplicationException {
                    RDFDataMgr.write(output, sceneGraphModel, RDFDataMgr.determineLang(null, responseMediaTypeStr, null));
                }
            };

            return Response.ok(out)
                    .type(responseMediaType)
                    .build();
        } else {
            throw new WebApplicationException(
                    String.format("Could not retrieve URI: %s, status code: %d, reason: %s",
                            ldrUri,
                            uriResponse.getStatusLine().getStatusCode(),
                            uriResponse.getStatusLine().getReasonPhrase()),
                    Status.BAD_REQUEST);
        }
        //}
        //catch (Exception e)
        //{
        //	throw new WebApplicationException("Sth. went wrong.");
        //}
    }
}
