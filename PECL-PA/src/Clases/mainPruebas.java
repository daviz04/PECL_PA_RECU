
package Clases;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JTextArea;

public class mainPruebas {
    public static void main(String[] args) {
        //Iniciamos el registro/historial de texto y lo vaciamos
        Registro log = new Registro();
        log.reiniciarRegistro();
        //Controlador imaginario para testeo sin interfaz
        Map<String, JTextArea> mapa = Map.of();
        Controlador falso = new Controlador(mapa);
        //Creamos el executor para el control de hilos
        ExecutorService ejecutor = Executors.newCachedThreadPool();
        Gestor gestor = new Gestor(log, ejecutor, falso, falso, falso);
        //Creamos el primer Zombi y lo iniciamos
        Barbaros paciente_0 = new Barbaros(gestor);
        ejecutor.execute(paciente_0);
        gestor.generadorHumanos();
    }
}
