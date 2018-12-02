package Client;

/**
 * Created by theeska on 05.06.17.
 */
public class MainClient /*implements Runnable*/{
    public static void main(String[] args) {
        ClientNetworking clientConnect = new ClientNetworking("127.0.0.1",5000);
        ClientAutorizationForm form1 = new ClientAutorizationForm(clientConnect);
        form1.setVisible(true);
    }

    /*public void run(){
        ClientNetworking clientConnect = new ClientNetworking("127.0.0.1",5000);
        ClientAutorizationForm form1 = new ClientAutorizationForm(clientConnect);
        form1.setVisible(true);
    }*/
}
