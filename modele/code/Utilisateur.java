package modele.code;
public class Utilisateur{
    private int id;
    private String pseudo;
    private String email;
    private String motDePasse;

    public Utilisateur(int id, String pseudo, String email, String motDePasse){
        this.id = id;
        this.pseudo = pseudo;
        this.email = email;
        this.motDePasse = motDePasse;
    }
    public String getEmail() {
        return email;
    }
    public int getId() {
        return id;
    }
    public String getMotDePasse() {
        return motDePasse;
    }
    public String getPseudo() {
        return pseudo;
    }

    public void setAll(int id,String pseudo,String email,String motDePasse){
        this.id = id;
        this.pseudo = pseudo;
        this.email = email;
        this.motDePasse = motDePasse;
    }
}
