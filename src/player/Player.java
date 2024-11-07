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
    private final ArrayList<Card> cardsInHand = new ArrayList<>();
    private final Deck cardsInDeck = new Deck();
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
     * Gets the list of cards currently in the player's hand.
     *
     * @return the list of cards in hand
     */
    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
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
        if (decks.isEmpty()) {
            return;
        }

        // Put the first card from the deck into the player's hand
        cardsInHand.add(decks.getFirst());

        // Remove the card from the deck
        decks.removeFirst();
    }

    /**
     * Removes a card from the player's hand at the specified index.
     *
     * @param idx the index of the card to remove
     */
    public void removeCardFromHand(final int idx) {
        cardsInHand.remove(idx);
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
        cardsInDeck.getDecks().clear();
    }
}
