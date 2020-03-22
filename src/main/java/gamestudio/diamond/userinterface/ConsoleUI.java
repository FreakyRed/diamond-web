package gamestudio.diamond.userinterface;

import gamestudio.diamond.core.Field;
import gamestudio.diamond.core.GameState;
import gamestudio.diamond.core.Piece;
import gamestudio.diamond.core.Square;
import gamestudio.diamond.userinterface.inputhandlers.ConsoleInputHandler;
import gamestudio.entity.Comment;
import gamestudio.entity.Score;
import gamestudio.service.comment.CommentException;
import gamestudio.service.comment.CommentService;
import gamestudio.service.comment.CommentServiceJDBC;
import gamestudio.service.score.ScoreException;
import gamestudio.service.score.ScoreService;
import gamestudio.service.score.ScoreServiceJDBC;

import java.util.*;

public class ConsoleUI implements UI {
    private Field field;
    private ConsoleInputHandler inputHandler;

    private ScoreService scoreService = new ScoreServiceJDBC();
    private CommentService commentService = new CommentServiceJDBC();

    private static final String GAME_NAME = "Diamond";

    public ConsoleUI(Field field) {
        this.field = field;
        this.inputHandler = new ConsoleInputHandler(field);
    }

    @Override
    public void run() {
        printInfoAboutGame();
        printScores();
        printFiveLatestComments();
        do {
            printField();
            printSquareCoordinates();
            handleInput();
        }
        while (field.getGameState() == GameState.PLAYING && !isDrawn());
        printField();
        printFinalMessage();
        askToComment();
        playAgain();
    }

    private void printFinalMessage() {
        if (field.areDrawConditionsMet()) {
            System.out.println("The game has been drawn. No captures or removes in last 50 turns, or no move was possible.");
        }

        if (inputHandler.isDrawnByPlayer()) {
            System.out.println("The game has been drawn.");
        }

        if (field.getGameState() == GameState.SOLVED) {
            System.out.println("Congratulations " + field.getCurrentPlayer().toString() + " player won!");
            scoreService.addScore(new Score(GAME_NAME, System.getProperty("user.name"), field.getScore(), new Date()));
            System.out.println("Entered your score: " + field.getScore() + " into the database");
        }
    }

    private void printGameDetails() {
        System.out.print("\u001B[34mGamephase: \u001B[0m" + field.getGamePhase() +
                "          " + "\u001B[34mCurrent Player: \u001B[0m" + field.getCurrentPlayer().toString());
        System.out.print("(" + getSymbolForPlayer() + ")\n");
    }

    private void printField() {
        printGameDetails();
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

    private void handleInput() {
        inputHandler.handleInput();
    }

    private void printInfoAboutGame() {
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

    private void playAgain() {
        System.out.println("\u001B[35mWould you like to play again? [Yes/No]\u001B[0m");
        String answer = new Scanner(System.in).nextLine().strip().toUpperCase();

        switch (answer) {
            case "YES":
                System.out.println("Starting the game again.");
                this.field = new Field();
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
        return field.areDrawConditionsMet() || inputHandler.isDrawnByPlayer();
    }

    private String[] getSquareCoordinates() {
        StringBuilder stringBuilder = new StringBuilder();
        field.getTiles().stream()
                .filter(t -> t instanceof Square)
                .map(s -> s.getPieces())
                .forEach(p -> inputHandler.findPositionOfPieceInField(p, stringBuilder));

        String[] squareCoordinates = stringBuilder.toString().split(" ");
        Arrays.sort(squareCoordinates);
        return squareCoordinates;
    }

    private void printScores() {
        List<Score> scores = scoreService.getBestScores(GAME_NAME);

        Collections.sort(scores);
        Collections.reverse(scores);

        System.out.println("\u001B[34mLeaderboard: \u001B[0m");
        for (Score score : scores) {
            System.out.println("\u001B[33m" + score.getPlayer().toUpperCase() + "\u001B[0m" +
                    "     " + score.getPoints() + "     " + score.getPlayedOn().toString());
        }
        pressEnterKeyToContinue();
    }

    private void askToComment() {
        System.out.println("\u001B[35mWould you like to add a comment to this game? [Yes/No]\u001B[0m");
        String answer = new Scanner(System.in).nextLine().strip().toUpperCase();

        switch (answer) {
            case "YES":
                System.out.println("Please enter your comment (max. 200 characters):");
                String comment = new Scanner(System.in).nextLine().strip();
                try {
                    commentService.addComment(new Comment(System.getProperty("user.name"), GAME_NAME, comment, new Date()));
                } catch (CommentException e) {
                    System.out.println("There was a error sending your comment. Please try again later");
                    return;
                }
                System.out.println("Thank you for your comment, it was send.");
                return;
            case "NO":
                return;
            default:
                System.out.println("Not a valid option, please enter 'Yes' or 'No'");
                askToComment();
                break;
        }
    }

    private void printFiveLatestComments() {
        try {
            List<Comment> commentList = commentService.getComments(GAME_NAME);
            Collections.sort(commentList, (x, y) -> {
                return y.getCommentedOn().compareTo(x.getCommentedOn());
            });

            System.out.println("\u001B[34mLatest comments: \u001B[0m");
            for (int i = 0; i < commentList.size(); i++) {
                if (i == 5) {
                    break;
                }
                Comment currentComment = commentList.get(i);
                System.out.println("\u001B[33m" + currentComment.getPlayer().toUpperCase() + "\u001B[0m" +
                        "       " + currentComment.getComment() + "      " + currentComment.getCommentedOn());
            }
        } catch (CommentException e) {
            System.out.println("Something went wrong, unable to load five last comments");
        }
        pressEnterKeyToContinue();
    }


}

