import java.util.ArrayList;
import java.util.Collections;

/**
 * This is the only class you should edit.
 * @author You
 *
 */
public class Player {
	// Add whatever variables you want. You MAY NOT use static variables, or otherwise allow direct communication between
	// different instances of this class by any means; doing so will result in a score of 0.

	// knowledge about hands
	private Hand myHand;
	private Hand truePartnerHand;

	// knowledge about the board
	private int numHints;
	private int numFuses;
	private ArrayList<Card> possibleRemainingCards;
	private ArrayList<Integer> tableau;
	private ArrayList<Card> discardPile;

	// partner's imperfect knowledge based on our hints
	private PartnerKnowledge partner;

	private boolean sawPartnerHand;

	/**
	 * This default constructor should be the only constructor you supply.
	 */
	public Player() {
		// initialize our hand knowledge
		this.myHand = new Hand();
		try {
			for (int i = 0; i < 5; i++) {
				this.myHand.add(i, new Card(-1, -1));
			}
		} catch (Exception e) {
			System.out.println("Error in Player constructor");
		}
		this.truePartnerHand = new Hand();
		this.sawPartnerHand = false;

		// initialize our board knowledge
		this.numHints = 8;
		this.numFuses = 3;
		this.possibleRemainingCards = new ArrayList<Card>();
		for (int i = 0; i < 5; i++) {
			for (int j = 1; j < 6; j++) {
				this.possibleRemainingCards.add(new Card(i, j));
				if (i == 1) {
				this.possibleRemainingCards.add(new Card(i, j));
				}
				if (i < 5) {
				this.possibleRemainingCards.add(new Card(i, j));
				}
			}
		}
		this.tableau = new ArrayList<Integer>();
		for (int i = 0; i < 5; i++) {
			this.tableau.add(0);
		}
		this.discardPile = new ArrayList<Card>();

		// initialize partner's knowledge
		this.partner = new PartnerKnowledge();
	}
	
	/**
	 * This method runs whenever your partner discards a card.
	 * @param startHand The hand your partner started with before discarding.
	 * @param discard The card he discarded.
	 * @param disIndex The index from which he discarded it.
	 * @param draw The card he drew to replace it; null, if the deck is empty.
	 * @param drawIndex The index to which he drew it.
	 * @param finalHand The hand your partner ended with after redrawing.
	 * @param boardState The state of the board after play.
	 * @throws Exception
	 */
	public void tellPartnerDiscard(Hand startHand, Card discard, int disIndex, Card draw, int drawIndex, 
			Hand finalHand, Board boardState) throws Exception {
		startHand.remove(disIndex);
		startHand.add(drawIndex, draw);
		possibleRemainingCards.remove(draw);
		numHints = boardState.numHints;
		finalHand = new Hand(startHand);
		this.discardPile.add(discard);
		this.truePartnerHand = finalHand;

		// update what we think our partner knows about their own hand
		this.partner.hand.remove(disIndex);
		this.partner.hand.add(drawIndex, new Card(-1, -1));
	}
	
	/**
	 * This method runs whenever you discard a card, to let you know what you discarded.
	 * @param discard The card you discarded.
	 * @param boardState The state of the board after play.
	 */
	public void tellYourDiscard(Card discard, Board boardState) {
		numHints = boardState.numHints;
		possibleRemainingCards.remove(discard);
		this.discardPile.add(discard);
	}
	
	/**
	 * This method runs whenever your partner played a card
	 * @param startHand The hand your partner started with before playing.
	 * @param play The card she played.
	 * @param playIndex The index from which she played it.
	 * @param draw The card she drew to replace it; null, if the deck was empty.
	 * @param drawIndex The index to which she drew the new card.
	 * @param finalHand The hand your partner ended with after playing.
	 * @param wasLegalPLay Whether the play was legal or not.
	 * @param boardState The state of the board after play.
	 */
	public void tellPartnerPlay(Hand startHand, Card play, int playIndex, Card draw, int drawIndex, Hand finalHand,
			boolean wasLegalPlay, Board boardState) throws Exception {
		// update what we think our partner knows about their own hand
		this.partner.hand.remove(playIndex);
		this.partner.hand.add(drawIndex, new Card(-1, -1));

		// update what we think our partner knows about the board
		this.partner.possibleRemainingCards.remove(play);

		// update what we know about our partner's hand
		this.truePartnerHand = finalHand;

		// update what we know about the board
		this.possibleRemainingCards.remove(draw);
		this.numHints = boardState.numHints;
		this.numFuses = boardState.numFuses;
		this.tableau = boardState.tableau;
		this.discardPile = boardState.discards;
	}
	
