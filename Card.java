
public class Card {
    private final Suit suit;
    private final Value value;
    private static final int JACK_QUEEN_KING_VALUE = 10;

    public Card(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
    }

    public Suit getSuit() {
        return suit;
    }

    public Value getValue() {
        return value;
    }

    public int getPoints() {
        switch (value) {
            case ACE:
                return 1;
            case TWO:
            case THREE:
            case FOUR:
            case FIVE:
            case SIX:
            case SEVEN:
            case EIGHT:
            case NINE:
                return value.ordinal() + 1;
            default:
                return JACK_QUEEN_KING_VALUE;
        }
    }

    @Override
    public String toString() {
        return "<" + value + " of " + suit + ">";
    }

    public enum Suit {
        CLUBS,
        DIAMONDS,
        HEARTS,
        SPADES
    }

    public enum Value {
        ACE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING
    }
}


