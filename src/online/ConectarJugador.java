package online;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

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
        String ip = cliente.getIp();
        // Puerto del cliente que va a hacer de host
        int puerto = Integer.parseInt(cliente.getListaJugadores().get(cliente.getListaJugadores().size() - 2).get(1));
        if (cliente.getDisponible()) {
            System.out.println("PUERTO: " + puerto);
            try {
                Socket s = new Socket(ip, puerto);

                cliente.setDisponible(false);
                System.out.println("se conecta al otro cliente");

                VentanaJuego ventana = new VentanaJuego(2, s);

                // Inicio hilos de comunicación
                EnviaDatos hiloEnvia = new EnviaDatos(s, 2);
                RecibeDatos hiloRecibe = new RecibeDatos(s, ventana);

                // Conecto ventana con el hilo que envia los datos
                ventana.setEnviaDatos(hiloEnvia);

                hiloEnvia.start();
                hiloRecibe.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
