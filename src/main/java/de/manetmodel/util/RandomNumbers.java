package de.manetmodel.util;

import java.util.Random;

public class RandomNumbers
{

	public static int getRandom(int min, int max)
	{
		if (min == max)
			return min;
		Random random = new Random();
		return random.nextInt(max - min) + min;
	}

	public static double getRandom(double min, double max)
	{
		if (min == max)
			return min;
		Random random = new Random();
		return min + (max - min) * random.nextDouble();
	}
}
