package Player;

import fileio.CardInput;

import java.util.ArrayList;

import Cards.Card;

public class Deck {
    private int nrCardsInDeck;
    private ArrayList<Card> decks = new ArrayList<>();

    public Deck() {
    }

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public void setNrCardsInDeck(final int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    public ArrayList<Card>  getDecks() {
        return decks;
    }

    public void setDecks(final ArrayList<Card>  decks) {
        this.decks = decks;
    }

}
