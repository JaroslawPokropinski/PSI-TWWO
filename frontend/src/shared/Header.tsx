import React, { useState } from 'react';
import Title from 'antd/lib/typography/Title';
import { useHistory } from 'react-router-dom';
import { Button } from 'antd';
import AppDrawer from './Drawer';

import logo from '../resources/logo.png';
import menuIcon from '../resources/menu.png';
import './Header.less';

function Header(props: { title: string }): JSX.Element {
  const { title } = props;
  const history = useHistory();
  const [isDrawerOpen, setDrawerOpen] = useState(false);
  return (
    <>
      <Button className="burger" onClick={() => setDrawerOpen(true)}>
        <img className="burger-icon" src={menuIcon} alt="menu" />
      </Button>
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
      <AppDrawer visible={isDrawerOpen} setVisible={(v) => setDrawerOpen(v)} />
    </>
  );
}

export default Header;
