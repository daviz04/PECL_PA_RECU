
package Clases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class AreaRecursos {
    //Numero asignado a cada zona: 1)Granero 2)Aserradero 3)Tesorería
    private final int zona; 
    private String zonaNombre = "";
    
    //Listas de aldeanos, barbaros y guerreros
    private List<Aldeanos> listaAldeanos = Collections.synchronizedList(new ArrayList<>());
    private List<Barbaros> listaBarbaros = Collections.synchronizedList(new ArrayList<>());
    
    //Semaforos y Locks
    private Semaphore capacidad = new Semaphore(4, true); 
    private Lock bajoAtaque = new ReentrantLock();
    private Condition finAtaque = bajoAtaque.newCondition();
    private boolean entrada;
    
    //Interfaz grafica
    private Registro log;
    ExecutorService ejecutor = Executors.newCachedThreadPool();
    private Controlador controlAreaRecursos;
    
    AreaRecursos(Registro log, ExecutorService ej, int zona, Controlador controlador){
        this.log = log;
        this.ejecutor = ej;
        this.zona = zona;
        this.controlAreaRecursos = controlador;
        switch(zona){
            case 1: 
                zonaNombre = "Granero";
            case 2:
                zonaNombre = "Aserradero";
            case 3:
                zonaNombre = "Tesorería";
        }
    }
    
    //Método para salir del tunel y recolectarRecurso el exterior, para al final volver
    public void recolectarRecurso(Aldeanos aldeano){
        listaAldeanos.add(aldeano);
        //Registramos el evento de exploracion
        log.evento((aldeano.getID() + " recolecta recurso de la zona: " + zona));
        int t = (int)(Math.random() * (5001)) + 5000; //Calculamos el tiempo que dura
        try {
            //Durante este tiempo el aldeano busca comida pero es susceptible a ataques zombi
            Thread.sleep(t);
            int recurso = (int)(Math.random() * (11)) + 10;
            aldeano.agregarRecursos(recurso);
            //Si termina la busqueda encuentra comida
        } catch (InterruptedException e) {
            //Si es interrumpido es por un ataque barbaro
            bajoAtaque.lock();
            try { 
                finAtaque.await();
                //Tiempo que tardara el ataque
                int t2 = (int)(Math.random() * (1500 - 500 + 1)) + 500;
                Thread.sleep(t2);
                aldeano.quitarRecursos();
            } catch(InterruptedException e2){
                System.out.println("Interrupcion en la espera del aldeano tras el ataque");
            } finally {
                bajoAtaque.unlock();
            }
        }
    }
    
    //Los ataques deben tener en cuenta la variable compartida lista de humanos
    public void ataqueBarbaro(Barbaros barbaro) {
        //Bloqueamos por el ataque
        bajoAtaque.lock();
        entrada = false;
        //Variable para determinar a que humano atacara el barbaro
        Aldeanos humanoAtacado = getHumanoAleatorio();
        log.evento(barbaro.getID() + " va a atacar a: " + humanoAtacado.getID());
        try {
            //Se interrumpe primero para que el humano deje de recolectarRecurso
            humanoAtacado.interrupt();
            //Determina el resultado del ataque
            int prob = (int)(Math.random() * 3) + 1; //Variable aleatoria para determinar si mata o hiere
            if(prob == 3){ 
                //Mata al humano 
                quitarHumanoLista(humanoAtacado); //Eliminacion de la lista de humanos
                
                //Registramos la muerte del humano por el ataque barbaro
                log.evento((barbaro.getID() + " ha matado a: " + humanoAtacado.getID()));
                
                barbaro.aumentarMuertes(); //Suma uno al numero de muertes del barbaro                               
                           
            } else { //Hiere al humano
                humanoAtacado.setBajoAtaque(true);
                //Registramos la herida del humano por el barbaro
                log.evento((humanoAtacado.getID() + " ha sido herido por: " + barbaro.getID()));
            }
            finAtaque.signalAll();
        } finally { 
            //Termina el ataque
            entrada = true;
            bajoAtaque.unlock();
            try { //El barbaro espera el tiempo del ataque
                int t = (int)(Math.random() * (1500 - 500 + 1)) + 500; 
                Thread.sleep(t);
            } catch (InterruptedException e){
                System.out.println("Error del zombi en la espera del ataque");
            }
        }
    }
    
    //Situa al aldeano en el exterior
    public synchronized void entrarZonaRecursos(Aldeanos aldeano){        
        aldeano.setActual(this);
        listaAldeanos.add(aldeano);
        controlAreaRecursos.add("AldeanosA" + getZona(), aldeano.getID());
        log.evento((aldeano.getID() + " ha entrado a la zona de recursos: " + zonaNombre));
    }
    
    public synchronized void saleZonaRecursos(Aldeanos aldeano){
        listaAldeanos.remove(aldeano);
        controlAreaRecursos.remove("AldeanoA" + getZona(), aldeano.getID());
        log.evento((aldeano.getID() + " regresa de la zona de recursos: " + zonaNombre));
    }
    
    //Añadir y quitar zombi de la lista
    public synchronized void addZombi(Barbaros zombie){
        listaBarbaros.add(zombie);
        controlAreaRecursos.add("ZombisE" + getZona(), zombie.getID());
    }
    public synchronized void removeZombi(Barbaros zombie){
        listaBarbaros.remove(zombie);
        controlAreaRecursos.remove("ZombisE" + getZona(), zombie.getID());
    }
    
    //Quita el humano que muere
    public synchronized void quitarHumanoLista(Aldeanos humano){ //Eliminacion de humanos de la lista
        listaAldeanos.remove(humano);
        controlAreaRecursos.remove("HumanosE" + getZona(), humano.getID());
    }
    
    //Devuelve si hay o no humanos en la zona
    public synchronized boolean estaVacio(){
        return listaAldeanos.isEmpty();
    }
    
    //Metodo para tomar a un humano aleatorio de manera sincronizada
    public synchronized Aldeanos getHumanoAleatorio(){
        int humanoListIndice = (int)(Math.random() * getNumHumanos());
        return listaAldeanos.get(humanoListIndice);
    }
    
    //Método par aver si hay espacio en el area de recursos
    public synchronized boolean getSemaforo(){
        int permisos = capacidad.availablePermits();
        return permisos > 0;
    }
   
    
    //Gets
    public List<Aldeanos> getListaAldeanos() {
        return listaAldeanos;
    }
    public List<Barbaros> getListaBarbaros() {
        return listaBarbaros;
    }
    private synchronized int getNumHumanos(){
        return listaAldeanos.size();
    }
    public synchronized int numZombies(){
        return listaBarbaros.size();
    }
    public int getZona() {
        return zona;
    }
    public String getZonaNombre(){
        return zonaNombre;
    }
    public boolean getEntrada(){
        return entrada;
    }
    
    public List<Barbaros> obtenerTop3Zombis(List<Barbaros> lista) {
        return lista.stream().sorted((z1, z2) -> Integer.compare(z2.getMuertes(), z1.getMuertes())).limit(3).collect(Collectors.toList());
    }
}