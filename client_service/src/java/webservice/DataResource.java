/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

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
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author JESS
 */
@Path("data")
public class DataResource {
    private static Socket socket;
    //private Socket socketduserveur;
    private  BufferedReader recu;
    private  PrintWriter envoye;
    String msg = new String();
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DataResource
     */
    public DataResource() {
        if(socket == null)
        {
            try { 
                socket = new Socket("localhost", 5001);
            } catch (IOException ex) {
                Logger.getLogger(DataResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }    
    }

    /**
     * Retrieves representation of an instance of webservice.DataResource
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/connect/{database_name}")
    @Produces("application/json")
    public String getJson(@PathParam("database_name")String database_name) throws IOException {
        //TODO return proper representation object
         try {
            JSONObject data = new JSONObject();
            data.append("database", database_name);
            envoyerMessage(data.toString());
            recu = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            msg = recu.readLine();
            //recu = null;
            JSONObject obj = new JSONObject(msg);
            return obj.toString();
        } catch (JSONException ex) {
            Logger.getLogger(DataResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "tsa mety";
    }

    /**
     * PUT method for updating or creating an instance of DataResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
    @GET
    @Path("/connect/{database_name}")
    @Produces("application/json")
    public String getJson2(@PathParam("database_name")String database_name) throws IOException {
        try {
            JSONObject data = new JSONObject();
            data.append("database", database_name);
            envoyerMessage(data.toString());
            recu = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            msg = recu.readLine();
            //recu = null;
            JSONObject obj = new JSONObject(msg);
            return obj.toString();
        } catch (JSONException ex) {
            Logger.getLogger(DataResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "tsa mety";
    }
    @GET
    @Path("/get/{table}")
    @Produces("application/json")
    public String get(@PathParam("table")String table_name) throws IOException{    
        org.json.JSONObject req = new org.json.JSONObject();
            try {
                req.append("requete", "SELECT * from "+table_name);
            } catch (org.json.JSONException ex) {
                
            }
            envoyerMessage(req.toString());
            recu = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            msg = recu.readLine();
            //recu = null;
            System.out.println(msg);
            return msg;
            //return "ok";
    }
    public void envoyerMessage(String msg)
    {
        try{
            envoye = new PrintWriter(socket.getOutputStream());
            envoye.println(msg);
            envoye.flush();
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
        }
        
    }
}
