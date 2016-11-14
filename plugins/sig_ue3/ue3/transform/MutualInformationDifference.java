package ue3.transform;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ue3.utility.Image2D;

public class MutualInformationDifference implements ImageDifference{

	@Override
	public double calculateDifference(Image2D image1, Image2D image2) {
		return getEntropyOfImg(image1) + getEntropyOfImg(image2)
		- getEntropyOfImages(image1, image2);
	}

	private double getEntropyOfImages(Image2D image1, Image2D image2) {
		return getEntropyOfInts(joinArrays(image1.asArray(), image2.asArray()));
	}

	private double getEntropyOfImg(Image2D image) {
		return getEntropyOfInts(image.asArray());
	}
	
	private double getEntropyOfInts(int[] array){
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		for(int value : array){
			map.put(value, map.getOrDefault(value, 0)+1);
		}
		
		double sum = 0.0;
		for(Entry<Integer,Integer> entry : map.entrySet()){
			double p = entry.getValue() / array.length;
			sum += p * (Math.log(p) / Math.log(2));
		}
		return sum * -1;
	}
	
	public int[] joinArrays(int[] array1, int[] array2){
		int[] array1and2 = new int[array1.length + array2.length];
		System.arraycopy(array1, 0, array1and2, 0, array1.length);
		System.arraycopy(array2, 0, array1and2, array1.length, array2.length);
		return array1and2;
	}

}
