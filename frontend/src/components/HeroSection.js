import React from 'react';
import '../App.css';
import { Button } from './Button';
import './HeroSection.css';

function HeroSection() {
    return (
        <div className="hero-container">
            <video src="/videos/video-2.mp4" autoPlay loop /*muted*/ />
            <h1>Najjjludji travel bajo</h1>
            <p>Desingerica</p>
            <div className="hero-btns">
                <Button className="btns" buttonStyle='btn--outline' buttonSize='btn--large'>Pocetak</Button>
                <Button className="btns" buttonStyle='btn--primary' buttonSize='btn--large'>Vise podataka <i className="fas fa-poo"/></Button>
            </div>  
        </div>
    )
}

export default HeroSection