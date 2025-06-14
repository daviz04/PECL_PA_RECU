
package Clases;
//Listas
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//Barreras
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
//Ejecutor para los hilos zombi
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Gestor {
    private final CyclicBarrier barrera1, barrera2, barrera3, barrera4;
    private final AreaRecursos granja, bosque, mina;
    private List<Aldeanos> listaPuerta1 = Collections.synchronizedList(new ArrayList<>());
    private List<Aldeanos> listaPuerta2 = Collections.synchronizedList(new ArrayList<>());
    private List<Aldeanos> listaPuerta3 = Collections.synchronizedList(new ArrayList<>());
    private final Registro log;
    private final CentroUrbano Ref;
    private Almacenes granero, aserradero, tesoreria;
    ExecutorService ejecutor = Executors.newCachedThreadPool();
    
    private Controlador control;
    
    public Gestor(Registro log, ExecutorService ejecutor, 
            Controlador controlTuneles, Controlador controlRefugio, Controlador controlExterior){
        this.log = log;
        this.ejecutor = ejecutor;
        this.control = controlTuneles;
        this.granero = new Almacenes(1, log, control, "granero");
        //Inicialización del refugio
        this.Ref = new CentroUrbano(log, control, granero, aserradero, tesoreria);
        //las zonas exteriores
        this.granja = new AreaRecursos(log, ejecutor, 1, controlExterior);
        this.bosque = new AreaRecursos(log, ejecutor, 2, controlExterior);
        this.mina = new AreaRecursos(log, ejecutor, 3, controlExterior);
        //y las barreras
        barrera1 = new CyclicBarrier(3);
        barrera2 = new CyclicBarrier(3);
        barrera3 = new CyclicBarrier(3);
        barrera4 = new CyclicBarrier(3);
    }
    
    //Método para crear los humanos
    public void generadorHumanos(){
        for(int i = 1; i < 10000; i++){
            Aldeanos humano = new Aldeanos(i, this, Ref, granero, aserradero, tesoreria);
            ejecutor.execute(humano);
            //Espera un tiempo aleatorio antes de generar el siguiente humano
            int num = (int)(Math.random() * (2000 - 500 + 1)) + 500;
            try {
                //Congelar el hilo principal
                Thread.sleep(num);
            } catch (InterruptedException e) {
                System.out.println("Espera al siguiente humano interrumpida");
            }
        }
    }
    
    //Metodo que introduce un aldeano en uno de los tuneles aleatoriamente y le saca al exterior
    public void accederAreaRecursos(Aldeanos aldeano){
        try {
            //Se escoge un tunel aleatorio y entra por la puerta que corresponde al tunel que corresponde
            int tunel = (int)(Math.random() * 3) + 1;           
            switch(tunel){
                case 1 -> procesoAreaRecursos(aldeano, barrera1, "Puerta1", listaPuerta1, granja);
                case 2 -> procesoAreaRecursos(aldeano, barrera2, "Puerta2", listaPuerta2, bosque);
                case 3 -> procesoAreaRecursos(aldeano, barrera3, "Puerta3", listaPuerta3, mina);
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("Ha saltado una interrupcion en la zona de puertas");}
    }
    
    //Metodo que cumple las funciones para acceder y salir del tunel
    private void procesoAreaRecursos(Aldeanos aldeano, CyclicBarrier B, String puertaID, 
            List<Aldeanos> L, AreaRecursos Z) throws InterruptedException, BrokenBarrierException {
        //Regeneramos las barreras si es necesario
        if(B.isBroken()) {B.reset();}
        //Espera en la barrera a formar grupo
        log.evento((aldeano.getID() + " espera en la puerta "));
        esperaPuerta(aldeano, L, puertaID);
        B.await();
        salirPuerta(aldeano, L, puertaID);
        //Atraviesa el tunel una vez se han reunido 3 personas
        log.evento((aldeano.getID() + " pasa en grupo por la puerta "));
        //Sale al exterior
        Z.entrarZonaRecursos(aldeano);
    }
    
    
    //Metodo que mueve el zombi a una zona aleatoria
    public void Deambular(Barbaros zombi){
        //Sacamos al zombi de la zona en la que esta (salvo que no tenga zona por sel paciente 0)
        if (zombi.getActual() != null) zombi.getActual().removeZombi(zombi);
        //Selecciona una zona aleatoria
        int nZona = (int)(Math.random() * 4) + 1;
        log.evento(zombi.getID() + " se mueve a la zona: " + nZona);
        //Asignamos el zombi a una zona y lo metemos en la interfaz y lista
        switch(nZona){
            case 1 -> {zombi.setActual(granja); zombi.getActual().addZombi(zombi);}
            case 2 -> {zombi.setActual(bosque); zombi.getActual().addZombi(zombi);}
            case 3 -> {zombi.setActual(mina); zombi.getActual().addZombi(zombi);}
        }
    }
    
    //Metodos de las listas de las barreras/puertas y su paso a interfaz
    private void esperaPuerta(Aldeanos humano, List<Aldeanos> L, String puertaID){
        L.add(humano);
        control.add(puertaID, humano.getID());
    }
    private void salirPuerta(Aldeanos humano, List<Aldeanos> L, String puertaID){
        L.remove(humano);
        control.remove(puertaID, humano.getID());
    }
    
    public CentroUrbano getRefugio(){
        return Ref;
    }
    public AreaRecursos getZonaExterior1(){
        return granja;
    }
    public AreaRecursos getZonaExterior2(){
        return bosque;
    }
    public AreaRecursos getZonaExterior3(){
        return mina;
    }
}