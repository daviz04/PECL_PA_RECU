
package Clases;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Aldeanos extends Thread {
    private int nid; //numero del id
    private String id;
    private int recursoCont = 0;
    private boolean bajoAtaque;

    private int eleccionRecurso;
    private Almacenes A1, A2, A3;
    private Gestor gestor;
    private CentroUrbano centroUrbano;
    private AreaRecursos actual;
    
    public Aldeanos(int id, Gestor tuneles, CentroUrbano R, Almacenes A1, Almacenes A2, Almacenes A3){
        bajoAtaque = false;
        this.nid = id;
        this.id = String.format("A%04d", id);
        this.gestor = tuneles;
        this.centroUrbano = R;
        this.A1 = A1;
        this.A2 = A2;
        this.A3 = A3;
    }
   
    
    //Gets y sets
    public String getID(){
        return id;
    }
    public int getNID(){
        return nid;
    }
    public boolean getHerido(){
        return bajoAtaque;
    }
    public void setBajoAtaque(boolean bajoAtaque){
        this.bajoAtaque = bajoAtaque;
    }
    public void setActual(AreaRecursos zona){
        this.actual = zona;
    }
    public int getEleccionRecurso(){
        return eleccionRecurso;
    }
    public void setEleccionRecurso(int eleccionRecurso){
        this.eleccionRecurso = eleccionRecurso;
    }
    public int getRecursoCont(){
        return recursoCont;
    }
    public void agregarRecursos(int num){
        this.recursoCont += num;
    }
    public void quitarRecursos(){
        this.recursoCont = 0;
    }
    //Ejecucion del hilo
    public void run(){
        centroUrbano.esperaComun(this);
        while(true){
            centroUrbano.planificarTrabajo(this);

            gestor.accederAreaRecursos(this);
            //Explora el exterior
            actual.recolectarRecurso(this);
            setActual(null); //Deja de estar en el exterior
            //Si el hilo vuelve bajoAtaque no habra conseguido comida
            if(!this.bajoAtaque){
               switch(eleccionRecurso){
                   case 1:
                    try {
                        A1.almacenar(this, "almacena grano");   
                    } catch (InterruptedException ex) {
                            Logger.getLogger(Aldeanos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   case 2:
                    try {
                        A2.almacenar(this, "almacena madera");             
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Aldeanos.class.getName()).log(Level.SEVERE, null, ex);
                    }          
                   case 3:
                    try {
                        A3.almacenar(this, "almacena oro");
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Aldeanos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }  
            }       
                if(this.bajoAtaque){
                    centroUrbano.areaRecuperacion(this);
                }
                //Repite el ciclo despues de 1,5-2s   
                int t =(int)(Math.random() * (1501)) + 500;
                try {sleep(t);} catch (InterruptedException e) {
                    System.out.println("Error del humano antes de repetir su ciclo");
            }
        }
    }   
}
