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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
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
            generarInformeSuelos();
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
        String file = "pruba.xls";
        try{
            FileOutputStream out = new FileOutputStream(file);
            escribirEncabezadoInforme(libro, hoja);
            libro.write(out);
            out.close();
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
            JOptionPane.showMessageDialog(null, "Error con la Base de Datos");
        }
    }
    
    public static void escribirEncabezadoInforme(Workbook wb, Sheet h) throws FileNotFoundException, IOException{
        //Titulos con estilos e imagenes
        /* 
                                    UNIVERSIDAD AUSTRAL DE CHILE                            Garamond LightCondensed 22
            LABORATORIO DE NUTRICIÓN Y SUELOS FORESTALES - FACULTAD DE CIENCIAS FORESTALES  Garamond LightCondensed 14
                    CASILLA 567 - VALDIVIA FONOFAX (63) 221431  E-mail  labnsf@uach.cl      Garamond LightCondensed 14
                            http://www.uach.cl/labsuelosforestales                          Arial 12 (Link)
         */
        CreationHelper helper = wb.getCreationHelper();
        
        //Estilo Celda del titulo  UNIVERSIDAD AUSTRAL DE CHILE
        CellStyle cs1 = wb.createCellStyle();
        cs1.setAlignment(CellStyle.ALIGN_CENTER);        
        Font f = wb.createFont();
        f.setFontName("Garamond");
        f.setFontHeightInPoints((short) 22);
        f.setColor((short)0x7fff);
        f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cs1.setFont(f);                
        Row fila = h.createRow(1);
        Cell celda = fila.createCell(7);
        celda.setCellStyle(cs1);
        celda.setCellValue("U   N  I  V  E  R  S  I  D  A  D         A  U  S  T  R  A  L        D  E            C  H  I  L  E");
        
        //Estilo Celda del titulo  LABORATORIO DE NUTRICIÓN Y SUELOS FORESTALES - FACULTAD DE CIENCIAS FORESTALES
        CellStyle cs2 = wb.createCellStyle();
        cs2.setAlignment(CellStyle.ALIGN_CENTER);        
        Font f2 = wb.createFont();
        f2.setFontName("Garamond");
        f2.setFontHeightInPoints((short) 14);
        f2.setColor((short)0x7fff);
        f2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cs2.setFont(f2);                
        fila = h.createRow(2);
        celda = fila.createCell(7);
        celda.setCellStyle(cs2);
        celda.setCellValue("LABORATORIO DE NUTRICIÓN Y SUELOS FORESTALES - FACULTAD DE CIENCIAS FORESTALES");
        
        //Estilo Celda del titulo   CASILLA 567 - VALDIVIA FONOFAX (63) 221431  E-mail  labnsf@uach.cl
        fila = h.createRow(3);
        celda = fila.createCell(7);
        celda.setCellStyle(cs2);
        celda.setCellValue("CASILLA 567 - VALDIVIA FONOFAX (63) 221431  E-mail  labnsf@uach.cl");
                
        //Estilo Celda del titulo  http://www.uach.cl/labsuelosforestales LINK
        CellStyle hlink_style = wb.createCellStyle();
        hlink_style.setAlignment(CellStyle.ALIGN_CENTER); 
        Font hlink_font = wb.createFont();
        hlink_font.setFontHeightInPoints((short) 12); 
        hlink_font.setUnderline(Font.U_SINGLE);
        hlink_font.setColor(IndexedColors.BLUE.getIndex());
        hlink_style.setFont(hlink_font);
        fila = h.createRow(4);
        celda = fila.createCell(7);
        celda.setCellStyle(hlink_style);
        celda.setCellValue("http://www.uach.cl/labsuelosforestales");
        
        int pictureIdx;
        try{ //agregar imagen del logo del laboratorio   Fila 10 Columna 13
            InputStream is = new FileInputStream("logoLab.png");
            byte[] bytes = IOUtils.toByteArray(is);
            pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            Drawing drawing = h.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(13);
            anchor.setRow1(10);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize();
        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        
    }
                
        
        
        
    
        
        //agregar jefe y analistas del laboratorio
        /*
        JEFE LABORATORIO:  GISELA ROMENY, Bioquímico
        ANALISTAS:  ILONA SLEBE, Agrónomo
                    GISELA ROMENY, Bioquímico
        
        */
    }

