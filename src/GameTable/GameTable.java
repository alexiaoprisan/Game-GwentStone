package GameTable;

import java.util.ArrayList;
import java.util.List;
import Cards.Minion;

import Player.Player;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class GameTable {
    private static final int MAX_ROW = 4;
    private static final int MAX_COL = 5;

    public List<List<Minion>> table;

    public GameTable() {
        table = new ArrayList<>(MAX_ROW);

        for (int i = 0; i < MAX_ROW; ++i) {
            table.add(new ArrayList<>(MAX_COL));
        }
    }

    public Minion getCardFromTable(int coordX, int coordY, ArrayNode output) {
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

        return rowX.get(coordY);
    }

    // gets a row from the table
    public List<Minion> getRowFromTable(int rowIndex) {
        List<Minion> row = table.get(rowIndex); // Retrieve the row at the specified index
        return new ArrayList<>(row);
    }

    public boolean addCard(int rowIndex, final Minion minion, ArrayNode output) {
        if(rowIndex < 0 || rowIndex >= MAX_ROW) {
            return false;
        }

        List<Minion> rowToPutCardOn = table.get(rowIndex);

        if(rowToPutCardOn.size() >= MAX_COL) {
            ObjectNode errorRow = output.addObject();
            errorRow.put("command", "placeCard");
            errorRow.put("handIdx", rowIndex);
            errorRow.put("error", "Cannot place card on table since row is full.");
            return false;
        }

        rowToPutCardOn.add(minion);
        return true;
    }

    public boolean removeCard(int coordX, int coordY) {
        if (coordX < 0 || coordX >= MAX_ROW) {
            return false;
        }

        List<Minion> rowToRemoveCardFrom = table.get(coordX);

        if (coordY < 0 || coordY >= rowToRemoveCardFrom.size()) {
            return false;
        }

        rowToRemoveCardFrom.remove(coordY);
        return true;
    }

    public void startNewRound(Player playerOne, Player playerTwo) {
        // Reset the hasAttackedThisTurn and usedAbilityThisTurn flags for all minions on the table and frozen minions
        for (int i = 0; i < MAX_ROW; ++i) {
            List<Minion> row = table.get(i);
            for (Minion minion : row) {
                minion.hasAttackedThisTurn = false;
                minion.usedAbilityThisTurn = false;
            }
        }
        playerOne.getHero().hasUsedAbility = false;
        playerTwo.getHero().hasUsedAbility = false;

    }

    public void unfreezeMinions(Player playerOne, Player playerTwo) {
        for (int i = 0; i < MAX_ROW; ++i) {
            List<Minion> row = table.get(i);
            for (Minion minion : row) {
                if ((i == 0 || i == 1) && playerTwo.isActive()) {
                    minion.setIsFrozen(false);
                } else if ((i == 2 || i == 3) && playerOne.isActive()) {
                    minion.setIsFrozen(false);
                }
            }
        }
    }

    public void getTableOutput(ArrayNode output) {
        // Populate the output with minion details from the table
        for (int i = 0; i < MAX_ROW; ++i) {
            List<Minion> row = table.get(i);
            ArrayNode rowArray = output.addArray(); // Create a new array for each row

            if (!row.isEmpty()) {
                for (Minion minion : row) {
                    ObjectNode card = rowArray.addObject(); // Add cards to the current row array
                    card.put("attackDamage", minion.getAttackDamage());

                    // Add colors to the card
                    ArrayNode colorsArray = card.putArray("colors");
                    for (String color : minion.getColors()) {
                        colorsArray.add(color);
                    }

                    // Add other attributes to the card
                    card.put("description", minion.getDescription());
                    card.put("health", minion.getHealth());
                    card.put("mana", minion.getMana());
                    card.put("name", minion.getName());
                }
            }
        }
    }

    public void getFrozenCardsTable(ArrayNode output) {
        // Iterate through each row in the table
        for (int i = 0; i < MAX_ROW; ++i) {
            List<Minion> row = table.get(i);

            // Check each minion in the current row
            for (Minion minion : row) {
                if (minion.isFrozen) {
                    // Create a JSON object for each frozen minion's details
                    ObjectNode card = output.addObject();

                    // Populate minion attributes
                    card.put("attackDamage", minion.getAttackDamage());
                    card.put("health", minion.getHealth());
                    card.put("mana", minion.getMana());
                    card.put("name", minion.getName());
                    card.put("description", minion.getDescription());

                    // Populate the colors array
                    ArrayNode colorsArray = card.putArray("colors");
                    for (String color : minion.getColors()) {
                        colorsArray.add(color);
                    }
                }
            }
        }
    }



    public boolean existsTankOnRow(int rowIndex) {
        List<Minion> row = table.get(rowIndex);
        for (Minion minion : row) {
            if (minion.isTank() == true) {
                return true;
            }
        }
        return false;

    }

    public void freezeRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= MAX_ROW) {
            return;
        }

        List<Minion> row = table.get(rowIndex);
        if (row.isEmpty()) {
            return;
        }
        for (Minion minion : row) {
            minion.isFrozen = true;
            minion.setIsFrozen(true);
            minion.freeze();
        }
    }

    public void clearTable() {
        for (int i = 0; i < MAX_ROW; ++i) {
            List<Minion> row = table.get(i);
            row.clear();
        }
    }



}
