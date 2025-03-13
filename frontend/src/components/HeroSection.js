import React from 'react';
import '../App.css';
import { Button } from './Button';
import '../styles/HeroSection.css';

function HeroSection() {
    return (
        <div className="hero-container">
            <video src="/videos/video-8.mp4" autoPlay loop onLoadedMetadata={(e) => e.target.volume = 0.1} muted />
            <h1>Start Today!</h1>
            <p>Whole world, just one click away</p>
            <div className="hero-btns">
                <Button className="btns" buttonStyle='btn--outline' buttonSize='btn--large'>Pocetak</Button>
                <Button className="btns" buttonStyle='btn--outline' buttonSize='btn--large'>Vise podataka</Button>
                <Button className="btns" buttonStyle='btn--outline' buttonSize='btn--large'>Najpovoljnije</Button>
                <Button className="btns" buttonStyle='btn--primary' buttonSize='btn--large'>Reklama</Button>
            </div>  
        </div>
    )
}

export default HeroSection