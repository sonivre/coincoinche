package com.coincoinche.engine;

import com.coincoinche.engine.contracts.Contract;
import com.coincoinche.engine.serialization.json.MoveBiddingDeserializer;
import com.coincoinche.engine.serialization.json.MoveBiddingSerializer;
import com.coincoinche.engine.teams.Player;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;

/**
 * MoveBidding represents a move during the bidding phase of the game. It can be:
 *
 * <ul>
 *   <li>pass
 *   <li>coinche
 *   <li>surcoinche
 *   <li>a contract higher than the previous one
 * </ul>
 */
@JsonSerialize(using = MoveBiddingSerializer.class)
@JsonDeserialize(using = MoveBiddingDeserializer.class)
public class MoveBidding extends Move implements Comparable<MoveBidding> {

  private enum Special {
    PASS("pass"),
    COINCHE("coinche"),
    SURCOINCHE("surcoinche");

    private final String shortName;

    private Special(String shortName) {
      this.shortName = shortName;
    }
  }

  private Contract contract;
  private Special specialMove;

  private MoveBidding(Contract contract, Special specialMove) {
    this.contract = contract;
    this.specialMove = specialMove;
  }

  /**
   * Create a bidding move corresponding to a contract.
   *
   * @param contract represents the contract claimed during the move.
   * @return the newly constructed bidding move.
   */
  public static MoveBidding contractMove(Contract contract) {
    return new MoveBidding(contract, null);
  }

  /**
   * Create a bidding move corresponding to a player passing.
   *
   * @return the newly constructed bidding move.
   */
  public static MoveBidding passMove() {
    return new MoveBidding(null, Special.PASS);
  }

  /**
   * Create a bidding move corresponding to a coinche.
   *
   * @return the newly constructed bidding move.
   */
  public static MoveBidding coincheMove() {
    return new MoveBidding(null, Special.COINCHE);
  }

  /**
   * Create a bidding move corresponding to a surcoinche.
   *
   * @return the newly constructed bidding move.
   */
  public static MoveBidding surcoincheMove() {
    return new MoveBidding(null, Special.SURCOINCHE);
  }

  @Override
  protected void applyOnRoundState(GameState state, Player player) throws IllegalMoveException {
    if (!(state instanceof GameStateBidding)) {
      throw new IllegalMoveException(this + " must be applied to a bidding state");
    }
    GameStateBidding biddingGameState = (GameStateBidding) state;
    List<Move> legalMoves = biddingGameState.getLegalMoves();
    if (!legalMoves.contains(this)) {
      throw new IllegalMoveException(this + " is not legal on state " + biddingGameState);
    }
    if (specialMove != null) {
      switch (specialMove) {
        case PASS:
          break;
        case COINCHE:
          biddingGameState.setCoinched(true);
          biddingGameState.setLastPlayer(player);
          break;
        case SURCOINCHE:
          biddingGameState.setSurcoinched(true);
          biddingGameState.setLastPlayer(player);
          break;
        default:
          break;
      }
      return;
    }
    biddingGameState.setHighestBidding(contract.withPlayer(biddingGameState.getCurrentPlayer()));
    biddingGameState.setLastPlayer(player);
  }

  @Override
  public int compareTo(MoveBidding o) {
    // Special moves: PASS < COINCHE < SURCOINCHE
    if (this.specialMove != null && o.specialMove != null) {
      return this.specialMove.compareTo(o.specialMove);
    }
    if (this.specialMove != null) {
      return -1;
    }
    if (o.specialMove != null) {
      return 1;
    }
    // Both moves are contract moves: compare their value
    if (o.contract.isHigherThan(this.contract)) {
      return -1;
    }
    if (this.contract.isHigherThan(o.contract)) {
      return 1;
    }
    // Contracts have same value: compare their color
    return this.contract.getSuit().compareTo(o.contract.getSuit());
  }

  public Contract getContract() {
    return contract;
  }

  public boolean isPass() {
    return specialMove != null && specialMove.equals(Special.PASS);
  }

  public boolean isCoinche() {
    return specialMove != null && specialMove.equals(Special.COINCHE);
  }

  public boolean isSurcoinche() {
    return specialMove != null && specialMove.equals(Special.SURCOINCHE);
  }

  /**
   * Attach a player to the move's contract.
   *
   * @param player is the player to attach.
   */
  protected void attachPlayer(Player player) {
    this.contract.setPlayer(player);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof MoveBidding)) {
      return false;
    }
    MoveBidding otherMove = (MoveBidding) obj;
    if (this.specialMove != null || otherMove.specialMove != null) {
      return this.specialMove == otherMove.specialMove;
    }
    return this.contract.equals(otherMove.contract);
  }

  @Override
  public String getShortName() {
    if (specialMove != null) {
      return specialMove.shortName;
    }
    return contract.getShortName();
  }

  @Override
  public String toString() {
    if (specialMove != null) {
      return specialMove.toString();
    }
    return contract.toString();
  }
}
