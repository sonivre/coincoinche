package com.coincoinche.engine.game;

import java.util.Map;
import java.util.Map.Entry;

/** GameResult represents the result of a game. */
public class GameResult<T> {
  private boolean finished;
  private boolean draw;
  // only defined when result is finished and is not a draw
  private Map<T, Integer> teamsPoints;

  private GameResult(boolean finished, boolean draw, Map<T, Integer> teamsPoints) {
    this.finished = finished;
    this.draw = draw;
    // this is defined only if the result is finished
    this.teamsPoints = teamsPoints;
  }

  public static <T> GameResult<T> unfinishedResult() {
    return new GameResult<T>(false, false, null);
  }

  public static <T> GameResult<T> finishedResult(Map<T, Integer> teamsPoints) {
    return new GameResult<T>(true, false, teamsPoints);
  }

  public static <T> GameResult<T> drawResult() {
    return new GameResult<T>(true, true, null);
  }

  public boolean isFinished() {
    return finished;
  }

  public boolean isDraw() {
    return draw;
  }

  public Map<T, Integer> getTeamsPoints() {
    return teamsPoints;
  }

  /**
   * Get the winner team of the game, i.e. the team with the most points.
   *
   * @return the team that won the game.
   */
  public T getWinnerTeam() {
    int maxPoints = Integer.MIN_VALUE;
    T winnerTeam = null;
    for (Entry<T, Integer> entry : teamsPoints.entrySet()) {
      if (entry.getValue() <= maxPoints) {
        continue;
      }
      maxPoints = entry.getValue();
      winnerTeam = entry.getKey();
    }
    return winnerTeam;
  }

  /**
   * Get the loser team of the game, i.e. the team with the least points.
   *
   * @return the team that lost the game.
   */
  public T getLoserTeam() {
    int minPoints = Integer.MAX_VALUE;
    T loserTeam = null;
    for (Entry<T, Integer> entry : teamsPoints.entrySet()) {
      if (entry.getValue() >= minPoints) {
        continue;
      }
      minPoints = entry.getValue();
      loserTeam = entry.getKey();
    }
    return loserTeam;
  }

  @Override
  public String toString() {
    StringBuffer prettyString = new StringBuffer();
    if (finished) {
      prettyString.append("Finished");
    } else {
      prettyString.append("Unfinished");
    }
    if (draw) {
      prettyString.append(" (draw)");
    }
    prettyString.append(": ");
    prettyString.append(teamsPoints);
    return prettyString.toString();
  }
}
