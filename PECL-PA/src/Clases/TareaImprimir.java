
package Clases;

import javax.swing.JTextArea;

public class TareaImprimir extends Thread {
    
    private JTextArea areaTexto;
    private String elemento;
    private String accion;
    
    public TareaImprimir(JTextArea aT, String elemento, String accion){
        this.areaTexto = aT;
        this.elemento = elemento;
        this.accion = accion;
    }
    
    //Metodos para añadir o quitar lineas (Ids)
    private void add(){
        //Sincronizamos el acceso al JTextArea para que no pueda ser editado por varios hilos a la vez
        synchronized (areaTexto) {
            areaTexto.append(elemento.toString() + "\n");
        }
    }
    private void remove(){
        synchronized (areaTexto) {
            //Separamos el contenido por lineas de texto
            String[] lineas = areaTexto.getText().split("\n");
            StringBuilder sb = new StringBuilder();
            //Comprobamos las lineas hasta encontrar el elemento a eliminar
            for (String linea : lineas) {
                if (!linea.equals(elemento)) {
                    sb.append(linea).append("\n");
                }
            }
            areaTexto.setText(sb.toString());
        }
    }
    public void actualizarDato(){
        remove();
        add();
    }
    
    public void run(){
        if(accion.equals("add")){add();} 
        if(accion.equals("remove")) remove();
        if(accion.equals("actualizar")) actualizarDato();
    }
    
}
