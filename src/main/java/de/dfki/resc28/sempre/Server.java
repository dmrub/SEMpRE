/*
 * This file is part of SEMpRE. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * You may not use this file except in compliance with the License.
 */
package de.dfki.resc28.sempre;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import de.dfki.resc28.igraphstore.IGraphStore;
import de.dfki.resc28.igraphstore.jena.FusekiGraphStore;
import de.dfki.resc28.sempre.services.Service;


@ApplicationPath("/")
public class Server extends Application
{
    public static String fOleURI = null;
    
	@Override
    public Set<Object> getSingletons() 
    {
		configure();
			
		Service modelRepo = new Service();
                ForwardedHeaderFilter f = new ForwardedHeaderFilter();
                WebAppExceptionMapper exm = new WebAppExceptionMapper();
		return new HashSet<Object>(Arrays.asList(f, modelRepo, exm));
    }
	
    public static synchronized void configure() 
    {
        try 
        {
            String configFile = System.getProperty("sempre.configuration");
            java.io.InputStream is;

            if (configFile != null) 
            {
                is = new java.io.FileInputStream(configFile);
                System.out.format("Loading sempre Repo configuration from %s ...%n", configFile);
            } 
            else 
            {
                is = Server.class.getClassLoader().getResourceAsStream("sempre.properties");
                System.out.println("Loading sempre Repo configuration from internal resource file ...");
            }

            java.util.Properties p = new Properties();
            p.load(is);

            fOleURI = getProperty(p, "oleURI", "sempre.oleURI");

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public static String getProperty(java.util.Properties p, String key, String sysKey) 
    {
        String value = System.getProperty(sysKey);
        if (value != null) 
        {
            return value;
        }
        return p.getProperty(key);
    }
}
