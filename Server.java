import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
    private int counter = 0;

    public static void main(String[] args) {
        Server server = new Server();
        server.mainServer(5557);
    }

    private void mainServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Session session = new Session(this, clientSocket);
                session.mainSession();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCounter() {
        return counter;
    }

    public void incrementCounter() {
        counter++;
    }

    public void decrementCounter() {
        counter--;
    }

    public void setCounter(int value) {
        counter = value;
    }

    public void addToCounter(int value) {
        counter += value;
    }
}

class Session {
    private Server server;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public Session(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mainSession() {
        try {
            while (true) {
                String line = reader.readLine().trim();
                if (line.equals("quit")) {
                    writer.write("la session va s'eteindre\n");
                    writer.flush();
                    break;
                } else if (line.equals("incr")) {
                    server.incrementCounter();
                    writer.write("le compteur est maintenant à : " + server.getCounter() + "\n");
                    writer.flush();
                } else if (line.equals("decr")) {
                    server.decrementCounter();
                    writer.write("le compteur est maintenant à : " + server.getCounter() + "\n");
                    writer.flush();
                } else if (line.equals("get")) {
                    writer.write("le compteur est maintenant à : " + server.getCounter() + "\n");
                    writer.flush();
                } else if (line.startsWith("add")) {
                    int value = Integer.parseInt(line.split(" ")[1]);
                    server.addToCounter(value);
                    writer.write("le compteur est maintenant à : " + server.getCounter() + "\n");
                    writer.flush();
                } else {
                    writer.write("j'ai bien reçu ton message : " + line + "\n");
                    writer.flush();
                }
            }
            reader.close();
            writer.close();
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
