import React from 'react';
import './App.css';
import Navbar from './components/Navbar';
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import Home from './components/pages/Home';
import Destination from './components/pages/Destination';
import DataDisplay from './components/pages/DataDisplay';
import GetData from './components/GettingData/GetData';

function App() {
  return (
    <>
    <Router>
      <Navbar />
      <Routes>
        <Route path='/' exact Component={Home} />
        <Route path='/dataDisplay' exact Component={GetData} />
      </Routes>
    </Router>
    </>
  );
}

export default App;
