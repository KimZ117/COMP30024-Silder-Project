/**
 * Created by Shreyash Patodia and Max Lee (Ho Suk Lee).
 * Student numbers: Shreyash - 767336, Max Lee - 719577
 * Login: Shreyash - spatodia, Max - hol2
 * Subject: COMP30024 Artificial Intelligence.
 * Semester 1, 2017.
 */

package com.teammaxine.board.scorers;

import com.teammaxine.board.elements.*;

/**
 * Simple scorer for testing purposes. Did not make the final
 * submission.
 */
public class AlphaScorer extends Scorer {
    // score += cell property * this

    // initialBoard is the board we start evaluating from.
    private Board initialBoard;
    double distance_change_score = 200;
    double lateral_move_penalty = -200;
    double action_finish_value = 50;
    // Scores to evaluate blockedness
    double b_blocked_score = -50;
    double blocking_value = 100;
    int moves;
    char player;

    public AlphaScorer(Board initialBoard, int depth, char player)
    {
        this.initialBoard = initialBoard;
        //this.lateral_move_penalty = (initialBoard.getSize() + 1) * -10;
        //this.action_finish_value = (initialBoard.getSize()) * 10;
        this.moves = (depth + 1)/2;
        this.player = player;
    }

    public double scoreBoard(Board board, char currentPlayer) {
        boolean playerIsHorizontal = this.player == 'H';
        boolean nextIsMyTurn = this.player == currentPlayer;

        if (playerIsHorizontal) {
            return scoreBoardHorizontal(board) - scoreBoardVertical(board);
        } else {
            return scoreBoardVertical(board) - scoreBoardHorizontal(board);
        }
    }

    public double scoreBoard(Board board, char playerPiece, int movesLeft) {
        boolean playerIsHorizontal = playerPiece == 'H';

        if (playerIsHorizontal) {
            return scoreBoardHorizontal(board, movesLeft);
        } else {
            return scoreBoardVertical(board, movesLeft);
        }
    }

    double scoreBoardVertical(Board board, int movesLeft) {
        double score = 0;

        int count = board.getVertical().getMyCells().size();
        int countBefore = initialBoard.getVertical().getMyCells().size();

        // inital board distance should be more than newBoard distance
        int distanceInitialBoard = sumVerticalDistance(this.initialBoard);
        int distanceNewBoard = sumVerticalDistance(board);
        // Should be positive for a good set of moves.
        int distanceChange = distanceInitialBoard - distanceNewBoard;
        // Higher is better
        score += distanceChange * distance_change_score;
        // Distance change should be the number of forward moves made.
        score += lateral_move_penalty * (moves - distanceChange - movesLeft);
        score += action_finish_value * (countBefore - count);
        return score;
    }

    double scoreBoardHorizontal(Board board, int movesLeft) {
        double score = 0;

        int maxDist = board.getSize();
        int count = board.getHorizontal().getMyCells().size();
        int countBefore = initialBoard.getHorizontal().getMyCells().size();

        // inital board distance should be more than newBoard distance
        int distanceInitialBoard = sumHorizontalDistance(this.initialBoard);
        int distanceNewBoard = sumHorizontalDistance(board);
        // Should be positive for a good set of moves.
        int distanceChange = distanceInitialBoard - distanceNewBoard;
        // Higher is better
        score += distanceChange * distance_change_score;
        score += lateral_move_penalty * (moves - distanceChange - movesLeft);
        score += action_finish_value * (countBefore - count);

        return score;
    }


    double horizontalRowSum(Board board) {
        double sum = 0;
        for(Cell c : board.getHorizontal().getMyCells().values())
            sum += c.getPos().getY();
        return sum;
    }

    double verticalColumnSum(Board board) {
        double sum = 0;
        for(Cell c : board.getVertical().getMyCells().values())
            sum += c.getPos().getX();
        return sum;
    }

    int horizontalMagnitudeOfBlockedness(Board b) {
        int magnitude = 0;
        Cell board[][] = b.getBoard();
        for(Cell c : b.getHorizontal().getMyCells().values()) {
            for (int i = c.getPos().getX() + 1; i < b.getSize(); i++) {
                char cellValue = board[c.getPos().getY()][i].getValue();
                if (cellValue == Board.CELL_BLOCKED) {
                    magnitude += b_blocked_score;
                }
            }
        }
        return magnitude;
    }

    int verticalMagnitudeOfBlockedness(Board b) {
        int magnitude = 0;
        Cell board[][] = b.getBoard();
        for(Cell c : b.getVertical().getMyCells().values()) {
            for (int i = c.getPos().getY() + 1; i < b.getSize(); i++) {
                char cellValue = board[i][c.getPos().getX()].getValue();
                if (cellValue == Board.CELL_BLOCKED) {
                    magnitude += b_blocked_score;
                }
            }
        }
        //System.out.println(magnitude);
        return magnitude;
    }


    /**
     * Functions finds how valuable the board is w.r.t to blocking the other player,
     * the
     * @param b
     * @return
     */
    double horizontalBlockingValue(Board b) {
        double score = 0;
        for(Cell c : b.getHorizontal().getMyCells().values()) {
            for(int i = 0; i < c.getPos().getY() - 1; i++) {
                char value = b.getBoard()[i][c.getPos().getX()].getValue();
                if(value == Board.CELL_VERTICAL) {
                    score += blocking_value;
                }
            }
        }
        return score;
    }

    double verticalBlockingValue(Board b) {
        double score = 0;
        for (Cell c : b.getVertical().getMyCells().values()) {
            for (int i = 0; i < c.getPos().getX() - 1; i++) {
                char value = b.getBoard()[c.getPos().getY()][i].getValue();
                if (value == Board.CELL_HORIZONTAL) {
                    score += blocking_value;
                }
            }
        }
        return score;
    }

}
