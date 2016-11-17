package ue3.transform;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import ue3.utility.Image2D;

public class MutualInformationDifference implements ImageDifference{

	@Override
	public double calculateDifference(Image2D image1, Image2D image2) {
		return calculateMutualInformation(image1, image2) * -1;
	}
	
	public double calculateMutualInformation(Image2D image1, Image2D image2) {
		return getEntropyOfImg(image1) + getEntropyOfImg(image2)
		- getEntropyOfImages(image1, image2);
	}

	private double getEntropyOfImages(Image2D image1, Image2D image2) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		int[] array1 = image1.asArray();
		int[] array2 = image2.asArray();
		
		for(int i = 0; i< array1.length; i++){
			String key = array1[i] + "|" + array2[i];
			if(map.containsKey(key)){
				map.put(key,map.get(key)+1);
			}else {
				map.put(key,1);
			}
		}
		
		double sum = 0.0;
		for(Entry<String,Integer> entry : map.entrySet()){
			double p = ((double) entry.getValue()) / array1.length;
			sum += p * (Math.log(p) / Math.log(2));
		}
		return sum * -1;
	}

	private double getEntropyOfImg(Image2D image) {
		return getEntropyOfInts(image.asArray());
	}
	
	private double getEntropyOfInts(int[] array){
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		for(int value : array){
			if(map.containsKey(value)){
				map.put(value,map.get(value)+1);
			}else {
				map.put(value,1);
			}
		}
		
		double sum = 0.0;
		for(Entry<Integer,Integer> entry : map.entrySet()){
			double p = ((double) entry.getValue()) / array.length;
			sum += p * (Math.log(p) / Math.log(2));
		}
		return sum * -1;
	}
	
	@Override
	public String getName() {
		return "Mutual Information Difference";
	}

}
