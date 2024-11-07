package CardsActions;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * This class is responsible for logging errors that occur during card actions,
 * such as invalid attacks or ability uses.
 */
public class CardsActionsErrors {

    /**
     * Prints an error for an invalid attack between two cards.
     *
     * @param error       the error message
     * @param attackerRow the row of the attacking card
     * @param attackerCol the column of the attacking card
     * @param attackedRow the row of the attacked card
     * @param attackedCol the column of the attacked card
     * @param output      the JSON array where the error is logged
     */
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

    /**
     * Prints an error for an invalid ability used by a minion.
     *
     * @param error       the error message
     * @param attackerRow the row of the attacker card
     * @param attackerCol the column of the attacker card
     * @param attackedRow the row of the attacked card
     * @param attackedCol the column of the attacked card
     * @param output      the JSON array where the error is logged
     */
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

    /**
     * Prints an error for an invalid attack on the other player's hero.
     *
     * @param error       the error message
     * @param attackerRow the row of the attacking card
     * @param attackerCol the column of the attacking card
     * @param output      the JSON array where the error is logged
     */
    public void printErrorAttackHero(String error, int attackerRow, int attackerCol, ArrayNode output) {
        ObjectNode errorRow = output.addObject();
        errorRow.put("command", "useAttackHero");
        ObjectNode cardAttacker = errorRow.putObject("cardAttacker");
        cardAttacker.put("x", attackerRow);
        cardAttacker.put("y", attackerCol);
        errorRow.put("error", error);
    }

    /**
     * Prints an error for an invalid ability used by a hero.
     *
     * @param error       the error message
     * @param attackerRow the row affected by the ability
     * @param output      the JSON array where the error is logged
     */
    public void printErrorAbilityHero(String error, int attackerRow, ArrayNode output) {
        ObjectNode errorRow = output.addObject();
        errorRow.put("affectedRow", attackerRow);
        errorRow.put("command", "useHeroAbility");
        errorRow.put("error", error);
    }
}
