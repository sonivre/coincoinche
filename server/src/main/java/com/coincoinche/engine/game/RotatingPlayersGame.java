package com.coincoinche.engine.game;

import java.util.List;

/** Implement a game with rotating players. */
public class RotatingPlayersGame<P> {

  private List<P> players;
  private int currentPlayerIndex = 0;

  public void setPlayers(List<P> players) {
    this.players = players;
  }

  /**
   * Get the player who should play currently.
   *
   * @return currrent player.
   */
  protected P getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  /** Rotate players turn. */
  protected void rotatePlayers() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
  }

  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }

  public void setCurrentPlayerIndex(int currentPlayerIndex) {
    this.currentPlayerIndex = currentPlayerIndex;
  }
}