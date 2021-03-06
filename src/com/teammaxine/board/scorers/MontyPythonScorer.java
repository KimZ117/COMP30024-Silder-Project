/**
 * Created by Shreyash Patodia and Max Lee (Ho Suk Lee).
 * Student numbers: Shreyash - 767336, Max Lee - 719577
 * Login: Shreyash - spatodia, Max - hol2
 * Subject: COMP30024 Artificial Intelligence.
 * Semester 1, 2017.
 */

package com.teammaxine.board.scorers;

import aiproj.slider.Move;
import com.teammaxine.board.actions.AgentAction;
import com.teammaxine.board.elements.Board;

import com.teammaxine.strategies.MonteCarlo;

import java.util.ArrayList;

/** Scorer that utilizes Monte Carlo search
 */
public class MontyPythonScorer extends Scorer {
    private static MonteCarlo innerXena = null;
    private static final int DEPTH = 6;
    private static final int TRIES = 2000;

    public MonteCarlo getInnerXena(char player) {
        if(innerXena == null)
            innerXena = new MonteCarlo(player, new MonteCarloScorer());
        return innerXena;
    }

    public double scoreBoard(Board boardToConsider, char playerPiece) {
        char enemy = (playerPiece == 'H') ? 'V' : 'H';

        Board board = new Board(boardToConsider);
        MonteCarlo xena = getInnerXena(playerPiece);
        ArrayList<AgentAction> moves;

        if(enemy == 'V')
            moves = board.getVertical().getOptimisticMoves();
        else
            moves = board.getHorizontal().getOptimisticMoves();

        double totalScore = 0;
        for(Move move : moves) {
            double localScore = 0;

            for(int i = 0; i < TRIES; i++) {
                localScore += xena.randomTraverse(board, move, DEPTH, enemy, playerPiece);
            }

            totalScore += localScore;
        }

        return totalScore / TRIES;
    }
}
