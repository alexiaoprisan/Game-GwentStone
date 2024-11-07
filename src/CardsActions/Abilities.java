package CardsActions;

import Cards.Minion;
import Player.Player;
import Player.Deck;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import Cards.Card;
import Cards.Hero;
import GameTable.GameTable;

import java.util.List;
import java.util.ArrayList;


public class Abilities {

    public void weakKnees(Minion attacker, Minion victim) {
        if(victim.getAttackDamage() - attacker.getAttackDamage() <= 0) {
            victim.setAttackDamage(0);
        }
        else {
            victim.setAttackDamage(victim.getAttackDamage() - attacker.getAttackDamage());
        }
    }

    public void godsPlan(Minion helpedMinion) {
        int newHealth = helpedMinion.getHealth() + 2;
        helpedMinion.setHealth(newHealth);
    }

    public void skyJack(Minion attacker, Minion victim) {
        int health = victim.getHealth();
        victim.setHealth(attacker.getHealth());
        attacker.setHealth(health);
    }

    public void shapeshift(Minion victim, GameTable gameTable, int row, int col) {
        int aux = victim.getAttackDamage();
        victim.setAttackDamage(victim.getHealth());
        victim.setHealth(aux);

        if(victim.getHealth() == 0) {
            // gameTable.removeCard(row, col);
        }
    }

    public void subZero(int row, GameTable gameTable) {
        gameTable.freezeRow(row);
    }

    public void lowBlow(int row, GameTable gameTable) {
        List<Minion> rowMinions = gameTable.getRowFromTable(row);
        int maxHealth = -1;
        int posHealthiestMinion = 0;
        for(Minion minion : rowMinions) {
            if (minion.getHealth() > maxHealth) {
                maxHealth = minion.getHealth();
                posHealthiestMinion++;
            }
        }
        if (maxHealth != -1) {
            gameTable.removeCard(row, posHealthiestMinion - 1);
        }
    }

    public void earthBorn(int row, GameTable gameTable) {
        List<Minion> rowMinions = gameTable.getRowFromTable(row);
        for(Minion minion : rowMinions) {
            minion.setHealth(minion.getHealth() + 1);
        }
    }

    public void bloodThirst(int row, GameTable gameTable) {
        List<Minion> rowMinions = gameTable.getRowFromTable(row);
        for(Minion minion : rowMinions) {
            minion.setAttackDamage(minion.getAttackDamage() + 1);
        }
    }

}
