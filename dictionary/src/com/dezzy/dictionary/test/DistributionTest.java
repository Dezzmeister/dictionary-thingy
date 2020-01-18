package com.dezzy.dictionary.test;

import com.dezzy.dictionary.stats.Distribution;

public final class DistributionTest {
	
	public static final void main(final String ... args) {
		final Distribution oddSet = new Distribution(1, 2, 5, 6, 7, 9, 12, 15, 18, 19, 27);
		final Distribution evenSet = new Distribution(21, 20, 16, 15, 11, 9, 8, 7, 5, 3);
		
		System.out.println("Odd Mean:\t " + oddSet.mean);
		System.out.println("Odd Stdev:\t " + oddSet.stdev);
		System.out.println("Odd Median:\t " + oddSet.median);
		System.out.println("Odd Q1:\t\t " + oddSet.quartile1);
		System.out.println("Odd Q3:\t\t " + oddSet.quartile3);
		
		System.out.println();
		System.out.println("Even Median:\t " + evenSet.median);
		System.out.println("Even Q1:\t " + evenSet.quartile1);
		System.out.println("Even Q3:\t " + evenSet.quartile3);
	}
}
