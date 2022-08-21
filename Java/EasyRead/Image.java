public class Image
{
    public short[][] image;
    private int sizex;
    private int sizey;
    public Image(int _sizex,int _sizey)
    {
        sizex = _sizex;
        sizey = _sizey;
        image = new short[sizex][sizey];
    }
}
