import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader lire;
    private PrintWriter ecrire;
    private String pseudoClient;
    private Map<String, PrintWriter> clients;

    public ClientHandler(Socket socket,Map<String, PrintWriter> clients) {
        this.clientSocket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            this.lire = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  // lire les messages du client
            this.ecrire = new PrintWriter(clientSocket.getOutputStream(), true);    // Envoyer des messages au client

            // Demander et enregistrer le pseudo du client
            this.ecrire.println("Veuillez entrer votre pseudo:");   
            pseudoClient = this.lire.readLine();      // Lire le pseudoClient du client
            clients.put(pseudoClient, this.ecrire);   // Enregistrer le pseudoClient et le flux de sortie du client

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
            clients.remove(pseudoClient);
            System.out.println(pseudoClient + " est déconnecté.");
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void broadcast(String message) {
        String[] recipientMessage = message.split(",");
        for (String pseudo : clients.keySet()) {
            PrintWriter recipientWriter = clients.get(pseudo);
    
            if (recipientMessage[0].equals(pseudo) || recipientMessage[0].equals("all")) {
                recipientWriter.println(pseudoClient +" : "+ recipientMessage[1]);
            }
        }
        clients.get(pseudoClient).println(recipientMessage[1]);
    }

}