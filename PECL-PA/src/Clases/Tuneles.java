
package Clases;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tuneles {
    private final int id;  //Organizacion de tuneles
    private Semaphore capacidad = new Semaphore(1, true); 
    private int cantidadGente = 0;
//El true es para que salgan antes de entrar los que esperan el semaforo porque los que salen acceden antes al semaforo que los que entran
    private boolean ocupado = false;
    private Lock salen = new ReentrantLock();
    private Condition terminaSalir = salen.newCondition();
    private volatile Aldeanos explorador;
    //Es volatil ya que la modificacion esta sincronizada, solo queremos asegurar que su lectura sea del dato más actualizado
    private Registro log;
    private Controlador control;
    
    Tuneles(int id, Registro L, Controlador control){
        this.id = id;
        this.log = L;
        this.control = control;
    }
    
    //Metodo para recorrer el tunel
    private void recorrerTunel(Aldeanos humano, String accion) throws InterruptedException {
        //Usamos un semáforo binario ya que solo puede acceder una persona
        capacidad.acquire(); //La posible interrupcion es capturada en los metodos entrar/salir
        setHumano(humano); //Humano que se verá en la interfaz
        //Registramos el acceso al tunel
        log.evento((humano.getID() + " " + accion + " por el tunel: " + id));
        try {
            //Tarda 1s en recorrerlo
            Thread.sleep(1000); 
        } catch(InterruptedException e){
            //Los fallos en hilos no se guardan en el Registro
            System.out.println("Interrupcion del hilo " + humano.getID() + " en el tunel.");
        } finally {
            //Registramos la salida del tunel
            log.evento((humano.getID() + " ha terminado de recorrer el tunel: " + id));
            //Al salir el tunel queda vacio
            setHumanoNull(humano);
            capacidad.release();
        }
    }

    //Entrar a los túneles usando el metodo para recorrerlo
    public void entrar(Aldeanos humano) {
        //Hacemos el lock por la variable compartida booleana que comprueba si hay gente saliendo
        cantidadGente++;
        salen.lock();
        try { //Si hay alguien saliendo espera
            while(ocupado) {terminaSalir.await();}
            recorrerTunel(humano, "entra");
        } catch (InterruptedException e) {
            System.out.println("Interrupcion en la entrada al tunel.");
        } finally {
            salen.unlock();
        }
    }
    
    //Salir de los túneles usando el metodo para recorrerlo, tiene prioridad frente a los que entran
    public void salir(Aldeanos humano){
        //Lock para el acceso a la variable compartida ocupado
        salen.lock();
        try {
            //Activamos el ocupado en el try para que si salta la interrupcion mientras se recorre el tunel se anule el ocupado
            ocupado = true;
            recorrerTunel(humano, "sale");
            ocupado = false;
            cantidadGente--;
        } catch (InterruptedException e) {
            System.out.println("Problema en el semaforo del tunel saliendo.");
        } finally {
            //Avisa a los que esperan de que pueden entrar
            terminaSalir.signalAll();
            salen.unlock();
        }
    }
    
    //Gets y sets
    private void setHumano(Aldeanos humano){
        this.explorador = humano;
        control.add("Tunel" + getID(), humano.getID());
    }
    private void setHumanoNull(Aldeanos humano){
        this.explorador = null;
        control.remove("Tunel" + getID(), humano.getID());
    }
    public int getID(){
        return id;
    }
    public synchronized int getCantidadGente(){
        return cantidadGente;
    }
}