
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int chips;
    private List<Card> hand;

    public Player(String name, int chips) {
        this.name = name;
        this.chips = chips;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getChips() {
        return chips;
    }

    public void addChips(int amount) {
        chips += amount;
    }

    public void removeChips(int amount) {
        chips -= amount;
    }

    public void resetChips(int amount) {
        chips = amount;
    }

    public void resetHand() {
        hand.clear();
    }

    public void addToHand(Card card) {
        hand.add(card);
    }

    public int getHandValue() {
        int value = 0;
        for (Card card : hand) {
            value += card.getPoints();
        }
        return value;
    }

    //return the player's hand
    public List<Card> PHand() {
        return hand;
    }

}

