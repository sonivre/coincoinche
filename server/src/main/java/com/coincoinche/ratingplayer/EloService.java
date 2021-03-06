package com.coincoinche.ratingplayer;

import com.coincoinche.engine.teams.Team;
import com.coincoinche.repositories.UserRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Computation of Elo rating for players. */
@Service
public class EloService {

  @Autowired private UserRepository userRepository;
  @Autowired EloTeamFactory eloTeamFactory;

  public EloService() {
    super();
  }

  /**
   * Update ratings of users from the provided coinche teams.
   *
   * @param winnerTeam is the coinche team winning the game.
   * @param loserTeam is the coinche team losing the game.
   * @return a Map containing the new elo rating per username.
   */
  public Map<String, Integer> updateRatings(Team winnerTeam, Team loserTeam)
      throws UserNotFoundException {
    EloTeam winner = eloTeamFactory.createEloTeamFromCoincheTeam(winnerTeam);
    EloTeam loser = eloTeamFactory.createEloTeamFromCoincheTeam(loserTeam);
    return updateEloTeamRatings(winner, loser);
  }

  private Map<String, Integer> updateEloTeamRatings(EloTeam winner, EloTeam loser) {
    Map<String, Integer> newEloRatings = new HashMap<>();
    int ratingWinner = winner.getRating();
    int ratingLoser = loser.getRating();
    double quotientWinner = Math.pow(10, ratingWinner / 400);
    double quotientLoser = Math.pow(10, ratingLoser / 400);
    double expectedWinner = quotientWinner / (quotientWinner + quotientLoser);
    double expectedLoser = quotientLoser / (quotientWinner + quotientLoser);
    for (EloPlayer player : winner.getPlayers()) {
      player.setRating(
          (int)
              Math.round(player.getRating() + player.getRatingAdjustment() * (1 - expectedWinner)));
      userRepository.save(player.getUser());
      newEloRatings.put(player.getUser().getUsername(), player.getRating());
    }
    for (EloPlayer player : loser.getPlayers()) {
      player.setRating(
          (int)
              Math.round(player.getRating() + player.getRatingAdjustment() * (0 - expectedLoser)));
      userRepository.save(player.getUser());
      newEloRatings.put(player.getUser().getUsername(), player.getRating());
    }

    return newEloRatings;
  }
}
