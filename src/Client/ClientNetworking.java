package Client;

import java.io.*;
import java.net.Socket;

/**
 * Created by theeska on 06.06.17.
 */
public class ClientNetworking { // класс для создание сокета для клиента, соединение с сервером и легкого в общения с ним
    BufferedReader reader; // перменная для чтения сообщений с сервера
    PrintWriter writer;// переменная для отправки собщений серверу
    Socket sock;
    static boolean account = false;

    public ClientNetworking(String a, int i) {// соединений с сервером
        try {
            sock = new Socket(a,i);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("Client: networking established");
            account=true;
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void sendFile(File f, Socket sock){
        try {
            BufferedOutputStream os = new BufferedOutputStream(sock.getOutputStream());
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(f));

            int i;
            byte[] pix = new byte[100000];

            while((i = is.read(pix))!=-1){
                os.write(pix, 0, i);
            }
            is.close();
            os.close();
        }
        catch(Exception e){
            System.out.println("Exception in sendFile() in Client");
            e.printStackTrace();
        }
    }


    public void recieveFile(String s,Socket sock){
        try {
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(s));
            BufferedInputStream is = new BufferedInputStream(sock.getInputStream());
            int i;
            byte[] pix = new byte[10000];
            while((i = is.read(pix))!=-1){
                os.write(pix, 0, i);
            }
            is.close();
            os.close();
        }
        catch(Exception e){
            System.out.println("Exception in recieveFile() in Client");
            e.printStackTrace();
        }
    }
}
