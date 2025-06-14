
package Clases;
//Listas
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//Locks
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CentroUrbano {
    //Variables
    private int Comida;
    private List<Aldeanos> plazaCentral = Collections.synchronizedList(new ArrayList<>());
    private List<Aldeanos> casaPrincipal = Collections.synchronizedList(new ArrayList<>());
    private List<Aldeanos> areaRecuperacion = Collections.synchronizedList(new ArrayList<>());
    private List<Aldeanos> cuartel = Collections.synchronizedList(new ArrayList<>());
    //Lock
    private Lock despensa = new ReentrantLock();
    private Condition hayComida = despensa.newCondition();
    //Registro
    private Registro log;
    //Interfaz
    private Controlador controlRefugio;

    public CentroUrbano(Registro r, Controlador cR){
        this.log = r;
        this.Comida = 0;
        this.controlRefugio = cR;
    }
    
    //Hacer el cuartel en el futuro
    public void cuartel(){
        
    }
    
    //Acceso al comedor
    public void comerComedor(Aldeanos humano){
        //Accede a la variable compartida para comer tarda entre 3 a 5s
        despensa.lock();
        entrar(plazaCentral, humano, "ZonaComedero");
        //Registramos el acceso
        log.evento(humano.getID() + " ha entrado en el comedor.");
        try {
            //imprimir();
            //Espera a que haya comida y luego come
            while(Comida == 0){
                log.evento((humano.getID() + " espera para comer."));
                hayComida.await();
            }
            int t = (int)(Math.random() * (5000 - 3000 + 1)) + 3000;
            //Registramos que va a comer
            log.evento((humano.getID() + " empieza a comer"));
            Thread.sleep(t);
            Comida--;
            //Registramos que ha comido
            log.evento((humano.getID() + " ha terminado de comer"));
        } catch (InterruptedException e) {
            System.out.println("Interrupcion durante la comida o espera para comer");
        }
        finally {
            salir(plazaCentral, humano, "ZonaComedero");
            log.evento(humano.getID() + " ha salido del comedor.");
            despensa.unlock();
        }
    }
    public void dejarComida(Aldeanos humano){
        despensa.lock();
        entrar(plazaCentral, humano, "ZonaComedero");
        log.evento(humano.getID() + " ha entrado en el comedor.");
        try {
            //Accede a la variable compartida para dejar comida
            Comida += 2;
            //Registramos el deposito de comida
            log.evento((humano.getID() + " ha dejado comida en la despensa, ahora hay " + getComida()));
            hayComida.signalAll();
        } finally {
            salir(plazaCentral, humano, "ZonaComedero");
            log.evento(humano.getID() + " ha salido del comedor.");
            despensa.unlock();
        }
    }
    
    //Acciones de espera en el refugio
    public void esperaComun(Aldeanos aldeano){
        entrar(casaPrincipal, aldeano, "CasaPrincipal");
        log.evento(aldeano.getID() + " ha entrado en la casa principal.");
        //Espera en la zona comun entre 1 y 2s (2-1=1)
        tiempoEspera(aldeano, "esperar en la casa principal", 2000, 4000);
        salir(casaPrincipal, aldeano, "Casa Principal");
        log.evento(aldeano.getID() + " ha salido de la Casa Principal.");
    }
    public void planificarTrabajo(Aldeanos aldeano){
        entrar(areaRecuperacion, aldeano, "PlazaCentral");
        log.evento(aldeano.getID() + " ha entrado en la Plaza Central.");
        //Espera en la zona de descanso entre 2 y 4s (4-2=2)
        tiempoEspera(aldeano, "planificar", 1000, 2001);
        salir(areaRecuperacion, aldeano, "PlazaCentral");
        int recurso = (int) (Math.random()* 3) + 1;
        //Hacer que elija la zona de recursos a la que irá
        log.evento(aldeano.getID() + " ha salido de la Plaza Central.");
    }
    public void curarseMedico(Aldeanos humano){
        entrar(areaRecuperacion, humano, "ZonaDescanso");
        log.evento(humano.getID() + " ha entrado en la zona de descanso.");
        //Descansa y pone el bit herida a 0 despues de entre 3 y 5s (5-3=2)
        tiempoEspera(humano, "curarse", 3000, 2001);
        humano.setHerido(false); //Se cura
        salir(areaRecuperacion, humano, "ZonaDescanso");
        log.evento(humano.getID() + " ha salido de la zona de descanso.");
    }
    
    //Metodo en comun a las actividades de espera, toma 4 valores:
    //El hilo, la accion, el tiempo base de la accion y cuanto tiempo puede variar/ampliarse
    private void tiempoEspera(Aldeanos humano, String accion, int tiempoBase, int tiempoVariacion){
        try {
            //imprimir(); -->Interfaz
            //Calculo del tiempo de espera
            int tiempo = (int)(Math.random() * tiempoVariacion) + tiempoBase;
            log.evento(humano.getID() + " empieza a " + accion);
            //Pausa el hilo
            Thread.sleep(tiempo);
            log.evento(humano.getID() + " ha terminado de " + accion);
        } catch (InterruptedException e) {
            System.out.println("Interrupcion en la espera de " + humano.getID() + " en el refugio.");
        } 
    }
    
    //Añadir y quitar de listas (entrada y salida de las zonas)
    private void entrar(List<Aldeanos> L, Aldeanos humano, String zona){
        L.add(humano);
        controlRefugio.add(zona, humano.getID());
    }
    private void salir(List<Aldeanos> L, Aldeanos humano, String zona){
        L.remove(humano);
        controlRefugio.remove(zona, humano.getID());
    }
    
    //Gets y sets
    public synchronized int getComida(){
        return Comida;
    }
    public List<Aldeanos> getPlazaCentral() {
        return plazaCentral;
    }
    public List<Aldeanos> getCasaPrincipal() {
        return casaPrincipal;
    }
    public List<Aldeanos> getAreaRecuperacion() {
        return areaRecuperacion;
    }
    public int humanosTotales(){
        int numHumanos = plazaCentral.size() + casaPrincipal.size() + areaRecuperacion.size();
        return numHumanos;
    }
}