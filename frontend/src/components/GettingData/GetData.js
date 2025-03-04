import React, { useEffect, useState } from 'react';
import './GetData.css';


const GetData = () => {

    const [data, setData] = useState(null);

    useEffect(() => {
        fetch('http://26.10.184.197:8080/api/allT')
            .then(response => response.json())
            .then(data => {
                console.log('Fetchovana data:', data);
                setData(data);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }, []);

    return  <div className="data-container">
        {data ? (
            <div>
                <h2>Data:</h2>
                <pre>{JSON.stringify(data, null, 2)}</pre>
            </div>
        ) : (
            <p>Nema podataka</p>
        )}
    </div>;
};

export default GetData;