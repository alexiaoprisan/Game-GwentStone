package cards;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Represents a card with its attributes, will be extended to
 * Hero and Minion.
 */
public class Card {
    public int mana;
    public int attackDamage;
    public int health;
    public String description;
    public ArrayList<String> colors;
    public String name;
    public boolean isFrozen;

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
    public Card(int mana, int health, int attackDamage, String description, String name,
                ArrayList<String> colors) {
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
    public void setMana(int mana) {
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
    public void setAttackDamage(int attackDamage) {
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
    public void setHealth(int health) {
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
    public void setDescription(String description) {
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
    public void setColors(ArrayList<String> colors) {
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
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the frozen status of the card.
     *
     * @param isFrozen the frozen status
     */
    public void setIsFrozen(boolean isFrozen) {
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
