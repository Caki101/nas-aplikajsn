import React from 'react'
import CardItem from './CardItem'
import './Cards.css'

function Cards() {
    return (
        <div className='cards'>
            <h1>Najbolje destinacije</h1>
            <div className="cards_container">
                <div className="cards_wrapper">
                    <ul className="cards_items">
                        <CardItem
                        src="images/img-1.jpg"
                        text="////"
                        label="Beach 1"
                        path='/dataDisplay'
                        />
                        <CardItem
                        src="images/img-4.jpg"
                        text="////"
                        label="Beach 2"
                        path='/dataDisplay'
                        />
                    </ul>
                </div>
            </div>  
        </div>
    )
}

export default Cards
