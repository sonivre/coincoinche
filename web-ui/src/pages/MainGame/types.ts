import { CardValue } from "../../assets/cards";

export type Player = {
  legalMoves: boolean[];
  authorisedContractValues: ContractValue[];
  authorisedSpecialBiddings: SpecialBidding[];
  authorisedContractSuits: Suit[];
  cardsInHand: CardValue[];
};

/**
 * START Authorised bidding formats returned by game engine
 */
export type AuthorisedSpecialBidding = {
  special: SpecialBidding
};
export type AuthorisedContractBidding = {
  value: ContractValue,
  suit: Suit
};
export type AuthorisedBidding = AuthorisedSpecialBidding | AuthorisedContractBidding;
export const isAuthorisedSpecialBidding =
  (authorisedBidding: AuthorisedBidding): authorisedBidding is AuthorisedSpecialBidding =>
    !!(authorisedBidding as AuthorisedSpecialBidding).special;
export const isAuthorisedContractBidding =
  (authorisedBidding: AuthorisedBidding): authorisedBidding is AuthorisedContractBidding =>
    !isAuthorisedSpecialBidding(authorisedBidding);
/**
 * END Authorised bidding formats returned by game engine
 */

export enum Position {
  top = 'top',
  right = 'right',
  bottom = 'bottom',
  left = 'left',
}

export enum GamePhase {
  bidding = 'bidding',
  main = 'main',
}

export type Players = {
  [Position.top]: Player;
  [Position.left]: Player;
  [Position.right]: Player;
  [Position.bottom]: Player;
};

export type Trick = {
  [Position.top]?: CardValue;
  [Position.left]?: CardValue;
  [Position.right]?: CardValue;
  [Position.bottom]?: CardValue;
};

export type GameState = {
  players: Players;
  currentPlayer: Position;
  currentTrick: Trick;
  currentPhase: GamePhase;
  contract: Contract | null;
};

export enum ContractValue {
  EIGHTY = '80',
  NINETY = '90',
  HUNDRED = '100',
  HUNDRED_TEN = '110',
  HUNDRED_TWENTY = '120',
  HUNDRED_THIRTY = '130',
  HUNDRED_FOURTY = '140',
  HUNDRED_FIFTY = '150',
  HUNDRED_SIXTY = '160',
  CAPOT = '250',
  GENERALE = '500',
}

export const getDisplayedValue = (value: ContractValue) => {
  if (value === ContractValue.CAPOT) {
    return 'CAPOT';
  }

  if (value === ContractValue.GENERALE) {
    return 'GENERALE';
  }

  return value;
};

export enum SpecialBidding {
  COINCHE = 'COINCHE',
  SURCOINCHE = 'SURCOINCHE',
  PASS = 'PASS',
}

export enum Suit {
  SPADES = 'spades',
  HEARTS = 'hearts',
  CLUBS = 'clubs',
  DIAMONDS = 'diamonds',
}

export const fromLetter = (letter: string): Suit => {
  if (letter === 's') {
    return Suit.SPADES;
  }

  if (letter === 'h') {
    return Suit.HEARTS;
  }

  if (letter === 'c') {
    return Suit.CLUBS;
  }

  if (letter === 'd') {
    return Suit.DIAMONDS;
  }
  throw new Error('Invalid suit letter');
};

export type Contract = {
  value: ContractValue;
  suit: Suit;
  specialBidding?: SpecialBidding;
}