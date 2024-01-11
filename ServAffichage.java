import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ServAffichage extends Application{

    private Scene scene;
    private static Map<String, PrintWriter> clients = new HashMap<>();
    private ClientHandler clientHandler;

    public static void main(String[] args) {
        launch(ServAffichage.class, args);
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        this.scene = new Scene(root, 900, 700);
        stage.setScene(scene);
        stage.setTitle("Server");
        stage.show();
        System.out.println("Affichage");
        listeConnecte(root);
        try (ServerSocket serverSocket = new ServerSocket(5555)){
            Socket clientSocket = serverSocket.accept();
            clientHandler = new ClientHandler(clientSocket,clients);
            clientHandler.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listeConnecte(BorderPane BorderPane){
        while (true) {
            System.out.println("true2");
            if (clientHandler != null) {
                System.out.println("pas null");
                clients = clientHandler.getClients();
                TextArea text = new TextArea();
                for (String pseudo : clients.keySet()){
                    text.setText(pseudo);
                }
                BorderPane.setLeft(text);
                this.scene.setRoot(BorderPane);
            }
        }
    }
}
