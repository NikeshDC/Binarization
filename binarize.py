import cv2
import numpy


##<<----------windowing using integral image for performance--------

def getIntegralImage(image, SQR = False, _dtype = 'int32'):
    '''returns the integral image for given image'''
    #image is expected to be in grayscale
    #if SQR is true computes integral of squares of each pixel
    intImage = numpy.zeros(image.shape, dtype = _dtype)
    image_width = image.shape[0]
    image_height = image.shape[1]

    if SQR == False:
        intImage[0,0] = image[0,0]
        for i in range(1, image_width):
            intImage[i,0] = intImage[i-1,0] + image[i,0]

        for i in range(1, image_height):
            intImage[0,i] = intImage[0, i-1] + image[0,i]

        for i in range(1, image_width):
            for j in range(1, image_height):
                intImage[i, j] = intImage[i-1, j] + intImage[i, j-1] - intImage[i-1, j-1] + image[i, j]
    else:
        intImage[0,0] = int(image[0,0]) * image[0, 0]
        for i in range(1, image_width):
            intImage[i,0] = intImage[i-1,0] + int(image[i,0]) * image[i,0]

        for i in range(1, image_height):
            intImage[0,i] = intImage[0, i-1] + int(image[0,i]) * image[0,i]

        for i in range(1, image_width):
            for j in range(1, image_height):
                intImage[i, j] = intImage[i-1, j] + intImage[i, j-1] - intImage[i-1, j-1] + int(image[i, j]) * image[i, j]
            
    return intImage


def windowIntegralImage(intImage, pos, window_size, sqrIntImage = []):
    '''returns mean of pixel values contained inside window_size centered around pos(x, y) using given integral image'''
    #if sqrIntImage != None then calculates variance assuming integral of squared images is given
    ####if mean of window is given no need to calculate again for variance calculation
    image_width = intImage.shape[0] - 1 #pixel-width of the given image; index of last column of pixels
    image_height = intImage.shape[1] - 1 #index of last row of pixels; subtracting 1 because it will be used often
    
    #window_size = (width, height) of the rectangular window
    window_x = (int)((window_size[0] - 1) /2) #size of window on either side
    window_y = (int)((window_size[1] - 1) /2)
    #pos = (x, y) coordinate of the central pixel around which window is to be created
    x = pos[0]
    y = pos[1]
    #starting index of window's width or x-axis
    wxs = max(0, round(x - window_x)) #wws = window width start
    wxe = min(round(x + window_x), image_width) #wwe = window width end
    wys = max(0, round(y - window_y)) #whs = window height start
    wye = min(round(y + window_y), image_height)

    wx = wxe - wxs + 1 #window width #size_x = wwe - wws + 1 as it must include both starting and ending index
    wy = wye - wys + 1
    nPixels = wx * wy  #number of pixels inside the window
    if wxs == 0:
        window_side = 0
    else:
        window_side = intImage[wxs - 1, wye]
        
    if wys == 0:
        window_up = 0
    else:
        window_up = intImage[wxe, wys - 1]

    if wxs == 0 or wys == 0:
        window_corner = 0
    else:
        window_corner = intImage[wxs-1, wys-1]
        
    sumWindow = intImage[wxe, wye] - window_up - window_side + window_corner
    meanWindow = (sumWindow/nPixels)

    if len(sqrIntImage) == 0:
            return meanWindow
    else:
        if wxs == 0:
            window_side = 0
        else:
            window_side = sqrIntImage[wxs - 1, wye]
            
        if wys == 0:
            window_up = 0
        else:
            window_up = sqrIntImage[wxe, wys - 1]

        if wxs == 0 or wys == 0:
            window_corner = 0
        else:
            window_corner = sqrIntImage[wxs-1, wys-1]
        
        sumSqrWindow = sqrIntImage[wxe, wye] - window_side - window_up + window_corner
        var = (sumSqrWindow  - meanWindow * meanWindow * nPixels) / nPixels
        return var

##----------windowing using integral image for performance-------->>
    

##<<----------windowing without performance considerations--------

