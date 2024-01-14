package modele.bd;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnexionBd {
    
    private Connection connection;
	private boolean isConnected;
	
    public ConnexionBd() throws ClassNotFoundException{
        this.connection = null;
        this.isConnected = false;
	}

	public void connect(String nomServeur, String nomBase, String nomLogin, String motDePasse) throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection("jdbc:mysql://"+nomServeur+":3306/"+nomBase,nomLogin,motDePasse);
		} catch ( SQLException ex ) {
			System.out.println("Msg : " + ex.getMessage() + "   " + ex.getErrorCode());
		}
		catch(ClassNotFoundException ex){
			System.out.println("Msg : " + ex.getMessage());
		}
		this.isConnected = this.connection != null;
	}

	public void close() throws SQLException {
        this.connection.close();
        this.connection = null;
        this.isConnected = false;
	}

    public boolean isConnected() { 
        return this.isConnected;
    }
    
	public Statement createStatement() throws SQLException {
		return this.connection.createStatement();
	}

	public PreparedStatement prepareStatement(String requete) throws SQLException{
		return this.connection.prepareStatement(requete);
	}
}

