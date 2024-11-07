package CardsActions;

import cards.Minion;
import gameTable.GameTable;

import java.util.List;

/**
 * This class contains the abilities that can be applied by the cards in the game.
 * Cards which have abilities are minions and heroes. The names of the methods
 * are the same as the abilities' names, as specified in the game's rules.
 */
public class Abilities {

    /**
     * This ability belongs to the "The Ripper" minion.
     * It decreases the attack damage of the victim by 2.
     *
     * @param victim the minion whose attack damage is affected
     */
    public void weakKnees(Minion victim) {
        if (victim.getAttackDamage() < 2) {
            victim.setAttackDamage(0);
        } else {
            victim.setAttackDamage(victim.getAttackDamage() - 2);
        }
    }

    /**
     * This ability belongs to the "Disciple" minion.
     * Increases the health of the specified minion by 2.
     *
     * @param helpedMinion the minion receiving the health boost
     */
    public void godsPlan(Minion helpedMinion) {
        int newHealth = helpedMinion.getHealth() + 2;
        helpedMinion.setHealth(newHealth);
    }

    /**
     * This ability belongs to the "Miraj" minion.
     * Swaps the health values of the attacking minion and the victim minion.
     *
     * @param attacker the minion initiating the ability
     * @param victim   the minion whose health is swapped with the attacker
     */
    public void skyJack(Minion attacker, Minion victim) {
        int health = victim.getHealth();
        victim.setHealth(attacker.getHealth());
        attacker.setHealth(health);
    }

    /**
     * This ability belongs to the "The Cursed One" minion.
     * Swaps the health and attack damage values of the specified minion.
     * If the minion's health becomes zero, the minion dies, so it is removed from the game table.
     *
     * @param victim    the minion whose attributes are swapped
     * @param gameTable the game table from which the minion may be removed
     * @param row       the row where the minion is located
     * @param col       the column where the minion is located
     */
    public void shapeshift(Minion victim, GameTable gameTable, int row, int col) {
        int aux = victim.getAttackDamage();
        victim.setAttackDamage(victim.getHealth());
        victim.setHealth(aux);

        if (victim.getHealth() == 0) {
            gameTable.removeCard(row, col);
        }
    }

    /**
     * This ability belongs to the "Lord Royce" Hero.
     * Freezes all minions in the specified row.
     *
     * @param row       the row to be frozen
     * @param gameTable the game table where the row is located
     */
    public void subZero(int row, GameTable gameTable) {
        gameTable.freezeRow(row);
    }

    /**
     * This ability belongs to the "Empress Thorina" hero.
     * Removes the minion with the highest health from the specified row.
     * If multiple minions have the same highest health, the first one in the row is killed.
     *
     * @param row       the row to search for the healthiest minion
     * @param gameTable the game table where the row is located
     */
    public void lowBlow(int row, GameTable gameTable) {
        List<Minion> rowMinions = gameTable.getRowFromTable(row);
        int maxHealth = -1;
        int posHealthiestMinion = 0;
        for (Minion minion : rowMinions) {
            if (minion.getHealth() > maxHealth) {
                maxHealth = minion.getHealth();
                posHealthiestMinion++;
            }
        }
        if (maxHealth != -1) {
            gameTable.removeCard(row, posHealthiestMinion - 1);
        }
    }

    /**
     * This ability belongs to the "King Mudface" hero.
     * Increases the health of all minions in the specified row by 1.
     *
     * @param row       the row whose minions' health will be increased
     * @param gameTable the game table where the row is located
     */
    public void earthBorn(int row, GameTable gameTable) {
        List<Minion> rowMinions = gameTable.getRowFromTable(row);
        for (Minion minion : rowMinions) {
            minion.setHealth(minion.getHealth() + 1);
        }
    }

    /**
     * This ability belongs to the "General Kocioraw" hero.
     * Increases the attack damage of all minions in the specified row by 1.
     *
     * @param row       the row whose minions' attack damage will be increased
     * @param gameTable the game table where the row is located
     */
    public void bloodThirst(int row, GameTable gameTable) {
        List<Minion> rowMinions = gameTable.getRowFromTable(row);
        for (Minion minion : rowMinions) {
            minion.setAttackDamage(minion.getAttackDamage() + 1);
        }
    }

}
