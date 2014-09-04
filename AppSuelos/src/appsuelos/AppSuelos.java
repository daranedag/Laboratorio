/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package appsuelos;

import java.io.*;
import java.util.*;

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
        leerArchivo("D:\\Dropbox\\readme.txt");
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
    
    public static void generarInforme(int tipo){
        
    }
    
    public static void consultar(String query){
        
    }
    
    public static boolean validarUsuario(String rut, String password){
        return true;
    }
}
