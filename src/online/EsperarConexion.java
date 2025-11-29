package online;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class EsperarConexion implements Runnable {

    private Cliente cliente;
    private ExecutorService pool;

    public EsperarConexion(Cliente c, ExecutorService p) {
        cliente = c;
        pool = p;
    }

    @Override
    public void run() {
        System.out.println("ESPERAR CONEXION");

        int puerto = cliente.getPuerto();

        try (ServerSocket ss = new ServerSocket(puerto)) {
            if (cliente.getDisponible()) {
                try {
                    Socket s = ss.accept();

                    cliente.setDisponible(false);
                    System.out.println("Entra al servidor");

                    VentanaJuego ventana = new VentanaJuego(1, s);

                    // Inicio hilos para la comunicación
                    EnviaDatos hiloEnvia = new EnviaDatos(s, 1);
                    RecibeDatos hiloRecibe = new RecibeDatos(s, ventana);

                    // Conecto ventana con el hilo que envia los datos
                    ventana.setEnviaDatos(hiloEnvia);

                    hiloEnvia.start();
                    hiloRecibe.start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
