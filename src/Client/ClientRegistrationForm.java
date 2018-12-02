package Client;

/**
 * Created by theeska on 06.06.17.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

//класс окна регистрации, наследует стандартное окно JFrame
public class ClientRegistrationForm extends JFrame{
    private JButton rButton = new JButton("Registrate");
    private JLabel rLabel = new JLabel("Use only Latin letters and numbers");
    private JLabel lLabel = new JLabel("Login");
    private JLabel p1Label = new JLabel("Password");
    private JLabel p2Label = new JLabel("Password again");
    private JTextField lField = new JTextField("",5);
    private JTextField p1Field = new JTextField("",5);
    private JTextField p2Field = new JTextField("",5);
    private JPanel empty = new JPanel();
    private ClientNetworking connectAutorization;
    JFrame autorFrame;

    // конструктор класса, в него передаётся объект класса ClientNetworking,для обмена сообщеними с сервером, а также объект класса Jframe для даьнейшего открытия окна авторизация
    public ClientRegistrationForm(ClientNetworking connect, JFrame autorizationform){
        super("Registration");
        autorFrame=autorizationform;
        this.setBounds(100,100,300,150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //слушатель класса, на кнпку стандартного закрыти окна, открывает окно авторизаци
        this.addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent event) {}

            public void windowClosed(WindowEvent event) {}

            public void windowClosing(WindowEvent event) {
                autorizationform.setVisible(true);
                autorizationform.repaint();
            }

            public void windowDeactivated(WindowEvent event) {}

            public void windowDeiconified(WindowEvent event) {}

            public void windowIconified(WindowEvent event) {}

            public void windowOpened(WindowEvent event) {}

        });

        // контекйнер в который добавляются все элементы в таблицу 4*2
        Container container = new Container();
        this.getContentPane().add(BorderLayout.CENTER,container);
        this.getContentPane().add(BorderLayout.NORTH,rLabel);
        container.setLayout(new GridLayout(4,2,2,2));
        container.add(lLabel);
        container.add(lField);
        container.add(p1Label);
        container.add(p1Field);
        container.add(p2Label);
        container.add(p2Field);
        container.add(empty);
        rButton.addActionListener(new RegistratedButton());//добавление слушател на кнопкурегистрации
        container.add(rButton);
        connectAutorization = connect;

    }

    class RegistratedButton implements ActionListener{//слушатель кнопки регистраци
        // если пароли совпадают,отправляет регистрационные данные на сервер изакрывает окно,иначе выводит ошибку исирает поля паролей
        public void actionPerformed(ActionEvent ev){
            if((p1Field.getText().equals(p2Field.getText())) & (p1Field.getText().length()!=0) & (lField.getText().length()!=0)){
                sendRegistrationData(ClientRegistrationForm.this.connectAutorization);
            } else{
                IncorrectPasswordFormat();
            }
        }

        private void sendRegistrationData(ClientNetworking connect) {
            try {
                connect.writer.println("Registration3228/");
                connect.writer.flush();
                String message = ClientRegistrationForm.this.lField.getText() + "/" + ClientRegistrationForm.this.p1Field.getText();
                connect.writer.println(message);
                connect.writer.flush();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }

            ClientRegistrationForm.this.dispose();
            ClientRegistrationForm.this.autorFrame.setVisible(true);
        }

        private void IncorrectPasswordFormat(){
            String message="Passwords do not match\n";
            message += "Try again!\n";
            JOptionPane.showMessageDialog(null,message,"Error",JOptionPane.ERROR_MESSAGE);
            ClientRegistrationForm.this.p1Field.setText("");
            ClientRegistrationForm.this.p2Field.setText("");
        }
    }



}
