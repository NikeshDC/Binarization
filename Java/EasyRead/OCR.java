public class OCR
{
    Image image;
    public class Binarization
    {
        public class Sauvola
        {
            int k, w;
            public Sauvola(int _k, int _w)
            {
                k = _k;
                w = _w;
            }
        }
        public class Otsu
        {
            int L = 255;  //bit depth of image is 8-bits; max level for histogram is 255
        }
    }
}
