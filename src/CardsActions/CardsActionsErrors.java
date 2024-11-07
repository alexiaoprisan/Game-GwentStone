package CardsActions;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CardsActionsErrors {

    public void printErrorAttack(String error, int attackerRow, int attackerCol, int attackedRow, int attackedCol, ArrayNode output) {

        ObjectNode errorRow = output.addObject();
        errorRow.put("command", "cardUsesAttack");
        ObjectNode cardAttacker = errorRow.putObject("cardAttacker");
        cardAttacker.put("x", attackerRow);
        cardAttacker.put("y", attackerCol);
        ObjectNode cardAttacked = errorRow.putObject("cardAttacked");
        cardAttacked.put("x", attackedRow);
        cardAttacked.put("y", attackedCol);
        errorRow.put("error", error);

    }

    public void printErrorAbility(String error, int attackerRow, int attackerCol, int attackedRow, int attackedCol, ArrayNode output) {

        ObjectNode errorRow = output.addObject();
        errorRow.put("command", "cardUsesAbility");
        ObjectNode cardAttacker = errorRow.putObject("cardAttacker");
        cardAttacker.put("x", attackerRow);
        cardAttacker.put("y", attackerCol);
        ObjectNode cardAttacked = errorRow.putObject("cardAttacked");
        cardAttacked.put("x", attackedRow);
        cardAttacked.put("y", attackedCol);
        errorRow.put("error", error);

    }

    public void printErrorAttackHero(String error, int attackerRow, int attackerCol, ArrayNode output) {

        ObjectNode errorRow = output.addObject();
        errorRow.put("command", "useAttackHero");
        ObjectNode cardAttacker = errorRow.putObject("cardAttacker");
        cardAttacker.put("x", attackerRow);
        cardAttacker.put("y", attackerCol);
        errorRow.put("error", error);
    }

    public void printErrorAbilityHero(String error, int attackerRow, ArrayNode output) {

        ObjectNode errorRow = output.addObject();
        errorRow.put("affectedRow", attackerRow);
        errorRow.put("command", "useHeroAbility");
        errorRow.put("error", error);
    }

}
