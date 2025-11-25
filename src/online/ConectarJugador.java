package online;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConectarJugador implements Runnable {

    private Cliente cliente;
    private ExecutorService pool;

    public ConectarJugador(Cliente c, ExecutorService p) {
        cliente = c;
        pool = p;
    }

    @Override
    public void run() {
        System.out.println("CONECTAR JUGADOR");
        pool = Executors.newCachedThreadPool();
        String ip = cliente.getIp();
        int puerto = Integer.parseInt(cliente.getListaJugadores().get(0).get(1));
        if (cliente.getDisponible()) {

            try {
                Socket s = new Socket(ip, puerto);
                pool.execute(new EnviaDatos(s));
                pool.execute(new RecibeDatos(s));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
