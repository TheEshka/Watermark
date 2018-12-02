package Server;

/**
 * Created by theeska on 07.06.17.
 */
public class MainServer /*implements Runnable*/{

    public static void main(String[] args) {
        SimpleServer server = new SimpleServer();
        server.go();
    }


    /*public void run() {
        SimpleServer server = new SimpleServer();
        server.go();
    }*/
}
