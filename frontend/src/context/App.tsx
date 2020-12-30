import React from 'react';
import {
  BrowserRouter as Router,
  Redirect,
  Route,
  Switch,
} from 'react-router-dom';
import Effects from '../effects/Effects';
import EffectsEditor from '../effects/EffectsEditor';
import Home from '../home/Home';
import Login from '../login/Login';
import ProgramsEditor from '../programs/ProgramsEditor';
import Programs from '../programs/Programs';

import './App.css';
import AuthContext from './AuthContext';
import Cards from '../cards/Cards';
import CardsEditor from '../cards/CardsEditor';
import PlanView from '../plans/PlanView';
import Plans from '../plans/Plans';

function App(): JSX.Element {
  return (
    <div className="App">
      <Router basename={process.env.PUBLIC_URL}>
        <AuthContext.Provider value={{ token: null }}>
          <Switch>
            <Route path="/plans/:state">
              <PlanView />
            </Route>
            <Route path="/plans">
              <Plans />
            </Route>
            <Route path="/cards/:state">
              <CardsEditor />
            </Route>
            <Route path="/cards">
              <Cards />
            </Route>
            <Route path="/programs/:state">
              <ProgramsEditor />
            </Route>
            <Route path="/programs">
              <Programs />
            </Route>
            <Route path="/effects/:state">
              <EffectsEditor />
            </Route>
            <Route path="/effects">
              <Effects />
            </Route>
            <Route path="/login">
              <Login />
            </Route>
            <Route path="/home">
              <Home />
            </Route>
            <Route path="/">
              <Redirect to="/login" />
            </Route>
          </Switch>
        </AuthContext.Provider>
      </Router>
    </div>
  );
}

export default App;
