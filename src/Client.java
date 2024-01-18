package src;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.util.Util;

import modele.bd.AmisBd;
import modele.bd.LikeBd;
import modele.bd.MessageBd;
import modele.bd.UtilisateurBd;
import modele.code.Like;
import modele.code.Message;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 5555;

    private static List<String> EnvoyerMassage = new ArrayList<>();
    private static List<String> RecevoirMessage= new ArrayList<>();
    private static List<String> NonSuivi= new ArrayList<>();
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;


    private String pseudoClient;
    public Client(String pseudo) throws ClassNotFoundException {
        this.pseudoClient = pseudo;
        List<modele.code.Utilisateur> liste;
                try {
                    liste = AmisBd.RecevoirMessage(pseudo);
                    for (modele.code.Utilisateur utilisateur : liste) {
                        System.out.println("amis : "+utilisateur.getPseudo());
                        RecevoirMessage.add(utilisateur.getPseudo());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                List<modele.code.Utilisateur> liste2;
                try {
                    liste2 = AmisBd.EnvoyerMessage(pseudo);
                    for (modele.code.Utilisateur utilisateur : liste2) {
                        EnvoyerMassage.add(utilisateur.getPseudo());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                List<modele.code.Utilisateur> liste3;
                try {
                    liste3 = AmisBd.NonSuivi(pseudo);
                    for (modele.code.Utilisateur utilisateur : liste3) {
                        NonSuivi.add(utilisateur.getPseudo());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
        for (Message m:MessageBd.recupererLesMessageDeTousSesAmisDansOrdreDate(pseudo)){
            String s = m.getDate()+"|||"+m.getPseudo()+"|||"+m.getContenu();
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
                            if (serverMessage.contains("///like")){
                                System.out.println(serverMessage);
                                PagePrincipale.afficheMessage(serverMessage);
                            }
                            else{
                                for (String s:RecevoirMessage){ //affiche les messages des amis
                                    if (serverMessage.contains(s)){
                                        PagePrincipale.afficheMessage(serverMessage);
                                    }
                                }
                                if (serverMessage.contains(pseudoClient)){
                                    PagePrincipale.afficheMessage(serverMessage);
                                }
                            }
                            
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



    public void Suivre(String pseudo) throws ClassNotFoundException{
        RecevoirMessage.add(pseudo);
        AmisBd.ajouteAmis(this.pseudoClient, pseudo);
        sendMessage("/follow "+pseudo);
        if (NonSuivi.contains(pseudo)){
            NonSuivi.remove(pseudo);
        }

    }

    public void supprimeAmis(String pseudo) throws ClassNotFoundException{
        RecevoirMessage.remove(pseudo);
        AmisBd.supprimeAmis(this.pseudoClient, pseudo);
        System.out.println("supprime amis : "+pseudo);
        sendMessage("/unfollow "+pseudo);
        if (!NonSuivi.contains(pseudo)){
            NonSuivi.add(pseudo);
        }
    }

    public List<String> getRecevoirMessage(){
        return RecevoirMessage;
    }

    public List<String> getNonSuivi(){
        return NonSuivi;
    }

    public void like(String date,String pseudo, String contenu) throws ClassNotFoundException{
        Message m=MessageBd.recupererMessage(date,pseudo,contenu);
        LikeBd.ajouteLike(m.getIdMessage(), this.pseudoClient);
        int compteur=LikeBd.countLikeToMessage(m.getIdMessage());
        sendMessage("/like "+m.getIdMessage()+" "+compteur);
    }

    public int getIdUser() throws ClassNotFoundException{
        return UtilisateurBd.recupererUtilisateur(pseudoClient).getId();
    
    }

}