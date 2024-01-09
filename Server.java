import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Server extends Application{
    private static final int PORT = 5555;
    private static Map<String, PrintWriter> clients = new HashMap<>();
    private Scene scene;
    private static ClientHandler clientHandler;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur en attente de connexions...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket,clients);
                launch(Server.class, args);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        this.scene = new Scene(root, 900, 700);
        stage.setScene(scene);
        stage.setTitle("Server");
        stage.show();
        System.out.println("Affichage");
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

    public static void creerClientHandler(Socket clientSocket, Map<String, PrintWriter> clients, ClientHandler clientHandler){
        clientHandler = new ClientHandler(clientSocket,clients);
        clientHandler.start();
    }

}
