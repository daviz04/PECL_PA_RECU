
package Clases;

public class Barbaros extends Thread{
    private int nid; //numero del id, de momento no es necesario pero ahí lo mantenemos
    private String id;
    private int muertes = 0;
    private AreaRecursos actual;
    private Gestor gestor;
    
    //Contructor para el Zombie 0
    public Barbaros(Gestor g){
        this.nid = 0;
        this.id = "Z0000";
        this.gestor = g;
    }
    
    //Constructor que se usará cuando un humano sea convertido a zombie
    private Barbaros(int id, AreaRecursos actual, Gestor gestor){
        this.nid = id;
        this.id = String.format("Z%04d", id);
        this.actual = actual;
        this.gestor = gestor;
    }
    
    //Conversion de humano a zombie
    public Barbaros conversionZombie(Aldeanos humano) {
        Barbaros convertido = new Barbaros(humano.getNID(), this.actual, this.gestor);
        return convertido;
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
                    actual.ataqueZombi(this);
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
