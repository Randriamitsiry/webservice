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
import org.json.*;

/**
 * REST Web Service
 *
 * @author JESS
 */
@Path("test")
public class TestResource {
    private static Socket socket;
    //private Socket socketduserveur;
    private  BufferedReader recu;
    private  PrintWriter envoye;
    String msg = new String();
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TestResource
     */
    public TestResource() throws IOException {
        if(socket == null)
        {
            socket = new Socket("localhost", 5001); 
        }   
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
        try {
            //TODO return proper representation object
            JSONObject data = new JSONObject();
            data.append("database", db_name);
            envoyerMessage(data.toString());
            recu = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            try {
                msg = recu.readLine();
            } catch (IOException ex) {
                Logger.getLogger(TestResource.class.getName()).log(Level.SEVERE, null, ex);
            }
            //recu = null;
            JSONObject obj = new JSONObject(msg);
            return obj.toString();
        } catch (IOException ex) {
            Logger.getLogger(TestResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        JSONObject error = new JSONObject();
        error.append("erreur", "Erreur de la connexion à la bdd");
        return error.toString();
    }

    /**
     * PUT method for updating or creating an instance of TestResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
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
