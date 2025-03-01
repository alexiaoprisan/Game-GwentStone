package cards;

import java.util.ArrayList;

/**
 * Represents a minion card that extends the base Card class.
 * Minions have additional attributes and methods that are specific to minions.
 */
public class Minion extends Card {
    /**
     * Indicates whether the minion has attacked during the current
     * player's turn.
     */
    private boolean hasAttackedThisTurn = false;

    /**
     * Indicates whether the minion has used its special ability during the
     * current player's turn.
     */
    private boolean usedAbilityThisTurn = false;

    /**
     * Constructs a Minion with specified attributes.
     *
     * @param mana         the mana cost of the minion
     * @param health       the health of the minion
     * @param attackDamage the attack damage of the minion
     * @param description  a description of the minion
     * @param name         the name of the minion
     * @param isFrozen     the frozen status of the minion
     * @param colors       the colors associated with the minion
     */
    public Minion(final int mana, final int health, final int attackDamage,
                  final String description, final String name, final boolean isFrozen,
                  final ArrayList<String> colors) {
        super();
        this.setMana(mana);
        this.setHealth(health);
        this.setAttackDamage(attackDamage);
        this.setDescription(description);
        this.setName(name);
        this.setIsFrozen(isFrozen);
        this.setColors(colors);
    }

    /**
     * Checks if the minion is a tank minion.
     *
     * @return true if the minion's name is "Goliath" or "Warden", which are tank minions,
     * false otherwise
     */
    public boolean isTank() {
        return getName().equals("Goliath") || getName().equals("Warden");
    }

    /**
     * Determines if the minion should be positioned in the front row based on its type.
     *
     * @return true if the minion is one of the specified front-row types, false otherwise
     */
    public boolean isFrontRow() {
        return getName().equals("Goliath") || getName().equals("Warden")
                || getName().equals("Miraj") || getName().equals("The Ripper");
    }

    /**
     * Checks if the minion has attacked in the current turn of the player.
     *
     * @return true if the minion has attacked this turn, false otherwise
     */
    public boolean hasAttackedThisTurn() {
        return hasAttackedThisTurn;
    }

    /**
     * Sets whether the minion has attacked in the current turn of the player.
     *
     * @param hasAttackedThisTurn true if the minion has attacked this turn, false otherwise
     */
    public void setHasAttackedThisTurn(final boolean hasAttackedThisTurn) {
        this.hasAttackedThisTurn = hasAttackedThisTurn;
    }

    /**
     * Checks if the minion has used its ability in the current turn of the player.
     *
     * @return true if the minion has used its ability this turn, false otherwise
     */
    public boolean usedAbilityThisTurn() {
        return usedAbilityThisTurn;
    }

    /**
     * Sets whether the minion has used its ability in the current turn of the player.
     *
     * @param usedAbilityThisTurn true if the minion has used its ability this turn, false otherwise
     */
    public void setUsedAbilityThisTurn(final boolean usedAbilityThisTurn) {
        this.usedAbilityThisTurn = usedAbilityThisTurn;
    }
}
