import React, { useCallback, useContext } from 'react';
import Title from 'antd/lib/typography/Title';
import { Form, Input, Button, Checkbox } from 'antd';
import { useHistory } from 'react-router-dom';
import axios from '../configuration/axios';
import AuthContext from '../context/AuthContext';

import logo from '../resources/logo.png';
import './Home.css';
import Header from '../shared/Header';

function Home(): JSX.Element {
  const history = useHistory();
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
      <Header title="Kreator programów kształcenia PWR" />

      <Button
        className="home-button"
        type="primary"
        size="large"
        onClick={onEffects}
      >
        Efekty kształcenia
      </Button>
      <Button
        className="home-button"
        type="primary"
        size="large"
        onClick={onCards}
      >
        Karty przedmiotów
      </Button>
      <Button
        className="home-button"
        type="primary"
        size="large"
        onClick={onPrograms}
      >
        Programy studiów
      </Button>
      <Button
        className="home-button"
        type="primary"
        size="large"
        onClick={onPlans}
      >
        Plany studiów
      </Button>
    </div>
  );
}

export default Home;
