import java.util.ArrayList;

class PartnerKnowledge {
    public Hand hand;
    public ArrayList<Card> possibleRemainingCards;
    
    public PartnerKnowledge() {
        this.hand = new Hand();
        try {
			for (int i = 0; i < 5; i++) {
				this.hand.add(i, new Card(-1, -1));
			}
		} catch (Exception e) {
			System.out.println("Error in Player constructor");
		}
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
    }

    public void updateHandColorKnowledge(int color, Hand truePartnerHand) {
        for (int i = 0; i < truePartnerHand.size(); i++) {
            try {
                if (truePartnerHand.get(i).color == color) {
                    this.hand.remove(i);
                    this.hand.add(i, truePartnerHand.get(i));
                }
            } catch (Exception e) {
                System.out.println("Error in updateHandColorKnowledge");
            }
        }
    }

    public void updateHandValueKnowledge(int value, Hand truePartnerHand) {
        for (int i = 0; i < truePartnerHand.size(); i++) {
            try {
                if (truePartnerHand.get(i).value == value) {
                    this.hand.remove(i);
                    this.hand.add(i, truePartnerHand.get(i));
                }
            } catch (Exception e) {
                System.out.println("Error in updateHandNumberKnowledge");
            }
        }
    }
}
