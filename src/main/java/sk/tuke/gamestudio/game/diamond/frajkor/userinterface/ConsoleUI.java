package sk.tuke.gamestudio.game.diamond.frajkor.userinterface;

import sk.tuke.gamestudio.game.diamond.frajkor.core.*;
import sk.tuke.gamestudio.game.diamond.frajkor.userinterface.inputhandlers.PlayerInputHandler;
import sk.tuke.gamestudio.game.diamond.frajkor.userinterface.inputhandlers.EasyBotInputHandler;
import sk.tuke.gamestudio.game.diamond.frajkor.userinterface.inputhandlers.InputHandler;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.comment.CommentException;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.rating.RatingException;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.score.ScoreException;
import sk.tuke.gamestudio.service.score.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.NoResultException;
import java.sql.Timestamp;
import java.util.*;

public class ConsoleUI implements UI {
    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    private Field field;
    private InputHandler playerInputHandler;
    private InputHandler botOpponentInputHandler;
    private boolean isOpponentBot = false;

    private static final String GAME_NAME = "Diamond";

    public ConsoleUI(Field field) {
        this.field = field;
        this.playerInputHandler = new PlayerInputHandler(field);
    }

    @Override
    public void run() {
        printGameDetails();
        printServices();
        askToPlayAgainstBot();
        do {
            gameLoop();
        }
        while (field.getGameState() == GameState.PLAYING && !isDrawn());
        printField();
        printFinalMessage();
        printScores();
        askForPlayerInput();
    }

    private void printServices() {
        printScores();
        printFiveLatestComments();
        printRatingsScreen();
    }

    private void askForPlayerInput() {
        askToComment();
        askToRateTheGame();
        askToPlayAgain();
    }


    private void printFinalMessage() {
        if (field.areDrawConditionsMet()) {
            System.out.println("The game has been drawn. No captures or removes in last 50 turns, or no move was possible.");
        }

        if (isDrawn()) {
            System.out.println("The game has been drawn.");
        }

        if (field.getGameState() == GameState.SOLVED) {
            if (isOpponentBot && field.getCurrentPlayer() == Player.WHITE) {
                System.out.println("Unfortunately, you have lost. Keep your head up and try again.");
            } else {
                System.out.println("Congratulations " + field.getCurrentPlayer().toString() + " player won!");
                addScore();
            }
        }
    }


    private void printCurrentGameInformation() {
        System.out.print("\u001B[34mGamephase: \u001B[0m" + field.getGamePhase() +
                "          " + "\u001B[34mCurrent Player: \u001B[0m" + field.getCurrentPlayer().toString());
        System.out.print("(" + getSymbolForPlayer() + ")\n");
    }

    private void printField() {
        printCurrentGameInformation();
        printFieldBody();
    }

    private void printFieldBody() {
        System.out.println("\u001B[33m==========================================================\u001B[0m");
        printUpperPart();
        printMiddlePart(9);
        printLowerPart();
        System.out.println("\u001B[33m==========================================================\u001B[0m");
    }

    private void printUpperPart() {
        printFirstPart(0, 'A');
        printSecondPart(3, 'D');
        printThirdPart(6, 'G');
    }

    private void printLowerPart() {
        printThirdPart(12, 'M');
        printSecondPart(15, 'P');
        printFirstPart(18, 'S');
    }

    private void printFirstPart(int i, char symbol) {
        System.out.print("\u001B[33m");
        System.out.println(symbol + "                             " + getCharacter(field.getField()[i][0]));
        System.out.print("\u001B[33m");
        System.out.println((char) (symbol + 1) + "                         " + getCharacter(field.getField()[i + 1][0]) +
                "       " + getCharacter(field.getField()[i + 1][1]));
        System.out.print("\u001B[33m");
        System.out.println((char) (symbol + 2) + "                             " + getCharacter(field.getField()[i + 2][0]));
    }

