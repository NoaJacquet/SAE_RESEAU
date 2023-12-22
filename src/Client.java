
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * La classe Client représente un client pour la communication avec un serveur.
 * Elle permet de lire les messages du serveur et d'envoyer des messages au serveur.
 */
public class Client {


    /**
     * Point d'entrée principal pour lancer le client.
     *
     * @param args les arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        new Client(); // permet de créer un nouveau client pour le lancer directement avec le fichier
    }

    /**
     * Construit un nouveau client et établit une connexion avec le serveur.
     */
    public Client() {
        try {
            Socket client = new Socket("localhost", 5556);

            // Thread permettant de lire les messages du serveur
            new Thread(() -> {
                while (true) {
                    try {
                        DataInputStream lire = new DataInputStream(client.getInputStream());
                        if (lire.available() > 0) {
                            String msg = lire.readUTF(); // permet de lire une chaîne de caractères en UTF-8 dans le flux de sortie du socket
                            System.out.println(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // Thread permettant d'écrire des messages au serveur
            new Thread(() -> {
                Scanner scanner = new Scanner(System.in); // permet d'écrire dans le terminal, équivalent du input en Python
                while (true) {
                    try {
                        DataOutputStream ecrire = new DataOutputStream(client.getOutputStream()); // permet d'écrire des données dans le flux de sortie du socket
                        String msg = scanner.nextLine(); // permet de lire une ligne de texte
                        ecrire.writeUTF(msg); // permet d'écrire une chaîne de caractères en UTF-8 dans le flux de sortie du socket
                        ecrire.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