	/**
	 * This method runs whenever you play a card, to let you know what you played.
	 * @param play The card you played.
	 * @param wasLegalPlay Whether the play was legal or not.
	 * @param boardState The state of the board after play.
	 */
	public void tellYourPlay(Card play, boolean wasLegalPlay, Board boardState) {
		// update what we know about the board
		this.possibleRemainingCards.remove(play);
		this.numHints = boardState.numHints;
		this.numFuses = boardState.numFuses;
		this.tableau = boardState.tableau;
		this.discardPile = boardState.discards;
	}
	
	/**
	 * This method runs whenever your partner gives you a hint as to the color of your cards.
	 * @param color The color hinted, from Colors.java: RED, YELLOW, BLUE, GREEN, or WHITE.
	 * @param indices The indices (from 0-4) in your hand with that color.
	 * @param partnerHand Your partner's current hand.
	 * @param boardState The state of the board after the hint.
	 */
	public void tellColorHint(int color, ArrayList<Integer> indices, Hand partnerHand, Board boardState) throws Exception {
		// update what we know about our partner's hand
		this.truePartnerHand = partnerHand;

		// update what we know about our own hand
		for (int i : indices) {
			Card c = this.myHand.remove(i);
			Card newCard = new Card(color, c.value);
			this.myHand.add(i, newCard);
		}

		// update what we know about the board
		this.numHints = boardState.numHints;
	}
	
	/**
	 * This method runs whenever your partner gives you a hint as to the numbers on your cards.
	 * @param number The number hinted, from 1-5.
	 * @param indices The indices (from 0-4) in your hand with that number.
	 * @param partnerHand Your partner's current hand.
	 * @param boardState The state of the board after the hint.
	 */
	public void tellNumberHint(int number, ArrayList<Integer> indices, Hand partnerHand, Board boardState) throws Exception {
		// update what we know about our partner's hand
		this.truePartnerHand = partnerHand;

		// update what we know about our own hand
		for (int i : indices) {
			Card c = this.myHand.remove(i);
			Card newCard = new Card(c.color, number);
			this.myHand.add(i, newCard);
		}

		// update what we know about the board
		this.numHints = boardState.numHints;
	}
	
