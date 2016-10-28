/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geek_risk.classe;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author JESS
 */
public class Client {
    private InetAddress ip_cli;
    private static Connexion conn;
    private static Socket sck;
    static BufferedReader recu;
    static PrintWriter envoye;
    static String msg;
    static DatagramPacket paquetEnvoie;
    static DatagramSocket conteneur;
    static FileWriter writer = null;
    static String reponse;
    public Client(Connexion conn) {
        this.conn = conn;
    }

    public Client() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public void start_thread() throws IOException
    {
        conn = new Connexion();
        envoye = new PrintWriter(sck.getOutputStream());
          Thread recevoir;
          recevoir = new Thread(new Runnable() {
          @Override
          public void run() {
           while(true)
           {
                
                try {
                    recu = new BufferedReader(new InputStreamReader(sck.getInputStream()));
                    String ret = recu.readLine();
                    JSONObject rc = new JSONObject(ret);
                    if(rc.has("database"))
                    {
                        System.out.println("database :");
                        JSONArray test = new JSONArray();
                        test = rc.getJSONArray("database");
                        String db = test.get(0).toString();
                        conn.setDatabase_name(db);
                        JSONObject tables = getAllTable();
                        reponse = tables.toString();
                        envoyerMessage();
                    }
                    else if(rc.has("requete"))
                    { 
                        //System.out.println(".run()");
                        msg = ret;
                        System.out.println(msg);
                        JSONObject rec = new JSONObject(msg);
                        JSONArray requete_brute = rec.getJSONArray("requete");
                        reponse = TraiterRequete(requete_brute.get(0).toString());
                        System.out.println(reponse);
                        envoyerMessage();   

                    }
                    else{
                         System.out.println("ATO tsika zao");
                        JSONObject erreur = new JSONObject();
                        erreur.append("database_error", "database_empty");
                        reponse =erreur.toString();
                        envoyerMessage();                       
                    }

                  } catch (Exception ex) {
                      System.out.println(ex.getMessage());
                  }                  
          }
           }
          });
          recevoir.start();
    }

    public Client(InetAddress ip_cli) {
        this.ip_cli = ip_cli;
    }

    public Client(InetAddress ip_cli, Connexion conn) {
        this.ip_cli = ip_cli;
        this.conn = conn;
    }

    public InetAddress getIp_cli() {
        return ip_cli;
    }

    public void setIp_cli(InetAddress ip_cli) {
        this.ip_cli = ip_cli;
    }

    public Socket getSck() {
        return sck;
    }

    public void setSck(Socket sck) {
        this.sck = sck;
    }

    public Connexion getConn() {
        return conn;
    }

    public void setConn(Connexion conn) {
        this.conn = conn;
    }
    public static JSONObject getAllTable()
    {
        System.out.println("getAllTable executé");
        JSONObject retour = null;
        try{
             retour = new JSONObject();
            int i;
            String URL = "jdbc:mysql://localhost:3306/"+conn.getDatabase_name();
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = (Connection) DriverManager.getConnection(URL,"root","");
            Statement st = (Statement) conn.createStatement();
            try{
                ResultSet rs = st.executeQuery("SHOW TABLES");
                while(rs.next())
                {
                    retour.append("table", rs.getString(1));
                }
                System.out.println("getAllTable executé avec succès");
            }
            catch(Exception e)
            {
                System.err.println("Show table failed");
            }
        }
       catch(Exception ex)
       {
           System.err.println("Failed to load all table of the database");
       }
        return retour; 
    }
    public static String TraiterRequete(String req) throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException
    {
        JSONObject retour = new JSONObject();
        int i;
        String URL = "jdbc:mysql://localhost:3306/"+conn.getDatabase_name();
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = (Connection)DriverManager.getConnection(URL,"root","");
        Statement st = (Statement) conn.createStatement();
        try{
            ResultSet rs = null;
            if(req.toLowerCase().contains("select") || req.toLowerCase().contains("desc"))
            {
                //retour.append("Action", "SELECT");
                rs = st.executeQuery(req);
                int colcount = rs.getMetaData().getColumnCount();
                while(rs.next())
                {
                    for(i=1;i<=colcount;i++)
                    {
                        retour.append(rs.getMetaData().getColumnName(i), rs.getObject(i));
                    }
                }
                System.out.println("Table :" + rs.getMetaData().getTableName(1));
                rs.close(); 
            }
            else if(req.toLowerCase().contains("update"))
            {
               // retour.append("Action", "UPDATE");
                st.executeUpdate(req);
                System.out.println("requete update executé");
                retour.append("Affecté", "Nombre de ligne mis à jour : "+ st.getUpdateCount());
                st.close();
            }
            else if(req.toLowerCase().contains("delete"))
            {
                retour.append("Action", "DELETE");
                st.execute(req);
                retour.append("Affecté", st.getUpdateCount()+ ": lignes supprimés");
                st.close();
            }
            else
            {
                //retour.append("Action", "INSERT");
                st.execute(req);
                retour.append("Affecté", st.getUpdateCount() + " : Lignes inserés");
                st.close();
            }
            
            /*int affected = rs.getRow();
            retour.append("Affecté", affected);*/
                       
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
        //System.out.println(retour);
        return retour.toString();
    }
    public static void envoyerMessage()
    {
        try{
            envoye = new PrintWriter(sck.getOutputStream());
            envoye.println(reponse);
            envoye.flush();
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
        }
        
    }
    
}
