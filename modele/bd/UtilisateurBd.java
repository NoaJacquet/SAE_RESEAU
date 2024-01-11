package modele.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import src.Client;
import java.util.ArrayList;
import java.util.List;
import modele.code.Utilisateur;

public class UtilisateurBd {
    private UtilisateurBd() {}
    public static List<Utilisateur> AllUtilisateur(){
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try{
            PreparedStatement ps = Client.getSQlConnect().prepareStatement("SELECT * FROM UTILISATEUR");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                utilisateurs.add(new Utilisateur(rs.getInt("id"), rs.getString("pseudo"), rs.getString("mot_de_passe")));
            }
            return utilisateurs;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
