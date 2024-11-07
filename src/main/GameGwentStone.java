package main;

import player.Player;
import player.Deck;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import cards.Card;
import cards.Hero;
import cards.Minion;
import gameTable.GameTable;
import CardsActions.CardsActionsErrors;
import CardsActions.Abilities;

/**
 * This class contains the logic of the game GwentStone Lite.
 * It initializes the players and the game table, handles each game played
 * by the players and processes the actions performed by the players.
 */
public class GameGwentStone {

    Player playerOne = new Player();
    Player playerTwo = new Player();
    GameTable gameTable = new GameTable();
    CardsActionsErrors actionsErrors = new CardsActionsErrors();
    Abilities abilities = new Abilities();
    int playerOneWins = 0;
    int playerTwoWins = 0;
    int totalGamesPlayed = 0;

    public int manaIncrement = 1;

    /**
     * Constructs a new GameGwentStone object.
     */
    public GameGwentStone() {
    }

    /**
     * Starts playing the game by processing the input data and performing the actions.
     * This method manages each game, reseting the players and the game table when a new game starts.
     *
     * @param input  the input data containing all the data needed to play the game
     * @param output the output to be populated with the game results
     */
    public void startPlayingTheGame(Input input, ArrayNode output) {
        for (GameInput game : input.getGames()) {
            totalGamesPlayed++;
            gameTable.clearTable();
            playerOne.resetPlayer();
            playerTwo.resetPlayer();

            processDataForEachPlayer(input, game.getStartGame());
            startNewGame(input, output, game);
        }
    }

