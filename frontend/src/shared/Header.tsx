import React from 'react';
import Title from 'antd/lib/typography/Title';

import logo from '../resources/logo.png';
import './Header.css';

function Header(props: { title: string }): JSX.Element {
  const { title } = props;
  return (
    <header className="pwr-header">
      <img className="head-logo" src={logo} alt="Logo PWR" />
      <Title className="head-text">{title}</Title>
    </header>
  );
}

export default Header;
