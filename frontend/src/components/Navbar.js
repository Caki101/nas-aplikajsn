import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Button } from './Button';
import '../styles/Navbar.css';

function Navbar() {
    const [click, setClick] = useState(false);
    const [button, setButton] = useState(true);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [profileOpen, setProfileOpen] = useState(false);

    const handleClick = () => setClick(!click);
    const closeMobileMenu = () => setClick(false);

    const showButton = () => {
        if (window.innerWidth <= 960) {
            setButton(false);
        } else {
            setButton(true);
        }
    };

    useEffect(() => {
        showButton();
        const userToken = localStorage.getItem('authToken');
        if (userToken) {
            setIsLoggedIn(true);
        }

        const handleResize = () => showButton();

        window.addEventListener('resize', handleResize);

        const handleStorageChange = () => {
            const token = localStorage.getItem('authToken');
            setIsLoggedIn(!!token);
        };

        window.addEventListener('storage', handleStorageChange)

        return () => {
            window.removeEventListener('resize', handleResize);
            window.removeEventListener('storage', handleStorageChange);
        };
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('authToken');
        setIsLoggedIn(false);
        setProfileOpen(false);
    };

    const toggleProfileContainer = () => {
        setProfileOpen(!profileOpen);
    };

    return (
        <>
            <nav className="navbar">
                <div className="navbar-container">
                    <Link to="/" className="navbar-logo">
                        Traveling <i className="fas fa-bus" />
                    </Link>
                    <div className="menu-icon" onClick={handleClick}>
                        <i className={click ? 'fas fa-times' : 'fas fa-bars'} />
                    </div>
                    <ul className={click ? 'nav-menu active' : 'nav-menu'}>
                        <li className="nav-item">
                            <Link to="/" className="nav-links" onClick={closeMobileMenu}>
                                Home
                            </Link>
                        </li>
                        <li className="nav-item">
                            <Link to="/destination" className="nav-links" onClick={closeMobileMenu}>
                                Destination
                            </Link>
                        </li>
                        <li className="nav-item">
                            <Link to="/services" className="nav-links" onClick={closeMobileMenu}>
                                Services
                            </Link>
                        </li>
                        <li className="nav-item">
                            <Link to="/dataDisplay" className="nav-links" onClick={closeMobileMenu}>
                                Data Display
                            </Link>
                        </li>

                        {isLoggedIn && (
                            <li className="nav-item" onClick={closeMobileMenu}>
                                <div className="nav-links" onClick={toggleProfileContainer}>
                                    Profile <i className={'fa fa-user'} />
                                </div>
                                <div className={`profile-container ${profileOpen ? "open" : ""}`}>
                                    <Link to="/Profile" className="profile-item" onClick={()=> setProfileOpen(false)}>
                                        More
                                    </Link>
                                    <Link to="/" className="profile-item" onClick={handleLogout}>
                                        Logout
                                    </Link>
                                </div>
                            </li>
                        )}
                        {!isLoggedIn && (
                            <li className="nav-item">
                                <Link to="/sign-up" className="nav-links-mobile" onClick={closeMobileMenu}>
                                    Sign up
                                </Link>
                            </li>
                        )}
                    </ul>

                    {button && !isLoggedIn && <Button buttonStyle="btn--outline">SIGN UP</Button>}
                </div>
            </nav>
        </>
    );
}

export default Navbar;