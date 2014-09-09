/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package appsuelos;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/**
 *
 * @author DieGui
 */
public class AppSuelos {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        generarInformeSuelos();
        leerArchivo("D:\\Dropbox\\readme.txt");
        ConexionPostgres.consultar();
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
    
    public static boolean validarUsuario(String rut, String password){
        return true;
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
