import React from 'react';
import styled from "styled-components";
import ValueSelector from "./ValueSelector";
import Container from "../utils/Container";
import SuitSelector from "./SuitSelector";
import {ContractValue, LegalBiddingMove, SpecialBidding, Suit} from "../../game-engine/gameStateTypes";
import {MoveType} from "../../websocket/events/types";

const getDisplayedValue = (value: ContractValue) => {
  if (value === ContractValue.CAPOT) {
    return 'CAPOT';
  }

  if (value === ContractValue.GENERALE) {
    return 'GENERALE';
  }

  return value;
};

const ValuesGroup = styled.div`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  width: 100%
`;

const SuitGroup = styled.div`
  display: flex;
  flex-direction: column;
`;

const Separator = styled.div`
  width: 100%;
  height: 2px;
  background-color: black;
  margin: 5px;
`;

type Props = {
  authorisedContractValues: ContractValue[];
  authorisedSpecialBiddings: SpecialBidding[];
  authorisedContractSuits: Suit[];
  lastContract: Partial<LegalBiddingMove>;
  onContractPicked: (contract: LegalBiddingMove) => void;
};

type State = {
  selectedValue: ContractValue | null;
  selectedSuit: Suit | null;
  selectedSpecialBidding: SpecialBidding | null;
}

export default class BiddingBoard extends React.Component<Props, State> {
  state = {
    selectedValue: null,
    selectedSuit: null,
    selectedSpecialBidding: null,
  };

  onValueClicked = (value: ContractValue) => {
    this.setState({
      selectedValue: value,
      selectedSpecialBidding: null,
    });
  };

  onSuitClicked = (suit: Suit) => {
    this.setState({
      selectedSuit: suit,
      selectedSpecialBidding: null,
    });
  };

  onSpecialBiddingClicked = (bidding: SpecialBidding) => {
    this.setState({
      selectedValue: null,
      selectedSuit: null,
      selectedSpecialBidding : bidding
    });
  };


  componentDidUpdate(prevProps: Readonly<Props>, prevState: Readonly<State>) {
    const { selectedSuit, selectedValue, selectedSpecialBidding } = this.state;
    const {
      selectedSuit: prevSelectedSuit,
      selectedValue: prevSelectedValue,
      selectedSpecialBidding: prevSelectedSpecialBidding
    } = prevState;

    if (prevSelectedSpecialBidding === null && selectedSpecialBidding !== null) {
      this.props.onContractPicked({
        moveType: MoveType.SPECIAL_BIDDING,
        bidding: selectedSpecialBidding!,
      });
      this.setState({
        selectedValue: null,
        selectedSuit: null,
        selectedSpecialBidding : null,
      });
    }

    if ((prevSelectedSuit === null || prevSelectedValue === null) && (selectedSuit !== null && selectedValue !== null)) {
      this.props.onContractPicked({
        moveType: MoveType.CONTRACT_BIDDING,
        value: selectedValue!,
        suit: selectedSuit!,
      });
      this.setState({
        selectedValue: null,
        selectedSuit: null,
        selectedSpecialBidding : null,
      });
    }
  }

  render() {
    const { authorisedContractValues, authorisedSpecialBiddings, authorisedContractSuits, lastContract } = this.props;
    const contractValues = Object.values(ContractValue);
    const contractSuits = Object.values(Suit);
    const specialBiddings = Object.values(SpecialBidding);
    let lastContractValue: string | undefined;
    let lastContractSuit: string | undefined;
    let lastContractSpecial: string | undefined;
    if (lastContract) {
      if (lastContract.moveType === MoveType.SPECIAL_BIDDING) {
        lastContractSpecial = lastContract.bidding;
      }
      if (lastContract.moveType === MoveType.CONTRACT_BIDDING) {
        lastContractValue = lastContract.value;
        lastContractSuit = lastContract.suit
      }
    }

    return (
      <Container
          direction="column"
          backgroundColor="darkgreen"
          borderRadius="50px"
          minWidth="300px"
          padding="20px"
      >
        <Container
            direction="row"
        >
          <ValuesGroup>
            {
              contractValues
                .map(value => {
                  const disabled = !authorisedContractValues.includes(value);
                  const onClick = disabled ? () => {} : () => this.onValueClicked(value);
                  const minWidth = [ContractValue.CAPOT, ContractValue.GENERALE].includes(value) ? '85px' : undefined;

                  return (
                    <ValueSelector
                      minWidth={minWidth}
                      key={value}
                      selectedByOpponent={lastContractValue === value}
                      disabled={disabled}
                      onClick={onClick}
                      selectedByPlayer={this.state.selectedValue === value}
                    >
                      {getDisplayedValue(value)}
                    </ValueSelector>
                  )
                })
            }
          </ValuesGroup>
          <SuitGroup>
            {
              contractSuits.map(suit => {
                const disabled = !authorisedContractSuits.includes(suit);
                const onClick = disabled ? () => {} : () => this.onSuitClicked(suit);

                return (
                  <SuitSelector
                    src={require(`../../assets/suits/${suit}.png`)}
                    key={suit}
                    selectedByOpponent={lastContractSuit === suit}
                    onClick={onClick}
                    selectedByPlayer={this.state.selectedSuit === suit}
                    disabled={disabled}
                  />
                )
              })
            }
          </SuitGroup>
        </Container>
        <Separator />
        <Container direction="row">
          {
            specialBiddings
              .map(bidding => {
                const selectedByOpponent = bidding === lastContractSpecial;
                const disabled = !authorisedSpecialBiddings.includes(bidding);
                const onClick = disabled ? () => {} : () => this.onSpecialBiddingClicked(bidding);

                return (
                  <ValueSelector
                    minWidth="90px"
                    key={bidding}
                    selectedByOpponent={selectedByOpponent}
                    disabled={disabled}
                    onClick={onClick}
                    selectedByPlayer={this.state.selectedSpecialBidding === bidding}
                  >
                    {bidding}
                  </ValueSelector>
                )
              })
          }
        </Container>
      </Container>
    )
  }
}