    private void printSecondPart(int i, char symbol) {
        System.out.print("\u001B[33m");
        System.out.println(symbol + "                     " + getCharacter(field.getField()[i][0]) +
                "               " + getCharacter(field.getField()[i][1]));
        System.out.print("\u001B[33m");
        System.out.println((char) (symbol + 1) + "                 " + getCharacter(field.getField()[i + 1][0]) +
                "       " + getCharacter(field.getField()[i + 1][1]) + "       " + getCharacter(field.getField()[i + 1][2]) +
                "       " + getCharacter(field.getField()[i + 1][3]));
        System.out.print("\u001B[33m");
        System.out.println((char) (symbol + 2) + "                     " + getCharacter(field.getField()[i + 2][0]) +
                "               " + getCharacter(field.getField()[i + 2][1]));
    }

    private void printThirdPart(int i, char symbol) {
        System.out.print("\u001B[33m");
        System.out.println(symbol + "              " + getCharacter(field.getField()[i][0]) + "              " +
                getCharacter(field.getField()[i][1]) + "               " +
                getCharacter(field.getField()[i][2]) + "             ");
        System.out.print("\u001B[33m");
        System.out.println((char) (symbol + 1) + "          " + getCharacter(field.getField()[i + 1][0]) +
                "       " + getCharacter(field.getField()[i + 1][1]) + "     " + getCharacter(field.getField()[i + 1][2]) +
                "         " + getCharacter(field.getField()[i + 1][3]) + "     " + getCharacter(field.getField()[i + 1][4]) +
                "        " + getCharacter(field.getField()[i + 1][5]));
        System.out.print("\u001B[33m");
        System.out.println((char) (symbol + 2) + "              " + getCharacter(field.getField()[i + 2][0]) +
                "              " + getCharacter(field.getField()[i + 2][1]) +
                "               " + getCharacter(field.getField()[i + 2][2]));
    }

    private void printMiddlePart(int i) {
        System.out.print("\u001B[33m");
        System.out.println("J       " + getCharacter(field.getField()[i][0]) + "              " + getCharacter(field.getField()[i][1]) +
                "               " + getCharacter(field.getField()[i][2]) + "               " + getCharacter(field.getField()[i][3]));
        System.out.print("\u001B[33m");
        System.out.println("K          " + getCharacter(field.getField()[i + 1][0]) + "      " + getCharacter(field.getField()[i + 1][1]) +
                "       " + getCharacter(field.getField()[i + 1][2]) + "       " + getCharacter(field.getField()[i + 1][3]) +
                "        " + getCharacter(field.getField()[i + 1][4]) + "       " + getCharacter(field.getField()[i + 1][5]));
        System.out.print("\u001B[33m");
        System.out.println("L       " + getCharacter(field.getField()[i + 2][0]) + "              " + getCharacter(field.getField()[i + 2][1]) +
                "               " + getCharacter(field.getField()[i + 2][2]) + "               " + getCharacter(field.getField()[i + 2][3]));
    }

    private String getCharacter(Piece piece) {
        switch (piece.getPieceType()) {
            case EMPTY:
                return ("\u001B[34m" + "." + "\u001B[0m");
            case NEUTRAL:
                return ("\u001B[31m" + "#" + "\u001B[0m");
            case BLACK:
                return ("\u001B[37m" + "&" + "\u001B[0m");
            case WHITE:
                return ("\u001B[30m" + "$" + "\u001B[0m");
            default:
                throw new IllegalStateException();
        }
    }

    private String getSymbolForPlayer() {
        switch (field.getCurrentPlayer()) {
            case BLACK:
                return ("\u001B[37m" + "&" + "\u001B[0m");
            case WHITE:
                return ("\u001B[30m" + "$" + "\u001B[0m");
            default:
                throw new IllegalStateException();
        }
    }

