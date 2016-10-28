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
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author JESS
 */
@Path("database")
public class Database {
    
    private static Socket socket;
    //private Socket socketduserveur;
    private  BufferedReader recu;
    private  PrintWriter envoye;
    String msg = new String();
    @Context
    private UriInfo context;
    
    public Database() {
        if(socket == null)
        {
            try { 
                socket = new Socket("localhost", 5001);
            } catch (IOException ex) {
                System.out.println("erreur de la creation du moteur de requete(socket)!");
            }
        }   
    }
    @GET
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String list() { 
            JSONObject data = new JSONObject();
        try {
            data.append("database", "mysql");
            data.append("requete", "show databases");
            envoyerMessage(data.toString());
            try {
                recu = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                msg = recu.readLine();
            } catch (IOException ex) {
                System.out.println("Erreur lors la lecture des reponses !");
            }
            //recu = null;
            JSONObject obj = new JSONObject(msg);
            return obj.toString();
        } catch (JSONException ex) {
            System.out.println("Erreur interne lors du traitement des données !");
        }
            return data.toString();
    }
    
    
   
    /*@GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
    
    throw new UnsupportedOperationException();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }*/

    private void envoyerMessage(String toString) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
