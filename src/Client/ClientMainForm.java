package Client;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;


/**
 * Created by theeska on 05.06.17.
 */
public class ClientMainForm extends JFrame{// окно главного меню программы
    // добавление экземпляров элементов, котрые будет содержатся вкне
    private ClientPicturePanel pic = new ClientPicturePanel();
    private JButton cButton = new JButton("Next");
    private JButton tButton = new JButton("Just do it");
    private JButton bButton = new JButton("Back");
    private JButton chButton = new JButton("Chose image");
    private JLabel label = new JLabel("Chose image on which you want to put watermark");
    private JRadioButton inRadio= new JRadioButton ("Insert digital watermarking");
    private JRadioButton outRadio= new JRadioButton ("Get digital watermarking");
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem compareMenuItem = new JMenuItem("Chose image for checking hash");
    //название изображения, которое используются в нтейрфейсе приложения
    private final File mainthem  = new File("mainthem1.jpg");
    private final File mainthem2  = new File("mainthem2.jpg");
    //название файла, куда будет записывать изображение с цвз
    private final File imgWithCvz  = new File("imgWithCvz.bmp");
    //название файла, куда будет записывать извелённое цвз
    private final File imgCvz  = new File("imgCvz.bmp");
    //переменная для хранения изображение
    private BufferedImage bi1 = null;
    //переменная для хранения ихображения
    private BufferedImage bi2 = null;
    // переменна где временно будут хранится путь к файлу
    private File inputFile1;
    private Container container = new Container();
    private Container tcontainer = new Container();
    private ClientNetworking connectMain;
    private boolean flag =true;

