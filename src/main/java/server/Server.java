package server;

import com.github.OMEN44.simpleSQL.connectors.Connector;
import com.github.OMEN44.simpleSQL.connectors.Datatype;
import com.github.OMEN44.simpleSQL.connectors.dbProfiles.MySQL;
import com.github.OMEN44.simpleSQL.entities.column.CreateColumn;
import com.github.OMEN44.simpleSQL.entities.column.ForeignKey;
import com.github.OMEN44.simpleSQL.entities.column.PrimaryKey;
import com.github.OMEN44.simpleSQL.entities.column.UniqueColumn;
import com.github.OMEN44.simpleSQL.entities.table.Table;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        MySQL mySQL = new MySQL(
                3306,
                "omenchat",
                "localhost",
                "root",
                ""
        );
        Connector conn = new Connector(mySQL);
        conn.test();
        //setup database
        Table.create(
                "users",
                new PrimaryKey("userID", Datatype.INT),
                new UniqueColumn("user", Datatype.VARCHAR),
                new CreateColumn("ip", Datatype.VARCHAR)
        ).writeToDatabase(conn);
        Table.create(
                "messages",
                new PrimaryKey("messageID", Datatype.INT),
                new CreateColumn("content", Datatype.VARCHAR),
                new ForeignKey("senderID", Datatype.INT, "userID", "users")
        ).writeToDatabase(conn);

        new Server(new ServerSocket(4444)).startServer();
    }

    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                //accept() pauses the server until a new user connects
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);

                // then this class is initialized in a new thread
                // allowing it to run wile the server is waiting for more connections
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            //closes server sockets and prints the error if unable to close
            try {
                serverSocket.close();
            } catch (IOException ex){
                e.printStackTrace();
            }
        }
    }
}

