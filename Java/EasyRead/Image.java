//import javax.imageio.ImageReader;

public class Image
{
    public int[][] pixel;
    protected int sizeX;
    protected int sizeY;
    protected byte bitDepth;
    public Image(int _sizex,int _sizey)
    {
        sizeX = _sizex;
        sizeY = _sizey;
        pixel = new int[sizeX][sizeY];
    }
    
    public int getWidth()
    {
        return sizeX;
    }
    
    public int getHeight()
    {
        return sizeY;
    }
}
