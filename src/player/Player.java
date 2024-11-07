package player;

import java.util.ArrayList;

import cards.Card;
import cards.Hero;
import fileio.CardInput;

/**
 * Represents a player in the game.
 * Each player has a mana, a hand of cards, a deck of cards, and a hero.
 */
public class Player {
    private int mana;
    private int nrCardsInHand;
    private ArrayList<Card> cardsInHand = new ArrayList<>();
    private int nrCardsInDeck;
    private Deck cardsInDeck = new Deck();
    private Hero hero;
    private boolean isActive; // Indicates whether the player is currently active


    /**
     * Default constructor initializing the player.
     */
    public Player() {
    }

    /**
     * Gets the current mana of the player.
     *
     * @return the current mana of the player
     */
    public int getMana() {
        return mana;
    }

    /**
     * Sets the player's mana to the specified value.
     *
     * @param mana the mana value to set
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * Gets the number of cards currently in the player's hand.
     *
     * @return the number of cards in hand
     */
    public int getNrCardsInHand() {
        return nrCardsInHand;
    }

    /**
     * Sets the number of cards currently in the player's hand.
     *
     * @param nrCardsInHand the number of cards in hand to set
     */
    public void setNrCardsInHand(final int nrCardsInHand) {
        this.nrCardsInHand = nrCardsInHand;
    }

    /**
     * Gets the number of cards in the player's deck.
     *
     * @return the number of cards in the deck
     */
    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    /**
     * Sets the number of cards in the player's deck.
     *
     * @param nrCardsInDeck the number of cards in the deck to set
     */
    public void setNrCardsInDeck(final int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    /**
     * Gets the list of cards currently in the player's hand.
     *
     * @return the list of cards in hand
     */
    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    /**
     * Sets the list of cards in the player's hand.
     *
     * @param cardsInHand the list of cards to set in hand
     */
    public void setCardsInHand(final ArrayList<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    /**
     * Gets the deck of cards the player has.
     *
     * @return the player's deck of cards
     */
    public Deck getCardsInDeck() {
        return cardsInDeck;
    }

    /**
     * Sets the player's deck of cards.
     *
     * @param cardsInDeck the deck to set
     */
    public void setCardsInDeck(final Deck cardsInDeck) {
        this.cardsInDeck = cardsInDeck;
    }

    /**
     * Checks if it is the player's turn.
     *
     * @return true if the player is active, false otherwise
     */
    public boolean playerIsActive() {
        return isActive;
    }

    /**
     * Sets the player's active status.
     *
     * @param active true if the player should be active, false otherwise
     */
    public void setActive(final boolean active) { // Renamed parameter to 'active'
        this.isActive = active;
    }


    /**
     * Gets the hero card of the player.
     *
     * @return the player's hero card
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Sets the hero card for the player.
     *
     * @param hero the hero card to set
     */
    public void setHero(final Hero hero) {
        this.hero = hero;
    }

    /**
     * Saves the deck from the input to the player's deck.
     *
     * @param cardInputs the input cards to set in the deck
     */
    public void setDeckFromInput(final ArrayList<CardInput> cardInputs) {
        for (CardInput cardInput : cardInputs) {
            Card card = new Card();
            card.setMana(cardInput.getMana());
            card.setAttackDamage(cardInput.getAttackDamage());
            card.setHealth(cardInput.getHealth());
            card.setDescription(cardInput.getDescription());
            card.setColors(cardInput.getColors());
            card.setName(cardInput.getName());
            card.setIsFrozen(false);
            cardsInDeck.getDecks().add(card);
        }
        cardsInDeck.setNrCardsInDeck(cardInputs.size());
    }

    /**
     * Draws the first card from the deck and adds it to the player's hand.
     */
    public void takeFirstCardFromDeck() {
        ArrayList<Card> decks = cardsInDeck.getDecks();
        if (decks.size() == 0) {
            return;
        }

        // Put the first card from the deck into the player's hand
        cardsInHand.add(decks.get(0));
        nrCardsInHand++;

        // Remove the card from the deck
        decks.remove(0);
    }

    /**
     * Removes a card from the player's hand at the specified index.
     *
     * @param idx the index of the card to remove
     */
    public void removeCardFromHand(final int idx) {
        cardsInHand.remove(idx);
        nrCardsInHand--;
    }

    /**
     * Changes the player's turn to the opposite player's turn.
     */
    public void changePlayerTurn() {
        isActive = !isActive;
    }

    /**
     * Increases the player's mana by a specified number,
     * depending on the number of rounds that have passed.
     *
     * @param gain the amount to increase the player's mana
     */
    public void increaseMana(final int gain) {
        mana += gain;
    }

    /**
     * Decreases the player's mana by a specified number,
     * when the player plays a card.
     *
     * @param cost the amount to decrease the player's mana
     */
    public void decreaseMana(final int cost) {
        mana -= cost;
    }


    /**
     * Resets the player's hand, deck, and mana.
     * This is used when a new game starts.
     */
    public void resetPlayer() {
        cardsInHand.clear();
        nrCardsInHand = 0;
        cardsInDeck.getDecks().clear();
        nrCardsInDeck = 0;
    }
}
