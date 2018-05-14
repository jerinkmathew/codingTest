package com.code.pokerhand.app;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PokerCostants {
	public static final int HAND_SIZE = 5;
	public static final Map<String, Integer> CARD_VALUE = Collections.unmodifiableMap(new HashMap<String, Integer>() {
		{
			put("1", 1);
			put("2", 2);
			put("3", 3);
			put("4", 4);
			put("5", 5);
			put("6", 6);
			put("7", 7);
			put("8", 8);
			put("9", 9);
			put("T", 10);
			put("J", 11);
			put("Q", 12);
			put("K", 13);
			put("A", 14);
		}
	});

	public static final Map<Integer, String> HAND_COMBINATION_MAP = Collections
			.unmodifiableMap(new HashMap<Integer, String>() {
				{
					put(1, "Highest Card");
					put(2, "Pair");
					put(3, "Two Pair");
					put(4, "Three Of A Kind");
					put(5, "Straight");
					put(6, "Flush");
					put(7, "Full House");
					put(8, "Four Of A Kind");
					put(9, "Srtaight Flush");
					put(10, "Royal Flush");
				}
			});

}
