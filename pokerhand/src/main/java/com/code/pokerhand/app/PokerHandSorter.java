package com.code.pokerhand.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.stream.Stream;

public class PokerHandSorter {

	private PokerPlayer playerOne;
	private PokerPlayer playerTwo;

	Card highestValueCard = null;
	private boolean isSameSuit;
	private int highestCardValue = 0;
	private Card card = null;
	private int playerHandRank = 1;
	private boolean isFrequencyFour = false;
	private boolean isFullHouseSet = false;
	private boolean isFrequencyThree = false;
	private boolean isPairOne = false;
	private boolean isPairTwo = false;
	private boolean isPairOneSet = false;
	private String cardSuit = "";

	// Starting point of the application
	public static void main(String[] args) {
		String fileName = "";
		if (0 < args.length) {
			fileName = args[0];
		} else {
			System.out.println("File name needs to be provided.");
			System.exit(0);
		}
		PokerHandSorter sorter = new PokerHandSorter();
		sorter.playerOne = new PokerPlayer();
		sorter.playerTwo = new PokerPlayer();
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			stream.forEach(playerHands -> {
				sorter.evaluateHand(playerHands);
			});
		} catch (IOException e) {
			System.out.println("Exception occured while reading the document: " + fileName);
		}
		System.out.println("Player 1 : " + sorter.playerOne.getWins());
		System.out.println("Player 2 : " + sorter.playerTwo.getWins());
	}

	// major operations to be done in the hands from the input
	public void evaluateHand(String playingCards) {
		if (!playingCards.isEmpty()) {
			PlayerHand playerOneHand = identifyPlayerHand(playingCards.substring(0, playingCards.length() / 2));
			PlayerHand playerTwoHand = identifyPlayerHand(
					playingCards.substring(playingCards.length() / 2 + 1, playingCards.length()));
			playerOneHand = analyzePlayerHand(playerOneHand);
			playerTwoHand = analyzePlayerHand(playerTwoHand);
			playerOneHand = calculateHandRank(playerOneHand);
			playerTwoHand = calculateHandRank(playerTwoHand);
			if (playerOneHand.getCombinationRank() > playerTwoHand.getCombinationRank()) {
				playerOne.setWins(playerOne.getWins() + 1);
			} else if (playerOneHand.getCombinationRank() < playerTwoHand.getCombinationRank()) {
				playerTwo.setWins(playerTwo.getWins() + 1);
			} else {
				breakTheTie(playerOneHand, playerTwoHand);
			}
		}
	}

	// Identifies each card and adding to the player's hand as a card.
	private PlayerHand identifyPlayerHand(String stringPlayerHand) {
		List<String> playersHand = Arrays.asList(stringPlayerHand.split(" "));
		List<Card> playerCards = new ArrayList<Card>();
		playersHand.forEach(eachCard -> {
			card = new Card(eachCard.split("")[0], eachCard.split("")[1]);
			playerCards.add(card);
		});
		PlayerHand playerHand = new PlayerHand(playerCards);
		return playerHand;
	}

	// Analyzes the players hand and identify the key parameters.
	private PlayerHand analyzePlayerHand(PlayerHand playerHand) {
		TreeMap<Card, Integer> cardCountMap = new TreeMap<Card, Integer>();
		highestCardValue = 0;
		highestValueCard = null;
		isSameSuit = true;
		cardSuit = "";
		playerHand.getPlayerCards().forEach(card -> {
			if ("".equals(cardSuit)) {
				cardSuit = card.getCardSuit();
			} else {
				if (!cardSuit.equals(card.getCardSuit())) {
					isSameSuit = false;
				}
			}
			if (highestCardValue < PokerCostants.CARD_VALUE.get(card.getCardValue())) {
				highestCardValue = PokerCostants.CARD_VALUE.get(card.getCardValue());
				highestValueCard = card;
			}
			Object cardCount = cardCountMap.get(card);
			if (null != cardCount) {
				cardCountMap.put(card, (Integer) cardCount + 1);
			} else {
				cardCountMap.put(card, 1);
			}
		});
		playerHand.setHighestCard(highestValueCard);
		playerHand.setSameSuit(isSameSuit);
		playerHand.setCardFrequencyMap(cardCountMap);
		playerHand.setConsecutive(checkForConsecutiveCards(cardCountMap));
		return playerHand;
	}

	// calculating the rank of each hand.
	private PlayerHand calculateHandRank(PlayerHand playerHand) {
		playerHandRank = 1;
		isFrequencyFour = false;
		isFullHouseSet = false;
		isFrequencyThree = false;
		isPairOne = false;
		isPairTwo = false;
		isPairOneSet = false;
		// checking all Royal flush, Straight Flush and Straight combinations (
		// consecutive cards)
		if (playerHand.isConsecutive()) {
			if (playerHand.isSameSuit() && PokerCostants.CARD_VALUE.get(playerHand.getHighestCard()) == 14)
				playerHandRank = 10;
			else if (playerHand.isSameSuit() && PokerCostants.CARD_VALUE.get(playerHand.getHighestCard()) != 14)
				playerHandRank = 9;
			else
				playerHandRank = 5;
		} else {
			// if the cards are non-consecutive.
			playerHand.getCardFrequencyMap().descendingMap().forEach((card, frequency) -> {
				if (frequency == 4) {
					isFrequencyFour = true;
					playerHandRank = 8;
					playerHand.setDecisionCard(card);
				} else if (frequency == 3) {
					isFrequencyThree = true;
					playerHand.setDecisionCard(card);
				} else if (frequency == 2 && !isPairOne) {
					isPairOne = true;
					if (isFrequencyThree) {
						playerHand.setDecisionSupportCard(card);
					} else {
						playerHand.setDecisionCard(card);
					}

				} else if (frequency == 2 && isPairOneSet) {
					isPairTwo = true;
					playerHand.setDecisionSupportCard(card);
				}
				if (isPairOne)
					isPairOneSet = true;
			});
			if (isFrequencyThree && isPairOne) {
				isFullHouseSet = true;
				playerHandRank = 7;
			}
			if (playerHand.isSameSuit() && !isFrequencyFour && !isFullHouseSet) {
				playerHandRank = 6;
				playerHand.setDecisionCard(playerHand.getHighestCard());
			}
			if (isFrequencyThree && !isPairOne) {
				playerHandRank = 4;
			}
			if (isPairOne && isPairTwo) {
				playerHandRank = 3;
			}
			if (isPairOne && !isPairTwo) {
				playerHandRank = 2;
			}
		}
		playerHand.setCombinationRank(playerHandRank);
		return playerHand;
	}

	// Checking The cards for consecutiveness
	private boolean checkForConsecutiveCards(TreeMap<Card, Integer> cardCountMap) {
		int lowestValue = PokerCostants.CARD_VALUE.get(cardCountMap.firstKey().getCardValue());
		int highestValue = PokerCostants.CARD_VALUE.get(cardCountMap.lastKey().getCardValue());
		if (cardCountMap.keySet().size() == PokerCostants.HAND_SIZE
				&& (highestValue - lowestValue) == PokerCostants.HAND_SIZE - 1)
			return true;
		return false;
	}

	// breaking the tie based on the decision card and decision supporting cards.
	// decision supporting cards are used for two pairs, and full house
	private void breakTheTie(PlayerHand playerOneHand, PlayerHand playerTwoHand) {
		if (playerOneHand.getCombinationRank() != 1) { // if rank=1, no need to check decision card.
			int playerOneDecisionCardValue = PokerCostants.CARD_VALUE
					.get(playerOneHand.getDecisionCard().getCardValue());
			int playerTwoDecisionCardValue = PokerCostants.CARD_VALUE
					.get(playerTwoHand.getDecisionCard().getCardValue());
			if (playerOneDecisionCardValue > playerTwoDecisionCardValue) {
				playerOne.setWins(playerOne.getWins() + 1);
			} else if (playerOneDecisionCardValue < playerTwoDecisionCardValue) {
				playerTwo.setWins(playerTwo.getWins() + 1);
			} else if (null != playerOneHand.getDecisionSupportCard()
					&& null != playerTwoHand.getDecisionSupportCard()) {

				int playerOneDecisionSupportCardValue = PokerCostants.CARD_VALUE
						.get(playerOneHand.getDecisionSupportCard().getCardValue());
				int playerTwoDecisionSupportCardValue = PokerCostants.CARD_VALUE
						.get(playerTwoHand.getDecisionSupportCard().getCardValue());
				if (playerOneDecisionSupportCardValue > playerTwoDecisionSupportCardValue) {
					playerOne.setWins(playerOne.getWins() + 1);
				} else if (playerOneDecisionSupportCardValue < playerTwoDecisionSupportCardValue) {
					playerTwo.setWins(playerTwo.getWins() + 1);
				} else {
					findWinnerByHighestCard(playerOneHand.getCardFrequencyMap().descendingKeySet(),
							playerTwoHand.getCardFrequencyMap().descendingKeySet());
				}
			} else {
				findWinnerByHighestCard(playerOneHand.getCardFrequencyMap().descendingKeySet(),
						playerTwoHand.getCardFrequencyMap().descendingKeySet());
			}
		} else {
			findWinnerByHighestCard(playerOneHand.getCardFrequencyMap().descendingKeySet(),
					playerTwoHand.getCardFrequencyMap().descendingKeySet());
		}
	}

	// if all other combination cards ties, check for the highest value card.
	private void findWinnerByHighestCard(NavigableSet<Card> playerOneHandSet, NavigableSet<Card> playerTwoHandSet) {
		int playerOneHighestCardValue = PokerCostants.CARD_VALUE.get(playerOneHandSet.first().getCardValue());
		int playerTwoHighestCardValue = PokerCostants.CARD_VALUE.get(playerTwoHandSet.first().getCardValue());
		if (playerOneHighestCardValue > playerTwoHighestCardValue) {
			playerOne.setWins(playerOne.getWins() + 1);
		} else if (playerOneHighestCardValue < playerTwoHighestCardValue) {
			playerTwo.setWins(playerTwo.getWins() + 1);
		} else {
			playerOneHandSet.remove(playerOneHandSet.first());
			playerTwoHandSet.remove(playerTwoHandSet.first());
			if (playerOneHandSet.size() > 0 && playerTwoHandSet.size() > 0) {
				findWinnerByHighestCard(playerOneHandSet, playerTwoHandSet);
			}
		}
	}
}
