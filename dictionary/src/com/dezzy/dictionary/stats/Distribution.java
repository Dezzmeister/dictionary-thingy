package com.dezzy.dictionary.stats;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * A statistical distribution consisting of individual data points.
 * 
 * @author Joe Desmond
 */
public final class Distribution {
	
	/**
	 * The name of the distribution
	 */
	public final String name;
	
	/**
	 * Extra info about the distribution
	 */
	public final String auxInfo;
	
	/**
	 * The data points, sorted in ascending order
	 */
	public final float[] data;
	
	/**
	 * Number of data points
	 */
	public final float size;
	
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
	 * The range of this data
	 */
	public final float range;
	
	/**
	 * Pearson's moment coefficient of skewness, accounting for sample size
	 */
	public final float skewness;
	
	/**
	 * The kurtosis
	 */
	public final float kurtosis;
	
	/**
	 * Constructs a distribution from the given data points and calculates various statistics. <br>
	 * <b>This constructor sorts the given array in ascending order</b>.
	 * 
	 * @param _name name of the distribution
	 * @param _auxInfo extra info about the distribution (for example, <code>"Time is measured in minutes"</code>).
	 * 				   This exists primarily to add more info in {@link #toString()}
	 * @param _data data points
	 */
	public Distribution(final String _name, final String _auxInfo, float ... _data) {
		name = _name;
		auxInfo = _auxInfo;
		data = _data;
		Arrays.sort(data);
		
		size = data.length;
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
		range = max - min;
		
		skewness = skewness(data, mean, stdev);
		kurtosis = kurtosis(data, mean, stdev);
	}
	
	/**
	 * Constructs a distribution from the given data points and calculates various statistics. <br>
	 * <b>This constructor sorts the given array in ascending order</b><br>
	 * Functions exactly like {@link Distribution#Distribution(String, String, float...) this constructor},
	 * except the empty string is passed in as <code>auxInfo</code>.
	 * 
	 * @param _name name of the distribution
	 * @param _data data points
	 */
	public Distribution(final String _name, float ... _data) {
		this(_name, "", _data);
	}
	
	/**
	 * Calculates the Pearson's moment coefficient of skewness.
	 * 
	 * @param data dataset
	 * @param mean population mean
	 * @param stdev population standard deviation
	 * @return skewness
	 */
	private static final float skewness(final float[] data, final float mean, final float stdev) {
		final float n = data.length;
		final float constant = (n / ((n - 1) * (n - 2) * (float) Math.pow(stdev, 3)));
		float sum = 0;
		
		for (int i = 0; i < data.length; i++) {
			sum += Math.pow(data[i] - mean, 3);
		}
		
		return constant * sum;
	}
	
	/**
	 * Calculates the kurtosis.
	 * 
	 * @param data dataset
	 * @param mean population mean
	 * @param stdev population standard deviation
	 * @return kurtosis
	 */
	private static final float kurtosis(final float[] data, final float mean, final float stdev) {
		final float n = data.length;
		final float coeff = (n * (n + 1)) / ((n - 1) * (n - 2) * (n - 3) * (float) Math.pow(stdev, 4));
		final float term2 = (3 * (n - 1) * (n - 1)) / ((n - 2) * (n - 3));
		float sum = 0;
		
		for (int i = 0; i < data.length; i++) {
			sum += Math.pow(data[i] - mean, 4);
		}
		
		return (coeff * sum) - term2;
	}
	
	/**
	 * Calculates the median of the data within the specified range.
	 * 
	 * @param data dataset
	 * @param startIndex start index (inclusive)
	 * @param endIndex end index (exclusive)
	 * @return median within <code>(endIndex - startIndex)</code>
	 */
	private static final float medianOf(final float[] data, final int startIndex, final int endIndex) {
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
	private static final float variance(final float[] data, final float mean) {
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
	private static final float mean(final float[] data) {
		float sum = data[0];
		
		for (int i = 1; i < data.length; i++) {
			sum += data[i];
		}
		
		return sum / data.length;
	}
	
	/**
	 * Returns a multi-line String with the name and auxiliary info of this distribution as a header, and statistics on their own lines. <br>
	 * <b>NOTE: Uses reflection to get names and values for each statistic!</b>
	 * 
	 * @return a list of this distribution's statistics
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(name);
		
		if (!auxInfo.equals("")) {
			sb.append(System.lineSeparator());
			sb.append(auxInfo);
		}
		
		final Field[] fields = getClass().getDeclaredFields();
		
		for (final Field field : fields) {
			if (field.getType() == float.class) {
				sb.append(System.lineSeparator());
				sb.append("\t");
				sb.append(field.getName());
				sb.append(":\t\t");
				try {
					sb.append(field.get(this));
				} catch (Exception e) {
					e.printStackTrace();
					sb.append("ERROR");
				}
			}
		}
		
		return sb.toString();
	}
}