    /**
     * Starts a new game by processing the actions performed by the players.
     * Selects the action to be performed based on the command received.
     *
     * @param input  the input data containing all the data needed to play the game
     * @param output the output to be populated with the game results
     * @param game   the game input containing the actions to be performed
     */
    public void startNewGame(Input input, ArrayNode output, GameInput game) {
        int completedPlayerTurns = 0;
        int playerTurn = game.getStartGame().getStartingPlayer();
        manaIncrement = 1;

        // Each player takes the first card from the deck
        playerOne.takeFirstCardFromDeck();
        playerTwo.takeFirstCardFromDeck();

        // Verify which player starts the round
        if (playerTurn == 1) {
            playerOne.setActive(true);
            playerTwo.setActive(false);

        } else {
            playerOne.setActive(false);
            playerTwo.setActive(true);
        }

        // Process each action performed by the players
        for (ActionsInput action : game.getActions()) {
            switch (action.getCommand()) {
                case "endPlayerTurn":
                    completedPlayerTurns++;
                    endRoundChanges(completedPlayerTurns);
                    break;
                case "getPlayerDeck":
                    methodGetPlayerDeck(action.getPlayerIdx(), output);
                    break;
                case "getPlayerHero":
                    methodGetPlayerHero(action.getPlayerIdx(), output);
                    break;
                case "getPlayerTurn":
                    methodGetPlayerTurn(output);
                    break;
                case "getPlayerMana":
                    methodGetPlayerMana(action.getPlayerIdx(), output);
                    break;
                case "getCardsInHand":
                    methodGetCardsInHand(action.getPlayerIdx(), output);
                    break;
                case "getFrozenCardsOnTable":
                    getFrozenCardsOnTable(output);
                    break;
                case "placeCard":
                    placeCard(action.getHandIdx(), output);
                    break;
                case "getCardsOnTable":
                    getCardsOnTable(output);
                    break;
                case "getCardAtPosition":
                    getCardAtPosition(action.getX(), action.getY(), output);
                    break;
                case "cardUsesAttack":
                    cardUsesAttack(action.getCardAttacker(), action.getCardAttacked(), output);
                    break;
                case "cardUsesAbility":
                    useCardAbility(action.getCardAttacker(), action.getCardAttacked(), output);
                    break;
                case "useAttackHero":
                    heroIsAttacked(action.getCardAttacker(), output);
                    break;
                case "useHeroAbility":
                    heroUseAbility(action.getAffectedRow(), output);
                    break;
                case "getPlayerOneWins":
                    getPlayerOneWins(output);
                    break;
                case "getPlayerTwoWins":
                    getPlayerTwoWins(output);
                    break;
                case "getTotalGamesPlayed":
                    getTotalGamesPlayed(output);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Handles the end of a round by updating the mana, the players' turns and the game table.
     *
     * @param completedPlayerTurns
     */
    private void endRoundChanges(int completedPlayerTurns) {

        // If both players have completed their turns, the round ends.
        // It happens every two turns, so it verifies the number of completed turns.
        if (completedPlayerTurns % 2 == 0) {

            // Increase the mana which will be added to the players' mana
            if (manaIncrement < 10) {
                manaIncrement++;
            }

            // Increase the mana for both players
            playerOne.increaseMana(manaIncrement);
            playerTwo.increaseMana(manaIncrement);

            // Each player takes the first card from the deck
            playerOne.takeFirstCardFromDeck();
            playerTwo.takeFirstCardFromDeck();

            // Start a new round
            gameTable.startNewRound(playerOne, playerTwo);
        }

        // Unfreeze the minions on the table
        gameTable.unfreezeMinions(playerOne, playerTwo);

        // Update the players' turns
        playerOne.changePlayerTurn();
        playerTwo.changePlayerTurn();

    }

    /**
     * Gets the data from the input and processes it for each player, saving the chosen deck and hero.
     *
     * @param input     the input data containing all the data needed to play the game
     * @param startGame the start game input containing the chosen decks and heroes
     */
    private void processDataForEachPlayer(Input input, final StartGameInput startGame) {

        // For the first player
        // Get the decks for the first player based on the index
        DecksInput decksPlayerOne = input.getPlayerOneDecks();
        int deckIdxPlayerOne = startGame.getPlayerOneDeckIdx();

        // Get the list of decks from the input for the first player
        ArrayList<ArrayList<CardInput>> allPlayerOneDecks = decksPlayerOne.getDecks();

        // Get the deck that the player has chosen from the list
        ArrayList<CardInput> chosenDeckByPlayerOne = allPlayerOneDecks.get(deckIdxPlayerOne);

        // Put the chosen deck in a new list and shuffle it, using the seed
        ArrayList<CardInput> selectedDeckByPlayerOne = new ArrayList<>(chosenDeckByPlayerOne);
        long seed = startGame.getShuffleSeed();
        Collections.shuffle(selectedDeckByPlayerOne, new Random(seed));

        // Save the data in the deck for the player
        playerOne.setDeckFromInput(selectedDeckByPlayerOne);

        // Get the hero card for the player
        CardInput heroPlayerOne = startGame.getPlayerOneHero();
        Hero heroCardPlayerOne = new Hero(heroPlayerOne.getMana(), 30, heroPlayerOne.getAttackDamage(),
                heroPlayerOne.getDescription(), heroPlayerOne.getName(), heroPlayerOne.getColors());
        playerOne.setHero(heroCardPlayerOne);
        playerOne.setMana(1);


        // For the second player
        // Get the decks for the second player based on the index
        DecksInput decksPlayerTwo = input.getPlayerTwoDecks();
        int deckIdxPlayerTwo = startGame.getPlayerTwoDeckIdx();

        // Get the list of decks from the input for the second player
        ArrayList<ArrayList<CardInput>> allPlayerTwoDecks = decksPlayerTwo.getDecks();

        // Get the deck that the player has chosen from the list
        ArrayList<CardInput> chosenDeckByPlayerTwo = allPlayerTwoDecks.get(deckIdxPlayerTwo);

        // Put the chosen deck in a new list and shuffle it, using the seed
        ArrayList<CardInput> selectedDeckByPlayerTwo = new ArrayList<>(chosenDeckByPlayerTwo);
        Collections.shuffle(selectedDeckByPlayerTwo, new Random(seed));

        // Save the data in the deck for the player
        playerTwo.setDeckFromInput(selectedDeckByPlayerTwo);

        // Get the hero card for the player
        CardInput heroPlayerTwo = startGame.getPlayerTwoHero();
        Hero heroCardPlayerTwo = new Hero(heroPlayerTwo.getMana(), 30, heroPlayerTwo.getAttackDamage(),
                heroPlayerTwo.getDescription(), heroPlayerTwo.getName(), heroPlayerTwo.getColors());
        playerTwo.setHero(heroCardPlayerTwo);
        playerTwo.setMana(1);

    }

    /**
     * Gets the player deck in order to display it in the output.
     *
     * @param playerIdx the index of the player
     * @param output    the output to be populated with the player's deck
     */
    private void methodGetPlayerDeck(final int playerIdx, ArrayNode output) {

        Deck deck = new Deck();

        if (playerIdx == 1) {
            deck = playerOne.getCardsInDeck();
        } else {
            deck = playerTwo.getCardsInDeck();
        }


        // Put the output in a JSON structure
        ObjectNode getPlayerDeckOutput = output.addObject();
        getPlayerDeckOutput.put("command", "getPlayerDeck");
        getPlayerDeckOutput.put("playerIdx", playerIdx);

        ArrayNode deckArray = getPlayerDeckOutput.putArray("output");

        for (Card card : deck.getDecks()) {
            ObjectNode cardNode = deckArray.addObject();
            cardNode.put("mana", card.getMana());
            cardNode.put("attackDamage", card.getAttackDamage());
            cardNode.put("health", card.getHealth());
            cardNode.put("description", card.getDescription());

            ArrayNode colorsArray = cardNode.putArray("colors");
            for (String color : card.getColors()) {
                colorsArray.add(color);
            }
            cardNode.put("name", card.getName());
        }
    }

    /**
     * Gets the player hero in order to display it in the output.
     *
     * @param playerIdx the index of the player
     * @param output    the output to be populated with the player's hero
     */
    private void methodGetPlayerHero(final int playerIdx, ArrayNode output) {

        // Get the player's hero based on the index
        Hero hero;
        if (playerIdx == 1) {
            hero = playerOne.getHero();
        } else {
            hero = playerTwo.getHero();
        }

        hero.printHeroInJson(playerIdx, output);
    }

    /**
     * Get the index of the player which is active in the current turn.
     *
     * @param output the output to be populated with the player's turn
     */
    private void methodGetPlayerTurn(ArrayNode output) {
        int isPlayerTurn;
        if (playerOne.isActive()) {
            isPlayerTurn = 1;
        } else {
            isPlayerTurn = 2;
        }
        ObjectNode turnOutput = output.addObject();
        turnOutput.put("command", "getPlayerTurn");
        turnOutput.put("output", isPlayerTurn);
    }

    /**
     * Gets the player's mana in order to display it in the output.
     *
     * @param playerIdx the index of the player
     * @param output    the output to be populated with the player's mana
     */
    private void methodGetPlayerMana(final int playerIdx, ArrayNode output) {
        int mana;
        if (playerIdx == 1) {
            mana = playerOne.getMana();
        } else {
            mana = playerTwo.getMana();
        }

        ObjectNode getManaOutput = output.addObject();
        getManaOutput.put("command", "getPlayerMana");
        getManaOutput.put("playerIdx", playerIdx);
        getManaOutput.put("output", mana);
    }

    /**
     * Gets the cards which are in the player's hand.
     *
     * @param playerIdx the index of the player
     * @param output    the output to be populated with the cards in the player's hand
     */
    private void methodGetCardsInHand(final int playerIdx, ArrayNode output) {
        ArrayList<Card> cardsInHand;
        if (playerIdx == 1) {
            cardsInHand = playerOne.getCardsInHand();
        } else {
            cardsInHand = playerTwo.getCardsInHand();
        }

        ObjectNode getCardsInHandOutput = output.addObject();
        getCardsInHandOutput.put("command", "getCardsInHand");
        getCardsInHandOutput.put("playerIdx", playerIdx);

        ArrayNode cardsInHandArray = getCardsInHandOutput.putArray("output");

        for (Card card : cardsInHand) {
            ObjectNode cardNode = cardsInHandArray.addObject();
            cardNode.put("mana", card.getMana());
            cardNode.put("attackDamage", card.getAttackDamage());
            cardNode.put("health", card.getHealth());
            cardNode.put("description", card.getDescription());

            ArrayNode colorsArray = cardNode.putArray("colors");
            for (String color : card.getColors()) {
                colorsArray.add(color);
            }
            cardNode.put("name", card.getName());
        }
    }

    /**
     * Used when the current player wants to place a card on the table.
     * It verifies if the player has enough mana to place the card and if the card can be placed on the table.
     *
     * @param handIdx the index of the card in the player's hand
     * @param output  the output to be populated with the result of the action
     */
    private void placeCard(final int handIdx, ArrayNode output) {
        Player currentPlayer;
        int rowIdx;
        if (playerOne.isActive() == true) {
            currentPlayer = playerOne;
        } else {
            currentPlayer = playerTwo;
        }

        // Check if the player has the card in the hand
        if (currentPlayer.getCardsInHand().size() <= handIdx) {
            return;
        }

        // Get the card from the player's hand
        Card card = currentPlayer.getCardsInHand().get(handIdx);

        // Put the card information in a Minion object, which will be placed on the table
        Minion minion = new Minion(card.getMana(), card.getHealth(), card.getAttackDamage(), card.getDescription(), card.getName(), false, card.getColors());

        // Find the row where the card will be placed, based on minion's type
        if (minion.isFrontRow() && playerOne.isActive() == true) {
            rowIdx = 2;
        } else if (minion.isFrontRow() && playerOne.isActive() == false) {
            rowIdx = 1;
        } else if (!minion.isFrontRow() && playerOne.isActive() == true) {
            rowIdx = 3;
        } else {
            rowIdx = 0;
        }

        // Check if the player has enough mana to place the card on the table
        if (currentPlayer.getMana() < card.getMana()) {
            ObjectNode errorNotEnoughMana = output.addObject();
            errorNotEnoughMana.put("command", "placeCard");
            errorNotEnoughMana.put("handIdx", handIdx);
            errorNotEnoughMana.put("error", "Not enough mana to place card on table.");
            return;
        } else if (gameTable.addCard(rowIdx, minion, output) == true) {
            // If the card was placed on the table, decrease the player's mana and remove the card from the hand
            currentPlayer.decreaseMana(card.getMana());
            currentPlayer.removeCardFromHand(handIdx);
        }


    }

    /**
     * Gets the cards which are placed on the table.
     *
     * @param output the output to be populated with the cards on the table
     */
    private void getCardsOnTable(ArrayNode output) {
        ObjectNode getCardsOnTableOutput = output.addObject();
        getCardsOnTableOutput.put("command", "getCardsOnTable");

        // Get the cards on the table
        ArrayNode cardsOnTableArray = getCardsOnTableOutput.putArray("output");

        // Print the cards on the table in JSON format
        gameTable.getTableOutput(cardsOnTableArray);
    }

    /**
     * Gets the frozen cards which are placed on the table.
     *
     * @param output the output to be populated with the frozen cards on the table
     */
    private void getFrozenCardsOnTable(ArrayNode output) {
        ObjectNode getFrozenCardsOnTableOutput = output.addObject();
        getFrozenCardsOnTableOutput.put("command", "getFrozenCardsOnTable");

        // Get the frozen cards on the table
        ArrayNode frozenCardsArray = getFrozenCardsOnTableOutput.putArray("output");

        // Print the frozen cards on the table in JSON format
        gameTable.getFrozenCardsTable(frozenCardsArray);
    }

    /**
     * Gets a card from the table at a specific position, using the row and column indexes.
     *
     * @param rowX   the row index of the card
     * @param rowY   the column index of the card
     * @param output the output to be populated with the card at the specified position
     */
    private void getCardAtPosition(int rowX, int rowY, ArrayNode output) {

        Minion minion = gameTable.getCardFromTable(rowX, rowY, output);
        if (minion != null) {
            ObjectNode getCardAtPositionOutput = output.addObject();
            getCardAtPositionOutput.put("command", "getCardAtPosition");
            getCardAtPositionOutput.put("x", rowX);
            getCardAtPositionOutput.put("y", rowY);

            ObjectNode cardNode = getCardAtPositionOutput.putObject("output");
            cardNode.put("mana", minion.getMana());
            cardNode.put("attackDamage", minion.getAttackDamage());
            cardNode.put("health", minion.getHealth());
            cardNode.put("description", minion.getDescription());

            ArrayNode colorsArray = cardNode.putArray("colors");
            for (String color : minion.getColors()) {
                colorsArray.add(color);
            }
            cardNode.put("name", minion.getName());


        }


    }

    /**
     * Method used when a player attacks a minion with a minion.
     * It handles the edge cases and verifies if the action is valid.
     *
     * @param cardAttacker the coordinates of the attacking card
     * @param cardAttacked the coordinates of the attacked card
     * @param output       the output to be populated with the result of the action
     */
    private void cardUsesAttack(Coordinates cardAttacker, Coordinates cardAttacked, ArrayNode output) {

        // Get the coordinates of the attacking and attacked cards
        int rowAttacker = cardAttacker.getX();
        int columnAttacker = cardAttacker.getY();
        int rowAttacked = cardAttacked.getX();
        int columnAttacked = cardAttacked.getY();

        // Get the attacking and attacked cards from the table
        Minion attacker = gameTable.getCardFromTable(rowAttacker, columnAttacker, output);
        Minion victim = gameTable.getCardFromTable(rowAttacked, columnAttacked, output);

        // Check if the attacker attacked the correct row
        if (playerOne.isActive() && (rowAttacked == 2 || rowAttacked == 3)) {
            actionsErrors.printErrorAttack("Attacked card does not belong to the enemy.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (playerTwo.isActive() && (rowAttacked == 0 || rowAttacked == 1)) {
            actionsErrors.printErrorAttack("Attacked card does not belong to the enemy.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        // Check if the attacker needed to attack a tank
        if (playerOne.isActive() && (gameTable.existsTankOnRow(0) == true || gameTable.existsTankOnRow(1) == true) && (victim.isTank() == false)) {
            actionsErrors.printErrorAttack("Attacked card is not of type 'Tank'.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (playerTwo.isActive() && (gameTable.existsTankOnRow(2) == true || gameTable.existsTankOnRow(3) == true) && (victim.isTank() == false)) {
            actionsErrors.printErrorAttack("Attacked card is not of type 'Tank'.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        // Check if the attacker has already attacked this turn
        if (attacker.hasAttackedThisTurn() == true) {
            actionsErrors.printErrorAttack("Attacker card has already attacked this turn.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (attacker.usedAbilityThisTurn() == true) {
            actionsErrors.printErrorAttack("Attacker card has already attacked this turn.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        // Check if the attacker is frozen
        if (attacker.isFrozen() == true) {
            actionsErrors.printErrorAttack("Attacker is frozen.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        // Check if the victim will die after the attack
        if (victim.getHealth() - attacker.getAttackDamage() <= 0) {
            // Remove the card from the table if it will die
            gameTable.removeCard(rowAttacked, columnAttacked);
        } else {
            // Decrease the health of the victim
            victim.setHealth(victim.getHealth() - attacker.getAttackDamage());
        }

        // Set the attacker to have attacked this turn, so it cannot attack again
        attacker.hasAttackedThisTurn = true;

    }

    /**
     * Method used when a player uses the Special Ability minion card.
     * It handles the edge cases and verifies if the action is valid.
     *
     * @param cardAttacker the coordinates of the attacking card
     * @param cardAttacked the coordinates of the attacked card
     * @param output       the output to be populated with the result of the action
     */
    public void useCardAbility(Coordinates cardAttacker, Coordinates cardAttacked, ArrayNode output) {
        // Get the coordinates of the attacking and attacked cards
        int rowAttacker = cardAttacker.getX();
        int columnAttacker = cardAttacker.getY();
        int rowAttacked = cardAttacked.getX();
        int columnAttacked = cardAttacked.getY();

        // Get the attacking and attacked cards from the table
        Minion attacker = gameTable.getCardFromTable(rowAttacker, columnAttacker, output);
        Minion victim = gameTable.getCardFromTable(rowAttacked, columnAttacked, output);

        // Check if the attacker has a Special Ability, as only some cards have abilities
        if (attacker.getName().equals("Goliath") || attacker.getName().equals("Warden")) {
            return;
        }

        // Check if the attacker attacked the correct row, his own row
        if (playerOne.isActive() && (rowAttacked == 0 || rowAttacked == 1) && attacker.getName().equals("Disciple")) {
            actionsErrors.printErrorAttack("Attacked card does not belong to the current player.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        // Check if the attacker card attacked the correct row, the enemy's row
        if ((playerTwo.isActive() && (rowAttacked == 2 || rowAttacked == 3)) && attacker.getName().equals("Disciple")) {
            actionsErrors.printErrorAbility("Attacked card does not belong to the current player.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (playerOne.isActive() && (rowAttacked == 2 || rowAttacked == 3) && !attacker.getName().equals("Disciple")) {
            actionsErrors.printErrorAttack("Attacked card does not belong to the enemy.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if ((playerTwo.isActive() && (rowAttacked == 0 || rowAttacked == 1)) && !attacker.getName().equals("Disciple")) {
            actionsErrors.printErrorAbility("Attacked card does not belong to the enemy.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        // Check if the attacker needed to attack a tank
        if (playerOne.isActive() && (gameTable.existsTankOnRow(0) == true || gameTable.existsTankOnRow(1) == true) && (victim.isTank() == false) && !attacker.getName().equals("Disciple")) {
            actionsErrors.printErrorAbility("Attacked card is not of type 'Tank'.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (playerTwo.isActive() && (gameTable.existsTankOnRow(2) == true || gameTable.existsTankOnRow(3) == true) && (victim.isTank() == false) && !attacker.getName().equals("Disciple")) {
            actionsErrors.printErrorAbility("Attacked card is not of type 'Tank'.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        // Check if the attacker has already attacked this turn
        if (attacker.hasAttackedThisTurn() == true) {
            actionsErrors.printErrorAbility("Attacker card has already attacked this turn.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (attacker.usedAbilityThisTurn() == true) {
            actionsErrors.printErrorAbility("Attacker card has already attacked this turn.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        // Check if the attacker is frozen
        if (attacker.isFrozen() == true) {
            actionsErrors.printErrorAbility("Attacker is frozen.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (attacker.getName().equals("The Ripper")) {
            abilities.weakKnees(victim);
        } else if (attacker.getName().equals("Disciple")) {
            abilities.godsPlan(victim);
        } else if (attacker.getName().equals("Miraj")) {
            abilities.skyJack(attacker, victim);
        } else if (attacker.getName().equals("The Cursed One")) {
            abilities.shapeshift(victim, gameTable, rowAttacked, columnAttacked);
        }

        // Set the attacker to have attacked this turn, so it cannot attack again
        attacker.usedAbilityThisTurn = true;
    }

    /**
     * Method used when a player attacks the enemy hero with a minion.
     * It handles the edge cases and verifies if the action is valid.
     *
     * @param cardAttacker the coordinates of the attacking card
     * @param output       the output to be populated with the result of the action
     */
    public void heroIsAttacked(Coordinates cardAttacker, ArrayNode output) {
        // Get the coordinates of the attacking card
        int rowAttacker = cardAttacker.getX();
        int columnAttacker = cardAttacker.getY();

        // Get the attacking card from the table
        Minion attacker = gameTable.getCardFromTable(rowAttacker, columnAttacker, output);
        if (attacker == null) {
            return;
        }

        // Check if the attacker needed to attack a tank first
        if (playerOne.isActive() && (gameTable.existsTankOnRow(0) == true || gameTable.existsTankOnRow(1) == true)) {
            actionsErrors.printErrorAttackHero("Attacked card is not of type 'Tank'.", rowAttacker, columnAttacker, output);
            return;
        }

        if (playerTwo.isActive() && (gameTable.existsTankOnRow(2) == true || gameTable.existsTankOnRow(3) == true)) {
            actionsErrors.printErrorAttackHero("Attacked card is not of type 'Tank'.", rowAttacker, columnAttacker, output);
            return;
        }

        // Check if the attacker has already attacked this turn
        if (attacker.hasAttackedThisTurn == true) {
            actionsErrors.printErrorAttackHero("Attacker card has already attacked this turn.", rowAttacker, columnAttacker, output);
            return;
        }

        // Check if the attacker is frozen
        if (attacker.isFrozen() == true) {
            actionsErrors.printErrorAttackHero("Attacker card is frozen.", rowAttacker, columnAttacker, output);
            return;
        }

        // Set the attacker to have attacked this turn, so it cannot attack again
        attacker.hasAttackedThisTurn = true;

        Hero hero;
        if (playerTwo.isActive()) {
            hero = playerOne.getHero();
            // Check if the hero will die after the attack
            if (hero.getHealth() - attacker.getAttackDamage() <= 0) {
                // If the hero will die, the game ends and player two wins
                ObjectNode gameEnded = output.addObject();
                gameEnded.put("gameEnded", "Player two killed the enemy hero.");

                // Increase the number of wins for player two
                playerTwoWins++;

            } else {
                // Decrease the health of the hero
                playerOne.getHero().setHealth(hero.getHealth() - attacker.getAttackDamage());
            }
        } else {
            hero = playerTwo.getHero();
            // Check if the hero will die after the attack
            if (hero.getHealth() - attacker.getAttackDamage() <= 0) {
                // If the hero will die, the game ends and player one wins
                ObjectNode gameEnded = output.addObject();
                gameEnded.put("gameEnded", "Player one killed the enemy hero.");

                // Increase the number of wins for player one
                playerOneWins++;

            } else {
                // Decrease the health of the hero
                playerTwo.getHero().setHealth(hero.getHealth() - attacker.getAttackDamage());
            }
        }

    }

    /**
     * Method used when a player uses the Special Ability hero card.
     *
     * @param row    the row where the hero will use the ability
     * @param output the output to be populated with the result of the action
     */
    public void heroUseAbility(int row, ArrayNode output) {

        // Check if the player has enough mana to use the hero's ability
        if (playerOne.isActive() && playerOne.getMana() < playerOne.getHero().getMana()) {
            actionsErrors.printErrorAbilityHero("Not enough mana to use hero's ability.", row, output);
            return;
        }

        if (playerTwo.isActive() && playerTwo.getMana() < playerTwo.getHero().getMana()) {
            actionsErrors.printErrorAbilityHero("Not enough mana to use hero's ability.", row, output);
            return;
        }


        // Check if the hero has already used the ability this turn
        if (playerOne.isActive() && playerOne.getHero().hasUsedAbility == true) {
            actionsErrors.printErrorAbilityHero("Hero has already attacked this turn.", row, output);
            return;
        }

        if (playerTwo.isActive() && playerTwo.getHero().hasUsedAbility == true) {
            actionsErrors.printErrorAbilityHero("Hero has already attacked this turn.", row, output);
            return;
        }

        // Get the hero from the player
        if (playerOne.isActive()) {
            Hero hero = playerOne.getHero();
            // Check if the hero attacked the correct row
            if ((row == 2 || row == 3) && (hero.getName().equals("Lord Royce") || hero.getName().equals("Empress Thorina"))) {
                actionsErrors.printErrorAbilityHero("Selected row does not belong to the enemy.", row, output);
                return;
            }
        }

        if (playerTwo.isActive()) {
            Hero hero = playerTwo.getHero();
            // Check if the hero attacked the correct row
            if ((row == 0 || row == 1) && (hero.getName().equals("Lord Royce") || hero.getName().equals("Empress Thorina"))) {
                actionsErrors.printErrorAbilityHero("Selected row does not belong to the enemy.", row, output);
                return;
            }
        }

        if (playerOne.isActive()) {
            Hero hero = playerOne.getHero();
            // Check if the hero attacked the correct row
            if ((row == 0 || row == 1) && (hero.getName().equals("General Kocioraw") || hero.getName().equals("King Mudface"))) {
                actionsErrors.printErrorAbilityHero("Selected row does not belong to the current player.", row, output);
                return;
            }
        }

        if (playerTwo.isActive()) {
            Hero hero = playerTwo.getHero();
            // Check if the hero attacked the correct row
            if ((row == 2 || row == 3) && (hero.getName().equals("General Kocioraw") || hero.getName().equals("King Mudface"))) {
                actionsErrors.printErrorAbilityHero("Selected row does not belong to the current player.", row, output);
                return;
            }
        }

        Hero hero;
        if (playerOne.isActive()) {
            hero = playerOne.getHero();

            // Set the hero to have used the ability this turn
            hero.hasUsedAbility = true;

            // Decrease the mana of the player
            playerOne.decreaseMana(playerOne.getHero().getMana());
        } else {
            hero = playerTwo.getHero();

            // Set the hero to have used the ability this turn
            hero.hasUsedAbility = true;

            // Decrease the mana of the player
            playerTwo.decreaseMana(playerTwo.getHero().getMana());
        }

        // Use the hero's ability based on the hero's name
        if (hero.getName().equals("Lord Royce")) {
            abilities.subZero(row, gameTable);
        } else if (hero.getName().equals("Empress Thorina")) {
            abilities.lowBlow(row, gameTable);

        } else if (hero.getName().equals("King Mudface")) {
            abilities.earthBorn(row, gameTable);
        } else if (hero.getName().equals("General Kocioraw")) {
            abilities.bloodThirst(row, gameTable);
        }

    }

    /**
     * Gets the number of wins for player one.
     *
     * @param output the output to be populated with the number of wins for player one
     */
    public void getPlayerOneWins(ArrayNode output) {
        ObjectNode getPlayerOneWinsOutput = output.addObject();
        getPlayerOneWinsOutput.put("command", "getPlayerOneWins");
        getPlayerOneWinsOutput.put("output", playerOneWins);
    }

    /**
     * Gets the number of wins for player two.
     *
     * @param output the output to be populated with the number of wins for player two
     */
    public void getPlayerTwoWins(ArrayNode output) {
        ObjectNode getPlayerTwoWinsOutput = output.addObject();
        getPlayerTwoWinsOutput.put("command", "getPlayerTwoWins");
        getPlayerTwoWinsOutput.put("output", playerTwoWins);
    }

    /**
     * Gets the total number of games played.
     *
     * @param output the output to be populated with the total number of games
     */
    public void getTotalGamesPlayed(ArrayNode output) {
        ObjectNode getTotalGamesPlayedOutput = output.addObject();
        getTotalGamesPlayedOutput.put("command", "getTotalGamesPlayed");
        getTotalGamesPlayedOutput.put("output", totalGamesPlayed);
    }

}
