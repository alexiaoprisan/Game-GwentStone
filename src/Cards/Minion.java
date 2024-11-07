package Cards;

import java.util.ArrayList;

public class Minion extends Card {
    public boolean hasAttackedThisTurn = false;
    public boolean usedAbilityThisTurn = false;


    public Minion(int mana, int health, int attackDamage, String description, String name, boolean isFrozen, ArrayList<String> colors) {
        super();
        this.setMana(mana);
        this.setHealth(health);
        this.setAttackDamage(attackDamage);
        this.setDescription(description);
        this.setName(name);
        this.setIsFrozen(isFrozen);
        this.setColors(colors);
    }



    public boolean isMinion() {
        return true;
    }

    // verifica daca am tank mai intai
    public boolean isTank() {
        if (name.equals("Goliath") || name.equals("Warden")) {
            return true;
        }
        return false;

    }

    public boolean isFrontRow() {
        if (name.equals("Goliath") || name.equals("Warden") || name.equals("Miraj") || name.equals("The Ripper")) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean hasAttackedThisTurn() {
        return hasAttackedThisTurn;
    }

    public boolean usedAbilityThisTurn() {
        return usedAbilityThisTurn;
    }

}
