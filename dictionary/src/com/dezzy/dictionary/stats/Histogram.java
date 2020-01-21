package com.dezzy.dictionary.stats;

import java.awt.image.BufferedImage;

public final class Histogram {
	
	/**
	 * Pixel width of one bar in the image
	 */
	private static final int BIN_WIDTH_PIXELS = 50;
	
	/**
	 * Pixel height of the margin below the histogram in the image
	 */
	private static final int LOWER_MARGIN_PIXELS = 50;
	
	/**
	 * The distribution represented by this histogram
	 */
	public final Distribution distribution;
	
	/**
	 * An array containing the number of data points in each bin
	 */
	public final int[] bins;
	
	/**
	 * The width of each bin
	 */
	public final float binWidth;
	
	/**
	 * Creates a histogram with the given distribution, number of bins, and bin width.
	 * 
	 * @param _distribution distribution
	 * @param _bins number of bins
	 * @param _binWidth width of each bin
	 */
	public Histogram(final Distribution _distribution, final int binCount, final float _binWidth) {
		distribution = _distribution;
		binWidth = _binWidth;
		bins = getBins(distribution, binCount, binWidth);
	}
	
	/**
	 * Constructs a histogram with the given distribution. The number of bins and bin width are set automatically.
	 * 
	 * @param _distribution distribution
	 */
	public Histogram(final Distribution _distribution) {
		this(_distribution, idealBinCount(_distribution), idealBinWidth(_distribution));
	}
	
	/**
	 * Calculates the frequency for each bin of a histogram given a distribution, desired bin count, and desired bin width.
	 * 
	 * @param distribution the distribution
	 * @param binCount bin count
	 * @param binWidth bin width
	 * @return bins
	 */
	private static final int[] getBins(final Distribution distribution, final int binCount, final float binWidth) {
		final int[] bins = new int[binCount];
		
		float min = distribution.min;
		float max = binWidth;
		int binIndex = 0;
		
		control: for (int i = 0; i < distribution.size; i++) {
			final float value = distribution.data[i];
			
			//While the value is outside the bin range. Does not check for min bound, because the data array is sorted.
			while (value > max) {
				min = max;
				max = min + binWidth;
				binIndex++;
				
				if (binIndex >= binCount) {
					break control;
				}
			}
			
			bins[binIndex]++;
		}
		
		return bins;
	}
	
	//TODO: Finish this
	private static final BufferedImage draw(final int[] bins, final float binWidth) {
		final int width = BIN_WIDTH_PIXELS * bins.length;
		final int height = width;
		
		return null;
	}
	
	/**
	 * Calculates the ideal number of bins for a distribution.
	 * 
	 * @param distribution the distribution
	 * @return ideal number of bins
	 */
	private static final int idealBinCount(final Distribution distribution) {
		return 1 + (int)(Math.sqrt(distribution.size));
	}
	
	/**
	 * Calculates the ideal bin count and ideal width of each bin for a distribution.
	 * 
	 * @param distribution the distribution
	 * @return ideal bin width
	 */
	private static final float idealBinWidth(final Distribution distribution) {
		final float binCount = idealBinCount(distribution);
		return distribution.range / binCount;
	}
}
