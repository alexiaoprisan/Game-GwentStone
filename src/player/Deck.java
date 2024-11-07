package player;

import java.util.ArrayList;

import cards.Card;

/**
 * Represents a deck of cards in the game. It manages the collection of cards
 * and tracks the number of cards in the deck.
 */
public class Deck {
    private int nrCardsInDeck; // The number of cards currently in the deck
    private ArrayList<Card> decks = new ArrayList<>(); // List of cards in the deck

    /**
     * Default constructor to initialize the deck with no cards.
     */
    public Deck() {

    }

    /**
     * Sets the number of cards in the deck.
     *
     * @param nrCardsInDeck the number of cards to set in the deck
     */
    public void setNrCardsInDeck(final int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    /**
     * Gets the list of cards currently in the deck.
     *
     * @return the list of cards in the deck
     */
    public ArrayList<Card> getDecks() {
        return decks;
    }

    /**
     * Sets the list of cards in the deck.
     *
     * @param decks the list of cards to set in the deck
     */
    public void setDecks(final ArrayList<Card> decks) {
        this.decks = decks;
        this.nrCardsInDeck = decks.size(); // Update the number of cards in the deck
    }
}
