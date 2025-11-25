package online;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class EnviaDatos implements Runnable {

    private Socket s;

    public EnviaDatos(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        System.out.println("Envia datos");
        try {
            DataOutputStream out = new DataOutputStream(this.s.getOutputStream());
            Scanner entrada = new Scanner(System.in);
            String mensaje = entrada.nextLine();
            out.writeBytes(mensaje + "\r\n");
            while (!mensaje.equals("DESCONECTAR")) {
                mensaje = entrada.nextLine();
                out.writeBytes(mensaje + "\r\n");
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
