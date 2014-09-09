/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package appsuelos;

/**
 *
 * @author DieGui
 */
import java.sql.*;
public class ConexionPostgres {
    public static void consultar() {
        String cc = "jdbc:postgresql://127.0.0.1/labNSF?" + "user=postgres&password=";  //modificar datos de conexion
        try {
            Class.forName("org.postgresql.Driver");
            Connection conexion = DriverManager.getConnection(cc);
            Statement comando = conexion.createStatement();
            String sql = "SELECT nombre, apellido FROM usuario ORDER BY apellido";
            ResultSet resultado = comando.executeQuery(sql);
            while(resultado.next()) {
                String n = resultado.getString("nombre");
                String a = resultado.getString("apellido");
                System.out.println(n + " " + a);
            }
            resultado.close();
            comando.close();
            conexion.close();
        } 
        catch(Exception e) {
            System.out.println(e.getMessage()+"hola");
        }
    }
}

