import React, { useState } from 'react';
import Sidebar from '../Sidebar';
import Content from '../Content';
import Mainbar from '../Mainbar';
import PasswordReset from '../PasswordReset'
import YourTravels from '../YourTravels';
import Settings from '../Settings';
import '../../styles/Profile.css';

function Profile() {
  const [selectedComponent, setSelectedComponent] = useState('home');

  const handleComponentChange = (component) => {
    setSelectedComponent(component);
  };
  console.log(selectedComponent);
  return (
    <div className="profile-board">
      <Sidebar onComponentChange={handleComponentChange} />
      <div className="profile-board-content">
        <Content />
        {selectedComponent === 'profile-home' && <Mainbar />}
        {selectedComponent === 'profile-reset-password' && <PasswordReset />}
        {selectedComponent === 'profile-your-travels' && <YourTravels />}
        {selectedComponent === 'profile-settings' && <Settings />}
      </div>
    </div>
  );
}

export default Profile;
