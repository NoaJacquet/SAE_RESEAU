
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe Server représente un serveur permettant la communication avec plusieurs clients.
 * Elle gère la connexion des clients et lance un thread distinct pour chaque client connecté.
 */
class Server {
    /**
     * Liste des sockets représentant les clients connectés au serveur.
     */
    public static List<Socket> clients = new ArrayList<>();

    /**
     * Construit un nouveau serveur et démarre le processus de gestion des connexions clients.
     */
    public Server() {
        demarrerServer();
    }

    /**
     * Démarre le serveur en écoutant les connexions entrantes et en lançant un thread pour chaque client.
     */
    public void demarrerServer() {
        try {
            ServerSocket server = new ServerSocket(5556);
            while (true) {
                System.out.println("En attente de connexion...");
                Socket client = server.accept();
                System.out.println("Client connecté");
                clients.add(client);
                Thread thread = new Thread(new ClientThread(client));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
