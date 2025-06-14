/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author caice
 */
public class Almacenes {
    //Recuento de cada recurso
    private int granero = 0;
    private int aserradero = 0;
    private int tesoreria = 0;
    
    //Maxima capacidad
    private int maxGranero = 200;
    private int maxAserradero = 150;
    private int maxTesoreria = 50;
    
    private List<Aldeanos> listaAldeanos = Collections.synchronizedList(new ArrayList<>());
    private final int id;  //Organizacion de tuneles
    private String nombre;
    private Semaphore capacidad = new Semaphore(3, true); 
    private int cantidadGente = 0;
//El true es para que salgan antes de entrar los que esperan el semaforo porque los que salen acceden antes al semaforo que los que entran
    private boolean ocupado = false;
    private Lock salen = new ReentrantLock();
    private Condition terminaSalir = salen.newCondition();
    private volatile Aldeanos aldeano;
    //Es volatil ya que la modificacion esta sincronizada, solo queremos asegurar que su lectura sea del dato más actualizado
    private Registro log;
    private Controlador controlAlmacenes;
    
    private Lock almacen = new ReentrantLock();
    private Condition hayRecurso = almacen.newCondition();
    
    Almacenes(int id, Registro L, Controlador control, String nombre){
        this.id = id;
        this.log = L;
        this.controlAlmacenes = control;
        this.nombre = nombre;
    }
    
     public void almacenar(Aldeanos aldeano, String accion) throws InterruptedException {
        //Usamos un semáforo binario ya que solo puede acceder una persona
        capacidad.acquire(); //La posible interrupcion es capturada en los metodos entrar/salir
        setAldeano(aldeano); //Humano que se verá en la interfaz
        //Registramos el acceso al tunel
        log.evento((aldeano.getID() + " " + accion + " al almacen: " + nombre));
        try {
            //Tiempo en dejar los recursos
            int t = (int)(Math.random() * (1001)) + 2000;
            Thread.sleep(t); 
            dejarRecurso(aldeano, 2);
        } catch(InterruptedException e){
            //Los fallos en hilos no se guardan en el Registro
            System.out.println("Interrupcion del hilo " + aldeano.getID() + " en el almacen.");
        } finally {
            //Registramos la salida del tunel
            log.evento((aldeano.getID() + " ha terminado de depositar recursos en: " + nombre));
            //Al salir el tunel queda vacio
            setHumanoNull(aldeano);
            capacidad.release();
        }
    }
     
     private void dejarRecurso(Aldeanos aldeano, int num){
        almacen.lock();
        entrar(listaAldeanos, aldeano, "Almacenes");
        log.evento(aldeano.getID() + " ha entrado al almacen: " + nombre);
        try {
            int cantidad = aldeano.getRecurso();
            switch(num){           
                case 1:
                    granero += cantidad;
                    aldeano.quitarRecursos();
                    log.evento((aldeano.getID() + " ha dejado comida en la despensa, ahora hay " + granero));
                case 2: 
                    aserradero += cantidad;
                    aldeano.quitarRecursos();
                    log.evento((aldeano.getID() + " ha dejado comida en la despensa, ahora hay " + aserradero));
                case 3: 
                    tesoreria += cantidad;
                    aldeano.quitarRecursos();
                    log.evento((aldeano.getID() + " ha dejado comida en la despensa, ahora hay " + tesoreria));
            }
            
            hayRecurso.signalAll();
        } finally {
            salir(listaAldeanos, aldeano, "Almacenes");
            log.evento(aldeano.getID() + " ha salido de los almacenes.");
            almacen.unlock();
        }
    }
     
     //Gets y sets
    private void setAldeano(Aldeanos aldeano){
        this.aldeano = aldeano;
        controlAlmacenes.add("Almacen" + getID(), aldeano.getID());
    }
    private void setHumanoNull(Aldeanos aldeano){
        this.aldeano = null;
        controlAlmacenes.remove("Almacen" + getID(), aldeano.getID());
    }
    public int getID(){
        return id;
    }
    
    public String getNombre(){
        return this.nombre;
    }
    public synchronized int getCantidadGente(){
        return cantidadGente;
    }
    
     private void entrar(List<Aldeanos> L, Aldeanos aldeano, String zona){
        L.add(aldeano);
        controlAlmacenes.add(zona, aldeano.getID());
    }
    private void salir(List<Aldeanos> L, Aldeanos aldeano, String zona){
        L.remove(aldeano);
        controlAlmacenes.remove(zona, aldeano.getID());
    }
}