    private void printGameDetails() {
        System.out.println("\n\u001B[33m         ========= DIAMOND =========\u001B[0m");
        System.out.println("The goal of this game(win condition) is to occupy 4 corners of any square.");
        System.out.println("Game has two phases: Placement phase and Movement phase");
        System.out.println("You can only place pieces on to the board during Placement phase.");
        System.out.println("After placing all 24 pieces(12 each for player), the game proceeds to Movement phase.");
        System.out.println("During Movement phase, you can either move your piece to new coordinates(if they're connected)\n" +
                "or you can choose to remove a neutral piece.");
        System.out.println("Neutral(red) pieces enter the game via captures on triangles.");
        System.out.println("Neutral pieces can be removed only if there are no adjacent player pieces near.");
        System.out.println("Draw happens if in 50 turns no piece has been captured or neutral piece removed " +
                "or current player cannot perform a move.\n");
        System.out.println("To show connected pieces, write 'SHOW' and coordinates of the specific piece. (e.g show A1, showK3)");
        System.out.println("To propose a draw, write 'DRAW' at any time.");
        System.out.println("To end the game, write 'EXIT' at any time.");

        pressEnterKeyToContinue();
    }

    private void pressEnterKeyToContinue() {
        System.out.println("Press Enter key to continue...");
        new Scanner(System.in).nextLine();
    }

    private void askToPlayAgain() {
        System.out.println("\u001B[35mWould you like to play again? [Yes/No]\u001B[0m");
        String answer = new Scanner(System.in).nextLine().strip().toUpperCase();

        switch (answer) {
            case "YES":
                System.out.println("Starting the game again.");
                this.field = new Field();
                this.playerInputHandler = new PlayerInputHandler(field);
                this.isOpponentBot = false;
                run();
                return;
            case "NO":
                System.out.println("Exiting the game");
                System.exit(0);
                break;
            default:
                System.out.println("Exiting anyway..");
                System.exit(0);
                break;
        }
    }

    private void askToPlayAgainstBot() {
        System.out.println("\u001B[35mWould you like to play against a bot? [Yes/No]\u001B[0m");
        String answer = new Scanner(System.in).nextLine().strip().toUpperCase();

        switch (answer) {
            case "YES":
                this.isOpponentBot = true;
                this.botOpponentInputHandler = new EasyBotInputHandler(this.field);
                System.out.println("Setting up the bot...You are going first");
                pressEnterKeyToContinue();
                return;
            case "NO":
                System.out.println("Setting up local multiplayer game..");
                break;
            default:
                System.out.println("Wrong answer, please try again.");
                askToPlayAgainstBot();
                break;
        }
    }

    private void printSquareCoordinates() {
        System.out.print("\u001B[34mSquares: \u001B[0m");
        for (int i = 0; i < getSquareCoordinates().length; i++) {
            System.out.print(getSquareCoordinates()[i] + " ");
            if (i == getSquareCoordinates().length / 2 - 2) {
                System.out.println();
            }
        }
        System.out.println();
    }

    private boolean isDrawn() {
        return field.areDrawConditionsMet() || playerInputHandler.isDrawnByPlayer();
    }

    private String[] getSquareCoordinates() {
        StringBuilder stringBuilder = new StringBuilder();
        field.getTiles().stream()
                .filter(t -> t instanceof Square)
                .map(s -> s.getPieces())
                .forEach(p -> playerInputHandler.findPositionOfPiecesInField(p, stringBuilder));

        String[] squareCoordinates = stringBuilder.toString().split(" ");
        Arrays.sort(squareCoordinates);
        return squareCoordinates;
    }

    private void printScores() {
        try {
            List<Score> scores = scoreService.getBestScores(GAME_NAME);
            System.out.println("\u001B[34mLeaderboard: \u001B[0m");

            if (scores.isEmpty()) {
                System.out.println("Leaderboard is empty");
                return;
            }
            Collections.sort(scores);
            Collections.reverse(scores);
            for (Score score : scores) {
                System.out.println("\u001B[33m" + score.getPlayer().toUpperCase() + "\u001B[0m" +
                        "     " + score.getPoints() + "     " + score.getPlayedOn().toString());
            }
        } catch (ScoreException e) {
            System.out.println("Could not print overall scores. ");
        }
    }

    private void askToComment() {
        System.out.println("\u001B[35mWould you like to add a comment to this game? [Yes/No]\u001B[0m");
        String answer = new Scanner(System.in).nextLine().strip().toUpperCase();

        switch (answer) {
            case "YES":
                addComment();
                return;
            case "NO":
                return;
            default:
                System.out.println("Not a valid option, please enter 'Yes' or 'No'");
                askToComment();
                break;
        }
    }

