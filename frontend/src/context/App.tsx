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

import './App.css';
import AuthContext from './AuthContext';

function App(): JSX.Element {
  return (
    <div className="App">
      <Router basename={process.env.PUBLIC_URL}>
        <AuthContext.Provider value={{ token: null }}>
          <Switch>
            <Route path="/effects/edit">
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