    // конструктор класса, в него передаётся объект класса ClientNetworking,для обмена сообщеними с сервером, а также объект класса Jframe для даьнейшего открытия окна авторизация
    public ClientMainForm(JFrame autorizationform,ClientNetworking connect){
        super("Digital Watermarking");
        connectMain=connect;
        this.setBounds(100,100,700,500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //слушатель для стандартной нопки закрытия окна: открывает окно авторизации
        this.addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent event) {}

            public void windowClosed(WindowEvent event) {}

            public void windowClosing(WindowEvent event) {
                if (flag == true) {
                    autorizationform.setVisible(true);
                    autorizationform.repaint();
                    connectMain.writer.println("account exit");
                    connectMain.writer.flush();
                }
            }
            public void windowDeactivated(WindowEvent event) {}

            public void windowDeiconified(WindowEvent event) {}

            public void windowIconified(WindowEvent event) {}

            public void windowOpened(WindowEvent event) {}
        });

        this.getContentPane().add(BorderLayout.SOUTH,container);// контейнер с кнопками внижней части окна
        container.setLayout(new GridLayout(2,2,2,2));

        ButtonGroup radiogroup= new ButtonGroup();
        radiogroup.add(inRadio);
        radiogroup.add(outRadio);
        container.add(inRadio);
        inRadio.setSelected(true);
        container.add(new JPanel());

        container.add(outRadio);

        cButton.addActionListener(new DoWatermarkButton());
        container.add(cButton);
        cButton.setEnabled(false);

        chButton.addActionListener(new OpenMenuListener());
        this.getContentPane().add(BorderLayout.EAST,chButton);

        Font font = new Font("TimesRoman", Font.BOLD,   24);
        label.setFont(font);
        this.getContentPane().add(BorderLayout.NORTH,label);


        //добавление кнропок в тулбар и установка слушателей на кнопки
        compareMenuItem.addActionListener(new CompareMenuListener());
        fileMenu.add(compareMenuItem);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);

        pic.setImageFile(mainthem);
        this.getContentPane().add(BorderLayout.CENTER,pic);// загруженное изображение по центру окна

        tButton.addActionListener(new TDoWatermarkButton());
        bButton.addActionListener(new BackButton());
        tcontainer.setLayout(new GridLayout(1,2,2,2));
        tcontainer.add(bButton);
        tcontainer.add(tButton);
        tButton.setEnabled(false);
    }

    //слушатель дя кнопки из тулбара, дважды запускает окно выбора файла, сранивает изображения и выдаёт результат
    public class CompareMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(ClientMainForm.this);
            inputFile1 = fileOpen.getSelectedFile();

            if (inputFile1 == null) {
                return;
            }

            byte[] imageArrayByte1 = null;

            BufferedImage bit = null;
            try {
                bit = ImageIO.read(inputFile1);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bit, "bmp", baos);
                baos.flush();
                imageArrayByte1 = baos.toByteArray();
                baos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            inputFile1 = null;
            fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(ClientMainForm.this);
            inputFile1 = fileOpen.getSelectedFile();

            if (inputFile1 == null) {
                return;
            }

            byte[] imageArrayByte2 = null;
            try {
                bit = ImageIO.read(inputFile1);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bit, "bmp", baos);
                baos.flush();
                imageArrayByte2 = baos.toByteArray();
                baos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (imageArrayByte1.length != imageArrayByte2.length) {
                JOptionPane.showMessageDialog(null, "Digital watermark is different", "Watermark Master", JOptionPane.ERROR_MESSAGE);
                return;
            }


            int length = imageArrayByte1.length;

            for (int i = 0; i < length; i++) {
                if (imageArrayByte1[i] != imageArrayByte2[i]) {
                    JOptionPane.showMessageDialog(null, "Digital watermark is different", "Watermark Master", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            JOptionPane.showMessageDialog(null, "Digital watermark are identical", "Watermark Master", JOptionPane.PLAIN_MESSAGE);
        }
    }

    //слушатель на кнопку, для загрузки первого изображения, на которое будет накладываться цвз
    public class OpenMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(ClientMainForm.this);
            inputFile1 = fileOpen.getSelectedFile();
            if (inputFile1==null){return;}
            pic.setImageFile(inputFile1);
            pic.setImage(pic.getImage());
            try {
                ClientMainForm.this.bi1 = ImageIO.read(inputFile1);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            ClientMainForm.this.cButton.setEnabled(true);
            if (flag==false){ClientMainForm.this.tButton.setEnabled(true);}
        }
    }

    //слушатель на кнопке для отправк первого изображени,меняет окно для загрузки второго изображения(самого цвз)
    public class DoWatermarkButton implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            if (inRadio.isSelected()==true) {
                flag=false;
                //sendImageIn1();
                JOptionPane.showMessageDialog(null, "Now, enter watermark picture", "Watermark Master", JOptionPane.PLAIN_MESSAGE);

                ClientMainForm.this.getContentPane().remove(container);
                ClientMainForm.this.getContentPane().add(BorderLayout.SOUTH,tcontainer);
                ClientMainForm.this.repaint();
                label.setText("Now enter watermark image");

                JFileChooser fileOpen = new JFileChooser();
                fileOpen.showOpenDialog(ClientMainForm.this);
                inputFile1 = fileOpen.getSelectedFile();
                while (inputFile1 == null) {
                    continue;
                }
                ClientMainForm.this.tButton.setEnabled(true);
                ClientMainForm.this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

                pic.setImageFile(inputFile1);
                pic.setImage(pic.getImage());
                try {
                    ClientMainForm.this.bi2 = ImageIO.read(inputFile1);
                } catch (IOException ex) {
                    ex.printStackTrace();

                }
            }

            if (outRadio.isSelected()==true) {
                sendImageOut();
                try{
                    bi1=null;
                    BufferedImage bi1 = ImageIO.read(connectMain.sock.getInputStream());
                    ImageIO.write(bi1,"bmp", imgCvz);
                    bi1.flush();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"There are not watermark on this image","Watermark Master",JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                    return;
                }
                JOptionPane.showMessageDialog(null,"Watermark is out.\n File name is imgCVZ.bmp","Watermark Master",JOptionPane.PLAIN_MESSAGE);
                connectMain.writer.println("compleated");
                connectMain.writer.flush();
                pic.setImageFile(mainthem);
                pic.setImage(pic.getImage());
                bi1=null;
                bi2=null;
            }

        }


        public void sendImageIn1() {
            try {
                connectMain.writer.println("Watermarking3228/");
                connectMain.writer.flush();
                ImageIO.write(ClientMainForm.this.bi1, "bmp", ClientMainForm.this.connectMain.sock.getOutputStream());
                bi1.flush();
                bi1=null;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void sendImageOut() {
            try {
                connectMain.writer.println("WatermarkingOut3228/");
                connectMain.writer.flush();
                ImageIO.write(ClientMainForm.this.bi1, "bmp", ClientMainForm.this.connectMain.sock.getOutputStream());
                bi1.flush();
                bi1=null;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }



    //слушатель для кнопки, отправка второго изображения, проверка,подходит ли по размеру,в случае удачи отправляет и принимает изображение с ЦВЗ? иначе выоит ошибку и возвращается к выбору второго изображения
    public class TDoWatermarkButton implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            //connectMain.writer.println("not back");
            //connectMain.writer.flush();
            sendImageIn1();
            sendImageIn2();
            try {
                String line = new String();
                line=connectMain.reader.readLine();
                if (line.equals("good image")){
                    bi2=null;
                    bi2 = ImageIO.read(connectMain.sock.getInputStream());
                    ImageIO.write(bi2, "bmp", imgWithCvz);
                    bi2.flush();
                    JOptionPane.showMessageDialog(null, "Digital watermark is ready.\n File name is imdVithCvz.bmp", "Watermark Master", JOptionPane.PLAIN_MESSAGE);
                    ClientMainForm.this.getContentPane().remove(tcontainer);
                    ClientMainForm.this.getContentPane().add(BorderLayout.SOUTH,container);
                    ClientMainForm.this.repaint();
                    ClientMainForm.this.setVisible(true);
                    pic.setImageFile(mainthem);
                    pic.setImage(pic.getImage());
                    ClientMainForm.this.cButton.setEnabled(false);
                    ClientMainForm.this.flag=true;
                    ClientMainForm.this.tButton.setEnabled(false);
                    ClientMainForm.this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

                    label.setText("Chose image on which you want to put watermark");
                    flag=true;
                    connectMain.writer.println("compleated");
                    connectMain.writer.flush();
                    bi1=null;
                    bi2=null;
                }else {
                    JOptionPane.showMessageDialog(null, "Digital watermark image is so big \n Chose another image", "Watermark Master", JOptionPane.ERROR_MESSAGE);
                    ClientMainForm.this.tButton.setEnabled(false);
                    bi1=null;
                    pic.setImageFile(mainthem2);
                    pic.setImage(pic.getImage());
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void sendImageIn1() {
            try {
                connectMain.writer.println("Watermarking3228/");
                connectMain.writer.flush();
                ImageIO.write(ClientMainForm.this.bi1, "bmp", ClientMainForm.this.connectMain.sock.getOutputStream());
                bi1.flush();
                bi1=null;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void sendImageIn2() {
            try {
                ImageIO.write(ClientMainForm.this.bi2, "bmp", ClientMainForm.this.connectMain.sock.getOutputStream());
                bi2.flush();
                bi2=null;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    //слушатель для кнопки назад, возвращается окно от этапа отправки второго зображеня в изначальное состояние
    public class BackButton implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            connectMain.writer.println("back");
            connectMain.writer.flush();

            ClientMainForm.this.getContentPane().remove(tcontainer);
            ClientMainForm.this.getContentPane().add(BorderLayout.SOUTH,container);
            ClientMainForm.this.repaint();
            ClientMainForm.this.setVisible(true);
            pic.setImageFile(mainthem);
            pic.setImage(pic.getImage());
            ClientMainForm.this.cButton.setEnabled(false);
            ClientMainForm.this.flag=true;
            ClientMainForm.this.tButton.setEnabled(false);
            ClientMainForm.this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            label.setText("Chose image on which you want to put watermark");
            flag=true;
            bi1=null;
            bi2=null;
        }
    }

}
