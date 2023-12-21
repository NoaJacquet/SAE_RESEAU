import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5557;

        try {
            Socket socket = new Socket(host, port);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (true) {
                System.out.print("Donne-moi une chaîne à envoyer au serveur : ");
                BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
                String userInput = userInputReader.readLine();

                writer.write(userInput + "\n");
                writer.flush();

                System.out.println(reader.readLine());

                if (userInput.equals("quit")) {
                    System.out.println(reader.readLine());
                    reader.close();
                    writer.close();
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    socket.close();
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
