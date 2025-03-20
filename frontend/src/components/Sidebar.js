import React from 'react';
import s from '../styles/Profile.module.css';

const Sidebar = ({ onComponentChange }) => {
  return (
    <div className={s["side-bar-menu"]}>
      <div className={s["side-bar-list"]}>
        <div onClick={() => onComponentChange('profile-home')} className={s["item"]}>
          <i className="fa fa-house" /> Main
        </div>

        <div onClick={() => onComponentChange('profile-your-travels')} className={s["item"]}>
          <i className="fa fa-bus" /> Your Travels
        </div>

        <div onClick={() => onComponentChange('profile-reset-password')} className={s["item"]}>
          <i className="fa fa-key" /> Reset Password
        </div>

        <div onClick={() => onComponentChange('profile-settings')} className={s["item"]}>
          <i className="fa fa-gear" /> Settings
        </div>
      </div>
    </div>
  );
};

export default Sidebar;
