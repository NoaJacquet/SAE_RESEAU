package modele.bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modele.code.Message;
import src.Main;

public class MessageBd {
        private MessageBd() {}


    public static void ajouteMessage(String pseudo, String contenu) throws ClassNotFoundException{
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("INSERT INTO MESSAGES  VALUES (?, (SELECT id_U FROM UTILISATEUR WHERE pseudo =?),?,?)");
            ps.setInt(1, prochainId());
            ps.setString(2, pseudo);
            ps.setString(3, contenu);
            LocalDateTime now = LocalDateTime.now();

            // Définissez le format de la date que vous souhaitez
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // Formatez la date en chaîne de caractères selon le format spécifié
            String formattedDateTime = now.format(formatter);
            ps.setString(4, formattedDateTime);
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    private static int prochainId() throws ClassNotFoundException{
        try{
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("SELECT MAX(id_M) FROM MESSAGES");
            ResultSet rs = ps.executeQuery();
            int id=0;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            return id+1;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public static List<Message> recupererLesMessageDeTousSesAmisDansOrdreDate(String pseudo) throws ClassNotFoundException {
        List<Message> messages = new ArrayList<>();

        try {
            // Requête SQL avec LEFT JOIN pour récupérer les messages des amis
            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement("select id_M,id_U,contenu,date,pseudo from MESSAGES natural join UTILISATEUR where pseudo=?");

            PreparedStatement ps2 = Main.getInstance().getSqlConnect().prepareStatement("select id_M,id_U,contenu,date,pseudo from MESSAGES natural join UTILISATEUR join AMIS on AMIS.suiveur= UTILISATEUR.id_U where AMIS.suivi in (select id_U from UTILISATEUR where pseudo=?)");
            ps.setString(1, pseudo);
            ps2.setString(1, pseudo);
            ResultSet rs = ps.executeQuery();
            ResultSet rs2 = ps2.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5));
                messages.add(message);
                
            }
            while (rs2.next()) {
                Message message = new Message(rs2.getInt(1), rs2.getInt(2), rs2.getString(3), rs2.getString(4), rs2.getString(5));
                messages.add(message);
                
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        // Tri de la liste par date dans l'ordre croissant
        Collections.sort(messages, (msg1, msg2) -> msg1.getDate().compareTo(msg2.getDate()));

        return messages;
    }
}
