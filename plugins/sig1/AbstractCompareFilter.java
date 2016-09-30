

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import ij.gui.GenericDialog;
import utility.ByteImage2D;
import utility.Image2D;
import utility.Image2DUtility;
import utility.ImageJUtility;

public abstract class AbstractCompareFilter extends AbstractBaseFilter {

	private Dictionary<String,AbstractBaseFilter> choices = new Hashtable<String,AbstractBaseFilter>();
	private String choice;
	
	public AbstractCompareFilter(){
		addFilterToChoices(new GaussFilter_());
		addFilterToChoices(new MedianFilter_());
		addFilterToChoices(new MeanFilter_());
	}
	
	private void addFilterToChoices(AbstractBaseFilter plugin){
		choices.put(plugin.getFilterName(), plugin);
	}
	
	
	@Override
	public void processImage(Image2D inputImage, Image2D outputImage) {
		final Image2D pluginImage = new ByteImage2D(Image2DUtility.convertFromImage2D(outputImage), outputImage.getWidth(), outputImage.getHeight());
		
		AbstractBaseFilter plugin = choices.get(choice);
		GenericDialog gd = new GenericDialog("User Input");
		plugin.prepareDialog(gd);
		gd.showDialog();
		if (gd.wasCanceled()) {
			return;
		}
		plugin.readDialogResult(gd);
		plugin.processImage(inputImage, pluginImage);
		
		compareImage(inputImage, pluginImage, outputImage);
		
		byte[] outPixels = Image2DUtility.convertFromImage2D(pluginImage);
		ImageJUtility.showNewImage(outPixels, pluginImage.getWidth(), pluginImage.getHeight(),plugin.getFilterName());
	}
	
	public abstract void compareImage(Image2D inputImage, Image2D pluginImage, Image2D outputImage);


	@Override
	public void readDialogResult(GenericDialog gd) {
		choice = gd.getNextChoice();
	}

	@Override
	public void prepareDialog(GenericDialog gd) {
		gd.addChoice("filter",choiceNames(), choices.keys().nextElement());
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
