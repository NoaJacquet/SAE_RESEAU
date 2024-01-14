package modele.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import src.Main;
import java.util.ArrayList;
import java.util.List;
import modele.code.Utilisateur;

public class AmisBd {
    private AmisBd() {}
    public static List<Utilisateur> ListeAmis(String pseudo) throws ClassNotFoundException{
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM AMIS join UTILISATEUR on AMIS.suivi = UTILISATEUR.id_U where suiveur = (select id_U from UTILISATEUR where pseudo=?)");
            ps.setString(1, pseudo);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                utilisateurs.add(new Utilisateur(rs.getInt("id_U"), rs.getString("pseudo"),rs.getString("email"),rs.getString("mdp")));
            }
            return utilisateurs;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<Utilisateur> ListeNonAmis(String pseudo) throws ClassNotFoundException{
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR WHERE id_U <> (SELECT id_U FROM UTILISATEUR WHERE pseudo = ?) AND id_U NOT IN (SELECT suivi FROM AMIS WHERE suiveur = (SELECT id_U FROM UTILISATEUR WHERE pseudo = ?)) AND id_U <> (SELECT id_U FROM UTILISATEUR WHERE pseudo = ?)");
            ps.setString(1, pseudo);
            ps.setString(2, pseudo);
            ps.setString(3, pseudo);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                utilisateurs.add(new Utilisateur(rs.getInt("id_U"), rs.getString("pseudo"),rs.getString("email"),rs.getString("mdp")));
            }
            return utilisateurs;
        }
        
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    public static void ajouteAmis(String pseudo, String pseudoAmis) throws ClassNotFoundException{
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("INSERT INTO AMIS (suiveur, suivi) VALUES ((SELECT id_U FROM UTILISATEUR WHERE pseudo = ?), (SELECT id_U FROM UTILISATEUR WHERE pseudo = ?))");
            ps.setString(1, pseudo);
            ps.setString(2, pseudoAmis);
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void supprimeAmis(String pseudo, String pseudoAmis) throws ClassNotFoundException{
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("DELETE FROM AMIS WHERE suiveur = (SELECT id_U FROM UTILISATEUR WHERE pseudo = ?) AND suivi = (SELECT id_U FROM UTILISATEUR WHERE pseudo = ?)");
            ps.setString(1, pseudo);
            ps.setString(2, pseudoAmis);
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
