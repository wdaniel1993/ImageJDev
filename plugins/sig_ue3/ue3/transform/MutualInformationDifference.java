package ue3.transform;

import java.util.HashMap;
import java.util.Iterator;
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

	private double getEntropyOfImg(Image2D image) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		Iterator<Integer> bytes = image.iterator();
		
		while(bytes.hasNext()){
			int value = bytes.next();
			if(map.containsKey(value)){
				map.put(value,map.get(value)+1);
			}else {
				map.put(value,1);
			}
		}
		
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
