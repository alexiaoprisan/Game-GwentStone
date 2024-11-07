package gametable;

import java.util.ArrayList;
import java.util.List;

import cards.Minion;
import player.Player;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * This class represents the game table where minions are placed during the game.
 * It contains methods to add, remove, and retrieve minions from the table.
 */
public class GameTable {
    private static final int MAX_ROW = 4; // Maximum number of rows on the table
    private static final int MAX_COL = 5; // Maximum number of columns on the table
    private final int three = 3;

    private List<List<Minion>> table; // A 2D list representing the table with minions

    /**
     * Constructs a new game table with a fixed number of rows and columns.
     */
    public GameTable() {
        table = new ArrayList<>(MAX_ROW);

        // Initialize each row as an ArrayList
        for (int i = 0; i < MAX_ROW; ++i) {
            table.add(new ArrayList<>(MAX_COL));
        }
    }

    /**
     * Gets the minion at the specified position on the table.
     *
     * @param coordX the row index
     * @param coordY the column index
     * @param output the JSON output array to store any errors
     * @return the Minion at the specified position
     */
    public Minion getCardFromTable(final int coordX, final int coordY, final ArrayNode output) {
        // Check if coordX is within the valid range
        if (coordX < 0 || coordX >= MAX_ROW) {
            ObjectNode errorRow = output.addObject();
            errorRow.put("command", "getCardAtPosition");
            errorRow.put("x", coordX);
            errorRow.put("y", coordY);
            errorRow.put("output", "No card available at that position.");
            return null;
        }

        // Get the row at coordX
        List<Minion> rowX = table.get(coordX);

        int cardsOnTheRowX = rowX.size();

        // Check if coordY is within the valid range for this row
        if (coordY < 0 || coordY >= cardsOnTheRowX) {
            ObjectNode errorRow = output.addObject();
            errorRow.put("command", "getCardAtPosition");
            errorRow.put("x", coordX);
            errorRow.put("y", coordY);
            errorRow.put("output", "No card available at that position.");
            return null;
        }

        return rowX.get(coordY); // Return the minion at the specified coordinates
    }

    /**
     * Retrieves a row of minions from the table.
     *
     * @param rowIndex the index of the row
     * @return a list of Minions in the specified row
     */
    public List<Minion> getRowFromTable(final int rowIndex) {
        List<Minion> row = table.get(rowIndex); // Retrieve the row at the specified index
        return new ArrayList<>(row); // Return a copy of the row
    }

    /**
     * Adds a Minion to the table at the specified row.
     *
     * @param rowIndex the index of the row to place the card
     * @param minion   the Minion to add to the row
     * @param output   the JSON output array to store any errors
     * @return true if the card is successfully added, false otherwise
     */
    public boolean addCard(final int rowIndex, final Minion minion, final ArrayNode output) {
        if (rowIndex < 0 || rowIndex >= MAX_ROW) {
            return false; // Invalid row index
        }

        List<Minion> rowToPutCardOn = table.get(rowIndex);

        // Check if the row is full, print an error if it is
        if (rowToPutCardOn.size() >= MAX_COL) {
            ObjectNode errorRow = output.addObject();
            errorRow.put("command", "placeCard");
            errorRow.put("handIdx", rowIndex);
            errorRow.put("error", "Cannot place card on table since row is full.");
            return false;
        }

        rowToPutCardOn.add(minion); // Add the card to the row
        return true;
    }

    /**
     * Removes a card from the table at the specified coordinates.
     *
     * @param coordX the row index
     * @param coordY the column index
     * @return true if the card is successfully removed, false otherwise
     */
    public boolean removeCard(final int coordX, final int coordY) {
        if (coordX < 0 || coordX >= MAX_ROW) {
            return false;
        }

        // Get the row to remove the card from
        List<Minion> rowToRemoveCardFrom = table.get(coordX);

        if (coordY < 0 || coordY >= rowToRemoveCardFrom.size()) {
            return false;
        }

        rowToRemoveCardFrom.remove(coordY); // Remove the card from the row
        return true;
    }

