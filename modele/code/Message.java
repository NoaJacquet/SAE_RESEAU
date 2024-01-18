package modele.code;


public class Message {
    private int idMessage;
    private int idUtilisateur;
    private String contenu;
    private String date;
    private String pseudo;
    public Message(int idMessage, int idUtilisateur, String contenu, String date, String pseudo){
        this.idMessage = idMessage;
        this.idUtilisateur = idUtilisateur;
        this.contenu = contenu;
        this.date = date;
        this.pseudo = pseudo;
    }
    public String getContenu() {
        return contenu;
    }
    public String getDate() {
        return date;
    }
    public int getIdMessage() {
        return idMessage;
    }
    public int getIdUtilisateur() {
        return idUtilisateur;
    }
    public String getPseudo() {
        return pseudo;
    }

}
