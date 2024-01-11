package src;
import java.io.*;
import java.net.*;
import java.sql.SQLException;

import modele.bd.ConnexionBd;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 5555;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader terminalInput = new BufferedReader(new InputStreamReader(System.in))
            )
            {

                System.out.println("Connexion au serveur réussie.");

                // Lire et afficher les messages du serveur en arrière-plan
                new Thread(() -> {
                    String serverMessage;
                    try {
                        while ((serverMessage = in.readLine()) != null) {
                            System.out.println(serverMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

                // Envoyer des messages depuis le terminal
                String pseudo = in.readLine(); // Attendre la demande de pseudo
                System.out.println("Connecté avec le pseudo: " + pseudo);

                String userInput;
                while ((userInput = terminalInput.readLine()) != null) {
                    out.println(userInput);
                }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ConnexionBd sqlConnect;

    public static ConnexionBd getSQlConnect() throws SQLException{
        sqlConnect.connect("localhost", "SAE_RESEAUX", "temha", "temha1011");
        return sqlConnect;
    }
}
