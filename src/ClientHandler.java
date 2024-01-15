package src;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import modele.bd.AmisBd;
import modele.bd.MessageBd;
import modele.bd.UtilisateurBd;
import modele.code.Utilisateur;

public class ClientHandler extends Thread{
    private Socket clientSocket;
    private BufferedReader lire;
    private PrintWriter ecrire;
    private String pseudoClient;
    private Map<String, PrintWriter> clients;
    private static List<String> EnvoyerMassage = new ArrayList<>();
    private static List<String> RecevoirMessage= new ArrayList<>();
    private static List<String> NonSuivi= new ArrayList<>();


    public ClientHandler(Socket socket,Map<String, PrintWriter> clients) {
        this.clientSocket = socket;
        this.clients = clients;

        try{
            this.lire = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  // lire les messages du client
            this.ecrire = new PrintWriter(clientSocket.getOutputStream(), true);    // Envoyer des messages au client
            this.pseudoClient = this.lire.readLine();      // Lire le pseudoClient du client
            clients.put(pseudoClient, this.ecrire);   // Enregistrer le pseudoClient et le flux de sortie du client
            
            try {
                for (Utilisateur u:AmisBd.RecevoirMessage(pseudoClient)){
                    RecevoirMessage.add(u.getPseudo());
                }
            } 
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                for (Utilisateur u:AmisBd.EnvoyerMessage(pseudoClient)){
                    EnvoyerMassage.add(u.getPseudo());
                }
            } 
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                for (Utilisateur u:AmisBd.NonSuivi(pseudoClient)){
                    NonSuivi.add(u.getPseudo());
                }
            } 
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println(pseudoClient + " est connecté.");

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            this.lire = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  // lire les messages du client
            this.ecrire = new PrintWriter(clientSocket.getOutputStream(), true);    // Envoyer des messages au client

            // Demander et enregistrer le pseudo du client
             
            // lire les messages du client et diffuser à tous les autres clients
            String message;
            while ((message = this.lire.readLine()) != null) {
                this.broadcast(message);
            }
        } 
        
        catch (IOException e) {
            e.printStackTrace();
        }
        
        finally {
            // Retirer le pseudoClient et le flux de sortie du client de la liste des clients
            clients.remove(this.pseudoClient);
            System.out.println(this.pseudoClient + " est déconnecté.");
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void broadcast(String message) {
        

        if (message.contains("/")){
            String[] recipientMessage = message.split(" ");
            if (recipientMessage[0].equals("/follow")) {
                for (String pseudo : clients.keySet()) {
                    if (pseudo.equals(recipientMessage[1])) {
                        PrintWriter recipientWriter = clients.get(pseudo);
                        recipientWriter.println(pseudoClient + " vous suit");
                        RecevoirMessage.add(recipientMessage[1]);
                    }
                    if (pseudo.equals(pseudoClient)) {
                        PrintWriter recipientWriter = clients.get(pseudo);
                        recipientWriter.println("Vous suivez "+recipientMessage[1]);
                    }
                }
                
                return;
            }

            else if (recipientMessage[0].equals("/unfollow")){
                for (String pseudo : clients.keySet()) {
                    if (pseudo.equals(recipientMessage[1])) {
                        PrintWriter recipientWriter = clients.get(pseudo);
                        recipientWriter.println(pseudoClient + " ne vous suit plus");
                        RecevoirMessage.remove(recipientMessage[1]);
                    }
                    if (pseudo.equals(pseudoClient)) {
                        PrintWriter recipientWriter = clients.get(pseudo);
                        recipientWriter.println("Vous ne suivez plus "+recipientMessage[1]);
                    }
                }
                return;
            }
        }

        else{
            String recipientMessage = message;
            List<String> utilisateurs = new ArrayList<>();
            try {
                for (Utilisateur u:UtilisateurBd.AllUtilisateur())
                {
                    utilisateurs.add(u.getPseudo());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            for (String pseudo : utilisateurs) {
                PrintWriter recipientWriter = clients.get(pseudo);
                if (recipientWriter != null){
                    LocalDateTime now = LocalDateTime.now();
                    
                    // Formatte la date selon le format de votre choix
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = now.format(formatter);
                        
                    // Ajoutez la date au message et l'affiche dans le panneau
                    String messageWithDate = formattedDate + " "+ pseudoClient +" : "+ recipientMessage;
                    recipientWriter.println(messageWithDate);
                }
            }
            try {
                MessageBd.ajouteMessage(pseudoClient, recipientMessage);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        
        }
    }

}