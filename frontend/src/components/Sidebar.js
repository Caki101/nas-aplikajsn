import React from 'react';
import "../styles/Profile.css";

const Sidebar = ({ onComponentChange }) => {
  return (
    <div className="side-bar-menu">
      <div className="side-bar-list">
        <div onClick={() => onComponentChange('profile-home')} className="item">
          <i className="fa fa-house" /> Main
        </div>
        
        <div onClick={() => onComponentChange('profile-your-travels')} className="item">
          <i className="fa fa-bus" /> Your Travels
        </div>

        <div onClick={() => onComponentChange('profile-reset-password')} className="item">
          <i className="fa fa-key" /> Reset Password
        </div>

        <div onClick={() => onComponentChange('profile-settings')} className="item">
          <i className="fa fa-gear" /> Settings
        </div>
      </div>
    </div>
  );
};

export default Sidebar;
