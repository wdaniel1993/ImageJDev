package ue3.transform;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import ue3.utility.Image2D;

/**
 * MutualInformationDifference
 * Calculate mutual information difference
 *
 */
public class MutualInformationDifference implements ImageDifference{

	@Override
	public double calculateDifference(Image2D image1, Image2D image2) {
		//higher the mutual information means less difference, so the value gets multiplied by -1
		return calculateMutualInformation(image1, image2) * -1;
	}
	
	//Calculate mutual infomation with the formular H(A) + H(B) - H(A,B)
	public double calculateMutualInformation(Image2D image1, Image2D image2) {
		return getEntropyOfImg(image1) + getEntropyOfImg(image2)
		- getEntropyOfImages(image1, image2);
	}

	private double getEntropyOfImages(Image2D image1, Image2D image2) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		Iterator<Integer> bytes1 = image1.iterator();
		Iterator<Integer> bytes2 = image2.iterator();
		
		while(bytes1.hasNext()){
			int a = bytes1.next();
			int b = bytes2.next();
			String key = a + "|" + b;
			if(map.containsKey(key)){
				map.put(key,map.get(key)+1);
			}else {
				map.put(key,1);
			}
		}
		
		double sum = 0.0;
		for(Entry<String,Integer> entry : map.entrySet()){
			double p = ((double) entry.getValue()) / image1.getPointCount();
			sum += p * (Math.log(p) / Math.log(2));
		}
		return sum * -1;
	}

	//Calculate entropy of image
	private double getEntropyOfImg(Image2D image) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		Iterator<Integer> bytes = image.iterator();
		
		//Calculate histogram
		while(bytes.hasNext()){
			int value = bytes.next();
			if(map.containsKey(value)){
				map.put(value,map.get(value)+1);
			}else {
				map.put(value,1);
			}
		}
		
		//Calculate entropoy with the sum of each probability of a scalar value multiplied by the log2 of the probability
		double sum = 0.0;
		for(Entry<Integer,Integer> entry : map.entrySet()){
			double p = ((double) entry.getValue()) / image.getPointCount();
			sum += p * (Math.log(p) / Math.log(2));
		}
		return sum * -1;
	}
	
	
	@Override
	public String getName() {
		return "Mutual Information Difference";
	}

}
