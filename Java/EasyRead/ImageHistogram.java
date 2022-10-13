
/**
 * histogram is distribution the number of pixels at various levels(0 to 255 for 8-bit image depth)
 * it may be represented as array of integers with a value for each level
 *
 * @author (Nikesh DC)
 * @version (1.1)
 */
public class ImageHistogram
{
    // instance variables - replace the example below with your own
    public int[] level;
    public double[] level_normalized;
    static final int L = 256;

    /**
     * Constructor for objects of class ImageHistogram
     * assuming 8-bit depth for image
     */
    public ImageHistogram(Image image)
    {
        level = new int[L];  //default initialized value for each element is 0
        level_normalized = new double[L];
        
        //constructing histogram
        for (int i=0; i < image.getWidth(); i++)
        {
           for(int j=0; j < image.getHeight(); j++)
           {
               level[image.pixel[i][j]] ++;  //increment value for each level
           }
        }
        
        //normalizing level values
        int N = image.getWidth() * image.getHeight(); //number of pixels in image
        
        for(int i=0; i<L; i++)
        {
            level_normalized[i] = (double)level[i]/N;
        }
    }
}
