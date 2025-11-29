package online;

import java.io.DataOutputStream;
import java.net.Socket;

public class EnviaDatos extends Thread {

    private Socket socket;
    private DataOutputStream salida;
    private int miID; // 1 si soy Host, 2 si soy Guest

    public EnviaDatos(Socket s, int id) {
        this.socket = s;
        this.miID = id;
        try {
            this.salida = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarTecla(int codigoTecla) {
        try {
            salida.writeInt(miID); // Envío quién soy
            salida.writeInt(codigoTecla); // Envío qué pulsé
            salida.flush(); // Me aseguro de haber mandado todos los datos
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