	/**
	 * This method runs when the game asks you for your next move.
	 * @param yourHandSize How many cards you have in hand.
	 * @param partnerHand Your partner's current hand.
	 * @param boardState The current state of the board.
	 * @return A string encoding your chosen action. Actions should have one of the following formats; in all cases,
	 *  "x" and "y" are integers.
	 * 	a) "PLAY x y", which instructs the game to play your card at index x and to draw a card back to index y. You
	 *     should supply an index y even if you know the deck to be empty. All indices should be in the range 0-4.
	 *     Illegal plays will consume a fuse; at 0 fuses, the game ends with a score of 0.
	 *  b) "DISCARD x y", which instructs the game to discard the card at index x and to draw a card back to index y.
	 *     You should supply an index y even if you know the deck to be empty. All indices should be in the range 0-4.
	 *     Discarding returns one hint if there are fewer than the maximum number available.
	 *  c) "NUMBERHINT x", where x is a value from 1-5. This command informs your partner which of his cards have a value
	 *     of the chosen number. An error will result if none of his cards have that value, or if no hints remain.
	 *     This command consumes a hint.
	 *  d) "COLORHINT x", where x is one of the RED, YELLOW, BLUE, GREEN, or WHITE constant values in Colors.java.
	 *     This command informs your partner which of his cards have the chosen color. An error will result if none of
	 *     his cards have that color, or if no hints remain. This command consumes a hint.
	 */
	public String ask(int yourHandSize, Hand partnerHand, Board boardState) throws Exception {
		// use our partner's hand to update what we know about the board
		if (!sawPartnerHand) {
			for (int i = 0; i < partnerHand.size(); i++) {
				this.possibleRemainingCards.remove(partnerHand.get(i));
			}
			this.sawPartnerHand = true;
		}

		// before we make any decisions, make sure we have the most up-to-date information
		this.truePartnerHand = partnerHand;
		this.numHints = boardState.numHints;
		this.numFuses = boardState.numFuses;

		// if we have a playable card (i.e. a card we can play without risk), play it
		int playableCardIndex = getPlayableCardIndex();
		if (playableCardIndex != -1) {
			this.myHand.remove(playableCardIndex);
			this.myHand.add(playableCardIndex, new Card(-1, -1));
			return "PLAY " + playableCardIndex + " " + playableCardIndex;
		} 

		if (this.numHints > 0) {
			// if our partner has a playable card but is unaware of it, give them a hint
			int partnerPlayableCardIndex = getUnawarePartnerPlayableCardIndex();
			if (partnerPlayableCardIndex != -1) {
				Card knownCard = this.truePartnerHand.get(partnerPlayableCardIndex);
				Card possibleCard = this.partner.hand.get(partnerPlayableCardIndex);
				if (possibleCard.color == -1) {
					this.partner.updateHandColorKnowledge(knownCard.color, partnerHand);
					return "COLORHINT " + knownCard.color;
				} else if (possibleCard.value == -1) {
					this.partner.updateHandValueKnowledge(knownCard.value, partnerHand);
					return "NUMBERHINT " + knownCard.value;
				}
			}

			// if that doesn't work, give our partner a generally useful hint
			Hint bestHint = getBestOverallHint();
			if (bestHint != null) {
				if (bestHint.type) {
					this.partner.updateHandColorKnowledge(bestHint.value, partnerHand);
					return "COLORHINT " + bestHint.value;
				} else if (!bestHint.type) {
					this.partner.updateHandValueKnowledge(bestHint.value, partnerHand);
					return "NUMBERHINT " + bestHint.value;
				}
			}
		}

		// if we have a card that we know is not immediately playable, discard it
		int discardableCardIndex = getDiscardableCardIndex();
		if (discardableCardIndex != -1) {
			this.myHand.remove(discardableCardIndex);
			this.myHand.add(discardableCardIndex, new Card(-1, -1));
			return "DISCARD " + discardableCardIndex + " " + discardableCardIndex;
		}

		// if we know we have a one with a color we don't have on the board, play it
		if (numFuses > 1) {
			int oneCardIndex = getOneCardIndex();
			if (oneCardIndex != -1) {
				this.myHand.remove(oneCardIndex);
				this.myHand.add(oneCardIndex, new Card(-1, -1));
				return "PLAY " + oneCardIndex + " " + oneCardIndex;
			}
		}

		// if all else fails, discard the first card -1, -1 card in our hand
		for (int i = 0; i < this.myHand.size(); i++) {
			Card c = this.myHand.get(i);
			if (c.value == -1 || c.color == -1) {
				this.myHand.remove(i);
				this.myHand.add(i, new Card(-1, -1));
				return "DISCARD " + i + " " + i;
			}
		}

		// if we get here, we have no idea what to do, so just discard the first card in our hand
		this.myHand.remove(0);
		this.myHand.add(0, new Card(-1, -1));
		return "DISCARD 0 0";
	}

	private int getPlayableCardIndex() throws Exception {
		for (int i = 0; i < this.myHand.size(); i++) {
			Card c = this.myHand.get(i);
			if (c.value != -1 && c.color != -1) {
				switch (c.color) {
					case 0:
						if (this.tableau.get(0) == c.value - 1) {
							return i;
						}
						break;
					case 1:
						if (this.tableau.get(1) == c.value - 1) {
							return i;
						}
						break;
					case 2:
						if (this.tableau.get(2) == c.value - 1) {
							return i;
						}
						break;
					case 3:
						if (this.tableau.get(3) == c.value - 1) {
							return i;
						}
						break;
					case 4:
						if (this.tableau.get(4) == c.value - 1) {
							return i;
						}
						break;
				}
			}
		}
		return -1;
	}

