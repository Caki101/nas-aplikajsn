import React, { useState, useEffect } from 'react';
import s from '../styles/Profile.module.css';

const Mainbar = ({ onComponentChange }) => {
  const [scheduledTravels, setScheduledTravels] = useState(0);
  const [email, setEmail] = useState('');

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

  useEffect(() => {
      const storedEmail = localStorage.getItem('email');
      if (storedEmail) {
          setEmail(storedEmail);
      }
  }, []);

  return (
    <div className={s["mainbar"]}>
      <h2>Profile Information</h2>
      <div className={s["info-item"]}>
        <label htmlFor="email">Email:</label>
        <input type="email" id="email" value={email} disabled readOnly />
      </div>
      <p className={s["scheduled-text"]}>
        You have <strong>{scheduledTravels}</strong> scheduled travels. To check them out, click
        <span onClick={() => onComponentChange('profile-your-travels')} className={s["item"]}> here!</span>
      </p>
    </div>
  );
};

export default Mainbar;
