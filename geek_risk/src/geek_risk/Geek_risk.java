/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geek_risk;

import geek_risk.classe.Client;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author JESS
 */
public class Geek_risk {

    /**
     * @param args the command line arguments
     */
    static ServerSocket socketserver;
    static Socket socketduserveur;
    static BufferedReader recu;
    static PrintWriter envoye;
    static String msg;
    static DatagramPacket paquetEnvoie;
    static DatagramSocket conteneur;
    static FileWriter writer = null;
    static String reponse;
    static int nbLigne;
    static int nb_conncte = 0;
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("En attente de connexion d'un client...");
           try{
                socketserver = new ServerSocket(5001);
                while(true)
                {
                   socketduserveur = socketserver.accept();
                   nb_conncte++;
                   System.out.println("Client connecté");
                    System.out.println("Client numero :" +nb_conncte);
                   Client client = new Client();
                   client.setSck(socketduserveur);
                   client.start_thread();
                }
           }
           catch(Exception ex)
           {
               JOptionPane.showMessageDialog(null, ex);
           }
        
    }
    
}
