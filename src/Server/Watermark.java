package Server;

/**
 * Created by theeska on 11.06.17.
 */
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

//класс в котором прописаны методы для наложения и извлечения водяного знака
public class Watermark {

    public static BufferedImage cvzIN(BufferedImage bi1,BufferedImage bi2){
        byte[] imageArrayByte = null;
        byte[] cvzArrayByte = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( bi1, "bmp", baos );
            baos.flush();
            imageArrayByte = baos.toByteArray();
            baos.close();

            baos = new ByteArrayOutputStream();
            ImageIO.write( bi2, "bmp", baos );
            baos.flush();
            cvzArrayByte = baos.toByteArray();
            baos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if ((imageArrayByte.length-54) < (cvzArrayByte.length*8)){
            return bi1;
        }


        int w=bi2.getWidth();
        imageArrayByte[54] = (byte) (w>>8);
        imageArrayByte[55] = (byte) (w & 0xff);
        int h=bi2.getHeight();
        imageArrayByte[56] = (byte) (h>>8);
        imageArrayByte[57] = (byte) (h & 0xff);
        int l = cvzArrayByte.length;
        imageArrayByte[58] = (byte) (l >> 16);
        imageArrayByte[59] = (byte) (l >> 8);
        imageArrayByte[60] = (byte) (l & 0xff);



        int k=68;
        for  (l=0;l< cvzArrayByte.length;l++){
            int byteCVZ = cvzArrayByte[l];

            for (int q = 0; q <= 7; q++) {

                int byteImage = imageArrayByte[k-q];
                if ((byteCVZ & 0x01)==1){
                    byteImage = byteImage | 0x01;
                }else{
                    byteImage = byteImage >> 1;
                    byteImage = byteImage << 1;
                }
                imageArrayByte[k-q]=(byte) byteImage;

                byteCVZ=byteCVZ >> 1;
            }
            k=k+8;

        }


        bi1=null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(imageArrayByte);
            bi1 = ImageIO.read(in);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bi1;
    }

    public static int negativ(byte b){
        if (b>0) {
            return b;
        }else{
            if (b<0){
                return (b+256);
            }else {
                return 0;
            }
        }
    }



    public static BufferedImage cvzOUT(BufferedImage bi1){
        File output1 = new File("ser1.1.bmp");
        byte[] imageArrayByte = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( bi1, "bmp", baos );
            baos.flush();
            imageArrayByte = baos.toByteArray();
            baos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        int w = negativ(imageArrayByte[54]) << 8;
        w = w + negativ(imageArrayByte[55]);
        int h = negativ(imageArrayByte[56]) << 8;
        h = h + negativ(imageArrayByte[57]);
        int length = negativ (imageArrayByte[58]) << 16;
        length=length+(negativ (imageArrayByte[59]) << 8);
        length=length+(negativ (imageArrayByte[60]));


        byte[] cvzArrayByte = new byte[length+1];
        int q=0;
        int byteCVZ=0;

        for (int l=61; q<=length; l++){
            int byteImage = imageArrayByte[l];

            byteImage = byteImage & 0x01;

            if (byteImage==1){
                byteCVZ=byteCVZ + 1;
            }else{
                byteCVZ = byteCVZ >> 1;
                byteCVZ = byteCVZ << 1;
            }

            if ((l-4)%8==0){
                cvzArrayByte[q]= (byte) byteCVZ;
                q++;
                byteCVZ=0;
            }else{
                byteCVZ = byteCVZ << 1;
            }

        }


        bi1=null;
        try {
            FileOutputStream outstr = new FileOutputStream(output1);
            outstr.write(cvzArrayByte);
            bi1 = ImageIO.read(output1);
            outstr.close();
            output1.delete();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bi1;
    }


}