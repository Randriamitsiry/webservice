/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import com.geek.inside.Message;
import com.geek.inside.TestHtml;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
        System.out.println(obj.toString());
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
    @Path("delete/{table}/{col}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String supprimer(@PathParam("table") String table,@PathParam("col") String col,@PathParam("id") String id)
    {
        try {
            JSONObject data = new JSONObject();
           // data.append("database", database);
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
    @Path("get/{table}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("table") String table)
    {
        try {
            JSONObject data = new JSONObject();
           // data.append("database", database);
            data.append("requete", "SELECT * from "+table);
            message.envoyerMessage(data.toString());
            String rec = message.getMessage();
            //recu = null;
            if(rec == null || rec.equals(""))
            {
                return "Message reçu vide";
            }
            JSONObject obj = new JSONObject(rec);
            System.out.println(obj.toString());
            System.out.println(obj.toString());
            return obj.toString();
        } catch (JSONException ex) {
            Logger.getLogger(TestResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Impossible de recuperer le contenu de la table";
    }
    
    @GET
    @Path("add/{table}/{params}")
    @Produces(MediaType.APPLICATION_JSON)
     public String ajouter(@PathParam("table")String table,@PathParam("params") String param) {
         try {
            JSONObject req_mod = new JSONObject();
            req_mod.append("requete", "INSERT INTO "+table +param);
            message.envoyerMessage(req_mod.toString());
            String req = message.getMessage();
            JSONObject obj = new JSONObject(req);
            return obj.toString();
            
        } catch (JSONException ex) {
            Logger.getLogger(TestResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Impossible d'effectuer l'ajout!";
     }  
     @GET
     @Path("update/{table}/{param}")
     @Produces(MediaType.APPLICATION_JSON)
     public String modifier(@PathParam("table")String table,@PathParam("param") String param) {
        try {
            JSONObject req_mod = new JSONObject();
            req_mod.append("requete", "UPDATE "+table +param);
            message.envoyerMessage(req_mod.toString());
            String req = message.getMessage();
            JSONObject obj = new JSONObject(req);
            return obj.toString();
            
        } catch (JSONException ex) {
            Logger.getLogger(TestResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Impossible d'effectuer la modification!";
     } 
     @GET
     @Path("get/{table}/{criteres: .*}")
     @Produces(MediaType.APPLICATION_JSON)
     public String get(@PathParam("table")String table,@PathParam("criteres")String critere) throws JSONException
     {
         //on cite le critere comme ceci : nom=abned&prenom=
         /*JSONObject req_mod = new JSONObject();
         req_mod.append("requete", "SELECT * FROM "+table+" WHERE "+critere);
         //System.out.println("SELECT * FROM "+table+" WHERE "+critere);
         message.envoyerMessage(req_mod.toString());
         String req = message.getMessage();
         JSONObject obj = new JSONObject(req);
        System.out.println("testtt "+ obj.toString());
         return obj.toString();*/
        if(TestHtml.isHtml(table) || TestHtml.isHtml(critere))
        {
            JSONObject obj = new JSONObject();
            obj.append("error", "Le donnée que vous avez entrer n'est pas valide !");
            return obj.toString();
        }
        else
        {
            JSONObject data = new JSONObject();
            data.append("requete", "SELECT * FROM "+table+" WHERE "+critere);
            message.envoyerMessage(data.toString());
            String rec = message.getMessage();
            //recu = null;
            if(rec == null || rec.equals(""))
            {
                return "Message reçu vide";
            }
            JSONObject obj = new JSONObject(rec);
            System.out.println(obj.toString());
            return obj.toString();
        }
     }
     
}
