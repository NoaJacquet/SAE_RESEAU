package modele.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import src.Main;
import java.util.ArrayList;
import java.util.List;
import modele.code.Utilisateur;

public class UtilisateurBd {
    private UtilisateurBd() {}
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
