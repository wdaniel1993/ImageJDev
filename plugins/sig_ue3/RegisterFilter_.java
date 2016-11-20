
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import ue3.utility.NearestNeighbourInterpolator;
import ue3.transform.BinaryDifference;
import ue3.transform.ImageDifference;
import ue3.transform.MutualInformationDifference;
import ue3.transform.SumOfSquaredErrorDifference;
import ue3.transform.TransformHelper;
import ue3.utility.Image2D;
import ij.gui.GenericDialog;

public class RegisterFilter_ extends AbstractRegisterFilter {

	private Dictionary<String, ImageDifference> choices = new Hashtable<String, ImageDifference>();
	private ImageDifference diffCalculator;
	
	private Image2D imageA;
	private Image2D imageB;

	public RegisterFilter_() {
		//add difference calculators for the choices in the dialog
		addImageDifferenceToChoices(new BinaryDifference());
		addImageDifferenceToChoices(new MutualInformationDifference());
		addImageDifferenceToChoices(new SumOfSquaredErrorDifference());
	}
	
	protected void prepareDialog(GenericDialog gd) {
		gd.addChoice("calculator", choiceNames(), choices.keys().nextElement());
		super.prepareDialog(gd);
	}
	
	protected void readDialogResult(GenericDialog gd) {
		diffCalculator = choices.get(gd.getNextChoice());
		super.readDialogResult(gd);
	}

	private void addImageDifferenceToChoices(ImageDifference id) {
		choices.put(id.getName(), id);
	}

	private String[] choiceNames() {
		Enumeration<String> keys = choices.keys();
		String[] names = new String[choices.size()];

		for (int i = 0; i < choices.size(); i++) {
			names[i] = keys.nextElement();
		}

		return names;
	}

	//Transform image and calculate difference according to user choice 
	@Override
	protected double transformAndCalculateDifference(double transX, double transY, double transRot) {
		Image2D transformedImage = TransformHelper.transformImage(imageA, transX, transY, transRot,
				new NearestNeighbourInterpolator());
		return diffCalculator.calculateDifference(transformedImage, imageB);
	}

	@Override
	protected void prepareImages(Image2D imageA, Image2D imageB) {	
		this.imageA = imageA;
		this.imageB = imageB;
	}

	@Override
	public String getFilterName() {
		return "RegisterFilter_";
	}

	@Override
	protected double calculateInitialDifference() {
		return diffCalculator.calculateDifference(imageA, imageB);
	}
} // class RegisterFilter_
