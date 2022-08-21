public class ImageUtility
{
    public static Image readImage(String filepath)
    {
        //read from image file and return it
        int sizex = 10;
        int sizey = 10;
        Image image = new Image(sizex, sizey);
        for (int i=0; i<sizex; i++)
        {
            for (int j=0; j<sizey; j++)
            {
                image.image[i][j] = (short)(Math.random() * 256);
            }
        }
        return image;
    }
    
    public class IntegralImage
    {
    }
}
