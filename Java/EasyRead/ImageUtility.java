import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtility
{
    public static Image readImage(String filepath)
    {
        File file = new File(filepath);
        return readImage(file);
    }
    public static Image readImage(File file)
    {
        //read from image file and return it
        BufferedImage bimage = null;
        try{
            bimage = ImageIO.read(file);
        }
        catch(IOException e)
        {
            System.out.println("Image File error");
            System.exit(0);
        }
        
        Image image = new Image(bimage.getWidth(), bimage.getHeight());
        
        for(int i=0; i<image.getWidth(); i++)
            for(int j=0; j<image.getHeight(); j++)
            {
                int pix;
                short r,g,b, gray;
                pix = bimage.getRGB(i,j);
                r = (short)((pix>>16) & 0x000000ff);
                g = (short)((pix>>8) & 0x000000ff);
                b = (short)((pix) & 0x000000ff);
                gray = (short)(0.21 * r + 0.72 * g + 0.07 * b);
                image.pixel[i][j] = gray;
            }
        return image;
    }
    
    public static void writeImage(Image image, String filename, String format, boolean binarized)
    {
        BufferedImage bimage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        //create buffered image
        if (binarized)
        {
            for(int i=0; i<image.getWidth(); i++)
                for(int j=0; j<image.getHeight(); j++)
                {
                    if(image.pixel[i][j] == 0)
                        bimage.setRGB(i,j, 0xffffffff);
                    else
                        bimage.setRGB(i,j, 0x000000);
                    //bimage.setRGB(i,j, image.pixel[i][j]);
                }
        }
        else
        {
            for(int i=0; i<image.getWidth(); i++)
                for(int j=0; j<image.getHeight(); j++)
                {
                    bimage.setRGB(i,j, image.pixel[i][j]);
                }
            
        }
        
        File out = new File(filename);
        try{
            ImageIO.write(bimage, format, out);
        }
        catch(IOException e)
        {
            System.out.println("Couldnot save image");
        }
    }
}
