import React, { useState } from 'react';
import LocationSearchInput from './LocationSearchInput';

function SearchForm({ onSearch }) {
  const [origin, setOrigin] = useState('');
  const [destination, setDestination] = useState('');
  const [date, setDate] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onSearch(origin, destination, date);
  };

  return (
    <form onSubmit={handleSubmit}>
      <LocationSearchInput label="Origin" onSelect={setOrigin} />
      <LocationSearchInput label="Destination" onSelect={setDestination} />
      <label>Departure Date:</label><br />
      <input type="date" value={date} onChange={(e) => setDate(e.target.value)} required /><br /><br />
      <button type="submit">Search Flights</button>
    </form>
  );
}

export default SearchForm;
