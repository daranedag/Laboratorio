/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package appsuelos;

import java.awt.HeadlessException;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
        }      
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void leerArchivo(String ruta) throws IOException{
        try{
            File archivo = new File(ruta);
            Scanner entrada = new Scanner(archivo);
            String linea;
            int i = 0;
            while(entrada.hasNextLine()){
                if(i<16){
                    entrada.nextLine();
                    i++;
                }
                else{
                    linea = entrada.nextLine();
                    ingresarMuestra(linea);
                }
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    public static void generarInformeSuelos() throws IOException{
        Workbook libro = new HSSFWorkbook();  //crear libro
        Sheet hoja = libro.createSheet("HojaDePrueba");
        PrintSetup ps = hoja.getPrintSetup();
        hoja.setAutobreaks(true);
        ps.setFitHeight((short) 1);
        ps.setFitWidth((short) 1);
        agregarImagenInforme(libro, hoja);
        String file = "InformeSuelos.xls";
        try{
            FileOutputStream out = new FileOutputStream(file);
            escribirEncabezadoInforme(libro, hoja);
            //agregarImagenInforme(libro, hoja);
            libro.write(out);
            out.close();
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(AppSuelos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void consultar(String query){
        
    }
    
    public static void ingresarMuestra(String line){
        String[] sep = line.split("\t");
        System.out.print(sep[1]);
        System.out.print("\t");
        System.out.println(sep[6]);
        /*
        for(int i=0; i<sep.length; i++){
            if(i==0 || i==6){
                System.out.print(sep[0]);
            }
            System.out.println();
        }*/
    }
    
    public static void validarUsuario(String rut, String password){
        try{
            con  = DriverManager.getConnection(url, userDB, passDB);
            String query = "SELECT nombre, apellido FROM usuario where rut=\'"+rut+"\' AND pass=\'"+password+"\'";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){              
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
        catch(HeadlessException e){
            System.out.print(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error con la Base de Datos");
        } catch (SQLException e) {
            System.out.print(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error con la Base de Datos");
        }
    }
    
    
    public static void agregarImagenInforme(Workbook wb, Sheet h) throws FileNotFoundException, IOException{
        try{
            InputStream is = new FileInputStream("C:\\Users\\DieGui\\Documents\\GitHub\\Laboratorio\\AppSuelos\\src\\appsuelos\\logo2.jpg");
            byte[] bytes = IOUtils.toByteArray(is);
            int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            is.close();
            CreationHelper helper = wb.getCreationHelper();
            // Create the drawing patriarch.  This is the top level container for all shapes. 
            Drawing drawing = h.createDrawingPatriarch();
            //add a picture shape
            ClientAnchor anchor = helper.createClientAnchor();
            //set top-left corner of the picture,
            //subsequent call of Picture#resize() will operate relative to it
            anchor.setCol1(13);
            anchor.setRow1(9);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            //auto-size picture relative to its top-left corner
            pict.resize();            
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }        
    }
    
    public static void escribirEncabezadoInforme(Workbook wb, Sheet h) throws FileNotFoundException, IOException{            
        //Estilo Celda del titulo  UNIVERSIDAD AUSTRAL DE CHILE
        CellStyle cs1 = wb.createCellStyle();
        cs1.setAlignment(CellStyle.ALIGN_CENTER);        
        Font f = wb.createFont();
        f.setFontName("Garamond");
        f.setFontHeightInPoints((short) 22);
        f.setColor((short)0x7fff);
        f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cs1.setFont(f);                
        Row fila = h.createRow(2);
        Cell celda = fila.createCell(7);
        celda.setCellStyle(cs1);
        celda.setCellValue("U   N  I  V  E  R  S  I  D  A  D       A  U  S  T  R  A  L      D  E         C  H  I  L  E");

        //Estilo Celda del titulo  LABORATORIO DE NUTRICIÓN Y SUELOS FORESTALES - FACULTAD DE CIENCIAS FORESTALES
        CellStyle cs2 = wb.createCellStyle();
        cs2.setAlignment(CellStyle.ALIGN_CENTER);        
        Font f2 = wb.createFont();
        f2.setFontName("Garamond");
        f2.setFontHeightInPoints((short) 14);
        f2.setColor((short)0x7fff);
        f2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cs2.setFont(f2);                
        fila = h.createRow(3);
        celda = fila.createCell(7);
        celda.setCellStyle(cs2);
        celda.setCellValue("LABORATORIO DE NUTRICIÓN Y SUELOS FORESTALES - FACULTAD DE CIENCIAS FORESTALES");

        //Estilo Celda del titulo   CASILLA 567 - VALDIVIA FONOFAX (63) 221431  E-mail  labnsf@uach.cl
        fila = h.createRow(4);
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
        fila = h.createRow(5);
        celda = fila.createCell(7);
        celda.setCellStyle(hlink_style);
        celda.setCellValue("http://www.uach.cl/labsuelosforestales");            
        //agregar jefe y analistas del laboratorio
        
        CellStyle cs3 = wb.createCellStyle();
        cs3.setAlignment(CellStyle.ALIGN_LEFT);        
        Font f3 = wb.createFont();
        f3.setFontName("Arial");
        f3.setFontHeightInPoints((short) 11);
        f3.setColor((short)0x7fff);
        f3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cs3.setFont(f3);
        
        CellStyle cs4 = wb.createCellStyle();
        cs4.setAlignment(CellStyle.ALIGN_LEFT);
        Font f4 = wb.createFont();
        f4.setFontName("Arial");
        f4.setFontHeightInPoints((short) 11);
        f4.setColor((short)0x7fff);
        cs4.setFont(f4);        
        
        fila = h.createRow(13);
        celda = fila.createCell(1);
        celda.setCellStyle(cs3);
        celda.setCellValue("JEFE LABORATORIO:   ");
        celda = fila.createCell(3);
        celda.setCellStyle(cs3);
        celda.setCellValue("GISELA ROMENY, Bioquímico");
        fila = h.createRow(15);
        celda = fila.createCell(1);
        celda.setCellStyle(cs4);
        celda.setCellValue("ANALISTAS:          ");
        celda = fila.createCell(3);
        celda.setCellStyle(cs4);
        celda.setCellValue("ILONA SLEBE, Agrónomo");
        fila = h.createRow(16);
        celda = fila.createCell(3);
        celda.setCellStyle(cs4);
        celda.setCellValue("GISELA ROMENY, Bioquímico");
        /*
        JEFE LABORATORIO:  GISELA ROMENY, Bioquímico
        ANALISTAS:  ILONA SLEBE, Agrónomo
                    GISELA ROMENY, Bioquímico        
        */
        
        //Configuración Ancho de Columnas
        for(int i=0; i<17; i++){
            switch(i){
                case 0:  //A
                    h.setColumnWidth(0, 8*256);
                    break;
                case 1: //B
                    h.setColumnWidth(1, 22*256);
                    break;
                case 2: //C
                    h.setColumnWidth(2, 14*256);
                    break;
                case 3: //D
                    h.setColumnWidth(3, 15*256);
                    break;
                case 4: //E
                    h.setColumnWidth(4, 10*256);
                    break;
                case 5: //F
                    h.setColumnWidth(5, 19*256);
                    break;    
                case 6: //G
                    h.setColumnWidth(6, 14*256);
                    break;
                case 7: //H
                    h.setColumnWidth(7, 13*256);
                    break;    
                case 8: //I
                    h.setColumnWidth(8, 10*256);
                    break;
                case 9://J
                    h.setColumnWidth(9, 10*256);
                    break;
                case 10://K
                    h.setColumnWidth(10, 12*256);
                    break;
                case 11://L
                    h.setColumnWidth(11, 10*256);
                    break;    
                case 12://M
                    h.setColumnWidth(12, 12*256);
                    break;
                case 13://N
                    h.setColumnWidth(13, 10*256);
                    break;    
                case 14://O
                    h.setColumnWidth(14, 10*256);
                    break;
                case 15://P
                    h.setColumnWidth(15, 12*256);
                    break;    
                case 16://Q
                    h.setColumnWidth(16, 3*256);
                    break; 
            }
        }
        
        //Agregar nombres y datos de la muestra y cliente
        fila = h.createRow(23);
        celda = fila.createCell(1);
        celda.setCellStyle(cs4);
        celda.setCellValue("SOLICITANTE");
        fila = h.createRow(24);
        celda = fila.createCell(1);
        celda.setCellStyle(cs4);
        celda.setCellValue("FECHA RECEPCIÓN");
        fila = h.createRow(25);
        celda = fila.createCell(1);
        celda.setCellStyle(cs4);
        celda.setCellValue("FECHA ENTREGA");
        fila = h.createRow(26);
        celda = fila.createCell(1);
        celda.setCellStyle(cs4);
        celda.setCellValue("ARCHIVO");
                
        //Agregar titulo del informe
        CellStyle cs5 = wb.createCellStyle();
        cs5.setAlignment(CellStyle.ALIGN_LEFT);
        Font f5 = wb.createFont();
        f5.setFontName("Arial");        
        f5.setFontHeightInPoints((short) 12);
        f5.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        f5.setColor((short)0x7fff);
        cs5.setFont(f5);
        fila = h.createRow(27);
        celda = fila.createCell(6);  
        celda.setCellStyle(cs5);
        celda.setCellValue("INFORME ANALISIS QUIMICO AGUA  ");
        
        // Bordes de datos y estilo de celdas
        CellStyle cs6 = wb.createCellStyle();
        cs6.setAlignment(CellStyle.ALIGN_CENTER);
        Font f6 = wb.createFont();
        f6.setFontName("Arial");        
        f6.setFontHeightInPoints((short) 12);
        cs6.setFont(f6);
        //Estilo borde izquierdo
        cs6.setBorderLeft(CellStyle.BORDER_MEDIUM);
        cs6.setLeftBorderColor(IndexedColors.BLACK.getIndex());        
        //Estilo borde derecho
        cs6.setBorderRight(CellStyle.BORDER_MEDIUM);
        cs6.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cs6.setBorderTop(CellStyle.BORDER_MEDIUM);
        cs6.setTopBorderColor(IndexedColors.BLACK.getIndex());
        
        CellStyle cs7 = wb.createCellStyle();
        cs7.setAlignment(CellStyle.ALIGN_CENTER);
        cs7.setFont(f6);
        cs7.setBorderLeft(CellStyle.BORDER_MEDIUM);
        cs7.setLeftBorderColor(IndexedColors.BLACK.getIndex());        
        //Estilo borde derecho
        cs7.setBorderRight(CellStyle.BORDER_MEDIUM);
        cs7.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cs7.setBorderTop(CellStyle.BORDER_THIN);
        cs7.setTopBorderColor(IndexedColors.BLACK.getIndex());
        
        CellStyle cs8 = wb.createCellStyle();
        cs8.setAlignment(CellStyle.ALIGN_CENTER);
        cs8.setFont(f6);
        cs8.setBorderLeft(CellStyle.BORDER_MEDIUM);
        cs8.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cs8.setBorderBottom(CellStyle.BORDER_MEDIUM);
        cs8.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                
        CellStyle cs9 = wb.createCellStyle();
        cs9.setAlignment(CellStyle.ALIGN_CENTER);
        cs9.setFont(f6);
        cs9.setBorderTop(CellStyle.BORDER_MEDIUM);
        cs9.setTopBorderColor(IndexedColors.BLACK.getIndex());
        
        CellStyle cs10 = wb.createCellStyle();
        cs10.setAlignment(CellStyle.ALIGN_CENTER);
        cs10.setFont(f6);
        cs10.setBorderLeft(CellStyle.BORDER_MEDIUM);
        cs10.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cs10.setBorderBottom(CellStyle.BORDER_MEDIUM);
        cs10.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        
        CellStyle cs11 = wb.createCellStyle();
        cs11.setBorderLeft(CellStyle.BORDER_MEDIUM);
        cs11.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        
        CellStyle cs12 = wb.createCellStyle();
        cs12.setBorderRight(CellStyle.BORDER_MEDIUM);
        cs12.setRightBorderColor(IndexedColors.BLACK.getIndex());
        
        CellStyle cs13 = wb.createCellStyle();
        cs13.setAlignment(CellStyle.ALIGN_CENTER);
        cs13.setFont(f6);
        
        CellStyle cs14 = wb.createCellStyle();
        cs14.setAlignment(CellStyle.ALIGN_CENTER);
        cs14.setFont(f6);
        cs14.setBorderLeft(CellStyle.BORDER_MEDIUM);
        cs14.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cs14.setBorderBottom(CellStyle.BORDER_MEDIUM);
        cs14.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        
        CellStyle cs15 = wb.createCellStyle();
        cs15.setBorderBottom(CellStyle.BORDER_MEDIUM);
        cs15.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        
        CellStyle cs16 = wb.createCellStyle();
        cs16.setBorderBottom(CellStyle.BORDER_MEDIUM);
        cs16.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cs16.setBorderRight(CellStyle.BORDER_MEDIUM);
        cs16.setRightBorderColor(IndexedColors.BLACK.getIndex());
        
        CellStyle cs17 = wb.createCellStyle();
        cs17.setBorderBottom(CellStyle.BORDER_MEDIUM);
        cs17.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cs17.setBorderRight(CellStyle.BORDER_MEDIUM);
        cs17.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cs17.setBorderTop(CellStyle.BORDER_THIN);
        cs17.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cs17.setAlignment(CellStyle.ALIGN_CENTER);
        cs17.setFont(f6);
        
        fila = h.createRow(33);
        for(int i=0; i<14; i++){
            celda = fila.createCell(i);
            celda.setCellStyle(cs6);
            if(i==0){
                celda.setCellValue("Nº");
            }
            if(i==2){
                celda.setCellStyle(cs9);
            }
            if(i==1 || i==3){
                celda.setCellStyle(cs9);
            }
        }
        fila = h.createRow(34);
        for(int i=0; i<14; i++){
            celda = fila.createCell(i);
            celda.setCellStyle(cs7);
            switch(i){
                case 0:
                    celda.setCellValue("LAB.");
                    break;
                case 1:
                    celda.setCellStyle(cs11);
                    break;
                case 2:
                    celda.setCellStyle(cs13);
                    celda.setCellValue("IDENTIFICACION");
                    break;
                case 3:
                    celda.setCellStyle(cs12);
                    break;
                case 4:
                    celda.setCellValue("pH");
                    break;
                case 5:
                    celda.setCellValue("Condctividad");
                    break;                    
                case 6:
                    celda.setCellValue("Na");
                    break;
                case 7:
                    celda.setCellValue("K");
                    break;
                case 8:
                    celda.setCellValue("Ca");
                    break;
                case 9:
                    celda.setCellValue("Mg");
                    break;
                case 10:
                    celda.setCellValue("Fe");
                    break;
                case 11:
                    celda.setCellValue("Mn");
                    break;
                case 12:
                    celda.setCellValue("Cu");
                    break;
                case 13:
                    celda.setCellValue("Zn");
                    break;                    
            }
        }
                
        fila = h.createRow(35);
        for(int i=0; i<14; i++){
            celda = fila.createCell(i);
            if(i==0 || i==1){
                celda.setCellStyle(cs14);
            }
            else if(i==2){
                celda.setCellStyle(cs15);
            }
            else if(i==3){
                celda.setCellStyle(cs16);
            }
            else if(i==5){
                celda.setCellStyle(cs17);
                celda.setCellValue("(µS/cm)");
                // (µS/cm)
            }
            else if(i==6){
                celda.setCellStyle(cs17);
                celda.setCellValue("----------------------------------------- (mg/L) ----------------------------------------");
                h.addMergedRegion(new CellRangeAddress(35, 35, 6, 13));
                //----------------------------------------- (mg/L) ----------------------------------------
                //Combinar y centrar celdas hasta la 13
            }
            else{
                celda.setCellStyle(cs17);
            }           
        }
    }
}

