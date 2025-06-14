
package Clases;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Aldeanos extends Thread {
    private int nid; //numero del id
    private String id;
    private int recurso = 0;
    private boolean herido;
    private boolean muerto = false;
    private boolean granero = false;
    private boolean aserradero = false;
    private boolean tesoreria = false;

    private Almacenes A1, A2, A3;
    private Gestor gestor;
    private CentroUrbano centroUrbano;
    private AreaRecursos actual;
    
    public Aldeanos(int id, Gestor tuneles, CentroUrbano R, Almacenes A1, Almacenes A2, Almacenes A3){
        herido = false;
        this.nid = id;
        this.id = String.format("A%04d", id);
        this.gestor = tuneles;
        this.centroUrbano = R;
        this.A1 = A1;
        this.A2 = A2;
        this.A3 = A3;
    }
    
    //Metodo que se activa cuando el humano muere por ataque de zombi
    public void morir() {
        muerto = true;
    }
    
    //Gets y sets
    public String getID(){
        return id;
    }
    public int getNID(){
        return nid;
    }
    public boolean getHerido(){
        return herido;
    }
    public void setHerido(boolean herido){
        this.herido = herido;
    }
    public void setActual(AreaRecursos zona){
        this.actual = zona;
    }
    public void setMuerto(){
        this.muerto = true;
    }
    public int getRecurso(){
        return recurso;
    }
    public void agregarRecurso(int num){
        this.recurso += num;
    }
    public void quitarRecursos(){
        this.recurso = 0;
    }
    
    public void setGranero(boolean bool){
        this.granero = bool;
    }
    public void setAserradero(boolean bool){
        this.aserradero = bool;
    }
    public void setTesoreria(boolean bool){
        this.tesoreria = bool;
    }
    
    public void limpiarAlmacen(){
        granero = false;
        aserradero = false;
        tesoreria = false;
    }
        
    //Ejecucion del hilo
    public void run(){
        while(!muerto){
            //Accede a la zona comun
            centroUrbano.esperaComun(this);
            centroUrbano.planificarTrabajo(this);
            //Selecciona un tunel y lo atraviesa en grupo de 3
            gestor.accederAreaRecursos(this);
            //Explora el exterior
            actual.recolectarRecurso(this);
            if (!muerto){ //Comprueba si regresa vivo
                //Si ha sobrevivido regresa por su tunel
                setActual(null); //Deja de estar en el exterior
                //Si el hilo vuelve herido no habra conseguido comida
                if(!this.herido){
                    if(granero){
                        try {
                            A1.almacenar(this, "almacena grano");
                            limpiarAlmacen();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Aldeanos.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else if(aserradero){
                        try {
                            A2.almacenar(this, "almacena madera");
                            limpiarAlmacen();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Aldeanos.class.getName()).log(Level.SEVERE, null, ex);
                        }          
                    }else if(tesoreria){
                         try {
                            A3.almacenar(this, "almacena oro");
                            limpiarAlmacen();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Aldeanos.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }  
                }
                //Va a descansar
                centroUrbano.planificarTrabajo(this);
                //Despues va a comer
                centroUrbano.comerComedor(this);
                //Si esta herido se cura
                if(this.herido){
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
}