	private int getUnawarePartnerPlayableCardIndex() throws Exception {
		for (int i = 0; i < this.partner.hand.size(); i++) {
			Card c = this.partner.hand.get(i);
			Card knownCard = this.truePartnerHand.get(i);
			if (c.value == -1 || c.color == -1) {
				switch (knownCard.color) {
					case 0:
						if (this.tableau.get(0) == knownCard.value - 1) {
							return i;
						}
						break;
					case 1:
						if (this.tableau.get(1) == knownCard.value - 1) {
							return i;
						}
						break;
					case 2:
						if (this.tableau.get(2) == knownCard.value - 1) {
							return i;
						}
						break;
					case 3:
						if (this.tableau.get(3) == knownCard.value - 1) {
							return i;
						}
						break;
					case 4:
						if (this.tableau.get(4) == knownCard.value - 1) {
							return i;
						}
						break;
				}
			}
		}
		return -1;
	}

	private int getOneCardIndex() throws Exception {
		for (int i = 0; i < this.myHand.size(); i++) {
			Card c = this.myHand.get(i);
			if (c.value == 1) {
				// calculate chance of success given top card in tableau
				int count = 0;
				for (int j = 0; j < this.tableau.size(); j++) {
					if (this.tableau.get(j) == 0) {
						count++;
					}
				}
				if (count > 0.5) {
					return i;
				}
			}
		}
		return -1;
	}

	// can be greatly improved
	private int getDiscardableCardIndex() throws Exception {
		for (int i = 0; i < this.myHand.size(); i++) {
			Card c = this.myHand.get(i);
			if (cardInTableau(c)) {
				return i;
			} else if (c.value == 5) {
				return i;
			} else if (c.color == -1 && c.value == -1) {
				return i;
			}
		}
		return -1;
	}

	private boolean cardInTableau(Card c) {
		if (c.color == -1 || c.value == -1) {
			return false;
		}
		return this.tableau.get(c.color) >= c.value - 1;
	}
	
	private Hint getBestOverallHint() throws Exception {
		int maxNumColorHints = getMaxNumColorHints();
		int maxNumValueHints = getMaxNumValueHints();

		if (maxNumColorHints <= 2 || maxNumValueHints <= 2) {
			return null;
		} else if (maxNumColorHints > maxNumValueHints) {
			return new Hint(true, getBestColorHint());
		} else {
			return new Hint(false, getBestValueHint());
		}
	}

	private int getBestColorHint() throws Exception {
		ArrayList<Integer> colors = new ArrayList<>(Collections.nCopies(5, 0));
		for (int i = 0; i < this.partner.hand.size(); i++) {
			Card knownC = this.partner.hand.get(i);
			Card c = this.truePartnerHand.get(i);
			if (knownC.color == -1) {
				colors.set(c.color, colors.get(c.color) + 1);
			}
		}

		return colors.indexOf(Collections.max(colors));
	}

	private int getBestValueHint() throws Exception {
		ArrayList<Integer> values = new ArrayList<>(Collections.nCopies(5, 0));
		for (int i = 0; i < this.partner.hand.size(); i++) {
			Card knownC = this.partner.hand.get(i);
			Card c = this.truePartnerHand.get(i);
			if (knownC.value == -1) {
				values.set(c.value - 1, values.get(c.value - 1) + 1);
			}
		}

		return values.indexOf(Collections.max(values)) + 1;
	}

	private int getMaxNumColorHints() throws Exception {
		ArrayList<Integer> colors = new ArrayList<>(Collections.nCopies(5, 0));

		for (int i = 0; i < this.partner.hand.size(); i++) {
			Card knownC = this.partner.hand.get(i);
			Card c = this.truePartnerHand.get(i);
			if (knownC.color == -1) {
				colors.set(c.color, colors.get(c.color) + 1);
			}
		}

		return Collections.max(colors);
	}

	private int getMaxNumValueHints() throws Exception {
		ArrayList<Integer> values = new ArrayList<>(Collections.nCopies(5, 0));

		for (int i = 0; i < this.partner.hand.size(); i++) {
			Card knownC = this.partner.hand.get(i);
			Card c = this.truePartnerHand.get(i);
			if (knownC.value == -1) {
				values.set(c.value - 1, values.get(c.value - 1) + 1);
			}
		}

		return Collections.max(values);
	}
}