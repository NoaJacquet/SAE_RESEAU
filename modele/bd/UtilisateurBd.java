package modele.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import src.Main;
import java.util.ArrayList;
import java.util.List;
import modele.code.Utilisateur;

/**
 * La classe UtilisateurBd gère les opérations liées à la manipulation des utilisateurs dans la base de données.
 */
public class UtilisateurBd {
    private UtilisateurBd() {}

    /**
     * Récupère la liste de tous les utilisateurs présents dans la base de données.
     *
     * @return Une liste d'objets Utilisateur représentant tous les utilisateurs.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static List<Utilisateur> AllUtilisateur() throws ClassNotFoundException{
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR");
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

    /**
     * Récupère un utilisateur à partir de son pseudo et mot de passe.
     *
     * @param pseudo   Le pseudo de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     * @return Un objet Utilisateur si les informations sont valides, sinon null.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static Utilisateur getUtilisateur(String pseudo, String password) throws ClassNotFoundException{
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR WHERE pseudo = ? AND mdp = ?");
            ps.setString(1, pseudo);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Utilisateur(rs.getInt("id_U"), rs.getString("pseudo"),rs.getString("email"),rs.getString("mdp"));
            }
            else{
                return null;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }



    /**
     * Récupère un utilisateur à partir de son pseudo.
     *
     * @param pseudo Le pseudo de l'utilisateur.
     * @return Un objet Utilisateur si le pseudo est trouvé, sinon null.
     * @throws ClassNotFoundException Si la classe n'est pas trouvée lors de l'accès à la base de données.
     */
    public static Utilisateur recupererUtilisateur(String pseudo) throws ClassNotFoundException{
        try {
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT * FROM UTILISATEUR WHERE pseudo = ?");
            ps.setString(1, pseudo);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Utilisateur(rs.getInt("id_U"), rs.getString("pseudo"),rs.getString("email"),rs.getString("mdp"));
            }
            else{
                return null;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
