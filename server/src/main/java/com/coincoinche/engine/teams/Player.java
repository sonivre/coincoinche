package com.coincoinche.engine.teams;

import com.coincoinche.engine.cards.Card;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/** Player represents a coinche player. */
public class Player {

  private List<Card> cards;
  private Team team;
  private String username;

  public Player(String username) {
    this.username = username;
    this.cards = new LinkedList<>();
  }

  /**
   * Add a card to the player's hand.
   *
   * @param card is the card to add.
   */
  public void addCard(Card card) {
    cards.add(card);
  }

  /** Sort cards in the player's hand according to the cards natural ordering. */
  public void sortCards() {
    cards.sort(null);
  }

  public List<Card> getCards() {
    return cards;
  }

  /**
   * Remove a card from the player's hand.
   *
   * @param card is the card to remove.
   */
  public void removeCard(Card card) {
    cards.remove(card);
  }

  /** Remove all cards in the player's hand. */
  public void clearCards() {
    cards.clear();
  }

  /**
   * Check that a player is a team mate of this player.
   *
   * <p>NB: We consider this player is his own team mate.
   *
   * @param player is another player.
   * @return true if players are team mates.
   */
  public boolean isTeamMate(Player player) {
    return team.containsPlayer(player);
  }

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  public String getUsername() {
    return username;
  }

  @Override
  public String toString() {
    return username;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Player)) {
      return false;
    }
    Player otherPlayer = (Player) obj;
    return this.username == otherPlayer.username;
  }

  @Override
  public int hashCode() {
    return Objects.hash(username);
  }
}
