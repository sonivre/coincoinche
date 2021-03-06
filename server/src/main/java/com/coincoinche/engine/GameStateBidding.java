package com.coincoinche.engine;

import com.coincoinche.engine.contracts.Contract;
import com.coincoinche.engine.contracts.ContractFactory;
import com.coincoinche.engine.serialization.json.GameStateBiddingSerializer;
import com.coincoinche.engine.teams.Player;
import com.coincoinche.engine.teams.Team;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.ArrayList;
import java.util.List;

/**
 * State of the game during the bidding phase. This is the phase of the game when players are
 * bidding to get the highest contract. The state is mainly represented with the following
 * attributes:
 *
 * <ul>
 *   <li>current player
 *   <li>last player who made a non-pass move
 *   <li>highest bidding so far
 *   <li>has someone coinched the highest bidding?
 *   <li>has someone surcoinched the highest bidding?
 *   <li>teams taking part in the game
 * </ul>
 */
@JsonSerialize(using = GameStateBiddingSerializer.class)
public class GameStateBidding implements GameStateTransition {

  private Player currentPlayer;
  // last player who made a non-pass move
  private Player lastPlayer;
  private Contract highestBidding;
  private boolean coinched;
  private boolean surcoinched;
  private List<Team> teams;

  protected GameStateBidding(
      Player currentPlayer,
      Player lastPlayer,
      Contract highestBidding,
      boolean coinched,
      boolean surcoinched) {
    this.currentPlayer = currentPlayer;
    this.lastPlayer = lastPlayer;
    this.highestBidding = highestBidding;
    this.coinched = coinched;
    this.surcoinched = surcoinched;
  }

  public static GameStateBidding initialGameStateBidding(Player firstPlayer) {
    return new GameStateBidding(firstPlayer, null, null, false, false);
  }

  @Override
  public List<Move> getLegalMoves() {
    List<Move> legalMoves = getUnsortedLegalMoves();
    legalMoves.sort(null);
    return legalMoves;
  }

  private List<Move> getUnsortedLegalMoves() {
    List<Move> legalMoves = new ArrayList<>();
    // Can always pass
    legalMoves.add(MoveBidding.passMove());
    // only surcoinche is legal if there is a coinche
    if (coinched) {
      if (currentPlayer.isTeamMate(highestBidding.getPlayer())) {
        legalMoves.add(MoveBidding.surcoincheMove());
      }
      return legalMoves;
    }
    // contracts strictly better than current contract are legal
    for (Contract contract : ContractFactory.createAllContracts()) {
      if (contract.isHigherThan(highestBidding)) {
        legalMoves.add(MoveBidding.contractMove(contract));
      }
    }
    // coinche is legal if an opponent has the highest bidding
    if (highestBidding != null && !currentPlayer.isTeamMate(highestBidding.getPlayer())) {
      legalMoves.add(MoveBidding.coincheMove());
    }
    return legalMoves;
  }

  /**
   * Return a boolean indicating if the game state must change, i.e. if the game gets to a state
   * other than the bidding state. This method makes sense if it's called after players have
   * rotated. to the game.
   */
  @Override
  public boolean mustChange() {
    return highestBidding != null && (surcoinched || currentPlayer.equals(lastPlayer));
  }

  protected void setCoinched(boolean coinched) {
    this.coinched = coinched;
  }

  protected void setSurcoinched(boolean surcoinched) {
    this.surcoinched = surcoinched;
  }

  protected void setHighestBidding(Contract highestBidding) {
    this.highestBidding = highestBidding;
  }

  protected void setTeams(List<Team> teams) {
    this.teams = teams;
  }

  public Contract getHighestBidding() {
    return highestBidding;
  }

  public boolean isCoinched() {
    return coinched;
  }

  public boolean isSurcoinched() {
    return surcoinched;
  }

  protected void setLastPlayer(Player lastPlayer) {
    this.lastPlayer = lastPlayer;
  }

  @Override
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  @Override
  public void rotatePlayers(CoincheGameRound round) {
    round.rotatePlayers();
    currentPlayer = round.getCurrentPlayer();
  }

  @Override
  public GameState createNextGameState(CoincheGameRound round) {
    // first player of playing phase is first player of bidding phase
    Player firstPlayer = round.getGlobalGame().getCurrentPlayer();
    GameStatePlaying nextState =
        GameStatePlaying.initialGameStatePlaying(firstPlayer, highestBidding);
    nextState.setTeams(teams);
    if (surcoinched) {
      nextState.setMultiplier(4);
    } else if (coinched) {
      nextState.setMultiplier(2);
    }
    return nextState;
  }
}
