package cards;

import java.util.ArrayList;

/**
 * Represents a card with its attributes, will be extended to
 * Hero and Minion.
 */
public class Card {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private boolean isFrozen;

    /**
     * Default constructor for a Card.
     */
    public Card() {
    }

    /**
     * Constructs a Card with specified attributes.
     *
     * @param mana         the mana cost of the card
     * @param health       the health of the card
     * @param attackDamage the attack damage of the card
     * @param description  the description of the card
     * @param name         the name of the card
     * @param colors       the colors associated with the card
     */
    public Card(final int mana, final int health, final int attackDamage, final String description,
                final String name, final ArrayList<String> colors) {
        this.mana = mana;
        this.health = health;
        this.attackDamage = attackDamage;
        this.description = description;
        this.name = name;
        this.colors = colors;
    }

    /**
     * Gets the mana cost of the card.
     *
     * @return the mana cost
     */
    public int getMana() {
        return mana;
    }

    /**
     * Sets the mana cost of the card.
     *
     * @param mana the mana cost to set
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * Gets the attack damage of the card.
     *
     * @return the attack damage of the card
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Sets the attack damage of the card.
     *
     * @param attackDamage the attack damage to set
     */
    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Gets the health of the card.
     *
     * @return the health of the card
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the health of the card.
     *
     * @param health the health to set
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * Gets the description of the card.
     *
     * @return the description of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the card.
     *
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Gets the colors associated with the card.
     *
     * @return an ArrayList of colors for the card
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     * Sets the colors associated with the card.
     *
     * @param colors an ArrayList of colors to set
     */
    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    /**
     * Gets the name of the card.
     *
     * @return the name of the card
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the card.
     *
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the frozen status of the card.
     *
     * @param isFrozen the frozen status
     */
    public void setIsFrozen(final boolean isFrozen) {
        this.isFrozen = isFrozen;
    }

    /**
     * Freezes the card, when a hero uses his ability
     * to freeze the card.
     */
    public void freeze() {
        setIsFrozen(true);
    }

    /**
     * Checks if the card is frozen.
     *
     * @return true if the card is frozen, false otherwise
     */
    public boolean isFrozen() {
        return isFrozen;
    }

}

