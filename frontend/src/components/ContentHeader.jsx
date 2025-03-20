import React, { useEffect, useState } from 'react';
import s from '../styles/Content.module.css';

const ContentHeader = () => {
    const [username, setUsername] = useState('');

    useEffect(() => {
        const storedUsername = localStorage.getItem('username');
        if (storedUsername) {
            setUsername(storedUsername);
        }
    }, []);

  return (
        <div className={s["content--header"]}>
            <h1 className={s["header--title"]}>{username}</h1>
            <div className={s["header--activity"]}>
                <div className={s["search-box"]}>
                    <input type="text" placeholder='Search...'/>
                    <i className="fa fa-search"></i>
                </div>

                <div className={s["notify"]}>
                    <i className="fa fa-bell"></i>
                </div>
            </div>
        </div>
    );
}

export default ContentHeader;
