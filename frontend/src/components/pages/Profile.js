import React, { useState } from 'react';
import Sidebar from '../Sidebar';
import Content from '../Content';
import Mainbar from '../Mainbar';
import PasswordReset from '../PasswordReset';
import YourTravels from '../YourTravels';
import Settings from '../Settings';
import s from '../../styles/Profile.module.css';

function Profile() {
  const [selectedComponent, setSelectedComponent] = useState('profile-home');

  const handleComponentChange = (component) => {
    setSelectedComponent(component);
  };

  console.log(selectedComponent);
  return (
    <div className={s["profile-board"]}>
      <Sidebar onComponentChange={handleComponentChange} />
      <div className={s["profile-board-content"]}>
        <Content />
        {selectedComponent === 'profile-home' && <Mainbar onComponentChange={handleComponentChange} />}
        {selectedComponent === 'profile-reset-password' && <PasswordReset />}
        {selectedComponent === 'profile-your-travels' && <YourTravels />}
        {selectedComponent === 'profile-settings' && <Settings />}
      </div>
    </div>
  );
}

export default Profile;
