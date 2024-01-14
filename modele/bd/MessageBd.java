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
            String sqlQuery = "SELECT M.id_M, M.id_U, M.contenu, M.date,U.pseudo FROM MESSAGES M NATURAL JOIN UTILISATEUR U LEFT JOIN AMIS A ON U.id_U = A.suivi WHERE U.pseudo = ? OR A.suivi = ? ORDER BY M.date"; 

            PreparedStatement ps = Main.getInstance().getSqlConnect().prepareStatement(sqlQuery);
            ps.setString(1, pseudo);
            ps.setString(2, pseudo);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                messages.add(new Message(rs.getInt("id_M"), rs.getInt("id_U"), rs.getString("contenu"), rs.getString("date"), rs.getString("pseudo")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Tri de la liste par date dans l'ordre croissant
        Collections.sort(messages, (msg1, msg2) -> msg1.getDate().compareTo(msg2.getDate()));

        return messages;
    }
}
