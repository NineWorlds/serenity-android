package us.nineworlds.serenity.core.subtitles;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * 
 * @author J. David
 * 
 * Clase que se encarga de la lectura y escritura de ficheros o lectura desde teclado
 *
 */
public class IOClass {
	
	/**
	 * Metodo que recibe el nombre del fichero (o la ruta relativa al directorio de usuario)
	 * y el fichero a escribir en forma de un array de Strings donde cada String representa una linea
	 * 
	 * @param nombreFichero nombre del fichero (o la ruta relativa al directorio de usuario)
	 * @param ficheroTotal array de Strings donde cada String representa una linea del fichero
	 */
	public static void escribirFicheroTxt(String nombreFichero, String[] ficheroTotal){
		FileWriter fichero = null;
	    PrintWriter pw = null;
	    try
	    {
	        fichero = new FileWriter(System.getProperty("user.dir")+"\\"+nombreFichero);
	        pw = new PrintWriter(fichero);
	
	        for (int i = 0; i < ficheroTotal.length; i++)
	            pw.println(ficheroTotal[i]);
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	       try {
	       // Aprovechamos el finally para 
	       // asegurarnos que se cierra el fichero.
	       if (null != fichero)
	          fichero.close();
	       } catch (Exception e2) {
	          e2.printStackTrace();
	       }
	    }
	}
	
	/**
	 * Metodo que recibe el nombre del fichero (o la ruta relativa al directorio de usuario)
	 * y el fichero a escribir en forma de un array de Strings donde cada String representa una linea
	 * 
	 * @param nombreFichero nombre del fichero (o la ruta relativa al directorio de usuario)
	 * @return array de Strings donde cada String representa una linea del fichero leido
	 */
	public static String[] leerFicheroTxt(String nombreFichero){
		
		String [] s = new String [0];
		String direccion = System.getProperty("user.dir")+"\\"+ nombreFichero;
		
		//Intenta cargar el archivo
		File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
		try {
	        // Apertura del fichero y creacion de BufferedReader para poder
	        // hacer una lectura comoda (disponer del metodo readLine()).
	        archivo = new File (direccion);
	        fr = new FileReader (archivo);
	        br = new BufferedReader(fr);

	        // Lectura del fichero
	        String linea = null;
	        while((linea=br.readLine())!=null){
	        	int n=0;
	        	String [] s2 = new String[s.length+1];
	        	for(n=0;n<s.length;n++)s2[n] = s[n];
	        	s2[n]=linea.trim();
	        	s=s2;
	        }
	     }catch(Exception e){
	        System.err.println("No se ha encontrado el archivo");
	        System.exit(-1);
	     }finally{
	        // En el finally cerramos el fichero, para asegurarnos
	        // que se cierra tanto si todo va bien como si salta 
	        // una excepcion.
	        try{                    
	           if( null != fr ){   
	              fr.close();     
	           }                  
	        }catch (Exception e2){ 
	           e2.printStackTrace();
	        }
	     }
		return s;
	}
	
	/**
	 * Metodo que lee una linea desde el teclado
	 * 
	 * @return Devuelve el String leido
	 */
	public static String leerTeclado(){
		String respuesta = null;
		try{
			InputStreamReader isr = new InputStreamReader (System.in);
			BufferedReader br = new BufferedReader (isr);
			respuesta = br.readLine();
		} catch (IOException e){
			System.err.println("excepcion al leer el teclado, programa finalizado");
			System.exit(-1);
		}
		return respuesta;
	}
}