    /**
     * Resets all minions from the table, when a new round starts.
     * This includes resetting the attack status and ability usage for each minion
     * and the ability usage for each hero.
     *
     * @param playerOne the first player
     * @param playerTwo the second player
     */
    public void startNewRound(final Player playerOne, final Player playerTwo) {
        for (int i = 0; i < MAX_ROW; ++i) {
            List<Minion> row = table.get(i);
            for (Minion minion : row) {
                minion.setHasAttackedThisTurn(false);
                minion.setUsedAbilityThisTurn(false);
            }
        }
        playerOne.getHero().setHasUsedAbility(false);
        playerTwo.getHero().setHasUsedAbility(false);
    }

    /**
     * Unfreezes minions based on the active player.
     *
     * @param playerOne the first player
     * @param playerTwo the second player
     */
    public void unfreezeMinions(final Player playerOne, final Player playerTwo) {
        for (int i = 0; i < MAX_ROW; ++i) {
            List<Minion> row = table.get(i);
            for (Minion minion : row) {
                // Unfreeze minions depending on the row and which player is active
                if ((i == 0 || i == 1) && playerTwo.playerIsActive()) {
                    minion.setIsFrozen(false);
                } else if ((i == 2 || i == three) && playerOne.playerIsActive()) {
                    minion.setIsFrozen(false);
                }
            }
        }
    }

    /**
     * Generates the table output in JSON format with all minions.
     *
     * @param output the JSON array to store the table output
     */
    public void getTableOutput(final ArrayNode output) {
        for (int i = 0; i < MAX_ROW; ++i) {
            List<Minion> row = table.get(i);
            ArrayNode rowArray = output.addArray();

            if (!row.isEmpty()) {
                for (Minion minion : row) {
                    ObjectNode card = rowArray.addObject();
                    card.put("attackDamage", minion.getAttackDamage());
                    ArrayNode colorsArray = card.putArray("colors");
                    for (String color : minion.getColors()) {
                        colorsArray.add(color);
                    }

                    card.put("description", minion.getDescription());
                    card.put("health", minion.getHealth());
                    card.put("mana", minion.getMana());
                    card.put("name", minion.getName());
                }
            }
        }
    }

    /**
     * Generates the list of frozen minions in the table in JSON format.
     *
     * @param output the JSON array to store the frozen cards
     */
    public void getFrozenCardsTable(final ArrayNode output) {
        for (int i = 0; i < MAX_ROW; ++i) {
            List<Minion> row = table.get(i);

            for (Minion minion : row) {
                if (minion.isFrozen()) {
                    ObjectNode card = output.addObject();
                    card.put("attackDamage", minion.getAttackDamage());
                    card.put("health", minion.getHealth());
                    card.put("mana", minion.getMana());
                    card.put("name", minion.getName());
                    card.put("description", minion.getDescription());

                    ArrayNode colorsArray = card.putArray("colors");
                    for (String color : minion.getColors()) {
                        colorsArray.add(color);
                    }
                }
            }
        }
    }

    /**
     * Checks if there is a tank minion present in a specific row.
     *
     * @param rowIndex the row index
     * @return true if a tank is present, false otherwise
     */
    public boolean existsTankOnRow(final int rowIndex) {
        List<Minion> row = table.get(rowIndex);
        for (Minion minion : row) {
            if (minion.isTank()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Freezes all minions in a row, when a freeze ability is used.
     *
     * @param rowIndex the row index
     */
    public void freezeRow(final int rowIndex) {
        if (rowIndex < 0 || rowIndex >= MAX_ROW) {
            return;
        }

        List<Minion> row = table.get(rowIndex);
        if (row.isEmpty()) {
            return;
        }

        for (Minion minion : row) {
            minion.setIsFrozen(true);
            minion.freeze();
        }
    }

    /**
     * Removes all minions from the table.
     * This method is called when a new game is started.
     */
    public void clearTable() {
        for (int i = 0; i < MAX_ROW; ++i) {
            List<Minion> row = table.get(i);
            row.clear();
        }
    }
}
