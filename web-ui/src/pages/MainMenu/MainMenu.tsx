import React from 'react';
import styled from 'styled-components';
import {Link} from "react-router-dom";

import logo from '../../assets/coincoinche_logo.png';
import LoginComponentToolbar from '../../components/LoginToolbarComponent';
import Title from "../../components/misc/Title";

type Props = {
  username?: string | undefined,
}

type State = {};

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding-top: 30px;
`;

const Logo = styled.img`
  height: 240px;
`;

const Menu = styled.ul`
  display: flex;
  justify-content: center;
  width: 900px;
  margin: 0 auto;
  list-style: none;
  background: linear-gradient(90deg, rgba(255, 225, 54, 0) 0%, rgba(255, 225, 54, 0.2) 25%, rgba(255, 225, 54, 0.2) 75%, rgba(255, 225, 54, 0) 100%);
  box-shadow: 0 0 25px rgba(0, 0, 0, 0.1), inset 0 0 1px rgba(255, 225, 54, 0.6);
`;

const MenuItem = styled.li`
  width: 200px;
  padding: 18px;
  font-family: 'Cookie',cursive;
  text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;
  font-size: 36pt;
  color: #FFE136;
  &:hover {
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1), inset 0 0 1px rgba(255, 255, 54, 0.6);
    background: rgba(255, 255, 255, 0.1);
  }
`;

export default class MainMenu extends React.Component<Props, State> {
  render() {
    return <Container>
      <LoginComponentToolbar username={this.props.username} />
      <Logo src={logo} className="App-logo" alt="logo" />
      <Title fontSize={140}>Coincoinche</Title>
      <Menu>
        <Link to={{
          pathname: '/game',
          state: {
            username: this.props.username
          }
        }} style={{ textDecoration: 'none' }}>
          <MenuItem>Jeu classé</MenuItem>
        </Link>
        <Link to="/rules" style={{ textDecoration: 'none' }}>
          <MenuItem>Règles</MenuItem>
        </Link>
        <Link to="/ladder" style={{ textDecoration: 'none' }}>
          <MenuItem>Ladder</MenuItem>
        </Link>
      </Menu>
    </Container>;
  }
}
