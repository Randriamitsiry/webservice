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
                    JSONArray test = null; //reponse
                    JSONObject retour_requete = null; // preperation du requete
                    // traite la requete en cours
                    //database :: base de donnee
                    //requete :: sql
                    Boolean has_database = rc.has("database") && rc.has("requete");
                    if(has_database)
                    {
                        test = rc.getJSONArray("database");
                        String db = test.get(0).toString();
                        conn.setDatabase_name(db);
                        
                        test = rc.getJSONArray("requete");
                        //test = rec.getJSONArray("requete");
                        reponse = TraiterRequete(test.get(0).toString());
                        envoyerMessage();
                    }
                    else if(rc.has("database"))
                    {
                        //System.out.println("database :");
                        //test = new JSONArray();
                        test = rc.getJSONArray("database"); // get the selected database
                        String db = test.get(0).toString();
                        if(getDatabase(db)) //test si la db existe ou non
                        {
                            conn.setDatabase_name(db);
                            retour_requete = getAllTable();
                        }
                        else
                        {
                            retour_requete = new JSONObject();
                            retour_requete.put("error", "Ce base de donnée n'existe pas !");
                        }
                        reponse = retour_requete.toString();
                        envoyerMessage();
                    }
                    else if(rc.has("requete"))
                    { 
                        //System.out.println(".run()");
                        msg = ret;
                        //System.out.println(msg);
                        JSONObject rec = new JSONObject(msg);
                        test = rec.getJSONArray("requete");
                        reponse = TraiterRequete(test.get(0).toString());
                       // System.out.println(reponse);
                        envoyerMessage();   

                    }
                    else{
                        //System.out.println("ATO tsika zao");
                        JSONObject erreur = new JSONObject();
                        erreur.append("error", "database_empty");
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
    public static boolean getDatabase(String database)
    {
        boolean retour = false;
        try{
            String URL = "jdbc:mysql://localhost:3306/mysql";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = (Connection) DriverManager.getConnection(URL,"root","");
            Statement st = (Statement) conn.createStatement();
            try{
                ResultSet rs = st.executeQuery("SHOW DATABASES like '"+database+"'");
                if(rs.next())
                {
                    retour = true;
                }
                else
                {
                    retour=false;
                }
                //System.out.println("getAllTable executé avec succès");
            }
            catch(Exception e)
            {
                System.err.println("Show table failed");
            }
        }
       catch(Exception ex)
       {
           System.err.println("Failed to load all database"+ex.getMessage());
       }
        return retour; 
    }
    public static JSONObject getAllTable()
    {
        //System.out.println("getAllTable executé");
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
                if(rs.next())
                {
                    do
                    {
                        retour.append("table", rs.getString(1));
                    }while(rs.next());
                }
                else
                {
                    retour.append("error", "Ce base de donné n'existe pas !");
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
           System.err.println("Failed to load all table of the database"+ex.getMessage());
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
            if(req.toLowerCase().contains("create"))
            {
                retour.append("action", "create");
                st.executeUpdate(req);
                retour.append("affacté", st.getUpdateCount());
                st.close();
            }
            else if(req.toLowerCase().contains("insert"))
            {
                retour.append("action", "INSERT");
                st.execute(req);
                retour.append("affecté", st.getUpdateCount() + " : Lignes inserés");
                st.close();
            }
            else if(req.toLowerCase().contains("update"))
            {
               // retour.append("Action", "UPDATE");
                st.executeUpdate(req);
                //System.out.println("requete update executé");
                retour.append("affecté", "Nombre de ligne mis à jour : "+ st.getUpdateCount());
                st.close();
            }
            else if(req.toLowerCase().contains("delete"))
            {
                //retour.append("Action", "DELETE");
                st.execute(req);
                retour.append("affecté", st.getUpdateCount()+ ": lignes supprimés");
                st.close();
            }
            else
            {
                //
                retour.append("action", "SELECT");
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
