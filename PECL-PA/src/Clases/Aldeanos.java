
package Clases;

public class Aldeanos extends Thread {
    private int nid; //numero del id
    private String id;
    private boolean herido;
    private boolean muerto = false;
    
    private Gestor tuneles;
    private CentroUrbano centroUrbano;
    private AreaRecursos actual;
    
    public Aldeanos(int id, Gestor tuneles, CentroUrbano R){
        herido = false;
        this.nid = id;
        this.id = String.format("A%04d", id);
        this.tuneles = tuneles;
        this.centroUrbano = R;
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
        
    //Ejecucion del hilo
    public void run(){
        while(!muerto){
            //Accede a la zona comun
            centroUrbano.esperaComun(this);
            //Selecciona un tunel y lo atraviesa en grupo de 3
            tuneles.accederAreaRecursos(this);
            //Explora el exterior
            actual.explorar(this);
            if (!muerto){ //Comprueba si regresa vivo
                //Si ha sobrevivido regresa por su tunel
                tuneles.regresarTunel(this, actual);
                setActual(null); //Deja de estar en el exterior
                //Si el hilo vuelve herido no habra conseguido comida
                if(!this.herido){
                    //Dejara la comida
                    centroUrbano.dejarComida(this);
                }
                //Va a descansar
                centroUrbano.planificarTrabajo(this);
                //Despues va a comer
                centroUrbano.comerComedor(this);
                //Si esta herido se cura
                if(this.herido){
                    centroUrbano.curarseMedico(this);
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
