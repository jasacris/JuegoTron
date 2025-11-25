package online;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        pool = Executors.newCachedThreadPool();

        int puerto = cliente.getPuerto();

        try (ServerSocket ss = new ServerSocket(puerto)) {
            if (cliente.getDisponible()) {
                try {
                    Socket s = ss.accept();

                    cliente.setDisponible(false);
                    System.out.println("Entra al servidor");

                    pool.execute(new EnviaDatos(s));
                    pool.execute(new RecibeDatos(s));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
