package de.dfki.resc28.sempre.services;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;

import de.dfki.resc28.LDrawParser.LDrawLexer;
import de.dfki.resc28.LDrawParser.LDrawParser;
import de.dfki.resc28.igraphstore.Constants;
import de.dfki.resc28.sempre.listener.SceneGraphListener;

@SuppressWarnings("deprecation")
@Path("models")
public class Service  
{
	@Context HttpServletRequest fRequest;
	@Context protected ServletContext fContext;
	@Context protected HttpHeaders fRequestHeaders;
	@Context protected UriInfo fRequestUrl;
	
	@GET
	@Produces({ "application/n-triples", Constants.CT_TEXT_TURTLE, Constants.CT_APPLICATION_RDFXML, Constants.CT_APPLICATION_XTURTLE, Constants.CT_APPLICATION_JSON, Constants.CT_APPLICATION_LD_JSON })
	public Response addModel( @HeaderParam(HttpHeaders.ACCEPT) @DefaultValue(Constants.CT_TEXT_TURTLE) final String acceptType, 
							  @QueryParam("uri") String ldrUri )
	{
		try
		{
			@SuppressWarnings("resource")
			HttpClient http = new DefaultHttpClient();
			HttpGet request = new HttpGet(ldrUri);

			request.setHeader("Accept", "text/plain");			// TODO: set accept-header from inputFormat! 
			HttpResponse response = (HttpResponse) http.execute(request);
			
			if (response.getStatusLine().getStatusCode() == 200)
			{
				DataInputStream input = new DataInputStream(new BufferedInputStream(response.getEntity().getContent()));
				
				
				SceneGraphListener sceneGraphListener = new SceneGraphListener(fRequestUrl.getRequestUri().toString());
				
				
				LDrawLexer lexer = new LDrawLexer(new ANTLRInputStream(input));
		        LDrawParser parser = new LDrawParser(new CommonTokenStream(lexer));
		        ParseTreeWalker walker = new ParseTreeWalker();
		        ParseTree tree = parser.file();  
		        walker.walk(sceneGraphListener, tree);
		        
							
				final Model sceneGraphModel = sceneGraphListener.getModel();
				StreamingOutput out = new StreamingOutput() 
				{
					public void write(OutputStream output) throws IOException, WebApplicationException
					{
						RDFDataMgr.write(output, sceneGraphModel, RDFDataMgr.determineLang(null, acceptType, null)) ;
					}
				};
				
				return Response.ok(out)
							   .type(acceptType)
							   .build();
			}
			else
			{
				return Response.status(Status.BAD_REQUEST).build();
			}
		}
		catch (Exception e)
		{
			throw new WebApplicationException("Sth. went wrong.");
		}
	}	
}
