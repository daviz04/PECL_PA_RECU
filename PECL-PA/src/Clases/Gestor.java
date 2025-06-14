
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
    private final Tuneles T1, T2, T3, T4;
    private final CyclicBarrier barrera1, barrera2, barrera3, barrera4;
    private final AreaRecursos granja, bosque, mina;
    private List<Aldeanos> listaPuerta1 = Collections.synchronizedList(new ArrayList<>());
    private List<Aldeanos> listaPuerta2 = Collections.synchronizedList(new ArrayList<>());
    private List<Aldeanos> listaPuerta3 = Collections.synchronizedList(new ArrayList<>());
    private final Registro log;
    private final CentroUrbano Ref;
    ExecutorService ejecutor = Executors.newCachedThreadPool();
    
    private Controlador controlTuneles;
    
    public Gestor(Registro log, ExecutorService ejecutor, 
            Controlador controlTuneles, Controlador controlRefugio, Controlador controlExterior){
        this.log = log;
        this.ejecutor = ejecutor;
        this.controlTuneles = controlTuneles;
        //Inicialización del refugio
        this.Ref = new CentroUrbano(log, controlRefugio);
        //los tuneles
        this.T1 = new Tuneles(1, log, controlTuneles);
        this.T2 = new Tuneles(2, log, controlTuneles);
        this.T3 = new Tuneles(3, log, controlTuneles);
        this.T4 = new Tuneles(4, log, controlTuneles);
        //las zonas exteriores
        this.granja = new AreaRecursos(log, ejecutor, 1, T1, controlExterior);
        this.bosque = new AreaRecursos(log, ejecutor, 2, T2, controlExterior);
        this.mina = new AreaRecursos(log, ejecutor, 3, T3, controlExterior);
        //y las barreras
        barrera1 = new CyclicBarrier(3);
        barrera2 = new CyclicBarrier(3);
        barrera3 = new CyclicBarrier(3);
        barrera4 = new CyclicBarrier(3);
    }
    
    //Método para crear los humanos
    public void generadorHumanos(){
        for(int i = 1; i < 10000; i++){
            Aldeanos humano = new Aldeanos(i, this, Ref);
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
                case 1 -> procesoAreaRecursos(aldeano, barrera1, "Puerta1", listaPuerta1, T1, granja);
                case 2 -> procesoAreaRecursos(aldeano, barrera2, "Puerta2", listaPuerta2, T2, bosque);
                case 3 -> procesoAreaRecursos(aldeano, barrera3, "Puerta3", listaPuerta3, T3, mina);
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            System.out.println("Ha saltado una interrupcion en la zona de puertas");}
    }
    
    //Metodo que cumple las funciones para acceder y salir del tunel
    private void procesoAreaRecursos(Aldeanos aldeano, CyclicBarrier B, String puertaID, 
            List<Aldeanos> L, Tuneles T, AreaRecursos Z) throws InterruptedException, BrokenBarrierException {
        //Regeneramos las barreras si es necesario
        if(B.isBroken()) {B.reset();}
        //Espera en la barrera a formar grupo
        log.evento((aldeano.getID() + " espera en la puerta " + T.getID()));
        esperaPuerta(aldeano, L, puertaID);
        B.await();
        salirPuerta(aldeano, L, puertaID);
        //Atraviesa el tunel una vez se han reunido 3 personas
        log.evento((aldeano.getID() + " pasa en grupo por la puerta " + T.getID()));
        T.entrar(aldeano);
        //Sale al exterior
        Z.entrarZonaRecursos(aldeano);
    }
    
    //Metodo llamado para volver desde el exterior al tunel
    public void regresarTunel(Aldeanos humano, AreaRecursos zona){
        zona.saleZonaRecursos(humano);
        switch(zona.getZona()){
            case 1 -> T1.salir(humano);
            case 2 -> T2.salir(humano);
            case 3 -> T3.salir(humano);
            case 4 -> T4.salir(humano);
        }
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
        controlTuneles.add(puertaID, humano.getID());
    }
    private void salirPuerta(Aldeanos humano, List<Aldeanos> L, String puertaID){
        L.remove(humano);
        controlTuneles.remove(puertaID, humano.getID());
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
    public Tuneles getTunel1(){
        return T1;
    }
    public Tuneles getTunel2(){
        return T2;
    }
    public Tuneles getTunel3(){
        return T3;
    }
    public Tuneles getTunel4(){
        return T4;
    }
}