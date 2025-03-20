import React, { useState } from 'react'
import CardItem from './CardItem'
import CardsData from './GettingData/CardsData'
import '../styles/Cards.css'

function Cards() {
    const [data, setData] = useState([]);

    return (
        <div className='cards'>
            <CardsData setData={setData} />
            <h1>Najbolje destinacije</h1>
            <div className="cards_container">
                <div className="cards_wrapper">
                    <ul className="cards_items">
                        {data.slice(0,2).map((item, index) => (
                            <CardItem
                                key={index}
                                src={item.img_link}
                                text={item.description}
                                label={item.label}
                                path={'/destination/'+item.id}
                            />
                        ))}
                    </ul>
                    <ul className="cards_items">
                        {data.slice(2,5).map((item, index) => (
                            <CardItem
                                key={index+2}
                                src={item.img_link}
                                text={item.description}
                                label={item.label}
                                path={'/destination/'+item.id}
                            />
                        ))}
                    </ul>
                </div>
            </div>  
        </div>
    )
}

export default Cards
