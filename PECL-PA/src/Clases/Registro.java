
package Clases;

import java.io.IOException;
//Escribir el .txt
import java.io.FileWriter;
import java.io.PrintWriter;
//Formato de mensaje
import java.text.SimpleDateFormat;
import java.util.Date;

public class Registro {
    
    private static final String rutaArchivo = "src/Log/apocalipsis.txt";
    
    //Limpia (o crea) el archivo de texto
    public void reiniciarRegistro() {
        try (FileWriter fileWriter = new FileWriter(rutaArchivo);
                PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("----- HISTORIAL APOCALIPTICO -----");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Acceso a escribir los eventos sincronizado
    public synchronized void evento(String message) {
        try (FileWriter fileWriter = new FileWriter(rutaArchivo, true); //El true evita que se borre lo anterior cada vez que se llama
                PrintWriter printWriter = new PrintWriter(fileWriter)) {
            //Añade al formato la fecha y hora hasta segundos
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            //Escribe el mensaje en el archivo
            printWriter.println(timeStamp + " -> " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
