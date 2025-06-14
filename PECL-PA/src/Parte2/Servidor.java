
package Parte2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private SimulacionDatos datos;

    public Servidor(SimulacionDatos datos) {
        this.datos = datos;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Servidor socket escuchando en puerto 5000...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            String command;
            while ((command = in.readLine()) != null) {
                switch (command) {
                    case "GET_REFUGIO":
                        out.println(datos.getCantidadHumanosEnRefugio());
                        break;
                    case "GET_RANKING":
                        out.println(datos.obtenerTop3Zombis());
                        break;
                    case "PAUSAR":
                        datos.pausar();
                        out.println("PAUSADO");
                        break;
                    case "REANUDAR":
                        datos.reanudar();
                        out.println("REANUDADO");
                        break;
                    default:
                        out.println("COMANDO NO VÁLIDO");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
