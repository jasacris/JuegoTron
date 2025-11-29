package online;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Servidor {

    private HashMap<Integer, ArrayList<String>> listaJugadores;

    public Servidor() {
        this.listaJugadores = new HashMap<Integer, ArrayList<String>>();
    }

    public void aniadirJugador(int clave, String ip, String puerto) {
        ArrayList<String> jugador = new ArrayList<String>();
        jugador.add(ip);
        jugador.add(puerto);
        listaJugadores.put(clave, jugador);
    }

    public HashMap<Integer, ArrayList<String>> getListaJugadores() {
        return this.listaJugadores;
    }

    public static void main(String[] args) {
        ExecutorService pool = null;
        Servidor servidor = null;
        try (ServerSocket ss = new ServerSocket(4444)) {
            pool = Executors.newCachedThreadPool();
            servidor = new Servidor();
            while (true) {
                try {
                    Socket s = ss.accept();
                    AniadirCliente a = new AniadirCliente(s, servidor);
                    pool.execute(a);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
