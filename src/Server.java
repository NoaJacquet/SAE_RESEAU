package src;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * La classe Server représente le serveur de chat qui gère les connexions des clients.
 */
public class Server {

    private static final int PORT = 5555;
    private static Map<String, PrintWriter> clients = new HashMap<>();

    /**
     * Le point d'entrée principal du serveur.
     *
     * @param args Les arguments de la ligne de commande (non utilisés dans cette application).
     */
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur en attente de connexions...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, clients).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
