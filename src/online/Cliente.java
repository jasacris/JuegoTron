package online;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Cliente {

    private static HashMap<Integer, List<String>> listaJugadores = new HashMap<Integer, List<String>>();
    private static String ip;
    private static int puerto = 8888;
    private Boolean disponible;

    public Cliente() {
        disponible = true;
    }

    public static HashMap<Integer, List<String>> getListaJugadores() {
        return listaJugadores;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public String getIp() {
        return ip;
    }

    public int getPuerto() {
        return puerto;
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente();

        try (Socket s = new Socket(InetAddress.getLocalHost(), 4444); DataInputStream dis = new DataInputStream(s.getInputStream()); DataOutputStream dos = new DataOutputStream(s.getOutputStream()); ObjectInputStream ois = new ObjectInputStream(s.getInputStream())) {

            ip = InetAddress.getLocalHost().toString().split("/")[1];
            // Envío al servidor mi ip y mi puerto
            dos.writeBytes(ip + "\r\n");
            dos.writeBytes(puerto + "\r\n");
            // Recibo la lista de jugadores del servidor
            listaJugadores = (HashMap<Integer, List<String>>) ois.readObject();
            // Muestro la lista de jugadores por pantalla
            mostrarJugadoresDisponibles();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        ExecutorService pool = Executors.newCachedThreadPool();

        if (listaJugadores.size() % 2 != 0) {
            // El primer cliente que se conecte esperará a que se conecte un segundo para poder jugar
            pool.execute(new EsperarConexion(cliente, pool));
        } else {
            // El segundo cliente se conectará al cliente que se conecto inmediatamente antes que el
            pool.execute(new ConectarJugador(cliente, pool));
        }

    }

    public static void mostrarJugadoresDisponibles() {

        System.out.println("LISTA DE JUGADORES CONECTADOS");
        System.out.println("----------------------------------------------");

        for (int i = 0; i < listaJugadores.size(); i++) {
            System.out.println("IP: " + listaJugadores.get(i).get(0) + " y Puerto: " + listaJugadores.get(i).get(1));
        }
    }
}
