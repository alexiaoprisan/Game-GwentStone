package Cards;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Card {
    public int mana;
    public int attackDamage;
    public int health;
    public String description;
    public ArrayList<String> colors;
    public String name;
    public boolean isFrozen;

    public Card() {
    }

    public Card(int mana, int health, int attackDamage, String description, String name, ArrayList<String> colors) {
        this.mana = mana;
        this.health = health;
        this.attackDamage = attackDamage;
        this.description = description;
        this.name = name;
        this.colors = colors;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsFrozen(boolean isFrozen) {
        this.isFrozen = isFrozen;
    }

    public void freeze() {
        setIsFrozen(true);
    }

    public void unfreeze() {
        setIsFrozen(false);
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public boolean getIsFrozen() {
        return isFrozen;
    }

    public void printCardToJson(ArrayNode output) {

        ObjectNode card = output.addObject();
        card.put("mana", mana);
        card.put("attackDamage", attackDamage);
        card.put("health", health);
        card.put("description", description);

        ArrayNode colorsArray = card.putArray("colors");
        for (String color : colors) {
            colorsArray.add(color);
        }
        card.put("name", name);
    }

}
