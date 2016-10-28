/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geek_risk.classe;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;

/**
 *
 * @author JESS
 */
public class Connexion {
    private String URL = "jdbc:mysql://localhost:3306";
    private Connection conn;
    private String database_name;
    private String username;
    private String password;
    public Connexion()
    {
    
    }

    public Connexion(String database_name, String username, String password) {
        this.database_name = database_name;
        this.username = username;
        this.password = password;
    }
    
    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public String getDatabase_name() {
        return database_name;
    }

    public void setDatabase_name(String database_name) {
        this.database_name = database_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void ouvrirConnexion()
    {
        try{
            String URL_complet = URL+"/"+database_name;
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = (Connection) DriverManager.getConnection(URL,username,password);
            System.out.println(conn.getHost());
        }
        catch(Exception ex)
        {
            System.err.println("Erreur :"+ex.getMessage());
        }
    }
    public void fermer_connexion()
    {
        try{
            conn.close();
        }
        catch(Exception e)
        {
            System.err.println("Erreur fermeture :"+e.getMessage());
        }
    }
}
