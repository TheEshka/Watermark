package Server;

import Client.ClientMainForm;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

/**
 * Created by theeska on 06.06.17.
 */
public class SimpleServer {
    private File passfile = new File("Passwords.txt");

    public void go() {
        String inputMes;
        try {
            ServerSocket serverSock = new ServerSocket(5000);
            while (true) {
                //блокировка сокета, до тех пор пока клиент не подключится
                Socket clientSock = serverSock.accept();
                System.out.println("Server: got a connection");

                //создани потоков для общения сервера с клиентом
                InputStreamReader isReader = new InputStreamReader(clientSock.getInputStream());
                BufferedReader reader = new BufferedReader(isReader);

                PrintWriter writer = new PrintWriter(clientSock.getOutputStream());

                //цикл, получающий запросы от клиента и выполняюший нужные действия, соответсвующие своим запросам
                while ((inputMes = reader.readLine()) != null) {
                    System.out.println("Server: read1 " + inputMes);

                    //запрос на регистрацию
                    if (inputMes.equals("Registration3228/")) {
                        inputMes = reader.readLine();
                        System.out.println("Server: read 2" + inputMes);
                        saveFile(passfile,inputMes);
                        continue;
                    }

                    //запрос на авторизацию
                    if (inputMes.equals("Autorization3228/")) {
                        inputMes = reader.readLine();
                        System.out.println("Server: read 3" + inputMes);
                        Boolean check = checkPas(passfile,inputMes);
                        if (check) {
                            writer.println("access recieved");
                            System.out.println("Server: access recieved");
                            writer.flush();
                        }else{
                            writer.println("access denied");
                            System.out.println("Server: access denied");
                            writer.flush();
                        }
                        continue;
                    }

                    //запрос на наложение водяного знака
                    if (inputMes.equals("Watermarking3228/")){
                        System.out.println("Server: read 4" + inputMes);
                        BufferedImage bi1 = ImageIO.read(clientSock.getInputStream());

                        boolean flag=true;
                        while (flag) {
                            /*inputMes = reader.readLine();
                            System.out.println("Server: read 4" + inputMes);
                            if (inputMes.equals("back")) {
                                break;
                            }*/
                            BufferedImage bi2 = ImageIO.read(clientSock.getInputStream());

                            //Watermark water = new Watermark();

                            if (Watermark.cvzIN(bi1, bi2).equals(bi1)) {
                                writer.println("big image");
                                System.out.println("Server: big image");
                                writer.flush();
                                bi2.flush();
                                bi2=null;
                                continue;
                            } else {
                                writer.println("good image");
                                System.out.println("Server: good image");
                                writer.flush();
                                flag=false;
                            }
                            ImageIO.write(Watermark.cvzIN(bi1, bi2), "bmp", clientSock.getOutputStream());
                            bi1=null;
                            bi2=null;
                            System.out.println("Server: soon1");
                            break;
                        }
                    }

                    //запрос чтоб достать водяной знак
                    if (inputMes.equals("WatermarkingOut3228/")){
                        System.out.println("Server: read 5" + inputMes);
                        BufferedImage bi1 = ImageIO.read(clientSock.getInputStream());

                        //Watermark water = new Watermark();

                        ImageIO.write(Watermark.cvzOUT(bi1), "bmp", clientSock.getOutputStream());
                        bi1=null;

                        System.out.println("Server: soon2");
                        continue;
                    }
                }

            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    //метод для запись получнных регистрацинных данных
    private void saveFile(File file,String lineToParse) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
            writer.append(lineToParse + "\n");
            writer.close();


        } catch (IOException ex) {
            System.out.println("Server: couldn't write the cardList out");
            ex.printStackTrace();
        }
    }

    //метод для проверки авторизационных данных
    private boolean checkPas(File file,String lineToParse) {
        boolean i=false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while (((line = reader.readLine()) != null) & (i==false)) {
                if (line.equals(lineToParse)) {
                    i = true;

                }
            }
            reader.close();
        } catch(Exception ex) {
            System.out.println("Server: couldn't read the card file");
            ex.printStackTrace();
        }
        return i;
    }
}

