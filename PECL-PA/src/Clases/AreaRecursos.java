
package Clases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class AreaRecursos {
    
    private final int zona; //Numero asignado a cada zona
    private final Tuneles tunel; //Tunel conectado a la zona
    
    private List<Aldeanos> listaAldeanos = Collections.synchronizedList(new ArrayList<>());
    private List<Barbaros> listaBarbaros = Collections.synchronizedList(new ArrayList<>());
    
    private Lock bajoAtaque = new ReentrantLock();
    private Condition finAtaque = bajoAtaque.newCondition();
    
    private Registro log;
    ExecutorService ejecutor = Executors.newCachedThreadPool();
    private Controlador controlExterior;
    
    AreaRecursos(Registro log, ExecutorService ej, int zona, Tuneles tunel, Controlador controlador){
        this.log = log;
        this.ejecutor = ej;
        this.zona = zona;
        this.tunel = tunel;
        this.controlExterior = controlador;
    }
    
    //Método para salir del tunel y explorar el exterior, para al final volver
    public void explorar(Aldeanos humano){
        listaAldeanos.add(humano);
        //Registramos el evento de exploracion
        log.evento((humano.getID() + " explora la zona exterior: " + zona));
        int t = (int)(Math.random() * (2001)) + 3000; //Calculamos el tiempo que dura
        try {
            //Durante este tiempo el humano busca comida pero es susceptible a ataques zombi
            Thread.sleep(t);
            //Si termina la busqueda encuentra comida
        } catch (InterruptedException e) {
            //Si es interrumpido es por un ataque zombi
            bajoAtaque.lock();
            try { 
                finAtaque.await();
                //Tiempo que tardara el ataque
                int t2 = (int)(Math.random() * (1500 - 500 + 1)) + 500;
                Thread.sleep(t2);
            } catch(InterruptedException e2){
                System.out.println("Interrupcion en la espera del humano tras el ataque");
            } finally {
                bajoAtaque.unlock();
            }
        }
    }
    
    //Los ataques deben tener en cuenta la variable compartida lista de humanos
    public void ataqueZombi(Barbaros zombi) {
        //Bloqueamos por el ataque
        bajoAtaque.lock();
        //Variable para determinar a que humano atacara el zombi
        Aldeanos humanoAtacado = getHumanoAleatorio();
        log.evento(zombi.getID() + " va a atacar a: " + humanoAtacado.getID());
        try {
            //Se interrumpe primero para que el humano deje de explorar
            humanoAtacado.interrupt();
            //Determina el resultado del ataque
            int prob = (int)(Math.random() * 3) + 1; //Variable aleatoria para determinar si mata o hiere
            if(prob == 3){ 
                //Mata al humano
                humanoAtacado.setMuerto();
                quitarHumanoLista(humanoAtacado); //Eliminacion de la lista de humanos
                
                //Registramos la muerte del humano por el ataque zombi
                log.evento((zombi.getID() + " ha matado a: " + humanoAtacado.getID()));
                
                zombi.aumentarMuertes(); //Suma uno al numero de muertes del zombi                               
                Barbaros zombieConvertido = zombi.conversionZombie(humanoAtacado); //Zombie del humano matado
                //Añadimos el zombi a la lista de la zona
                addZombi(zombieConvertido);
                //Informamos del nuevo zombi
                log.evento((zombieConvertido.getID() + " a aparecido en la zona " + getZona()));
                //Ejecutamos el nuevo zombi en el pool
                ejecutor.execute(zombieConvertido);                      
            } else { //Hiere al humano
                humanoAtacado.setHerido(true);
                //Registramos la herida del humano por el zombi
                log.evento((humanoAtacado.getID() + " ha sido herido por: " + zombi.getID()));
            }
            finAtaque.signalAll();
        } finally { 
            //Termina el ataque
            bajoAtaque.unlock();
            try { //El zombi espera el tiempo del ataque
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
        controlExterior.add("AldeanosA" + getZona(), aldeano.getID());
        log.evento((aldeano.getID() + " ha entrado a la zona de recursos: " + zona));
    }
    
    public synchronized void saleZonaRecursos(Aldeanos aldeano){
        listaAldeanos.remove(aldeano);
        controlExterior.remove("AldeanoA" + getZona(), aldeano.getID());
        log.evento((aldeano.getID() + " regresa de la zona de recursos: " + zona));
    }
    
    //Añadir y quitar zombi de la lista
    public synchronized void addZombi(Barbaros zombie){
        listaBarbaros.add(zombie);
        controlExterior.add("ZombisE" + getZona(), zombie.getID());
    }
    public synchronized void removeZombi(Barbaros zombie){
        listaBarbaros.remove(zombie);
        controlExterior.remove("ZombisE" + getZona(), zombie.getID());
    }
    
    //Quita el humano que muere
    public synchronized void quitarHumanoLista(Aldeanos humano){ //Eliminacion de humanos de la lista
        listaAldeanos.remove(humano);
        controlExterior.remove("HumanosE" + getZona(), humano.getID());
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
    
    public List<Barbaros> obtenerTop3Zombis(List<Barbaros> lista) {
        return lista.stream().sorted((z1, z2) -> Integer.compare(z2.getMuertes(), z1.getMuertes())).limit(3).collect(Collectors.toList());
    }
}