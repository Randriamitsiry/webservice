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
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
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
    
    private Message message;
    @Context
    private UriInfo context;
    
    public Database() {
        message = new Message();
    }
    @GET
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String list() { 
        try {
            JSONObject data = new JSONObject();
            data.append("database", "mysql");
            data.append("requete", "show databases");
            message.envoyerMessage(data.toString());
            String rec = message.getMessage();
            //recu = null;
            JSONObject obj = new JSONObject(rec);
            return obj.toString();
        } catch (Exception e) {
        }
        return "Il n'y a aucun base de donnée au serveur de donnée!";
    }
    
    @GET
    @Path("create/{database}")
    @Produces(MediaType.APPLICATION_JSON)
    public String create(@PathParam("database") String database)
    {
        try {
            JSONObject data = new JSONObject();
            data.append("database", "mysql");
            data.append("requete", "create database "+database);
            message.envoyerMessage(data.toString());
            String rec = message.getMessage();
            //recu = null;
            JSONObject obj = new JSONObject(rec);
            return obj.toString();
        } catch (Exception e) {
        }
        return "Impossible de créer ce base de donnée!";
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
}
