import React, { useState } from 'react';
import SearchForm from './SearchForm';

function Dashboard() {
  const [flights, setFlights] = useState([]);

  const fetchFlights = async (origin, destination, date) => {
    const res = await fetch(`http://localhost:8080/api/amadeus/search?origin=${origin}&destination=${destination}&date=${date}`);
    const data = await res.json();
    setFlights(data);
  };

  return (
    <div>
      <h2>Search Flights</h2>
      <SearchForm onSearch={fetchFlights} />

      <h3>Results:</h3>
      <ul>
        {flights.map((flight, i) => (
          <li key={i}>
            {flight.origin} → {flight.destination} on {flight.departureDate} – £{flight.price}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Dashboard;