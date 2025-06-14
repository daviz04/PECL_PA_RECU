
package Parte2;

import Clases.Gestor;
import Clases.Barbaros;
import Clases.AreaRecursos;
import Clases.CentroUrbano;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SimulacionDatos {
    private CentroUrbano refugio;
    private Gestor gestor;
    private AreaRecursos Z1;
    private AreaRecursos Z2;
    private AreaRecursos Z3;
    private AreaRecursos Z4;

    public SimulacionDatos(Gestor gestor) {
        this.gestor = gestor;
        refugio = gestor.getRefugio();
        Z1 = gestor.getZonaExterior1();
        Z2 = gestor.getZonaExterior2();
        Z3 = gestor.getZonaExterior3();
        Z4 = gestor.getZonaExterior4();
    }

    public int getCantidadHumanosEnRefugio() {
        return refugio.humanosTotales();
    }

    public List<Barbaros> getZombiesTotal() {
        List<Barbaros> zombiesCompletos = new ArrayList<>();
        zombiesCompletos.addAll(Z1.getListaBarbaros());
        zombiesCompletos.addAll(Z2.getListaBarbaros());
        zombiesCompletos.addAll(Z3.getListaBarbaros());
        zombiesCompletos.addAll(Z4.getListaBarbaros());
        return zombiesCompletos;
    }

    public void pausar() {
        //gestor.pausar();
    }

    public void reanudar() {
       // gestor.reanudar();
    }
    
    public List<Barbaros> obtenerTop3Zombis() {
        List<Barbaros> zombies = getZombiesTotal();
       
        return zombies.stream()
                .sorted(Comparator.comparingInt(Barbaros::getMuertes).reversed()) // Ordenamos por muertes en orden descendente
                .limit(3) // Limitamos a los 3 primeros
                .collect(Collectors.toList()); // Los recogemos en una lista
    }
}
