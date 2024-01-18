package modele.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modele.code.Utilisateur;
import src.Main;

public class LikeBd {
    private LikeBd() {}
    public static int countLikeToMessage(int idMessage) throws ClassNotFoundException{
        int cpt=0;
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT count(*) FROM LIKES where id_M = ?");
            ps.setInt(1, idMessage);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                cpt = rs.getInt(1);
            }
            return cpt;
        }
        catch(SQLException e){
            e.printStackTrace();
            return cpt;
        }
    }

    public static int countDisikeToMessage(int idMessage) throws ClassNotFoundException{
        int cpt=0;
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT count(*) FROM DISLIKES where id_M = ?");
            ps.setInt(1, idMessage);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                cpt = rs.getInt(1);
            }
            return cpt;
        }
        catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public static void ajouteLike(int id, String pseudoClient) throws ClassNotFoundException{
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("INSERT INTO LIKES VALUES (?,(SELECT id_U FROM UTILISATEUR WHERE pseudo =?))");
            ps.setInt(1, id);
            ps.setString(2, pseudoClient);
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static int countLikeToMessageByUser(int idMessage,int idUser) throws ClassNotFoundException{
        int cpt=0;
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT count(*) FROM LIKES where id_M = ? and id_U = ?");
            ps.setInt(1, idMessage);
            ps.setInt(2, idUser);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                cpt = rs.getInt(1);
            }
            return cpt;
        }
        catch(SQLException e){
            e.printStackTrace();
            return cpt;
        }
    }
}