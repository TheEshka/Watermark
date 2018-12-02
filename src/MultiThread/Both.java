package MultiThread;

import Client.MainClient;
import Server.MainServer;

/**
 * Created by theeska on 12.06.17.
 */
public class Both {
    public static void main(String[] args)
    {
        MainClient s = new MainClient();
        MainServer c = new MainServer();
        //new Thread(s).start();
        //new Thread(c).start();
    }
}
