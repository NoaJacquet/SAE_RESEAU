package src;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.stage.Stage;
import modele.bd.ConnexionBd;
import modele.code.Utilisateur;

public class Main extends Application{
    private ConnexionBd sqlConnect;
    private static Main instance;
    private Utilisateur utilisateurBd;

    public static Main getInstance(){
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

    public ConnexionBd getSqlConnect() throws ClassNotFoundException {
        try {
            if (sqlConnect == null) {
                sqlConnect = new ConnexionBd();
                sqlConnect.connect("localhost", "SAE_RESEAUX", "temha", "temha1011");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.sqlConnect;
    }

    @Override
    public void init () throws Exception{
        instance = this;
        this.utilisateurBd = null;
        try {
            if (sqlConnect == null) {
                sqlConnect = new ConnexionBd();
                sqlConnect.connect("localhost", "SAE_RESEAUX", "temha", "temha1011");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Utilisateur getUtilisateurBd() {
        return utilisateurBd;
    }
    public void setUtilisateurBd(Utilisateur utilisateurBd) {
        this.utilisateurBd = utilisateurBd;
    }


    public static void main(String[] args) {

        // Lancer l'application JavaFX
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // La page de connexion est gérée dans la classe LoginPage
        LoginPage loginPage = new LoginPage(primaryStage);
        loginPage.show();
    }
}


// "lib/**/*.jar",
// "/home/baba/Téléchargements/openjfx-21.0.1_linux-x64_bin-sdk/*",


// , "java.jdt.ls.vmargs": "--module-path /home/baba/Téléchargements/openjfx-21.0.1_linux-x64_bin-sdk/javafx-sdk-21.0.1/lib --add-modules javafx.controls,javafx.fxml"