package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Created by theeska on 05.06.17.
 */
public class ClientAutorizationForm extends JFrame{
    //окно авторизации, которое наследует обычное окно Jframe и добавляем все элементы в глобальные переменные
    private JButton cButton = new JButton("Sign in");
    private JButton rButton = new JButton("Registration");
    private JLabel lLabel = new JLabel("Login");
    private JLabel pLabel = new JLabel("Password");
    private JTextField lField = new JTextField("",5);
    private JTextField pField = new JTextField("",5);
    // объект класса в котором прописаны переменные для лягкого обмена сообщениями с сервером
    private ClientNetworking connectAutorization;

    //консруктор класса, передаётся объект класса Client Networking, в котором содержатся переменные для обмена текстовыми сообениями с сервером
    public ClientAutorizationForm(ClientNetworking connect){
        super("Autorization");
        this.setBounds(100,100,250,100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //слушатель кнопки стандартного закрытия окна, закрывает сокет
        this.addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent event) {}

            public void windowClosed(WindowEvent event) {}

            public void windowClosing(WindowEvent event) {
                try{connectAutorization.sock.close();}catch(Exception ex){ex.getStackTrace();}
            }

            public void windowDeactivated(WindowEvent event) {}

            public void windowDeiconified(WindowEvent event) {}

            public void windowIconified(WindowEvent event) {}

            public void windowOpened(WindowEvent event) {}

        });


        // далее создатя контейнер в котором распологаются кнопки в таблице 3*2 и к кнопком добавлется "слушатель"
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3,2,2,2));
        container.add(lLabel);
        container.add(lField);
        container.add(pLabel);
        container.add(pField);
        rButton.addActionListener(new RegistrationButton());
        container.add(rButton);
        cButton.addActionListener(new SignInButton());
        container.add(cButton);
        cButton.requestFocusInWindow();
        connectAutorization=connect;
    }


    // создание класса который является слушателем дл кнопки Sign In
    class SignInButton implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            sendAutorizationData(ClientAutorizationForm.this.connectAutorization);
        }

        private void sendAutorizationData(ClientNetworking connect) {
            try {
                connect.writer.println("Autorization3228/");//отправка запроса на авторизацию
                connect.writer.flush();
                String message = ClientAutorizationForm.this.lField.getText() + "/" + ClientAutorizationForm.this.pField.getText();
                connect.writer.println(message);// отправка аворизационных данных
                connect.writer.flush();
                if (connect.reader.readLine().equals("access recieved")){//проверка ответа сервера
                    ClientAutorizationForm.this.setVisible(false);
                    ClientMainForm mainForm = new ClientMainForm(ClientAutorizationForm.this,ClientAutorizationForm.this.connectAutorization);
                    mainForm.setVisible(true);
                }else {
                    IncorrectPasswordFormat();//метод выполняющиймя при отказе доступа
                }
            }catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }


        private void IncorrectPasswordFormat(){ // метод выводит окно ошибки и чистит поле пароля
            String message="Not correct login/password\n";
            message += "Try again!\n";
            JOptionPane.showMessageDialog(null,message,"Error",JOptionPane.ERROR_MESSAGE);
            ClientAutorizationForm.this.pField.setText("");
        }
    }

    //слушатель кнопк регистрация, скрывает окно авторизации и открывает окно регистрации
    class RegistrationButton implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            ClientRegistrationForm registrationForm = new ClientRegistrationForm(ClientAutorizationForm.this.connectAutorization,ClientAutorizationForm.this);
            ClientAutorizationForm.this.dispose();
            registrationForm.setVisible(true);

        }
    }
}


