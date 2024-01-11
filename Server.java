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

public class Server{
    private static final int PORT = 5555;
    private static Map<String, PrintWriter> clients = new HashMap<>();
    private static ClientHandler clientHandler;

    public static void main(String[] args) {
        // ServAffichage.main(args);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur en attente de connexions...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket,clients).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void creerClientHandler(Socket clientSocket, Map<String, PrintWriter> clients, ClientHandler clientHandler){
        clientHandler = new ClientHandler(clientSocket,clients);
        clientHandler.start();
    }

}
