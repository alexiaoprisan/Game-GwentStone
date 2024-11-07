package Cards;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Hero extends Card {
    public boolean hasUsedAbility = false;
    public Hero(int mana, int health, int attackDamage, String description, String name, ArrayList<String> colors) {
        this.mana = mana;
        this.health = health;
        this.attackDamage = attackDamage;
        this.description = description;
        this.name = name;
        this.colors = colors;
    }

    public boolean heroHasUsedAbility() {
        return hasUsedAbility;
    }


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
