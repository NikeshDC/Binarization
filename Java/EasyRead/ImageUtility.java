import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtility
{
    public static Image readImage(String filepath)
    {
        //read from image file and return it
        BufferedImage bimage = null;
        try{
            bimage = ImageIO.read(new File(filepath));
        }
        catch(IOException e)
        {
            System.out.println("Image File error");
            System.exit(0);
        }
        Image img = new Image(bimage.getWidth(), bimage.getHeight());
        
        for(int i=0; i<img.getWidth(); i++)
            for(int j=0; j<img.getHeight(); j++)
            {
                int pix;
                short r,g,b, gray;
                pix = bimage.getRGB(i,j);
                r = (short)((pix>>16) & 0x000000ff);
                g = (short)((pix>>8) & 0x000000ff);
                b = (short)((pix) & 0x000000ff);
                gray = (short)(0.21 * r + 0.72 * g + 0.07 * b);
                img.pixel[i][j] = gray;
            }
        return img;
    }
}
