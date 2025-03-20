import { useEffect } from 'react';

function CardsData ({ setData }) {
    useEffect(() => {
        const cardsData = async () => {
            try {
                const response = await fetch('http://26.10.184.197:8080/api/best_offers', {
                    headers: {
                        "Api-Key-Header": "api_123"
                    }
                });
                const result = await response.json();
                setData(result);
            } catch (error) {
                console.log('error', error);
            }
        };

        cardsData();
    }, [setData]);

    return null;
};

export default CardsData