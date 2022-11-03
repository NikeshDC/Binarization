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
    
    public static int findMaxPixelValue(Image img)
    {
        int max = 0;   //8-bit depth image
        for(int i=0; i<img.getWidth(); i++)
            for(int j=0; j<img.getHeight(); j++)
            {
                if(max < img.pixel[i][j])
                    max = img.pixel[i][j];
            }
        return max;
    }
    
    public static int findMinPixelValue(Image img)
    {
        int min = 255;  //8-bit depth image
        for(int i=0; i<img.getWidth(); i++)
            for(int j=0; j<img.getHeight(); j++)
            {
                if(min >  img.pixel[i][j])
                    min = img.pixel[i][j];
            }
        return min;
    }
    
    public static Image enhanceText(Image img, int imageMean, int imageSD)
    {//sd- standard deviation of the whole image
        Image enhancedImg = new Image(img.getWidth(), img.getHeight());
        //perform enhancement*********************************************************************************************************
        int Na = 9;
        int Imax = findMaxPixelValue(img);  //maximum intensity of pixel
        int Imin = findMinPixelValue(img);  //minimum intensity of pixel
        
        int Fseg = Imin + (Imax - Imin)/Na;   //first segment (text segment) largest value
        int Lseg = Imin + (Imax - Imin)/Na;   //last segment (background) smallest value
        
        for(int i=0; i< img.getWidth(); i++)
        {
            for(int j=0; j<img.getHeight(); j++)
            {
                //segmentation into first or last segments
                if(img.pixel[i][j] < Fseg)
                    enhancedImg.pixel[i][j] = 0;   //intensity value for text pixels
                else if(img.pixel[i][j] > Lseg)
                    enhancedImg.pixel[i][j] = imageMean;
                else
                    enhancedImg.pixel[i][j] = img.pixel[i][j];
                    
                //uniform illumination
                if(enhancedImg.pixel[i][j] >= (imageMean - imageSD/2) && enhancedImg.pixel[i][j] <= (imageMean + imageSD/2))
                    enhancedImg.pixel[i][j] = imageMean;
            }
        }
        
        return enhancedImg;
    }
    
    public static Image dilate(Image img, int windowSize)
    {
        Image dilatedImg = new Image(img.getWidth(), img.getHeight());
        //perform dilation
        ImageWindow imgWin = new ImageWindow(img, windowSize, false);
        for(int i=0; i<img.getWidth(); i++)
            for(int j=0; j<img.getHeight(); j++)
                dilatedImg.pixel[i][j] = imgWin.maxBinarized(i, j);
                
        return dilatedImg;
    }
    
    public static Image dilate(Image img, int windowSize, ImageWindow imgWin)
    {
        Image dilatedImg = new Image(img.getWidth(), img.getHeight());
        //perform dilation
        //ImageWindow imgWin = new ImageWindow(img, windowSize, false);
        for(int i=0; i<img.getWidth(); i++)
            for(int j=0; j<img.getHeight(); j++)
                dilatedImg.pixel[i][j] = imgWin.maxBinarized(i, j);
                
        return dilatedImg;
    }
    
    public static Image erode(Image img, int windowSize)
    {
        Image erodedImg = new Image(img.getWidth(), img.getHeight());
        //perform dilation
        ImageWindow imgWin = new ImageWindow(img, windowSize, false);
        for(int i=0; i<img.getWidth(); i++)
            for(int j=0; j<img.getHeight(); j++)
                erodedImg.pixel[i][j] = imgWin.minBinarized(i, j);
                
        return erodedImg;
    }
    
    public static Image erode(Image img, int windowSize, ImageWindow imgWin)
    {
        Image erodedImg = new Image(img.getWidth(), img.getHeight());
        //perform dilation
        //ImageWindow imgWin = new ImageWindow(img, windowSize, false);
        for(int i=0; i<img.getWidth(); i++)
            for(int j=0; j<img.getHeight(); j++)
                erodedImg.pixel[i][j] = imgWin.minBinarized(i, j);
                
        return erodedImg;
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
                int r,g,b, gray;
                pix = bimage.getRGB(i,j);
                r = ((pix>>16) & 0x000000ff);
                g = ((pix>>8) & 0x000000ff);
                b = ((pix) & 0x000000ff);
                gray = (int)(0.33 * r + 0.33 * g + 0.33 * b);    //grayscale conversion
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
                        bimage.setRGB(i,j, 0xffffffff);//white pixel
                    else
                        bimage.setRGB(i,j, 0x000000);//black pixel
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
