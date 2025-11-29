package online;

import java.io.DataInputStream;
import java.net.Socket;

public class RecibeDatos extends Thread {

    private Socket socket;
    private DataInputStream entrada;
    private VentanaJuego ventana;

    public RecibeDatos(Socket s, VentanaJuego v) {
        this.socket = s;
        this.ventana = v;
        try {
            this.entrada = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Leeo: Quien envió (1 o 2) y que tecla pulsó
                int idRemoto = entrada.readInt();
                int tecla = entrada.readInt();

                // Actualizo la dirección en la simulación local
                ventana.cambiarDireccion(tecla, idRemoto);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