def windowImage(image, pos, window_size):
    '''returns pixel values contained inside window_size(width, height) centered around pos(x, y)'''
    image_width = image.shape[0] - 1 #pixel-width of the given image; index of last column of pixels
    image_height = image.shape[1] - 1 #index of last row of pixels; subtracting 1 because it will be used often
    
    #window_size = (width, height) of the rectangular window
    window_x = (window_size[0] - 1) /2 #size of window on either side
    window_y = (window_size[1] - 1) /2
    #pos = (x, y) coordinate of the central pixel around which window is to be created
    x = pos[0]
    y = pos[1]
    #starting index of window's width or x-axis
    wws = max(0, round(x - window_x)) #wws = window width start
    wwe = min(round(x + window_x), image_width) #wwe = window width end
    whs = max(0, round(y - window_y)) #whs = window height start
    whe = min(round(y + window_y), image_height)

    ww = wwe - wws + 1 #size_x = wwe - wws + 1 as it must include both starting and ending index
    wh = whe - whs + 1

    #constructing new array for the windowed pixels
    windowed_image = numpy.zeros((ww, wh))
    for wx in range(ww):
        for wy in range(wh):
            windowed_image[wx, wy] = image[wx+wws, wy+whs]
    return windowed_image
##----------windowing without performance considerations-------->>


def binarize(image, USE_INTEGRAL = True):
    '''image is numpy array of dimension 2 i.e. expects grayscale image value'''
    #if USE_INTEGRAL = True uses integral image for performance
    #window_size = (min(image.shape[0]/10, 15), min(image.shape[1]/10, 15)) # alternate window defination
    #window size and 'k' may require tuning
    window_size = (15, 15) #may require tuning
    k = 0.5 #may require tuning
    R = 128
    binarized_image = numpy.ones(image.shape)

    if not USE_INTEGRAL:
        for img_x in range(image.shape[0]):
            for img_y in range(image.shape[1]):
                #create window centered around a pixel
                windowed_image = windowImage(image, (img_x, img_y), window_size)
                #calculate mean and variance of windowed image
                m = numpy.mean(windowed_image)
                s = numpy.std(windowed_image)
                #using sauvolas algorithm to calculate threshold
                #t = m ∗ (1 + k ∗ (s/R − 1)) ; m is mean s is standard deviation k=0.5, R = 128
                threshold = m * (1 + k * (s/R - 1))
                #binarize the pixel based on threshold
                if image[img_x, img_y] < threshold:
                    binarized_image[img_x, img_y] = 0  ##if less than threshold then is black pixel
                #print("iteration:", img_x, ", ", img_y)
    else:
        intImage = getIntegralImage(image)
        sqrIntImage = getIntegralImage(image, True, 'int64')
        for img_x in range(image.shape[0]):
            for img_y in range(image.shape[1]):
                #create window centered around a pixel
                #windowed_image = windowImage(image, (img_x, img_y), window_size)
                #calculate mean and variance of windowed image
                m = windowIntegralImage(intImage, (img_x, img_y), window_size)
                v = windowIntegralImage(intImage, (img_x, img_y), window_size, sqrIntImage)
##                if v <0:
##                    windowed_image = windowImage(image, (img_x, img_y), window_size)
##                    print("neg: (",img_x,", ",img_y,"): ",v," M:",m)
##                    print(windowed_image)
##                    
                s = numpy.sqrt(v)
                #using sauvolas algorithm to calculate threshold
                #t = m ∗ (1 + k ∗ (s/R − 1)) ; m is mean s is standard deviation k=0.5, R = 128
                threshold = m * (1 + k * (s/R - 1))
                #binarize the pixel based on threshold
                if image[img_x, img_y] < threshold:
                    binarized_image[img_x, img_y] = 0  ##if less than threshold then is black pixel
                #print("iteration:", img_x, ", ", img_y)
    return binarized_image




##binarization test--------------------<<
imgpath = "image1.jpg"   #path of image file to binarize
img = cv2.imread(imgpath, cv2.IMREAD_GRAYSCALE) ##reading image in grayscale mode
##print(img.shape)
print("binarizing")
img_bin = binarize(img) * 255
print("binarized")
#print(img_bin)
##plt.imshow(img)
##plt.show()
cv2.imshow('Binarization-sample', img_bin)
cv2.waitKey(0)
cv2.destroyAllWindows()
##binarization test-------------------->>

####window test--------------------------<<
##a = numpy.array([[1,2,3,4,5],
##                 [1,2,3,4,5],
##                 [1,2,3,4,5],
##                 [1,2,3,4,5],
##                 [1,2,3,4,5]])
##c = numpy.array([[255,255,255,255,255],
##                [255,255,255,255,255],
##                [255,255,255,255,255],
##                [255,255,255,255,255],
##                [255,255,255,255,255],
##                [255,255,255,255,255]])
##
##b = getIntegralImage(c)
##sb = getIntegralImage(c, True)
##print(sb)
##print(b)
##for i in range(b.shape[0]):
##    for j in range(b.shape[1]):
##        m = windowIntegralImage(b, (i,j), (3,3), sb)
##        print("var(",i," ,",j,")",m)
####window test-------------------------->>

input()
