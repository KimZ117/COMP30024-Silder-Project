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
 * Simple scorer for testing purposes, did not make
 * out submission.
 */
public class DeltaScorer extends Scorer {
    // score += cell property * this

    // initialBoard is the board we start evaluating from.
    private Board initialBoard;

    double distance_change_score = 10;
    // Scores to evaluate blockedness
    double b_blocked_score = -10;
    double other_blocked_score = 20;

    public DeltaScorer(Board initialBoard, int depth) {
        this.initialBoard = initialBoard;
        //
        // this.depth = depth;
    }

    public double scoreBoard(Board board, char playerPiece) {
        boolean playerIsHorizontal = playerPiece == 'H';

        if (playerIsHorizontal) {
            return scoreBoardHorizontal(board);
        } else {
            return scoreBoardVertical(board);
        }
    }

    double scoreBoardVertical(Board board) {
        double score = 0;

        int maxDist = board.getSize();
        int count = board.getVertical().getMyCells().size();

        // inital board distance should be more than newBoard distance
        int distanceInitialBoard = sumVerticalDistance(this.initialBoard);
        int distanceNewBoard = sumVerticalDistance(board);
        // Should be positive for a good set of moves.
        int distanceChange = distanceInitialBoard - distanceNewBoard;
        // Higher is better
        score += distanceChange * distance_change_score;

        //System.out.println
        //score += verticalMagnitudeOfBBlockedness(board) - verticalMagnitudeOfBBlockedness(initialBoard);
        //score += verticalHBlockedness(board) - verticalHBlockedness(initialBoard);

        return score;
    }

    double scoreBoardHorizontal(Board board) {
        double score = 0;

        int maxDist = board.getSize();
        int count = board.getHorizontal().getMyCells().size();

        // inital board distance should be more than newBoard distance
        int distanceInitialBoard = sumHorizontalDistance(this.initialBoard);
        int distanceNewBoard = sumHorizontalDistance(board);
        // Should be positive for a good set of moves.
        int distanceChange = distanceInitialBoard - distanceNewBoard;
        // Higher is better
        score += distanceChange * distance_change_score;

        //score += horizontalMagnitudeOfBBlockedness(board) - horizontalMagnitudeOfBBlockedness(initialBoard);
        // Sometimes we may get closer to the goal by moving laterally and then
        // moving forward.
        // + H V + +
        // + + + + +
        // In the above H may be able to move laterally and then move forward
        // but we want to not promote that sort of move, because it means that
        // we are making less progress than the opponent and we want to allow
        // them to move and let us go straight ahead instead.

        //score += horizontalVBlockedness(board) - horizontalVBlockedness(initialBoard);
        return score;
    }

    double horizontalMagnitudeOfBBlockedness(Board board) {
        double magnitude = 0;
        for(Cell c : board.getHorizontal().getMyCells().values()) {
            for(int i = c.getPos().getX() + 1; i < board.getSize(); i++) {
                if(board.getBoard()[c.getPos().getY()][i].getValue() == Board.CELL_BLOCKED)
                    magnitude += b_blocked_score;
            }
        }
        return magnitude;
    }

    double verticalMagnitudeOfBBlockedness(Board board) {
        double magnitude = 0;
        for(Cell c : board.getVertical().getMyCells().values()) {
            for (int i = c.getPos().getY() + 1; i < board.getSize(); i++) {
                if (board.getBoard()[i][c.getPos().getX()].getValue() == Board.CELL_BLOCKED)
                    magnitude += b_blocked_score;
            }
        }
        return magnitude;
    }

    double horizontalVBlockedness(Board board) {
        double magnitude = 0;
        for(Cell c : board.getHorizontal().getMyCells().values()) {
            for (int i = c.getPos().getX() + 1; i < board.getSize(); i++) {
                if (board.getBoard()[c.getPos().getY()][i].getValue() == Board.CELL_VERTICAL)
                    magnitude += other_blocked_score;
            }
        }
        return magnitude;
    }

    double verticalHBlockedness(Board board) {
        double magnitude = 0;
        for(Cell c : board.getVertical().getMyCells().values()) {
            for (int i = c.getPos().getY() + 1; i < board.getSize(); i++) {
                if (board.getBoard()[i][c.getPos().getX()].getValue() == Board.CELL_HORIZONTAL)
                    magnitude += other_blocked_score;
            }
        }
        return magnitude;
    }
}
