
public class Dealer extends Player {
    private final Deck deck;

    public Dealer(String name, int chips, Deck deck) {
        super(name, chips);
        this.deck = deck;
    }

    public void shuffleDeck() {
        deck.shuffle();
    }

    public void dealCards(Player player1, Player player2) {
        // addToHand(deck.deal());
        player1.addToHand(deck.deal());
        // addToHand(deck.deal());
        player2.addToHand(deck.deal());
    }

}
