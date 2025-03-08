import React from 'react'
import { Button } from './Button'
import './Footer.css'
import { Link } from 'react-router-dom'

function Footer() {
        return (
        <div className="footer-container">
            <section className="footer-subscription">
                <p className="footer-subscription-heading">
                    Budi u toku sa letovanjima
                </p>
                <p className="footer-subscription text">
                    Prikljuci se
                </p>
                <div className="input-areas">
                    <form>
                        <input type="email" name="email" placeholder='Unesi Email' className="footer-input" />
                        <Button buttonStyle={'btn--outline'}>Email</Button>
                    </form>
                </div>
            </section>
            <div className="footer-links">
                <div className="footer-link-wrapper">
                    <div className="footer-link-items">
                        <h2>About us</h2>
                        <Link to='/sign-up'>Test 1</Link>
                        <Link to='/'>Test 2</Link>
                        <Link to='/'>Test 3</Link>
                        <Link to='/'>Test 4</Link>
                        <Link to='/'>Test 5</Link>
                    </div>
                    <div className="footer-link-items">
                        <h2>Contact</h2>
                        <Link to='/sign-up'>Test 1</Link>
                        <Link to='/'>Test 2</Link>
                        <Link to='/'>Test 3</Link>
                        <Link to='/'>Test 4</Link>
                        <Link to='/'>Test 5</Link>
                    </div>
                </div>
                <div className="footer-link-wrapper">
                    <div className="footer-link-items">
                        <h2>idk 1</h2>
                        <Link to='/sign-up'>Test 1</Link>
                        <Link to='/'>Test 2</Link>
                        <Link to='/'>Test 3</Link>
                        <Link to='/'>Test 4</Link>
                        <Link to='/'>Test 5</Link>
                    </div>
                    <div className="footer-link-items">
                        <h2>idk 2</h2>
                        <Link to='/sign-up'>Test 1</Link>
                        <Link to='/'>Test 2</Link>
                        <Link to='/'>Test 3</Link>
                        <Link to='/'>Test 4</Link>
                        <Link to='/'>Test 5</Link>
                    </div>
                </div>
            </div>
            <section className="social-media">
                <div className="social-media-wrap">
                    <div className="footer-logo">
                        <Link to='/' className="social-logo">
                            Traveling <i className="fas fa-bus" />
                        </Link>
                    </div>
                    <small className="website-rights">Traveling Agency</small>
                    <div className="social-icons">
                        <Link className="social-icon-link instagram"
                        to="/"
                        target="_blank"
                        aria-label="Instagram"
                        >
                            <i className="fab fa-instagram"></i>
                        </Link>
                        <Link className="social-icon-link x"
                        to="/"
                        target="_blank"
                        aria-label="X"
                        >
                            <i className="fab fa-x-twitter"></i>
                        </Link>
                        <Link className="social-icon-link linkedin"
                        to="/"
                        target="_blank"
                        aria-label="LinkedIn"
                        >
                            <i className="fab fa-linkedin"></i>
                        </Link>
                    </div>
                </div>
            </section>
        </div>
    )
}

export default Footer
