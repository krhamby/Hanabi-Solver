import java.util.ArrayList;

class PartnerKnowledge {
    public Hand hand;
    public ArrayList<Card> possibleRemainingCards;
    
    public PartnerKnowledge() {
        this.hand = new Hand();
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
}
