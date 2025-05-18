import React, { useState, useEffect, useRef } from 'react';

function LocationSearchInput({ label, onSelect }) {
  const [query, setQuery] = useState('');
  const [suggestions, setSuggestions] = useState([]);
  const [showDropdown, setShowDropdown] = useState(false);
  const containerRef = useRef(null);

  useEffect(() => {
    const fetchLocations = async () => {
      if (query.length < 2) {
        setSuggestions([]);
        return;
      }

      try {
        const res = await fetch(`http://localhost:8080/api/amadeus/locations?query=${query}`);
        const data = await res.json();
        setSuggestions(data);
        setShowDropdown(true);
      } catch (err) {
        console.error('Error fetching locations:', err);
        setSuggestions([]);
      }
    };

    const delay = setTimeout(() => {
      fetchLocations();
    }, 300); // debounce

    return () => clearTimeout(delay);
  }, [query]);

    const handleSelect = (iataCode) => {
      setQuery(iataCode);
      onSelect(iataCode);
      setShowDropdown(false);
    };


  return (
    <div ref={containerRef} style={{ marginBottom: '2rem', position: 'relative' }}>
      <label>{label}</label><br />
      <input
        type="text"
        value={query}
        placeholder="Type city or country..."
        onChange={(e) => setQuery(e.target.value)}
        onFocus={() => query && setShowDropdown(true)}
        onBlur={() => setTimeout(() => setShowDropdown(false), 200)} // fixed blur issue
        style={{ width: '100%', padding: '0.5rem' }}
      />
      {showDropdown && suggestions.length > 0 && (
        <ul style={{
          position: 'absolute',
          zIndex: 10,
          background: 'white',
          border: '1px solid #ccc',
          width: '100%',
          listStyle: 'none',
          margin: 0,
          padding: 0,
          maxHeight: '200px',
          overflowY: 'auto',
          boxShadow: '0 4px 8px rgba(0,0,0,0.1)'
        }}>
          {suggestions.map((s, index) => (
            <li
              key={index}
              onClick={() => handleSelect(s.iataCode)}
              style={{
                padding: '0.5rem',
                cursor: 'pointer',
                borderBottom: '1px solid #eee'
              }}
            >
              ✈️ {s.cityName} – {s.airportName} ({s.iataCode}), {s.countryName}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default LocationSearchInput;
