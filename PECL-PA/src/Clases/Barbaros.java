
package Clases;

public class Barbaros extends Thread{
    private int nid; //numero del id, de momento no es necesario pero ahí lo mantenemos
    private String id;
    private int muertes = 0;
    private AreaRecursos actual;
    private Gestor gestor;
   
    //Constructor que se usará cuando un humano sea convertido a zombie
    public Barbaros(int id, Gestor gestor){
        this.nid = id;
        this.id = String.format("B%04d", id);
        this.gestor = gestor;
    }
    

    //Gets y sets
    public String getID(){
        return id;
    }
    public int getMuertes(){
        return muertes;
    }
    public void setActual(AreaRecursos zona){
        this.actual = zona;
    }
    public AreaRecursos getActual(){
        return this.actual;
    }
    
    //Aumenta uno con cada humano matado
    public void aumentarMuertes(){
        this.muertes++;
    }
    
    //Ejecucion
    @Override
    public void run(){
        while(true){
            //Se mueve a una zonas aleatoria
            gestor.Deambular(this);
            //Comprueba si hay humano en la zona y si encuentra, selecciona uno y lo ataca
            try {
                if(!actual.estaVacio()){ //Comprueba si hay personas en la zona 
                    actual.ataqueBarbaro(this);
                } else { //Si la zona esta vacia solo espera
                    int t = (int)(Math.random() * 1001) + 2000;
                    sleep(t);
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupcion en el ciclo del zombi");
            }     
        }
    }
}
