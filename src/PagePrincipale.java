package src;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PagePrincipale {
    private Stage stage;
    
    private Client client;
    private VBox listeNonAmis = new VBox();
    private VBox Amis = new VBox();
    private static TextArea messageArea = new TextArea();
    

    public PagePrincipale(Stage stage,Client client) {
        this.stage = stage;
        this.client=client;
        stage.setTitle("Page principale");
        this.listeNonAmis.getChildren().addAll(new Label("Non amis"));
        Amis.getChildren().addAll(new Label("amis"));
        messageArea.setEditable(false);
    }


    private HBox createFriendDisplay(String friendName, Client client, boolean isAmi) {
            HBox interieur = new HBox();
            Label label = new Label(friendName);
            Button button = new Button(isAmi ? "Supprimer" : "Ajouter");

            button.setOnAction(e -> {
                try {
                    handleFriendAction(friendName, client, isAmi);
                    updateFriendDisplay(interieur, friendName, client, !isAmi);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            });

            interieur.getChildren().addAll(label, button);
            return interieur;
        }

        private void handleFriendAction(String friendName, Client client, boolean isAmi) throws ClassNotFoundException {
            if (isAmi) {
                client.supprimeAmis(friendName);
            } else {
                client.ajouteAmis(friendName);
            }
        }
    
        private void updateFriendDisplay(HBox container, String friendName, Client client, boolean isAmi) {
            container.getChildren().clear();
            Label label = new Label(friendName);
            Button button = new Button();
            
            if (isAmi) {
                button.setText("Supprimer");
                button.setOnAction(e -> {
                    try {
                        handleFriendAction(friendName, client, true);
                        updateFriendDisplay(container, friendName, client, false);
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                });
                Amis.getChildren().add(container);
                listeNonAmis.getChildren().remove(container);
            } else {
                button.setText("Ajouter");
                button.setOnAction(e -> {
                    try {
                        handleFriendAction(friendName, client, false);
                        updateFriendDisplay(container, friendName, client, true);
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                });
                listeNonAmis.getChildren().add(container);
                Amis.getChildren().remove(container);
            }
    
            container.getChildren().addAll(label, button);
        }
    

    public void show() throws ClassNotFoundException {
        BorderPane borderPane = new BorderPane();
        // liste non amis
        // Affichage des non-amis
        for (String string : client.getNonAmis()) {
            HBox interieur = createFriendDisplay(string, client, false);
            listeNonAmis.getChildren().add(interieur);
        }
        borderPane.setLeft(listeNonAmis);

        // Affichage des amis
        for (String string : client.getAmis()) {
            HBox interieur = createFriendDisplay(string, client, true);
            Amis.getChildren().add(interieur);
        }
        borderPane.setRight(Amis);


        // Panel en bas pour écrire des messages
        TextField messageInput = new TextField();
        Button sendButton = new Button("Envoyer");
        sendButton.setOnAction(e -> {
            String message = messageInput.getText();
            client.sendMessage(message);
            messageInput.clear();
        });
        HBox messageBox = new HBox(messageInput, sendButton);
        messageBox.setSpacing(10);
        messageBox.setPadding(new Insets(10));
        borderPane.setBottom(messageBox);

        // Panel au centre avec la zone des messages
        
        borderPane.setCenter(messageArea);

        Scene scene = new Scene(borderPane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
    
    public static void afficheMessage(String message){
        messageArea.setText(message + "\n" + messageArea.getText());
    }
}
