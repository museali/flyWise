import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Dashboard() {
  const navigate = useNavigate();
  const [flights, setFlights] = useState([]);
  const [error, setError] = useState('');

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

  const fetchFlights = async () => {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      setError('No token found');
      return navigate('/login');
    }

    try {
      const res = await fetch(`http://localhost:8080/api/flights/search?origin=Paris&destination=London&date=2024-06-15`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
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
      console.log('Fetched flights:', data);
      setFlights(data);
    } catch (err) {
      console.error('Error fetching flights:', err);
      setError('❌ Failed to fetch flights.');
    }
  };

  useEffect(() => {
    fetchFlights();
  }, []);

  return (
    <div>
      <h2>Dashboard</h2>
      <button onClick={handleLogout}>Logout</button>

      {error && <p style={{ color: 'red' }}>{error}</p>}

      <h3>Flights from Paris to London on 2024-06-15</h3>
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
