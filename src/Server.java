package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import modele.bd.MessageBd;
import modele.bd.UtilisateurBd;
import modele.code.Message;

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
                ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                clientHandler.start();

                new Thread(()->{
                    BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                    while(true){
                        try {
                            String consoleInput = consoleReader.readLine();
        
                            if (consoleInput.contains("/deleteUser")) {
                                String[] parts = consoleInput.split(" ");
                                String pseudo = parts[1];
                                try {
                                    if (UtilisateurBd.pseudoExiste(pseudo))
                                    informClientHandlerDeleteUser(pseudo);
                                    else{
                                        System.out.println("Ce pseudo n'existe pas");
                                    }
                                } 
                                catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                            else if (consoleInput.contains("/deleteMessage")) {
                                String[] parts = consoleInput.split(" ");
                                String idMessageString = parts[1];
                                try{
                                    int idMessage = Integer.parseInt(idMessageString);
                                    if (MessageBd.messageIdExiste(idMessage))
                                    informClientHandlerDeleteMessage(idMessage);
                                    else{
                                        System.out.println("Ce message n'existe pas");
                                    }
                                }
                                catch(NumberFormatException e){
                                    System.out.println("L'identifiant du message doit être un nombre entier.");
                                }
                                catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                                
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
        else{
            try {
                UtilisateurBd.deleteUser(pseudo);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Informe le ClientHandler de la suppression du message.
     * @param idMessage
     * 
     */
    private static void informClientHandlerDeleteMessage(int idMessage) {
        if (clients.isEmpty()) {
            try {
                MessageBd.supprimerMessage(idMessage);
                System.out.println("Message supprimé. acun client");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Message message = null;
            try {
                message = MessageBd.recupererMessageById(idMessage);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String envoie = "///SUPPRIMER" + "|||" + message.getDate() + "|||" + message.getPseudo() + "|||" + message.getContenu();
            
            // Retirer l'entrée du client associée au pseudo après la suppression du message
            for (String pseudo : clients.keySet()) {
                PrintWriter writer = clients.get(pseudo);
                if (writer != null) {
                    writer.println(envoie);
                }
            }
            
            try {
                MessageBd.supprimerMessage(idMessage);
                System.out.println("Message supprimé. avec client");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    
}
