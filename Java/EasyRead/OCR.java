public class OCR
{
    Image image;
    
    public void setImage(Image _image)
    {
        image = _image;
    }
    
    public class Binarization
    {
        
        public class Sauvola
        {
            int w;
            float k; //k ranges from 0 to 1
            public Image binarizedImage;
            
            public Sauvola(float _k, int _windowSize)
            {
                k = _k;
                w = _windowSize;
            }
            
            public void setParam(float _k, int _w)
            {
                k = _k;
                w = _w;
            }
            
            public void binarize()
            {
                binarizedImage = new Image(image.getWidth(), image.getHeight());
                int threshold;
                int mean;   //mean centered around a window of size w
                float sd;    //standard deviation centered around a window of size w
                int R = 128;  //dynamic range of standartd deviation
                ImageWindow imageWindow = new ImageWindow(image, w);
                
                //for every pixel in image calculate threshold value and compare to assign binarization
                for (int i=0; i < image.getWidth(); i++)
                {
                    for(int j=0; j < image.getHeight(); j++)
                    {
                        mean = imageWindow.mean(i, j);
                        sd = (float) Math.sqrt(imageWindow.variance(i, j, mean));
                        threshold = (int) (mean * (1 + k *(sd/R - 1)));
                        if (image.pixel[i][j] < threshold)
                            {binarizedImage.pixel[i][j] = 1;}
                        else
                            {binarizedImage.pixel[i][j] = 0;}
                    }
                }
            }
            
            public void binarize(int secondaryMean)
            {
                binarizedImage = new Image(image.getWidth(), image.getHeight());
                int threshold;
                int mean;   //mean centered around a window of size w
                float sd;    //standard deviation centered around a window of size w
                int R = 128;  //dynamic range of standartd deviation
                ImageWindow imageWindow = new ImageWindow(image, w);
                
                //for every pixel in image calculate threshold value and compare to assign binarization
                for (int i=0; i < image.getWidth(); i++)
                {
                    for(int j=0; j < image.getHeight(); j++)
                    {
                        mean = imageWindow.mean(i, j);
                        sd = (float) Math.sqrt(imageWindow.variance(i, j, mean));
                        threshold = (int) (mean * (1 + k *(sd/R - 1)));
                        threshold = (threshold + secondaryMean)/2;
                        if (image.pixel[i][j] < threshold)
                            {binarizedImage.pixel[i][j] = 1;}
                        else
                            {binarizedImage.pixel[i][j] = 0;}
                    }
                }
            }
        }
        
        public class Otsu
        {
            int L = 256;  //assuming bit depth of image is 8-bits; max level for histogram is 255
            public Image binarizedImage;
            public int threshold;
            
            public void binarize()
            {
                binarizedImage = new Image(image.getWidth(), image.getHeight());
                //int threshold = 0;
                
                ImageHistogram histogram = new ImageHistogram(image);
                
                double uT = 0.0;
                for(int i=0; i < L; i++)
                    uT += i * histogram.level_normalized[i]; 
                
                double uk, wk;  //expected level(mean) and probability of occurance for probable text pixels seperated by threshold 'k'
                double sb;  //between class variance that is a measure of goodness of threshold seperating the background and text
                double max_sb = -1.0;  //sb must be positive
                for(int k=0; k<(L-1); k++)
                {
                    uk = 0.0;
                    wk = 0.0;
                    for(int i=0; i <= k; i++)
                        uk += i * histogram.level_normalized[i]; 
                    
                    for(int i=0; i <= k; i++)
                        wk += histogram.level_normalized[i]; 
                        
                    sb = Math.pow((uT*wk - uk), 2) / (wk * (1-wk));
                    
                    if (max_sb < sb)
                    {
                        max_sb = sb;
                        threshold = k;  //all pixels from 0 up to and including k are 'text'
                    }
                }
                
                for(int i=0; i< image.getWidth(); i++)
                {
                    for(int j=0; j<image.getHeight(); j++)
                    {
                        if(image.pixel[i][j] <= threshold)
                        {binarizedImage.pixel[i][j] = 1;} //all pixels from 0 up to and including threshold are 'text'
                        else
                        {binarizedImage.pixel[i][j] = 0;} 
                        
                        //binarizedImage.pixel[i][j] = (image.pixel[i][j] <= threshold) ? (short)1 : (short)0;
                    }
                }
            }
        }
    }
}
