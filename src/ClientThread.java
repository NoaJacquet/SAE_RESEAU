
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * La classe ClientThread représente un thread pour gérer la communication avec un client dans un serveur multi-client.
 * Elle lit continuellement les messages du client et les diffuse à tous les clients connectés.
 */
public class ClientThread implements Runnable {

    private Socket client;

    /**
     * Construit un nouveau ClientThread avec le socket client spécifié.
     *
     * @param client le socket client
     */
    public ClientThread(Socket client) {
        this.client = client;
    }

    /**
     * Lit continuellement les messages du client et les diffuse à tous les clients connectés.
     */
    @Override
    public void run() {
        while (true) {
            try {
                DataInputStream lire = new DataInputStream(client.getInputStream()); // récupère le flux d'entrée du socket
                if (lire.available() > 0) {
                    String msg = lire.readUTF();
                    for (Socket socket : Server.clients) {
                        try {
                            DataOutputStream ecrire = new DataOutputStream(socket.getOutputStream());
                            ecrire.writeUTF(msg);
                            ecrire.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
