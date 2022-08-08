import cv2
import numpy
#import matplotlib.pyplot as plt

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
    

def binarize(image):
    '''image is numpy array of dimension 2 i.e. expects grayscale image value'''
    #window_size = (min(image.shape[0]/10, 15), min(image.shape[1]/10, 15)) # alternate window defination
    #window size and 'k' may require tuning
    window_size = (15, 15) #may require tuning
    k = 0.5 #may require tuning
    R = 128
    binarized_image = numpy.ones(image.shape)

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
    return binarized_image
    
##binarization test--------------------<<
imgpath = "image.jpg"   #path of image file to binarize
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

###window test--------------------------<<
##a = numpy.array([[1,2,3,4,5],
##                 [1,2,3,4,5],
##                 [1,2,3,4,5],
##                 [1,2,3,4,5],
##                 [1,2,3,4,5]])
##
##b = windowImage(a, (2,2), (3,3))
#print(b)
###window test-------------------------->>


input()
