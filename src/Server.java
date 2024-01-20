package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

        // Lire les commandes de la console
                
    private static BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

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
                ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                clientHandler.start();

                new Thread(()->{
                    try {
                        String consoleInput = consoleReader.readLine();
    
                        if (consoleInput.contains("/deleteUser")) {
                            String[] parts = consoleInput.split(" ");
                            String pseudo = parts[1];
                            System.out.println("Suppression de l'utilisateur " + pseudo + "...");
                            informClientHandlerDeleteUser(pseudo);
    
    
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Informe le ClientHandler de la suppression de l'utilisateur.
     * @param pseudo
     * 
     */
    private static void informClientHandlerDeleteUser(String pseudo) {
        if (clients.containsKey(pseudo)) {
            PrintWriter userWriter = clients.get(pseudo);
            // Informe le ClientHandler de la suppression de l'utilisateur
            userWriter.println("Votre compte a été supprimé. La connexion sera fermée.");
        }
    }

}
