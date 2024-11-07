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
    private boolean hasUsedAbility = false;

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
    public Hero(final int mana, final int health, final int attackDamage,
                final String description, final String name,
                final ArrayList<String> colors) {
        super(mana, health, attackDamage, description, name, colors);
    }

    /**
     * Converts the hero's details to JSON format and adds them to the specified output ArrayNode,
     * including a player index identifier.
     *
     * @param playerIdx the index of the player associated with this hero
     * @param output    the JSON array to which the hero's details are added
     */
    public void printHeroInJson(final int playerIdx, final ArrayNode output) {
        ObjectNode getPlayerHeroOutput = output.addObject();
        getPlayerHeroOutput.put("command", "getPlayerHero");
        getPlayerHeroOutput.put("playerIdx", playerIdx);
        ObjectNode heroNode = getPlayerHeroOutput.putObject("output");
        heroNode.put("mana", getMana());
        heroNode.put("description", getDescription());

        ArrayNode colorsArray = heroNode.putArray("colors");
        for (String color : getColors()) {
            colorsArray.add(color);
        }
        heroNode.put("name", getName());
        heroNode.put("health", getHealth());
    }

    /**
     * Returns whether the hero has used his special ability.
     *
     * @return true if the hero has used his ability, false otherwise
     */
    public boolean hasUsedAbility() {
        return hasUsedAbility;
    }

    /**
     * Sets whether the hero has used his special ability.
     *
     * @param hasUsedAbility true if the hero has used his ability, false otherwise
     */
    public void setHasUsedAbility(final boolean hasUsedAbility) {
        this.hasUsedAbility = hasUsedAbility;
    }
}
