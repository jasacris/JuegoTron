package online;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class RecibeDatos implements Runnable {

    private Socket s;

    public RecibeDatos(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        System.out.println("Recibe datos");
        try {
            DataInputStream in = new DataInputStream(this.s.getInputStream());
            String mensaje = in.readLine();
            while (!mensaje.equals("DESCONECTAR")) {
                System.out.println(mensaje);
                mensaje = in.readLine();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (this.s != null) {
                try {
                    this.s.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

}
