package main;

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
import Cards.Minion;
import GameTable.GameTable;
import CardsActions.CardsActionsErrors;
import CardsActions.Abilities;



public class GameGwentStone {

    Player playerOne = new Player();
    Player playerTwo = new Player();
    GameTable gameTable = new GameTable();
    CardsActionsErrors actionsErrors = new CardsActionsErrors();
    Abilities abilities = new Abilities();

    public int manaIncrement = 1;


    public GameGwentStone() {
    }

    public void startPlayingTheGame(Input input, ArrayNode output) {
        System.out.println("Start playing the game");
        for (GameInput game : input.getGames()) {
            processDataForEachPlayer(input, game.getStartGame());
            startNewGame(input, output, game);
        }
    }

    public void startNewGame(Input input, ArrayNode output, GameInput game) {
        int completedPlayerTurns = 0;
        int playerTurn = game.getStartGame().getStartingPlayer();
        manaIncrement = 1;

        playerOne.takeFirstCardFromDeck();
        playerTwo.takeFirstCardFromDeck();

        if(playerTurn == 1) {
            playerOne.setActive(true);
            playerTwo.setActive(false);

        } else {
            playerOne.setActive(false);
            playerTwo.setActive(true);
        }

        for (ActionsInput action : game.getActions()) {
            switch (action.getCommand()) {
                case "endPlayerTurn":
                    completedPlayerTurns++;
                    endRoundChanges(playerTurn, completedPlayerTurns);
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
                    methodGetPlayerMana(action.getPlayerIdx(), game.getStartGame(), output);
                    break;
                case "getCardsInHand":
                    methodGetCardsInHand(action.getPlayerIdx(), output);
                    break;
                case "getFrozenCardsOnTable":
                    getFrozenCardsOnTable(gameTable, output);
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
                default:
                    break;
            }
        }
    }

    private void endRoundChanges(int playerTurn, int completedPlayerTurns) {

        // they completed a round
        if (completedPlayerTurns % 2 == 0){

            if(manaIncrement < 10) {
                manaIncrement++;
            }

            playerOne.increaseMana(manaIncrement);
            playerTwo.increaseMana(manaIncrement);

            playerOne.takeFirstCardFromDeck();
            playerTwo.takeFirstCardFromDeck();

            gameTable.startNewRound(playerOne, playerTwo);
        }

        if (playerTurn == 1) {
            playerTurn = 2;
        } else {
            playerTurn = 1;
        }

        playerOne.changePlayerTurn();
        playerTwo.changePlayerTurn();

    }

    private void processDataForEachPlayer(Input input, final StartGameInput startGame) {

        // For the first player
        // Get the decks for the current player based on the index
        DecksInput decksPlayerOne = input.getPlayerOneDecks();
        int deckIdxPlayerOne = startGame.getPlayerOneDeckIdx();

        // Get the list of decks from the input
        ArrayList<ArrayList<CardInput>> allPlayerOneDecks = decksPlayerOne.getDecks();

        // Get the deck that the player has chosen
        ArrayList<CardInput> chosenDeckByPlayerOne = allPlayerOneDecks.get(deckIdxPlayerOne);

        // Put the chosen deck in a new list and shuffle it
        ArrayList<CardInput> selectedDeckByPlayerOne = new ArrayList<>(chosenDeckByPlayerOne);
        long seed = startGame.getShuffleSeed();
        Collections.shuffle(selectedDeckByPlayerOne, new Random(seed));

        // Set the deck for the player
        playerOne.setDeckFromInput(selectedDeckByPlayerOne);
        //playerOne.takeFirstCardFromDeck();

        CardInput heroPlayerOne = startGame.getPlayerOneHero();
        Hero heroCardPlayerOne = new Hero(heroPlayerOne.getMana(), 30, heroPlayerOne.getAttackDamage(),
                heroPlayerOne.getDescription(), heroPlayerOne.getName(), heroPlayerOne.getColors());
        playerOne.setHero(heroCardPlayerOne);
        playerOne.setMana(1);


        // For the second player
        // Get the decks for the current player based on the index
        DecksInput decksPlayerTwo = input.getPlayerTwoDecks();
        int deckIdxPlayerTwo = startGame.getPlayerTwoDeckIdx();

        // Get the list of decks from the input
        ArrayList<ArrayList<CardInput>> allPlayerTwoDecks = decksPlayerTwo.getDecks();

        // Get the deck that the player has chosen
        ArrayList<CardInput> chosenDeckByPlayerTwo = allPlayerTwoDecks.get(deckIdxPlayerTwo);

        // Put the chosen deck in a new list and shuffle it
        ArrayList<CardInput> selectedDeckByPlayerTwo = new ArrayList<>(chosenDeckByPlayerTwo);
        Collections.shuffle(selectedDeckByPlayerTwo, new Random(seed));

        // Set the deck for the player
        playerTwo.setDeckFromInput(selectedDeckByPlayerTwo);
        //playerTwo.takeFirstCardFromDeck();

        CardInput heroPlayerTwo = startGame.getPlayerTwoHero();
        Hero heroCardPlayerTwo = new Hero(heroPlayerTwo.getMana(), 30, heroPlayerTwo.getAttackDamage(),
                heroPlayerTwo.getDescription(), heroPlayerTwo.getName(), heroPlayerTwo.getColors());
        playerTwo.setHero(heroCardPlayerTwo);
        playerTwo.setMana(1);

    }

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

