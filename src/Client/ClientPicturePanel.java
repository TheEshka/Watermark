package Client;

/**
 * Created by theeska on 06.06.17.
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ClientPicturePanel extends JPanel {
    // Храним 2 изображения: оригинальное и текущее.
    // Оригинальное используется для получения текущего в зависимости от размеров панели.
    // Текущее непосредственно прорисовывается на панели.
    private BufferedImage originalImage = null;
    private Image image = null;
    public ClientPicturePanel() {
        //super();
        initComponents();
    }

    private void initComponents() {
        setLayout(null);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
    }

    // Реакция на изменение размеров панели - изменение размера изображения.
    private void formComponentResized(ComponentEvent evt) {
        int w = this.getWidth();
        int h = this.getHeight();
        if ((originalImage != null) && (w > 0) && (h > 0)) {
            image = originalImage.getScaledInstance(w, h, Image.SCALE_DEFAULT);
            this.repaint();
        }
    }
    // Берем прорисовку в свои руки.
    public void paintComponent(Graphics g) {
        initComponents();
        // Рисуем картинку
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }

        // Рисуем подкомпоненты.
        super.paintChildren(g);
        // Рисуем рамку
        super.paintBorder(g);
    }

    // Методы для настройки картинки.
    public BufferedImage getImage() {
        return originalImage;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        int w = this.getWidth();
        int h = this.getHeight();
        if ((originalImage != null) && (w > 0) && (h > 0)) {
            this.image = originalImage.getScaledInstance(w, h, Image.SCALE_DEFAULT);
            this.repaint();
        }

    }
    public void setImageFile(File imageFile) {
        try {
            if (imageFile == null) {
                originalImage = null;
            }
            BufferedImage bi = ImageIO.read(imageFile);
            originalImage = bi;
        } catch (IOException ex) {
            System.err.println("Неудалось загрузить картинку!");
            ex.printStackTrace();
        }
        //repaint();
    }
}
