/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geek.inside;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author JESS
 */
public class Message {
    private static Socket socket;
    //private Socket socketduserveur;
    private  BufferedReader recu;
    private  PrintWriter envoye;
    private String message;
    
    public Message()
    {
        if(socket == null)
        {
            try { 
                socket = new Socket("localhost", 5001);
            } catch (IOException ex) {
                System.out.println("Impossible de créer la connection au serveur des données !");
            }
        } 
    }
    public String envoyerMessage(String msg)
    {
        String ret = null;
        try{
            if(socket != null)
            {
                envoye = new PrintWriter(socket.getOutputStream());
                envoye.println(msg);
                envoye.flush();
            }
            else
            {
                ret = "Impossible d'effectuer l'envoi du requete !";
            }
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
        }
        return ret;
    }
    public static Socket getSocket() {
        return socket;
    }
    public BufferedReader getRecu() {
        return recu;
    }

    public void setRecu(BufferedReader recu) {
        this.recu = recu;
    }

    public PrintWriter getEnvoye() {
        return envoye;
    }

    public void setEnvoye(PrintWriter envoye) {
        this.envoye = envoye;
    }
    //equivallen de recevoir message
    public String getMessage() {
        try {
            recu = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            message = recu.readLine();
            
        } catch (Exception e) {
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
}