    private void addComment() {
        System.out.println("Please enter your comment:");
        String comment = new Scanner(System.in).nextLine().strip();
        try {
            commentService.addComment(new Comment(System.getProperty("user.name"), GAME_NAME, comment, new Date()));
        } catch (CommentException e) {
            System.out.println("There was a error sending your comment. Please try again later");
            return;
        }
        System.out.println("Thank you for your comment, it has been sent.");
    }

    private void printFiveLatestComments() {
        try {
            List<Comment> commentList = commentService.getComments(GAME_NAME);
            System.out.println("\u001B[34mLatest comments: \u001B[0m");
            if (commentList.isEmpty()) {
                System.out.println("Comment list for this game is empty.");
                return;
            }
            Collections.sort(commentList, (x, y) -> {
                return y.getCommentedOn().compareTo(x.getCommentedOn());
            });

            for (int i = 0; i < commentList.size(); i++) {
                if (i == 5) {
                    break;
                }
                Comment currentComment = commentList.get(i);
                System.out.println("\u001B[33m" + currentComment.getPlayer().toUpperCase() + "\u001B[0m" +
                        "       " + currentComment.getComment() + "      " + currentComment.getCommentedOn());
            }
        } catch (CommentException e) {
            System.out.println("Something went wrong, unable to load five last comments ");
        }
    }

    private void printRatingsScreen() {
        printAverageRating();
        printCurrentPlayerRating();
        pressEnterKeyToContinue();
    }

    private void printAverageRating() {
        try {
            int averageRating = ratingService.getAverageRating(GAME_NAME);
            System.out.println("\u001B[34mThe average rating \u001B[0mfor this game is: " + averageRating);
        } catch (RatingException | NullPointerException | NoResultException e) {
            System.out.println("Unable to get average rating for this game ");
        }
    }

    private void printCurrentPlayerRating() {
        try {
            int currentPlayerRating = ratingService.getRating(GAME_NAME, System.getProperty("user.name"));
            System.out.println("\u001B[34mYour current rating \u001B[0mis: " + currentPlayerRating);
        } catch (RatingException | NoResultException e) {
            System.out.println("You have not rated this game yet. ");
        }
    }

    private void askToRateTheGame() {
        System.out.println("\u001B[35mWould you like to rate the game? [Yes/No]\u001B[0m");
        printCurrentPlayerRating();
        String answer = new Scanner(System.in).nextLine().strip().toUpperCase();

        switch (answer) {
            case "YES":
                rateTheGame();
                return;
            case "NO":
                return;
            default:
                System.out.println("Not a valid option, please enter 'Yes' or 'No'");
                askToRateTheGame();
                break;
        }
    }

    private void rateTheGame() {
        int rating;
        do {
            System.out.println("Please enter your rating from 0 to 5");
            rating = new Scanner(System.in).nextInt();
        } while (rating < 0 || rating > 5);

        try {
            ratingService.setRating(new Rating(System.getProperty("user.name"), GAME_NAME, rating, new Date()));
        } catch (RatingException e) {
            System.out.println("There was a error sending your rating. Please try again later");
            return;
        }
        System.out.println("Thank you for your rating.");
    }

    private void gameLoop() {
        printField();
        printSquareCoordinates();

        if (isOpponentBot && field.getCurrentPlayer() == Player.WHITE) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            botOpponentInputHandler.handleInput();
        } else {
            playerInputHandler.handleInput();
        }
        System.out.println();
        System.out.println();
    }

    private void addScore() {
        try {
            scoreService.addScore(
                    new Score(GAME_NAME, System.getProperty("user.name"), field.getScore(), new Timestamp(new Date().getTime())));
            System.out.println("Entered your score: " + field.getScore() + " into the database as " + System.getProperty("user.name"));
        } catch (ScoreException e) {
            System.out.println("Could not add your score ");
        }
    }


}

