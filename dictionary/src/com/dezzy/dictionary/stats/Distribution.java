package com.dezzy.dictionary.stats;

import java.util.Arrays;

/**
 * A statistical distribution consisting of individual data points.
 * 
 * @author Joe Desmond
 */
public final class Distribution {
	
	/**
	 * The data points, sorted in ascending order
	 */
	private final float[] data;
	
	/**
	 * Population mean
	 */
	public final float mean;
	
	/**
	 * Population variance
	 */
	public final float variance;
	
	/**
	 * Population standard deviation
	 */
	public final float stdev;
	
	/**
	 * The median
	 */
	public final float median;
	
	/**
	 * The first quartile value
	 */
	public final float quartile1;
	
	/**
	 * The third quartile value
	 */
	public final float quartile3;
	
	/**
	 * The interquartile range
	 */
	public final float IQR;
	
	/**
	 * The minimum value
	 */
	public final float min;
	
	/**
	 * The maximum value
	 */
	public final float max;
	
	/**
	 * Constructs a distribution from the given data points. <br>
	 * <b>This constructor sorts the given array in ascending order</br>.
	 * 
	 * @param _data data points
	 */
	public Distribution(float ... _data) {
		data = _data;
		Arrays.sort(data);
		
		mean = mean(data);
		variance = variance(data, mean);
		stdev = (float) Math.sqrt(variance);
		median = medianOf(data, 0, data.length);
		quartile1 = medianOf(data, 0, data.length / 2);
		
		if (data.length % 2 == 1) {
			quartile3 = medianOf(data, (data.length / 2) + 1, data.length);
		} else {
			quartile3 = medianOf(data, data.length / 2, data.length);
		}
		
		IQR = quartile3 - quartile1;
		min = data[0];
		max = data[data.length - 1];
	}
	
	/**
	 * Calculates the median of the data within the specified range.
	 * 
	 * @param data dataset
	 * @param startIndex start index (inclusive)
	 * @param endIndex end index (exclusive)
	 * @return median within <code>(endIndex - startIndex)</code>
	 */
	private final float medianOf(final float[] data, final int startIndex, final int endIndex) {
		final int range = endIndex - startIndex;
		
		if (range % 2 == 1) {
			return data[startIndex + (range / 2)];
		} else {
			final float first = data[startIndex + (range / 2) - 1];
			final float second = data[startIndex + (range / 2)];
			
			return (first + second) / 2.0f;
		}
	}
	
	/**
	 * Calculates the variance of a dataset, given the mean.
	 * 
	 * @param data dataset
	 * @param mean mean of the dataset
	 * @return the variance
	 */
	private final float variance(final float[] data, final float mean) {
		//Mean squared error of the dataset
		float mse = 0;
		
		for (int i = 0; i < data.length; i++) {
			mse += (data[i] - mean) * (data[i] - mean);
		}
		
		return mse / data.length;		
	}
	
	/**
	 * Calculates the mean of a set of data points.
	 * 
	 * @param data data points
	 * @return the average
	 */
	private final float mean(final float[] data) {
		float sum = data[0];
		
		for (int i = 1; i < data.length; i++) {
			sum += data[i];
		}
		
		return sum / data.length;
	}
}