    private void methodGetPlayerTurn(ArrayNode output) {
        int isPlayerTurn;
        if(playerOne.isActive()) {
            isPlayerTurn = 1;
        } else {
            isPlayerTurn = 2;
        }
        ObjectNode turnOutput = output.addObject();
        turnOutput.put("command", "getPlayerTurn");
        turnOutput.put("output", isPlayerTurn);
    }

    private void methodGetPlayerMana(final int playerIdx, final StartGameInput startGame, ArrayNode output) {
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

    private void placeCard(final int handIdx, ArrayNode output) {
        Player currentPlayer;
        int rowIdx;
        if (playerOne.isActive() == true) {
            currentPlayer = playerOne;
        } else {
            currentPlayer = playerTwo;
        }

        if (currentPlayer.getCardsInHand().size() <= handIdx) {
            return;
        }
        Card card = currentPlayer.getCardsInHand().get(handIdx);
        Minion minion = new Minion(card.getMana(), card.getHealth(), card.getAttackDamage(), card.getDescription(), card.getName(), false, card.getColors());


        if (minion.isFrontRow() && playerOne.isActive() == true) {
            rowIdx = 2;
        }
        else if (minion.isFrontRow() && playerOne.isActive() == false) {
            rowIdx = 1;
        }
        else if (!minion.isFrontRow() && playerOne.isActive() == true) {
            rowIdx = 3;
        }
        else {
            rowIdx = 0;
        }

        if (currentPlayer.getMana() < card.getMana()) {
            ObjectNode errorNotEnoughMana = output.addObject();
            errorNotEnoughMana.put("command", "placeCard");
            errorNotEnoughMana.put("handIdx", handIdx);
            errorNotEnoughMana.put("error", "Not enough mana to place card on table.");
            return;
        }
        else if (gameTable.addCard(rowIdx, minion, output) == true) {
            currentPlayer.decreaseMana(card.getMana());
            currentPlayer.removeCardFromHand(handIdx);
        }


    }

    private void getCardsOnTable(ArrayNode output) {
        ObjectNode getCardsOnTableOutput = output.addObject();
        getCardsOnTableOutput.put("command", "getCardsOnTable");

        ArrayNode cardsOnTableArray = getCardsOnTableOutput.putArray("output");

        gameTable.getTableOutput(cardsOnTableArray);
    }

    private void getFrozenCardsOnTable(GameTable gameTable, ArrayNode output) {
        ObjectNode getFrozenCardsOnTableOutput = output.addObject();
        getFrozenCardsOnTableOutput.put("command", "getFrozenCardsOnTable");

        ArrayNode frozenCardsArray = getFrozenCardsOnTableOutput.putArray("output");

        gameTable.getFrozenCardsTable(frozenCardsArray);
    }

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

    private void cardUsesAttack(Coordinates cardAttacker, Coordinates cardAttacked, ArrayNode output) {
        int rowAttacker = cardAttacker.getX();
        int columnAttacker = cardAttacker.getY();
        int rowAttacked = cardAttacked.getX();
        int columnAttacked = cardAttacked.getY();

        Minion attacker = gameTable.getCardFromTable(rowAttacker, columnAttacker, output);
        Minion victim = gameTable.getCardFromTable(rowAttacked, columnAttacked, output);

        if (playerOne.isActive() && (rowAttacked == 2 || rowAttacked == 3)) {
            actionsErrors.printErrorAttack("Attacked card does not belong to the enemy.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (playerTwo.isActive() && (rowAttacked == 0 || rowAttacked == 1)) {
            actionsErrors.printErrorAttack("Attacked card does not belong to the enemy.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (playerOne.isActive() && (gameTable.existsTankOnRow(0) == true || gameTable.existsTankOnRow(1) == true) && (victim.isTank() == false)) {
            actionsErrors.printErrorAttack("Attacked card is not of type 'Tank'.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (playerTwo.isActive() && (gameTable.existsTankOnRow(2) == true || gameTable.existsTankOnRow(3) == true) && (victim.isTank() == false)) {
            actionsErrors.printErrorAttack("Attacked card is not of type 'Tank'.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (attacker.hasAttackedThisTurn() == true) {
            actionsErrors.printErrorAttack("Attacker card has already attacked this turn.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (attacker.usedAbilityThisTurn() == true) {
            actionsErrors.printErrorAttack("Attacker card has already attacked this turn.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (attacker.isFrozen() == true) {
            actionsErrors.printErrorAttack("Attacker is frozen.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        System.out.println("attacker: " + attacker.getAttackDamage());
        System.out.println("victim: " + victim.getHealth());

        if (victim.getHealth() - attacker.getAttackDamage() <= 0) {
            // gameTable.removeCard(rowAttacked, columnAttacked);
        }
        else {
            victim.setHealth(victim.getHealth() - attacker.getAttackDamage());
        }
        attacker.hasAttackedThisTurn = true;


    }

    public void useCardAbility(Coordinates cardAttacker, Coordinates cardAttacked, ArrayNode output) {
        int rowAttacker = cardAttacker.getX();
        int columnAttacker = cardAttacker.getY();
        int rowAttacked = cardAttacked.getX();
        int columnAttacked = cardAttacked.getY();

        Minion attacker = gameTable.getCardFromTable(rowAttacker, columnAttacker, output);
        Minion victim = gameTable.getCardFromTable(rowAttacked, columnAttacked, output);

        if (attacker.getName().equals("Goliath") || attacker.getName().equals("Warden")) {
            return;
        }
        if (playerOne.isActive() && (rowAttacked == 0 || rowAttacked == 1) && attacker.getName().equals("Disciple")) {
            actionsErrors.printErrorAttack("Attacked card does not belong to the current player.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

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

        if (playerOne.isActive() && (gameTable.existsTankOnRow(0) == true || gameTable.existsTankOnRow(1) == true) && (victim.isTank() == false) && !attacker.getName().equals("Disciple")) {
            actionsErrors.printErrorAbility("Attacked card is not of type 'Tank'.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (playerTwo.isActive() && (gameTable.existsTankOnRow(2) == true || gameTable.existsTankOnRow(3) == true) && (victim.isTank() == false) && !attacker.getName().equals("Disciple")) {
            actionsErrors.printErrorAbility("Attacked card is not of type 'Tank'.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (attacker.hasAttackedThisTurn() == true) {
            actionsErrors.printErrorAbility("Attacker card has already attacked this turn.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (attacker.usedAbilityThisTurn() == true) {
            actionsErrors.printErrorAbility("Attacker card has already attacked this turn.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (attacker.isFrozen() == true) {
            actionsErrors.printErrorAbility("Attacker is frozen.", rowAttacker, columnAttacker, rowAttacked, columnAttacked, output);
            return;
        }

        if (attacker.getName().equals("The Ripper")) {
            abilities.weakKnees(attacker, victim);
        }
        else if(attacker.getName().equals("Disciple")) {
            abilities.godsPlan(victim);
        }
        else if(attacker.getName().equals("Miraj")) {
            abilities.skyJack(attacker, victim);
        }
        else if(attacker.getName().equals("The Cursed One")) {
            abilities.shapeshift(victim, gameTable, rowAttacked, columnAttacked);
        }

        attacker.usedAbilityThisTurn = true;



    }

    public void heroIsAttacked(Coordinates cardAttacker, ArrayNode output) {
        int rowAttacker = cardAttacker.getX();
        int columnAttacker = cardAttacker.getY();

        Minion attacker = gameTable.getCardFromTable(rowAttacker, columnAttacker, output);
        if (attacker == null) {
            return;
        }

        if (playerOne.isActive() && (gameTable.existsTankOnRow(0) == true || gameTable.existsTankOnRow(1) == true)) {
            actionsErrors.printErrorAttackHero("Attacked card is not of type 'Tank'.", rowAttacker, columnAttacker, output);
            return;
        }

        if (playerTwo.isActive() && (gameTable.existsTankOnRow(2) == true || gameTable.existsTankOnRow(3) == true)) {
            actionsErrors.printErrorAttackHero("Attacked card is not of type 'Tank'.", rowAttacker, columnAttacker, output);
            return;
        }

        if (attacker.hasAttackedThisTurn == true) {
            actionsErrors.printErrorAttackHero("Attacker card has already attacked this turn.", rowAttacker, columnAttacker, output);
            return;
        }

        if (attacker.isFrozen() == true) {
            actionsErrors.printErrorAttackHero("Attacker is frozen.", rowAttacker, columnAttacker, output);
            return;
        }

        attacker.hasAttackedThisTurn = true;

        Hero hero;
        if (playerTwo.isActive()) {
            hero = playerOne.getHero();
            if (hero.getHealth() - attacker.getAttackDamage() <= 0) {
                ObjectNode gameEnded = output.addObject();
                gameEnded.put("gameEnded", "Player two killed the enemy hero.");

            } else {
                playerOne.getHero().setHealth(hero.getHealth() - attacker.getAttackDamage());
            }
        } else {
            hero = playerTwo.getHero();
            if (hero.getHealth() - attacker.getAttackDamage() <= 0) {
                ObjectNode gameEnded = output.addObject();
                gameEnded.put("gameEnded", "Player one killed the enemy hero.");
            } else {
                playerTwo.getHero().setHealth(hero.getHealth() - attacker.getAttackDamage());
            }
        }


    }

    public void heroUseAbility(int row, ArrayNode output) {

        if (playerOne.isActive()) {
            Hero hero = playerOne.getHero();
            if ((row == 2 || row == 3) && (hero.getName().equals("Lord Royce") || hero.getName().equals("Empress Thorina"))) {
                actionsErrors.printErrorAbilityHero("Selected row does not belong to the enemy.", row, output);
                return;
            }
        }

        if (playerTwo.isActive()) {
            Hero hero = playerTwo.getHero();
            if ((row == 0 || row == 1) && (hero.getName().equals("Lord Royce") || hero.getName().equals("Empress Thorina"))) {
                actionsErrors.printErrorAbilityHero("Selected row does not belong to the enemy.", row, output);
                return;
            }
        }

        if (playerOne.isActive() && playerOne.getHero().hasUsedAbility == true) {
            actionsErrors.printErrorAbilityHero("Hero has already attacked this turn.", row, output);
            return;
        }

        if (playerTwo.isActive() && playerTwo.getHero().hasUsedAbility == true) {
            actionsErrors.printErrorAbilityHero("Hero has already attacked this turn.", row, output);
            return;
        }


        if (playerOne.isActive() && playerOne.getMana() < playerOne.getHero().getMana()) {
            actionsErrors.printErrorAbilityHero("Not enough mana to use hero's ability.", row, output);
            return;
        }

        if (playerTwo.isActive() && playerTwo.getMana() < playerTwo.getHero().getMana()) {
            actionsErrors.printErrorAbilityHero("Not enough mana to use hero's ability.", row, output);
            return;
        }

        Hero hero;
        if (playerOne.isActive()) {
            hero = playerOne.getHero();
            hero.hasUsedAbility = true;
            playerOne.decreaseMana(playerOne.getHero().getMana());
        }
        else {
            hero = playerTwo.getHero();
            hero.hasUsedAbility = true;
            playerTwo.decreaseMana(playerTwo.getHero().getMana());
        }

        if (hero.getName().equals("Lord Royce")) {
            abilities.subZero(row, gameTable);

        } else if (hero.getName().equals("Empress Thorina")) {
            abilities.lowBlow(row, gameTable);

        } else if (hero.getName().equals("King Mudface")) {
            abilities.earthBorn(row, gameTable);
        }
        else if (hero.getName().equals("General Kocioraw")) {
            abilities.bloodThirst(row, gameTable);
        }

    }




}
