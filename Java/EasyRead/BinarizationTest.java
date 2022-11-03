import javax.swing.*;

public class BinarizationTest
{
    public static void test()
    {
        String imagepath = "a-n.png";
        String savepath = "save-n.png";
        //String imagepath = args[0];
        JFrame window = new JFrame();
        ImagePanel myimgpanel = new ImagePanel();
        Image img = ImageUtility.readImage(imagepath);
        
        OCR ocr = new OCR();
        OCR.Binarization bin = ocr.new Binarization();
        //float k = Float.parseFloat(args[0]);
        float k = 0.20f;
        int w = 25;
        //int w = Integer.parseInt(args[1]);
        //OCR.Binarization.Otsu otsu = bin.new Otsu();
        OCR.Binarization.Sauvola sauv = bin.new Sauvola(k, w);
        ocr.setImage(img);
        
        //##image preprocessing
        // ImageWindow imageWindow = new ImageWindow(img, w);
        // int imageMean = imageWindow.getImageMean();
        // int imageVar = imageWindow.getImageVariance(imageMean);
        // int imageSD = (int)Math.sqrt(imageVar);
        // Image enhancedImage = ImageUtility.enhanceText(img, imageMean, imageSD);
        // ocr.setImage(enhancedImage);
        
        //otsu.binarize();
        sauv.binarize();
        //sauv.binarize(otsu.threshold, 0.05f);
        
        //CLOSE operation: dilation and then erosion
        //int dilErodWinSize = 3;
        //Image dilatedBinImage = ImageUtility.dilate(sauv.binarizedImage, dilErodWinSize);
        //Image erodedBinImage = ImageUtility.erode(dilatedBinImage, dilErodWinSize);
        
        //OPEN operation: dilation and then erosion
        //int dilErodWinSize = 3;
        //Image dilatedBinImage = ImageUtility.dilate(sauv.binarizedImage, dilErodWinSize);
        //Image erodedBinImage = ImageUtility.erode(sauv.binarizedImage, dilErodWinSize);
        
        //ImageUtility.writeImage(sauv.binarizedImage, savepath, "jpg", true); //saving output
       
        //##setting image to display
        myimgpanel.setImg(sauv.binarizedImage, true);
        //myimgpanel.setImg(enhancedImage);
        
        window.getContentPane().add(myimgpanel);
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(600, 600);
        window.setVisible(true);
    }
}
