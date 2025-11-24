package online;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Cliente {
    private static HashMap<Integer, List<String>> listaJugadores = new HashMap<Integer, List<String>>();
	private static String ip;
	private static int puerto = 9999;
	private Boolean disponible;

    public Cliente (){
        disponible = true;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente();

        try (Socket s = new Socket(InetAddress.getLocalHost(), 4444);
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(s.getInputStream())){

            ip = InetAddress.getLocalHost().toString().split("/")[1];

            dos.writeBytes(ip + "\r\n");
            dos.writeBytes(puerto + "\r\n");

            listaJugadores = (HashMap<Integer, List<String>>) ois.readObject();

            mostrarJugadoresDisponibles();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        /*ExecutorService pool = new Executors.newCachedThreadPool();
        pool.execute();*/
    }

    public static void mostrarJugadoresDisponibles(){

        System.out.println("LISTA DE JUGADORES DISPONIBLES");
        System.out.println("----------------------------------------------");

        for (int i = 0; i < listaJugadores.size(); i++) {
            System.out.println("IP: " + listaJugadores.get(i).get(0) + " y Puerto: " + listaJugadores.get(i).get(1));
        }
    }
}
