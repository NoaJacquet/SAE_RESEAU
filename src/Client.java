package src;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import modele.bd.AmisBd;
import modele.bd.MessageBd;
import modele.code.Message;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 5555;

    private static List<String> amis = new ArrayList<>();
    private static List<String> nonAmis= new ArrayList<>();
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;


    private String pseudoClient;
    public Client(String pseudo) throws ClassNotFoundException {
        this.pseudoClient = pseudo;
        List<modele.code.Utilisateur> liste;
                try {
                    liste = AmisBd.ListeAmis(pseudo);
                    for (modele.code.Utilisateur utilisateur : liste) {
                        System.out.println("amis : "+utilisateur.getPseudo());
                        amis.add(utilisateur.getPseudo());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                List<modele.code.Utilisateur> liste2;
                try {
                    liste2 = AmisBd.ListeNonAmis(pseudo);
                    for (modele.code.Utilisateur utilisateur : liste2) {
                        System.out.println("non amis : "+utilisateur.getPseudo());
                        nonAmis.add(utilisateur.getPseudo());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

        for (Message m:MessageBd.recupererLesMessageDeTousSesAmisDansOrdreDate(pseudo)){
            String s = m.getDate()+" "+m.getPseudo()+" : "+m.getContenu();
            PagePrincipale.afficheMessage(s);
        }

        try{
            this.socket = new Socket(SERVER_IP, SERVER_PORT);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void lancement() {

                System.out.println("Connexion au serveur réussie.");

                // Lire et afficher les messages du serveur en arrière-plan
                new Thread(() -> {
                    try {
                        String serverMessage;
                        while ((serverMessage = this.in.readLine()) != null) {
                            PagePrincipale.afficheMessage(serverMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

                // Envoyer des messages depuis le terminal
                out.println(this.pseudoClient);
                System.out.println("Connecté avec le pseudo: " + this.pseudoClient);
    }


    public void sendMessage(String message) {
        this.out.println(message);
    }

    public void lireMessage(){
            new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = this.in.readLine()) != null) {

                    PagePrincipale.afficheMessage(serverMessage);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
    }

    public void ajouteAmis(String pseudo) throws ClassNotFoundException{
        amis.add(pseudo);
        AmisBd.ajouteAmis(this.pseudoClient, pseudo);
        System.out.println("ajoute amis : "+pseudo);
        sendMessage("/follow "+pseudo);
        if (nonAmis.contains(pseudo)){
            nonAmis.remove(pseudo);
        }

    }

    public void supprimeAmis(String pseudo) throws ClassNotFoundException{
        amis.remove(pseudo);
        AmisBd.supprimeAmis(this.pseudoClient, pseudo);
        System.out.println("supprime amis : "+pseudo);
        sendMessage("/unfollow "+pseudo);
        if (!nonAmis.contains(pseudo)){
            nonAmis.add(pseudo);
        }
    }

    public List<String> getAmis(){
        return amis;
    }

    public List<String> getNonAmis(){
        return nonAmis;
    }
}