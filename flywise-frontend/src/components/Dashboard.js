import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Dashboard() {
  const navigate = useNavigate();
  const [flights, setFlights] = useState([]);
  const [error, setError] = useState('');
  const [search, setSearch] = useState({
    origin: '',
    destination: '',
    date: ''
  });

  const handleLogout = async () => {
    const username = localStorage.getItem('username');

    try {
      await fetch(`http://localhost:8080/api/auth/logout?username=${username}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      });

      localStorage.clear();
      navigate('/login');
    } catch (e) {
      console.error('Logout failed:', e);
    }
  };

  const fetchFlights = async (origin, destination, date) => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      setError('No token found');
      return navigate('/login');
    }

    try {
      const url = `http://localhost:8080/api/flights/search?origin=${origin}&destination=${destination}&date=${date}`;

      const res = await fetch(url, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });

      if (!res.ok) {
        if (res.status === 403 || res.status === 401) {
          setError('Unauthorized. Please login again.');
          return navigate('/login');
        }
        throw new Error(`Server error: ${res.status}`);
      }

      const data = await res.json();
      setFlights(data);
      setError('');
    } catch (err) {
      console.error('Fetch failed:', err);
      setError('❌ Failed to fetch flights.');
    }
  };

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    fetchFlights(search.origin, search.destination, search.date);
  };

  return (
    <div>
      <h2>Dashboard</h2>
      <button onClick={handleLogout}>Logout</button>

      <hr />

      <h3>Search Flights</h3>
      <form onSubmit={handleSearchSubmit}>
        <input
          type="text"
          name="origin"
          placeholder="Origin"
          value={search.origin}
          onChange={(e) => setSearch({ ...search, origin: e.target.value })}
          required
        />
        <input
          type="text"
          name="destination"
          placeholder="Destination"
          value={search.destination}
          onChange={(e) => setSearch({ ...search, destination: e.target.value })}
          required
        />
        <input
          type="date"
          name="date"
          value={search.date}
          onChange={(e) => setSearch({ ...search, date: e.target.value })}
          required
        />
        <button type="submit">Search</button>
      </form>

      {error && <p style={{ color: 'red' }}>{error}</p>}

      <h3>Flight Results</h3>
      <ul>
        {flights.length > 0 ? (
          flights.map((flight, index) => (
            <li key={index}>
              ✈️ {flight.origin} → {flight.destination} on {flight.departureDate} – £{flight.price}
            </li>
          ))
        ) : (
          <p>No flights found.</p>
        )}
      </ul>
    </div>
  );
}

export default Dashboard;
