import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import ij.gui.GenericDialog;
import utility.ByteImage2D;
import utility.Image2D;
import utility.Image2DUtility;
import utility.ImageJUtility;

/**
 * Abstract base class for all filters which compare two images
 */
public abstract class AbstractCompareFilter extends AbstractBaseFilter {

	private Dictionary<String,AbstractBaseFilter> choices = new Hashtable<String,AbstractBaseFilter>();
	private String choice;
	
	public AbstractCompareFilter(){
		/*
		 * Adds filter choices for the input dialog
		 * the original image and the filtered image will be compared
		 */
		addFilterToChoices(new GaussFilter_());
		addFilterToChoices(new MedianFilter_());
		addFilterToChoices(new MeanFilter_());
		addFilterToChoices(new HistogramEqualizationFilter_());
		addFilterToChoices(new InvertFilter_());
	}
	
	private void addFilterToChoices(AbstractBaseFilter plugin){
		choices.put(plugin.getFilterName(), plugin);
	}
	
	
	@Override
	public void processImage(Image2D inputImage, Image2D outputImage) {
		//copies the outputImage
		final Image2D pluginImage = new ByteImage2D(Image2DUtility.convertFromImage2D(outputImage), outputImage.getWidth(), outputImage.getHeight());
		
		//use the selected filter on the image
		AbstractBaseFilter plugin = choices.get(choice);
		plugin.inputDialog();
		plugin.processImage(inputImage, pluginImage);
		
		//compare the original image with the filtered one
		compareImage(inputImage, pluginImage, outputImage);
		
		//output the filtered image in a separate window (the comparison will be shown in the base class)
		byte[] outPixels = Image2DUtility.convertFromImage2D(pluginImage);
		ImageJUtility.showNewImage(outPixels, pluginImage.getWidth(), pluginImage.getHeight(),plugin.getFilterName());
	}
	
	public abstract void compareImage(Image2D inputImage, Image2D pluginImage, Image2D outputImage);


	@Override
	public void readDialogResult(GenericDialog gd) {
		choice = gd.getNextChoice();
		super.readDialogResult(gd);
	}

	@Override
	public void prepareDialog(GenericDialog gd) {
		gd.addChoice("filter",choiceNames(), choices.keys().nextElement());
		super.prepareDialog(gd);
	}
	
	private String[] choiceNames() {
		Enumeration<String> keys = choices.keys();
	    String[] names = new String[choices.size()];

	    for (int i = 0; i < choices.size(); i++) {
	        names[i] = keys.nextElement();
	    }

	    return names;
	}

}
