package com.code.pokerhand.app;

import java.util.List;
import java.util.TreeMap;

public class PlayerHand {
	private List<Card> playerCards;
	private int combinationRank;
	private Card highestCard;
	private boolean isSameSuit;
	private boolean isConsecutive;
	private Card decisionCard;
	private Card decisionSupportCard;
	private TreeMap<Card, Integer> cardFrequencyMap;
	
	public PlayerHand(List<Card> playerCards) {
		this.playerCards = playerCards;
	}

	public List<Card> getPlayerCards() {
		return playerCards;
	}

	public void setPlayerCards(List<Card> playerCards) {
		this.playerCards = playerCards;
	}

	public int getCombinationRank() {
		return combinationRank;
	}

	public void setCombinationRank(int combinationRank) {
		this.combinationRank = combinationRank;
	}

	public Card getHighestCard() {
		return highestCard;
	}

	public void setHighestCard(Card highestCard) {
		this.highestCard = highestCard;
	}

	public boolean isSameSuit() {
		return isSameSuit;
	}

	public void setSameSuit(boolean isSameSuit) {
		this.isSameSuit = isSameSuit;
	}

	public boolean isConsecutive() {
		return isConsecutive;
	}

	public void setConsecutive(boolean isConsecutive) {
		this.isConsecutive = isConsecutive;
	}

	public Card getDecisionCard() {
		return decisionCard;
	}

	public void setDecisionCard(Card decisionCard) {
		this.decisionCard = decisionCard;
	}

	public Card getDecisionSupportCard() {
		return decisionSupportCard;
	}

	public void setDecisionSupportCard(Card decisionSupportCard) {
		this.decisionSupportCard = decisionSupportCard;
	}

	public TreeMap<Card, Integer> getCardFrequencyMap() {
		return cardFrequencyMap;
	}

	public void setCardFrequencyMap(TreeMap<Card, Integer> cardCountMap) {
		this.cardFrequencyMap = cardCountMap;
	}

}
