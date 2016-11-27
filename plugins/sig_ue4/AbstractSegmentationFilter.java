import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;

public abstract class AbstractSegmentationFilter implements PlugInFilter {

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

	protected abstract void readDialogResult(GenericDialog gd);

	protected abstract void prepareDialog(GenericDialog gd);
}
