import React, { useState, useEffect } from 'react';
import '../styles/Profile.css';

const Mainbar = ({ onComponentChange }) => {
  const [scheduledTravels, setScheduledTravels] = useState(0);

  useEffect(() => {
    const fetchScheduledTravels = async () => {
      const username = localStorage.getItem("username");

      if (username) {
        const response = await fetch(`http://26.10.184.197:8080/auth/utc_${username}`);
        const data = await response.json();

        setScheduledTravels(Number(data));
      }
    };

    fetchScheduledTravels();
  }, []);

  return (
    <div className="mainbar">
      <h2>Profile Information</h2>
      <div className="info-item">
        <label htmlFor="email">Email:</label>
        <input type="email" id="email" value="randomemail@example.com" disabled readOnly />
      </div>
      <p className="scheduled-text">
        You have <strong>{scheduledTravels}</strong> scheduled travels. To check them out, click
        <span onClick={() => onComponentChange('profile-your-travels')} className="item"> here!</span>
      </p>
    </div>
  );
};

export default Mainbar;
