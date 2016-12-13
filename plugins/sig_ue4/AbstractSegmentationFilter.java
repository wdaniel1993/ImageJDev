import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;

/**
 * AbstractSegmentationFilter
 * Base class for the RegionGrowing filters
 */
public abstract class AbstractSegmentationFilter implements PlugInFilter {

	/*
	 * Prepares fields for dialog and reads the data from the dialog
	 */
	public boolean inputDialog(){
		GenericDialog gd = new GenericDialog("User Input");
		prepareDialog(gd);
		gd.showDialog();
		if (gd.wasCanceled()) {
			return false;
		}
		readDialogResult(gd);
		return true;
	}

	/*
	 * Read results from dialog
	 */
	protected abstract void readDialogResult(GenericDialog gd);

	/*
	 * Add fields to dialog
	 */
	protected abstract void prepareDialog(GenericDialog gd);
}
