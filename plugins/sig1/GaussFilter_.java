import ij.gui.GenericDialog;

public class GaussFilter_ extends AbstractMaskFilter {

	private double[][] mask;
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
	protected String getFilterName() {
		// TODO Auto-generated method stub
		return "Gauss Filter";
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
	            	matrix[i][j]=Math.pow(Math.E,-((Math.pow(x,2)+Math.pow(y,2))/(2*Math.pow(sigma,2))));
	            }
	        }
			mask = matrix;
		}
		return mask;
	}

	@Override
	protected void readDialogResult(GenericDialog gd) {
		this.sigma = (int)gd.getNextNumber();		
	}

	@Override
	protected void prepareDialog(GenericDialog gd) {
		gd.addNumericField("Sigma", this.sigma , 0);
	}
	
	

}
