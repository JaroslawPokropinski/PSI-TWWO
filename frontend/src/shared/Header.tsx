import React from 'react';
import Title from 'antd/lib/typography/Title';
import { useHistory } from 'react-router-dom';

import logo from '../resources/logo.png';
import './Header.css';

function Header(props: { title: string }): JSX.Element {
  const { title } = props;
  const history = useHistory();
  return (
    <header
      className="pwr-header"
      onClick={() => history.push('/home')}
      onKeyDown={() => history.push('/home')}
      role="button"
      tabIndex={0}
    >
      <img className="head-logo" src={logo} alt="Logo PWR" />
      <Title className="head-text">{title}</Title>
    </header>
  );
}

export default Header;
