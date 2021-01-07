import React, { useCallback, useContext } from 'react';
import { Drawer, Button } from 'antd';
import { useHistory } from 'react-router-dom';
import { FormattedMessage } from 'react-intl';

import plIcon from '../resources/pl.png';
import enIcon from '../resources/en.png';
import { LangContext } from '../context/LangContext';
import AuthContext from '../context/AuthContext';

export default function AppDrawer({
  visible = false,
  setVisible = () => {},
}: {
  visible: boolean;
  setVisible: (v: boolean) => void;
}): JSX.Element {
  const langContext = useContext(LangContext);
  const auth = useContext(AuthContext);
  const history = useHistory();

  const onEffects = useCallback(() => {
    history.push('/effects');
  }, [history]);

  const onCards = useCallback(() => {
    history.push('/cards');
  }, [history]);

  const onPrograms = useCallback(() => {
    history.push('/programs');
  }, [history]);

  const onPlans = useCallback(() => {
    history.push('/plans');
  }, [history]);

  const onLogout = useCallback(() => {
    auth.token = null;
    history.push('/login');
  }, [auth, history]);

  return (
    <Drawer
      title="Menu"
      placement="left"
      closable={false}
      onClose={() => setVisible(false)}
      visible={visible}
    >
      <Button
        className="home-button"
        type="default"
        size="large"
        onClick={onLogout}
      >
        <FormattedMessage id="logout" />
      </Button>
      <Button className="flag" onClick={() => langContext.selectLanguage('pl')}>
        <img
          className="flag-icon"
          draggable={false}
          src={plIcon}
          alt="Polski"
        />
      </Button>
      <Button className="flag" onClick={() => langContext.selectLanguage('en')}>
        <img
          className="flag-icon"
          draggable={false}
          src={enIcon}
          alt="English"
        />
      </Button>
      {auth.token == null ? null : (
        <>
          <Button
            className="home-button"
            type="primary"
            size="large"
            onClick={onEffects}
          >
            <FormattedMessage id="efects" />
          </Button>
          <Button
            className="home-button"
            type="primary"
            size="large"
            onClick={onCards}
          >
            <FormattedMessage id="cards" />
          </Button>
          <Button
            className="home-button"
            type="primary"
            size="large"
            onClick={onPrograms}
          >
            <FormattedMessage id="programs" />
          </Button>
          <Button
            className="home-button"
            type="primary"
            size="large"
            onClick={onPlans}
          >
            <FormattedMessage id="plans" />
          </Button>
        </>
      )}
    </Drawer>
  );
}
