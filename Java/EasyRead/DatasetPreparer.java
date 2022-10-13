import java.io.File;

public class DatasetPreparer
{
    public static void main(String[] args)
    {
        String outputDirectoryName = "outputs";
        String inputDirectoryName = "inputs";
        //String imagepath = args[0];
        File inputdir = new File(inputDirectoryName);
        
        if(!inputdir.isDirectory())
        {
            System.out.println("No directory");
            return;
        }
        
        OCR ocr = new OCR();
        OCR.Binarization bin = ocr.new Binarization();
        //float k = Float.parseFloat(args[0]);
        float k = -1f;
        int w = 5;
        //int w = Integer.parseInt(args[1]);
        OCR.Binarization.Otsu otsu = bin.new Otsu();
        OCR.Binarization.Sauvola sauv = bin.new Sauvola(k, w);
        
        String sauvolaFileName = "sauv";
        String otsuFileName = "otsu";
        String infoSeperator = "_";
        String kName = "k";
        String wName = "w";
        String outFormat = "jpg";
        
        int step_w = 5;
        int min_w = 5;
        int max_w = 50;
        float step_k = 0.05f;
        float min_k = 0f;
        float max_k = 1f;
        
        
        
        for(File fileEntry : inputdir.listFiles())
        {
            String fileExt[] = fileEntry.getName().split("\\.(?=[^\\.]+$)");
            String fileName = fileExt[0];
            String fileFormat = fileExt[1];
            System.out.println(fileName + " " + fileFormat);
            Image img = ImageUtility.readImage(fileEntry);
            ocr.setImage(img);
            
            //otsu method file saving
            otsu.binarize();
            String outFileName = outputDirectoryName + "/" + fileName + infoSeperator + otsuFileName + "." + outFormat;
            ImageUtility.writeImage(otsu.binarizedImage, outFileName, outFormat, true);
            
            //sauvola only saving file
            for(k=min_k; k <= max_k; k+=step_k)
            {
                for(w=min_w; w<= max_w; w+= step_w)
                {
                    sauv.setParam(k, w);
                    sauv.binarize();
                    outFileName = outputDirectoryName + "/" + fileName + infoSeperator + sauvolaFileName + infoSeperator + kName + 
                    String.format("%.02f", k)+ infoSeperator + wName + w + "." + outFormat;
                    ImageUtility.writeImage(sauv.binarizedImage, outFileName, outFormat, true);
                }
            }
             
            //sauv.binarize(otsu.threshold);
        }
        
        
        
        
    }
}
