import { useState, useEffect } from 'react';
import api from '../api/axios';
import Chat from '../components/Chat';

function MentorDashboard() {
  const [bookings, setBookings] = useState([]);
  const [available, setAvailable] = useState(true);
  const [openChat, setOpenChat] = useState(null);
  const mentorId = localStorage.getItem('id');
  const mentorName = localStorage.getItem('name');

  const loadBookings = async () => {
    const res = await api.get(`/bookings/mentor/${mentorId}`);
    setBookings(res.data);
  };

  useEffect(() => {
    loadBookings();
  }, []);

  const updateStatus = async (bookingId, status) => {
    await api.put(`/bookings/${bookingId}/status`, null, { params: { status } });
    loadBookings();
  };

  const toggleAvailability = async () => {
    const newValue = !available;
    await api.put(`/mentors/${mentorId}/availability`, null, { params: { available: newValue } });
    setAvailable(newValue);
  };

  return (
    <div className="browse-wrap">
      <h2>Welcome, {mentorName}</h2>

      <button className="btn-primary" style={{ marginBottom: 20 }} onClick={toggleAvailability}>
        {available ? 'Set as Busy' : 'Set as Available'}
      </button>

      <h3>Booking Requests</h3>
      {bookings.length === 0 && <p>No requests yet.</p>}

      {bookings.map((b) => (
        <div key={b.id} className="mentor-card">
          <p><strong>{b.user?.name}</strong> requested {b.date} at {b.time}</p>
          <span className={`badge badge-${b.status.toLowerCase()}`}>{b.status}</span>
          {b.status === 'ACCEPTED' && (
  <>
    <button className="back-link" onClick={() => setOpenChat(openChat === b.id ? null : b.id)}>
      {openChat === b.id ? 'Close chat' : 'Message student'}
    </button>
    {openChat === b.id && <Chat bookingId={b.id} senderRole="MENTOR" />}
  </>
)}
          {b.status === 'PENDING' && (
            <div style={{ marginTop: 8 }}>
              <button className="btn-primary" onClick={() => updateStatus(b.id, 'ACCEPTED')}>
                Accept
              </button>
              <button className="back-link" style={{ marginLeft: 12 }} onClick={() => updateStatus(b.id, 'REJECTED')}>
                Reject
              </button>
            </div>
          )}
        </div>
      ))}
    </div>
  );
}

export default MentorDashboard;