import filter.AbstractMaskFilter;
import ij.gui.GenericDialog;
import ij.process.ImageProcessor;
import utility.Image2D;
import utility.ImageJUtility;

public class GaussFilter_ extends AbstractMaskFilter {

	private double[][] mask;
	private double maxGauss = 0;
	private double sigma = 2;
	
	@Override
	protected int transformImagePoint(int x, int y, Image2D mask) {
		int startIndexX = x - this.getRadius() > 0 ? 0 : this.getRadius() - x;
		int startIndexY = y - this.getRadius() > 0 ? 0 : this.getRadius() - y;
		int stopIndexX = startIndexX +  mask.getWidth();
		int stopIndexY = startIndexY +  mask.getHeight();
		
		double[][]gaussMatrix = getMatrixGauss();
		
		double sum = 0;
		double usedGaussSum = 0;
		for(int maskX = startIndexX; maskX < stopIndexX; maskX ++ ){
			for(int maskY = startIndexY; maskY < stopIndexY; maskY ++ ){
				sum += mask.get(maskX-startIndexX, maskY-startIndexY) * gaussMatrix[maskX][maskY];
				usedGaussSum += gaussMatrix[maskX][maskY];
			}
		}
		sum /=  usedGaussSum;
		
		return (int) (sum + 0.5);
	}
	
	@Override
	public void run(ImageProcessor ip) {
		super.run(ip);
		
		int maskWidth = this.getRadius()*2 +1;
		double[][] mask = getMatrixGauss();
		int[][] gaussFilter = new int[maskWidth][maskWidth];
				
		
		for(int x= 0; x< maskWidth ; x++){
			for(int y= 0; y< maskWidth; y++){
				gaussFilter[x][y] = (int) (255 * (mask[x][y]/maxGauss) + 0.5);
			}
		}
		
		byte[] outPixels = ImageJUtility.convertFrom2DIntArr(gaussFilter, maskWidth, maskWidth);
		ImageJUtility.showNewImage(outPixels, maskWidth, maskWidth, "gauss mask");
	}

	@Override
	public String getFilterName() {
		return "gauss filter";
	}
	
	public double [][] getMatrixGauss()
	{
		if(mask == null){
			int maskWidth = this.getRadius()*2 +1;
			double [][] matrix = new double[maskWidth][maskWidth];
			for(int j=0;j<maskWidth;++j)
	        {
	            for(int i=0;i<maskWidth;++i)
	            {
	            	int x = i-(maskWidth/2);
	            	int y = j-(maskWidth/2);
	            	matrix[i][j]=(1/(2*Math.PI*sigma*sigma))*Math.pow(Math.E,-((Math.pow(x,2)+Math.pow(y,2))/(2*Math.pow(sigma,2))));
	            	if(matrix[i][j] > maxGauss){
	            		maxGauss = matrix[i][j];
	            	}
	            }
	        }
			mask = matrix;
		}
		return mask;
	}

	@Override
	public void readDialogResult(GenericDialog gd) {
		super.readDialogResult(gd);
		this.sigma = gd.getNextNumber();		
	}

	@Override
	public void prepareDialog(GenericDialog gd) {
		super.prepareDialog(gd);
		gd.addNumericField("Sigma", this.sigma , 1);
	}
	
	

}
