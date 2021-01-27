import React, { useCallback, useContext } from 'react';
import { Button } from 'antd';
import { useHistory } from 'react-router-dom';
import { FormattedMessage } from 'react-intl';
import Header from '../shared/Header';
import { LangContext } from '../context/LangContext';

import './Home.css';

function Home(): JSX.Element {
  const history = useHistory();
  const lang = useContext(LangContext);
  // const authContext = useContext(AuthContext);

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

  return (
    <div className="Home">
      <Header title={lang.messages['app.title']} />

      <Button
        className="home-button"
        type="primary"
        size="large"
        onClick={onEffects}
      >
        <FormattedMessage id="Studies effects" />
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
    </div>
  );
}

export default Home;
