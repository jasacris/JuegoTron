package online;

import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import javax.swing.*;

public class VentanaJuego extends JFrame implements KeyListener, ActionListener {

    private EnviaDatos enviaDatos;
    private Socket socketJuego;

    // TABLERO
    private final int ANCHO = 600;
    private final int ALTO = 600;
    private final int TAMANO_MOTO = 10; // Cada "cuadrito" mide 10x10
    private final int COLUMNAS = ANCHO / TAMANO_MOTO; // Para que la moto siempre este en una posición de la matriz y no entre medias de dos
    private final int FILAS = ALTO / TAMANO_MOTO;

    // Matriz: Vacio = 0, J1 = 1, J2 = 2
    private int[][] tablero = new int[COLUMNAS][FILAS];

    // Posiciones en la matriz
    // Jugador 1 empieza a la izquierda, Jugador 2 a la derecha
    private int x1 = 5, y1 = FILAS / 2;
    private int x2 = COLUMNAS - 5, y2 = FILAS / 2;

    // Direcciones actuales
    // J1 va a la derecha (1,0), J2 a la izquierda (-1,0)
    public int dx1 = 1, dy1 = 0;
    public int dx2 = -1, dy2 = 0;

    private Timer timer;
    private boolean juegoAndando = true;
    private int miID; // 1 o 2

    public VentanaJuego(int miID, Socket socket) {
        this.miID = miID;
        this.socketJuego = socket;

        setTitle("Tron - Jugador " + miID);
        setSize(ANCHO, ALTO);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Inicializar el tablero vacío
        for (int i = 0; i < COLUMNAS; i++) {
            for (int j = 0; j < FILAS; j++) {
                tablero[i][j] = 0;
            }
        }

        PanelDibujo panel = new PanelDibujo();
        add(panel);
        addKeyListener(this);

        // Bucle del juego, se ejecuta cada 500 milisegundos
        timer = new Timer(500, this);
        timer.start();

        setVisible(true);
    }

    public void setEnviaDatos(EnviaDatos envia) {
        this.enviaDatos = envia;
    }

    // --- BUCLE ---
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!juegoAndando) {
            return;
        }

        if (miID == 1) {
            System.out.println("x1: " + x1 + ", y1: " + y1);
        } else {
            System.out.println("x2: " + x2 + ", y2: " + y2);
        }

        // 1. Calcular nuevas posiciones
        x1 += dx1;
        y1 += dy1;
        x2 += dx2;
        y2 += dy2;

        // 2. Detectar Colisiones (Para el J1)
        if (verificarColision(x1, y1)) {
            gameOver("¡El Jugador 1 ha chocado!");
            return;
        }
        // 2. Detectar Colisiones (Para el J2)
        if (verificarColision(x2, y2)) {
            gameOver("¡El Jugador 2 ha chocado!");
            return;
        }

        // 3. Pintar el rastro en la matriz
        tablero[x1][y1] = 1;
        tablero[x2][y2] = 2;

        // 4. Redibujar pantalla
        repaint();
    }

    private boolean verificarColision(int x, int y) {
        // Chocar con paredes
        if (x < 0 || x >= COLUMNAS || y < 0 || y >= FILAS) {
            return true;
        }
        // Chocar con rastro (si la casilla no es 0)
        if (tablero[x][y] != 0) {
            return true;
        }

        return false;
    }

    private void gameOver(String mensaje) {
        juegoAndando = false;
        timer.stop();
        JOptionPane.showMessageDialog(this, mensaje + "\nFin del Juego.");
        cerrarTodo();
    }

    // Método para cerrar conexión y ventana
    public void cerrarTodo() {
        try {
            // Cerrar el socket provoca que los hilos Envia/Recibe se detengan o lancen error
            if (socketJuego != null && !socketJuego.isClosed()) {
                socketJuego.close();
            }
        } catch (Exception e) {
            System.out.println("Error al cerrar socket: " + e.getMessage());
        }

        // Cierra la ventana visualmente
        this.dispose();

        // MATA COMPLETAMENTE LA APLICACIÓN (Detiene todos los hilos)
        System.exit(0);
    }

    // --- ENTRADA DE TECLADO ---
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        cambiarDireccion(key, miID); // Cambio mi dirección local

        // Enviar mi cambio de dirección al rival
        if (enviaDatos != null) {
            enviaDatos.enviarTecla(key);
        }
    }

    // Este método cambia la dirección si no es la opuesta a la dirección actual
    public void cambiarDireccion(int key, int idJugador) {
        // JUGADOR 1
        if (idJugador == 1) {
            if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && dy1 != 1) {
                dx1 = 0;
                dy1 = -1;
            }
            if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && dy1 != -1) {
                dx1 = 0;
                dy1 = 1;
            }
            if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && dx1 != 1) {
                dx1 = -1;
                dy1 = 0;
            }
            if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && dx1 != -1) {
                dx1 = 1;
                dy1 = 0;
            }
        } // JUGADOR 2
        else if (idJugador == 2) {
            if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && dy2 != 1) {
                dx2 = 0;
                dy2 = -1;
            }
            if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && dy2 != -1) {
                dx2 = 0;
                dy2 = 1;
            }
            if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && dx2 != 1) {
                dx2 = -1;
                dy2 = 0;
            }
            if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && dx2 != -1) {
                dx2 = 1;
                dy2 = 0;
            }
        }
    }

    // Métodos de la implementación de KeyListener
    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // --- PINTADO ---
    class PanelDibujo extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Recorre la matriz y pinta los rastros
            for (int i = 0; i < COLUMNAS; i++) {
                for (int j = 0; j < FILAS; j++) {
                    if (tablero[i][j] == 1) {
                        g.setColor(Color.CYAN);
                        g.fillRect(i * TAMANO_MOTO, j * TAMANO_MOTO, TAMANO_MOTO, TAMANO_MOTO);
                    } else if (tablero[i][j] == 2) {
                        g.setColor(Color.ORANGE);
                        g.fillRect(i * TAMANO_MOTO, j * TAMANO_MOTO, TAMANO_MOTO, TAMANO_MOTO);
                    }
                }
            }

            // Pinta los jugadores (posición actual) para que resalten
            g.setColor(Color.WHITE);
            g.fillRect(x1 * TAMANO_MOTO, y1 * TAMANO_MOTO, TAMANO_MOTO, TAMANO_MOTO);
            g.fillRect(x2 * TAMANO_MOTO, y2 * TAMANO_MOTO, TAMANO_MOTO, TAMANO_MOTO);
        }
    }
}
