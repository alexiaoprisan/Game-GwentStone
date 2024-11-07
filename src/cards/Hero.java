package cards;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

/**
 * Represents a hero card that extends the basic Card class.
 */
public class Hero extends Card {
    /**
     * Indicates whether the hero has used his special ability.
     */
    public boolean hasUsedAbility = false;

    /**
     * Constructs a Hero with specified attributes.
     *
     * @param mana         the mana cost of the hero
     * @param health       the health of the hero
     * @param attackDamage the attack damage when the hero attacks
     * @param description  a description of the hero
     * @param name         the name of the hero
     * @param colors       the colors associated with the hero
     */
    public Hero(int mana, int health, int attackDamage, String description, String name, ArrayList<String> colors) {
        this.mana = mana;
        this.health = health;
        this.attackDamage = attackDamage;
        this.description = description;
        this.name = name;
        this.colors = colors;
    }

    /**
     * Converts the hero's details to JSON format and adds them to the specified output ArrayNode,
     * including a player index identifier.
     *
     * @param playerIdx the index of the player associated with this hero
     * @param output    the JSON array to which the hero's details are added
     */
    public void printHeroInJson(int playerIdx, ArrayNode output) {
        ObjectNode getPlayerHeroOutput = output.addObject();
        getPlayerHeroOutput.put("command", "getPlayerHero");
        getPlayerHeroOutput.put("playerIdx", playerIdx);
        ObjectNode heroNode = getPlayerHeroOutput.putObject("output");
        heroNode.put("mana", mana);
        heroNode.put("description", description);

        ArrayNode colorsArray = heroNode.putArray("colors");
        for (String color : colors) {
            colorsArray.add(color);
        }
        heroNode.put("name", name);
        heroNode.put("health", health);
    }
}
