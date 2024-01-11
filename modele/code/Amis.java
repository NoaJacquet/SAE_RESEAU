package modele.code;
public class Amis {
    private int suiveur;
    private int suivi;
    public Amis(int idUtilisateur1, int idUtilisateur2){
        this.suiveur = idUtilisateur1;
        this.suivi = idUtilisateur2;
    }
    public int getSuiveur() {
        return suiveur;
    }
    public int getSuivi() {
        return suivi;
    }
}
