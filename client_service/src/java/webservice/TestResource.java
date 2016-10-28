/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import com.geek.inside.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.json.*;

/**
 * REST Web Service
 *
 * @author JESS
 */
@Path("test")
public class TestResource {
    private Message message;
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TestResource
     */
    public TestResource() throws IOException {
        message = new Message();
    }

    /**
     * Retrieves representation of an instance of webservice.TestResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() throws JSONException {
        //TODO return proper representation object
        JSONObject data = new JSONObject();
        data.append("test", "data_content");
        return data.toString();
    }

    /**
     *
     * @param db_name
     * @return
     * @throws JSONException
     */
    @GET
    @Path("connect/{database}")
    @Produces(MediaType.APPLICATION_JSON)
    public String connect(@PathParam("database")String db_name) throws JSONException {
        //TODO return proper representation object
        JSONObject data = new JSONObject();
        data.append("database", db_name);
        message.envoyerMessage(data.toString());
        String rec = message.getMessage();
        //recu = null;
        if(rec == null || rec.equals(""))
        {
            return "Message reçu vide";
        }
        JSONObject obj = new JSONObject(rec);
        return obj.toString();
    }

    /**
     * PUT method for updating or creating an instance of TestResource
     * @param content representation for the resource
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
        
    }
    
    @GET
    @Path("{database}/{table}/{col}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String supprimer(@PathParam("database") String database,@PathParam("table") String table,@PathParam("col") String col,@PathParam("id") String id)
    {
        try {
            JSONObject data = new JSONObject();
            data.append("database", database);
            data.append("requete", "delete from "+table+" where "+col+"= '"+id+"'");
            message.envoyerMessage(data.toString());
            String rec = message.getMessage();
            //recu = null;
            if(rec == null || rec.equals(""))
            {
                return "Message reçu vide";
            }
            JSONObject obj = new JSONObject(rec);
            return obj.toString();
        } catch (JSONException ex) {
            Logger.getLogger(TestResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Impossible d'effectuer la suppression !";
    }
     @GET
    @Path("{database}/{table}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("database") String database,@PathParam("table") String table)
    {
        try {
            JSONObject data = new JSONObject();
            data.append("database", database);
            data.append("requete", "SELECT * from "+table);
            message.envoyerMessage(data.toString());
            String rec = message.getMessage();
            //recu = null;
            if(rec == null || rec.equals(""))
            {
                return "Message reçu vide";
            }
            JSONObject obj = new JSONObject(rec);
            return obj.toString();
        } catch (JSONException ex) {
            Logger.getLogger(TestResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Impossible de recuperer le contenu de la table";
    }
    
    @GET
    @Path("{database}/{table}/ajouter/{params}")
    @Produces(MediaType.APPLICATION_JSON)
     public String ajouter(@PathParam("database")String database,@PathParam("table")String table,@PathParam("params") String params) {
         try {
            JSONObject data = new JSONObject();
            data.append("database", database);
            String[] values= params.split("&");
            String val="";
            int i=0;
             for (String value : values) {
                 val +="'"+value+"'";
                 val += (++i == values.length)? "":",";
             }
            data.append("requete", "insert into "+table+" values("+val+")");
            message.envoyerMessage(data.toString());
            String rec = message.getMessage();
            JSONObject obj = new JSONObject(rec);
            return obj.toString();
        } catch (JSONException ex) {
            Logger.getLogger(TestResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Impossible d'effectuer la suppression !";
     }
}
