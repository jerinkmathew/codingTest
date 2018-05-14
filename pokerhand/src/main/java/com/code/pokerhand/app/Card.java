package com.code.pokerhand.app;

public class Card implements Comparable<Card> {

	private String cardValue;
	private String cardSuit;

	public Card(String cardValue, String cardSuit) {
		this.cardValue = cardValue;
		this.cardSuit = cardSuit;
	}

	public String getCardValue() {
		return cardValue;
	}

	public void setCardValue(String cardValue) {
		this.cardValue = cardValue;
	}

	public String getCardSuit() {
		return cardSuit;
	}

	public void setCardSuit(String cardSuit) {
		this.cardSuit = cardSuit;
	}

	@Override
	public int hashCode() {
		return PokerCostants.CARD_VALUE.get(cardValue);
	}

	@Override
	public int compareTo(Card o) {
		return Integer.compare(PokerCostants.CARD_VALUE.get(this.cardValue), PokerCostants.CARD_VALUE.get(o.cardValue));
	}
}