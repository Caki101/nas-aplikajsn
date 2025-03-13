import React from 'react';
import './App.css';
import Navbar from './components/Navbar';
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import Home from './components/pages/Home';
import Destination from './components/pages/Destination';
import DataDisplay from './components/pages/DataDisplay';
import Services from './components/pages/Services';
import SignUp from './components/pages/SignUp';
import Profile from './components/pages/Profile';

function App() {
  return (
    <>
    <Router>
      <Navbar />
      <Routes>
        <Route path='/' exact Component={Home} />
        <Route path='/destination' exact Component={Destination} />
        <Route path='/services' exact Component={Services} />
        <Route path='/sign-up' exact Component={SignUp} />
        <Route path='/dataDisplay' exact Component={DataDisplay} />
        <Route path='/profile' exact Component={Profile} />
      </Routes>
    </Router>
    </>
  );
}

export default App;
