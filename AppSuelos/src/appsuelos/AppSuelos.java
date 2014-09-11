/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package appsuelos;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
/**
 *
 * @author DieGui
 */
public class AppSuelos {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    static final String url = "jdbc:postgresql://localhost:5432/labNSF";  //modificar datos de conexion
    static Connection con; 
    static String userDB = "javapgsql";
    static String passDB = "javapgsql";
    static String nombre;
    static String apellido;
    static Inicio v1;
    static Tareas v2;
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        try{
            Class.forName("org.postgresql.Driver");            
            v1 = new Inicio();
            v1.setVisible(true);
            //generarInformeSuelos();
            //leerArchivo("D:\\Dropbox\\readme.txt");            
        }      
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void leerArchivo(String ruta) throws IOException{
        try{
            File archivo = new File(ruta);
            Scanner entrada = new Scanner(archivo);
            while(entrada.hasNextLine()){
                String linea = entrada.nextLine();
                System.out.println(linea);
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    public static void generarInformeSuelos() throws IOException{
        Workbook libro = new HSSFWorkbook();  //crear libro
        Sheet hoja = libro.createSheet("HojaDePrueba");
        escribirEncabezadoInforme(hoja);
        String file = "pruba.xls";
        try{
            FileOutputStream out = new FileOutputStream(file);
            libro.write(out);
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(AppSuelos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void consultar(String query){
        
    }
    
    public static void validarUsuario(String rut, String password){
        try{
            con  = DriverManager.getConnection(url, userDB, passDB);
            String query = "SELECT nombre, apellido FROM usuario where rut=\'"+rut+"\' AND pass=\'"+password+"\'";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                System.out.print("Correcto");                
                nombre = rs.getString("nombre");
                apellido = rs.getString("apellido");
                stmt.execute("END");
                v1.setVisible(false);
                v2 = new Tareas();
                v2.setVisible(true);
                v2.jLabel1.setText(nombre + " " +apellido);                
            }
            else{
                JOptionPane.showMessageDialog(null, "Rut o Contraseña Incorrecta");
            }            
            stmt.close();
            con.close();
        }
        catch(Exception e){
            System.out.print(e.getMessage());
        }
    }
    
    public static void escribirEncabezadoInforme(Sheet h){
        //Titulos con estilos e imagenes
        /* 
                                    UNIVERSIDAD AUSTRAL DE CHILE
            LABORATORIO DE NUTRICIÓN Y SUELOS FORESTALES - FACULTAD DE CIENCIAS FORESTALES
                    CASILLA 567 - VALDIVIA FONOFAX (63) 221431  E-mail  labnsf@uach.cl
                            http://www.uach.cl/labsuelosforestales
         */
        for(int i=0; i<5; i++){
            Row fila = h.createRow(i);
            for(int j=0; j<26; j++){
                Cell celda = fila.createCell(j);
                celda.setCellValue("UNIVERSIDAD AUSTRAL DE CHILE");
            }            
        }
                
        
        
        //agregar imagen del logo del laboratorio
        
        //agregar jefe y analistas del laboratorio
        /*
        JEFE LABORATORIO:  GISELA ROMENY, Bioquímico
        ANALISTAS:  ILONA SLEBE, Agrónomo
                    GISELA ROMENY, Bioquímico
        
        */
    }
}
