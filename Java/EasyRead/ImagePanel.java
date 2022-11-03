import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class ImagePanel extends JPanel
{
    Image img;
    BufferedImage bimg;
    
    public void setImg(Image _img)
    {
        img = _img;
        setBuffImg(false);
    }
    
    public void setImg(Image _img, boolean binarized)
    {
        img = _img;
        setBuffImg(binarized);
    }
    
    public void paint(Graphics g)
    {
        if(img != null)
            g.drawImage(bimg,0,0,null);
    }
    
    private void setBuffImg(boolean binarized)
    {
        bimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        int pix;
        
        if(!binarized)
        {
            for(int i=0; i<img.getWidth(); i++)
                for(int j=0; j<img.getHeight(); j++)
                {
                    pix = img.pixel[i][j] | (img.pixel[i][j] <<8) | (img.pixel[i][j]<<16);  //grayscale value
                    bimg.setRGB(i,j, pix);
                }
        }
        else
        {
            for(int i=0; i<img.getWidth(); i++)
                for(int j=0; j<img.getHeight(); j++)
                {
                    if(img.pixel[i][j] == 0)
                        bimg.setRGB(i,j, 0xffffffff);
                    else
                        bimg.setRGB(i,j, 0x000000);
                }
        }
    }
}
