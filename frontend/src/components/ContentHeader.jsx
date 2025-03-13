import React, { useEffect, useState } from 'react';

const ContentHeader = () => {
    const [username, setUsername] = useState('');

    useEffect(() => {
        const storedUsername = localStorage.getItem('username');
        if (storedUsername) {
            setUsername(storedUsername);
        }
    }, []);
  return (
        <div className='content--header'>
            <h1 className="header--title">{username}</h1>
            <div className="header--activity">
                <div className="search-box">
                    <input type="text" placeholder='Search...'/>
                    <i className="fa fa-search"></i>
                </div>

                <div className="notify">
                    <i className="fa fa-bell"></i>
                </div>
            </div>
        </div>
    )
}

export default ContentHeader