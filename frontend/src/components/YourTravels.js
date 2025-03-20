import React, { useState, useEffect } from 'react';
import s from '../styles/Profile.module.css';

const YourTravels = () => {
  const [travels, setTravels] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const travelsPerPage = 7;

  useEffect(() => {
    const fetchTravels = async () => {
      const username = localStorage.getItem("username");

      if (username) {
        const response = await fetch('http://26.10.184.197:8080/auth/user_travels', {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            username: username,
          }),
        });

        if (response.ok) {
          const data = await response.json();
          console.log(data)
          setTravels(data);
        } else {
          console.error('Failed to fetch', response.statusText)
        }
      } else {
        console.error('No username in localStorage!')
      }
    };

    fetchTravels();
  }, []);

  const indexOflastTravel = currentPage * travelsPerPage;
  const indexOfFirstTravel = indexOflastTravel - travelsPerPage;
  const currentTravels = travels.slice(indexOfFirstTravel, indexOflastTravel);

  const handleNextPage = () => {
    if (currentPage * travelsPerPage < travels.length) {
      setCurrentPage(currentPage + 1);
    }
  };

  const handlePreviousPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  }

  return (
    <div className={s["mainbar"]}>
      <h2>All your travels!</h2>
      {currentTravels.length > 0 ? (
        currentTravels.map((travel, index) => (
          <div key={index} className={s["travel-item"]}>
            <div className={s["travel-details"]}>
              <span className={s["travel-date"]}>{travel.date}</span>
              <span className={s["travel-place"]}>{travel.place}</span>
            </div>
            <hr className={s["travel-underline"]} />
          </div>
        ))
      ) : (
        <p>No scheduled travels available.</p>
      )}

      <div className={s["pagination-buttons"]}>
        {currentPage > 1 && (
          <button onClick={handlePreviousPage} className={s["page-buttons"]}>
            Previous page
          </button>
        )}
        {currentPage * travelsPerPage < travels.length && (
          <button onClick={handleNextPage} className={s["page-buttons"]}>
            Next page
          </button>
        )}
      </div>
    </div>
  );
};

export default YourTravels;
