import React, { useState } from 'react'
import s from '../styles/Destination.module.css'

function DestinationSection() {
    const [showPlaces, setShowPlaces] = useState(false)
    const [showCities, setShowCities] = useState(false)
    
  return (
    <div className={s["destination-container"]}>
        <div className={s["travel-container"]}>
            <div className={s["dropdown"]}>
                <button className={s["travel-btn"]} onClick={() => setShowPlaces(!showPlaces)}>
                    Mesto
                </button>
                {showPlaces && (
                    <ul className={s["dropdown-menu"]}>
                        <li>Grcka</li>
                        <li>Italia</li>
                        <li>Portugal</li>
                    </ul>
                )}
            </div>
            <div className={s["dropdown"]}>
                <button className={s["travel-btn"]} onClick={() => setShowCities(!showCities)}>
                    Grad
                </button>
                {showCities && (
                    <ul className={s["dropdown-menu"]}>
                        <li>Kyi</li>
                        <li>z</li>
                        <li>xx</li>
                    </ul>
                )}
            </div>

            <button className={s["travel-btn"]}>Datum</button>
            <div className={s["search-box"]}>
                <input type="text" placeholder='Search...' />
                <i className={s["fa fa-search"]}></i>
            </div>
        </div>
    </div>
  )
}

export default DestinationSection