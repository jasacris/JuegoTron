package online;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class AniadirCliente implements Runnable {

    private Socket s;
    private Servidor servidor;

    public AniadirCliente(Socket s, Servidor servidor) {
        this.s = s;
        this.servidor = servidor;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try (DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream())) {
            String ip = dis.readLine();
            String puerto = dis.readLine();
            int clave = this.servidor.getListaJugadores().size();

            servidor.aniadirJugador(clave, ip, puerto);

            oos.writeObject(servidor.getListaJugadores());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
