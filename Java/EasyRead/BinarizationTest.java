import javax.swing.*;

public class BinarizationTest
{
    public static void main(String[] args)
    {
        String imagepath = "a.png";
        //String imagepath = args[0];
        JFrame window = new JFrame();
        ImagePanel myimgpanel = new ImagePanel();
        Image img = ImageUtility.readImage(imagepath);
        
        OCR ocr = new OCR();
        OCR.Binarization bin = ocr.new Binarization();
        //float k = Float.parseFloat(args[0]);
        float k = 0.3f;
        int w = 25;
        //int w = Integer.parseInt(args[1]);
        OCR.Binarization.Otsu otsu = bin.new Otsu();
        OCR.Binarization.Sauvola sauv = bin.new Sauvola(k, w);
        ocr.setImage(img);
        otsu.binarize();
        //sauv.binarize();
        sauv.binarize(otsu.threshold);
        
       
        myimgpanel.setImg(sauv.binarizedImage, true);
        
        window.getContentPane().add(myimgpanel);
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(600, 600);
        window.setVisible(true);
    }
}
