
 public class ImageWindow
    {
        public Image image;
        private IntegralImage integralImage;
        private SqrIntegralImage sqrIntegralImage;
        public int windowSide;
        
        //declared as memeber variables here for performance reason i.e. only single initialization 
        //instead of local scope initialization again and again for each pixel
        private int wxs, wxs_clamped, wxe, wys, wys_clamped, wye; 
        private int wl, wt, wc;
        private int noOfPixels;
        
        public ImageWindow(Image _image, int _windowSize)
        {
            image = _image;
            integralImage = new IntegralImage(image);
            sqrIntegralImage = new SqrIntegralImage(image);
            windowSide = _windowSize / 2;  //number of pixels to any side of the center pixel
            //_windowSize must atleast be 2
        }
        
        public int sum(int x, int y)
        {//return sum of pixels centered around (x,y) in the image using integralImage
            //actual window size may be smaller than the specified window size at the image borders as there may not be enough pixels for the window
            //int wxs, wxs_clamped, wxe, wys, wys_clamped, wye; //starting and ending xy coordinate positions for the actual window; wxs = window start postion for x axis
            wxs = x-windowSide - 1;
            wxs_clamped = Math.max(wxs, 0);  //starting point for actual window
            wxe = Math.min(x+windowSide, image.sizeX - 1);
            wys = y-windowSide - 1;
            wxs_clamped = Math.max(wxs, 0);
            wye = Math.min(y+windowSide, image.sizeY - 1);
            
            //int wl; //sum of pixels to left of the window to subtract to obtain the sum of window using integral image
            if (wxs < 0)
                {wl = 0;}
            else
                {wl = integralImage.pixel[wxs][wye];}
            //int wt; //sum of pixels to top of the window
            if (wys < 0)
                {wt = 0;}
            else
                {wt = integralImage.pixel[wxe][wys];}
            //int wc; //sum up to starting corner (intersection of left and top parts)
            if (wxs < 0 || wys < 0)
                {wc = 0;}
            else
                {wc = integralImage.pixel[wxs][wys];}
                
            noOfPixels = (wxe - wxs_clamped) * (wye - wys_clamped); //no of pixels inside the actual window
            
            return (integralImage.pixel[wxe][wye] - wl -wt +wc);
        }
        
        private int sqrSum(int x, int y)
        {//return sum of pixels centered around (x,y) in the image using sqrIntegralImage
            //this should be called after corresponding sum() call so that which wxs, wxe... shouldnot be computed again
            //i.e. this is only for finding variance
            
            // //actual window size may be smaller than the specified window size at the image borders as there may not be enough pixels for the window
            // //int wxs, wxs_clamped, wxe, wys, wys_clamped, wye; //starting and ending xy coordinate positions for the actual window; wxs = window start postion for x axis
            // wxs = x-windowSide - 1;
            // wxs_clamped = Math.max(wxs, 0);  //starting point for actual window
            // wxe = Math.min(x+windowSide, image.sizeX - 1);
            // wys = y-windowSide - 1;
            // wxs_clamped = Math.max(wxs, 0);
            // wye = Math.min(y+windowSide, image.sizeY - 1);
            
            //int wl; //sum of pixels to left of the window to subtract to obtain the sum of window using integral image
            if (wxs < 0)
                {wl = 0;}
            else
                {wl = sqrIntegralImage.pixel[wxs][wye];}
            //int wt; //sum of pixels to top of the window
            if (wys < 0)
                {wt = 0;}
            else
                {wt = sqrIntegralImage.pixel[wxe][wys];}
            //int wc; //sum up to starting corner (intersection of left and top parts)
            if (wxs < 0 || wys < 0)
                {wc = 0;}
            else
                {wc = sqrIntegralImage.pixel[wxs][wys];}
                
            //noOfPixels = (wxe - wxs_clamped) * (wye - wys_clamped); //no of pixels inside the actual window
            
            return (sqrIntegralImage.pixel[wxe][wye] - wl -wt +wc);
        }
        
        public int mean(int x, int y)
        {//return mean of pixels centered around (x,y) in the image using integralImage
            //int wxs, wxe, wys, wye; //starting and ending xy coordinate positions; wxs = window start postion for x axis
            return sum(x, y) / noOfPixels;
        }
        
        public int variance(int x, int y, int _mean)
        {//return variance of pixels centered around (x,y) in the image using integralImage and sqrIntegralImage
            //int wxs, wxe, wys, wye; //starting and ending xy coordinate positions; wxs = window start postion for x axis
            //int _mean = mean(x,y); mean has been already supplied in parameter itself 
            return ((sqrSum(x, y) - noOfPixels * _mean * _mean) / (noOfPixels));
        }
        
        public int variance(int x, int y)
        {//return variance of pixels centered around (x,y) in the image using integralImage and sqrIntegralImage
            //int wxs, wxe, wys, wye; //starting and ending xy coordinate positions; wxs = window start postion for x axis
            int _mean = mean(x,y);
            return ((sqrSum(x, y) - noOfPixels * _mean * _mean) / (noOfPixels));
        }
    }
