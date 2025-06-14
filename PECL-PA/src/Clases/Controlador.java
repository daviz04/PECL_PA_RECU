
package Clases;

import javax.swing.JTextArea;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//Conecta las cloases con la interfaz
public class Controlador {
    
    private ExecutorService actualizador = Executors.newCachedThreadPool();
    private Map<String, JTextArea> areas;
    
    //Se asigna un controlador a cada elemento de la interfaz
    public Controlador(Map<String, JTextArea> areas){
        this.areas = areas;
    }
    
    //Metodo interno para ejecucion y control de hilos
    private void actualizarInterfaz(String areaID, String accion, String elemento){
        JTextArea area = areas.get(areaID);
        TareaImprimir tarea = new TareaImprimir(area, elemento, accion);
        actualizador.execute(tarea);
    }
    
    //Recibe el nombre del JTextArea y el ID que deben actualizar en la interfaz
    public void add(String areaID, String elemento){
        actualizarInterfaz(areaID, "add", elemento);
    }
    public void remove(String areaID, String elemento){
        actualizarInterfaz(areaID, "remove", elemento);
    }
    public void actualizaDatos(String areaID, String elemento){
        actualizarInterfaz(areaID, "actualizar", elemento);
    }
}