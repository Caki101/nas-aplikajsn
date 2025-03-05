import React from 'react'
import { Button } from './Button'
import './Footer.css'

function Footer() {
        return (
        <div className="footer-container">
            <section className="footer-subscription">
                <p className="footer-subscription-heading">
                    Budi u toku sa letovanjima
                </p>
                <p className="footer-subsription text">
                    Prikljuci se
                </p>
                <div className="input-areas">
                    <form>
                        <input type="email" name="email" placeholder='Unesi Email' className="footer-input" />
                        <Button buttonStyle={'btn--outline'}>Subscribe</Button>
                    </form>
                </div>
            </section>
        </div>
    )
}

export default Footer
