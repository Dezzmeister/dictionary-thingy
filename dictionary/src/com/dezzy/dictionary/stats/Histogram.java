package com.dezzy.dictionary.stats;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A histogram, backed by a distribution. A histogram contains information on its bins, as well as an image.
 *
 * @author Joe Desmond
 */
public final class Histogram {
	
	/**
	 * Pixel width of one bar in the image
	 */
	private static final int BIN_WIDTH_PIXELS = 50;
	
	/**
	 * Pixel height of the margins below and to the left of the histogram in the image
	 */
	private static final int MARGIN_PIXELS = 50;
	
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
	 * The histogram image
	 */
	public final BufferedImage image;
	
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
		image = draw(bins, binWidth);
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
	 * Saves the image of this histogram to the given location, with the given extension.
	 * 
	 * @param path path to save this histogram to (ex. <code>"stats/histogram.png"</code>)
	 * @param format the image format (ex. <code>"png", "jpg", etc.</code>)
	 * @throws IOException if there is a problem saving the image
	 */
	public final void saveTo(final String path, final String format) throws IOException {
		final File destination = new File(path);
		ImageIO.write(image, format, destination);
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
		final int maxBinValue = max(bins);
		
		final BufferedImage histogramImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D hg2 = (Graphics2D) histogramImage.createGraphics();
		hg2.setColor(Color.WHITE);
		hg2.fillRect(0, 0, width, height);
		
		hg2.setColor(Color.GREEN);
		for (int i = 0; i < bins.length; i++) {
			final int count = bins[i];
			final int pixelHeight = (int) ((count / ((float) maxBinValue)) * height);
			final int x = i * BIN_WIDTH_PIXELS;
			final int y = width - pixelHeight;
			
			hg2.fillRect(x, y, BIN_WIDTH_PIXELS, pixelHeight);
		}
		
		hg2.dispose();
		
		return histogramImage;
	}
	
	/**
	 * Returns the maximum value in an array.
	 * 
	 * @param array array
	 * @return max value in the array
	 */
	private static final int max(final int[] array) {
		int max = array[0];
		
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		
		return max;
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
