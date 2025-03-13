import React from 'react';
import '../styles/Profile.css'

const Mainbar = () => {
  return (
    <div className="mainbar">
      <h2>Profile Information</h2>
      <div className="info-item">
        <label htmlFor="email">Email:</label>
        <input
          type="email"
          id="email"
          value="randomemail@example.com"
          disabled
          readOnly
        />
      </div>
      <div className="info-item">
        <label htmlFor="scheduledTravels">Scheduled Travels:</label>
        <input
          type="text"
          id="scheduledTravels"
          value="3"
          disabled
          readOnly
        />
      </div>
    </div>
  );
};

export default Mainbar;
