import { useState, useEffect } from 'react';
import api from '../api/axios';
import Chat from '../components/Chat';

function MyBookings() {
  const [openChat, setOpenChat] = useState(null);
  const [bookings, setBookings] = useState([]);
  const [rateMsg, setRateMsg] = useState('');
  const userId = localStorage.getItem('id');

  const loadBookings = async () => {
    const res = await api.get(`/bookings/user/${userId}`);
    setBookings(res.data);
  };

  useEffect(() => {
    loadBookings();
  }, []);

  const rateBooking = async (bookingId, rating) => {
    setRateMsg('');
    try {
      await api.put(`/bookings/${bookingId}/rate`, null, { params: { rating } });
      setRateMsg('Thanks for rating!');
      loadBookings();
    } catch (err) {
      setRateMsg(err.response?.data?.message || 'Rating failed');
    }
  };

  const statusClass = (status) => {
    if (status === 'ACCEPTED') return 'status-available';
    if (status === 'REJECTED') return 'status-busy';
    return '';
  };

  return (
    <div className="browse-wrap">
      <h2>My Bookings</h2>

      {bookings.length === 0 && <p>No bookings yet — head to Browse to request a session.</p>}

      {bookings.map((b) => (
        <div key={b.id} className="mentor-card">
          <p><strong>{b.mentor?.name}</strong> — {b.mentor?.college}, {b.mentor?.course}</p>
          <p>{b.date} at {b.time}</p>
          <span className={`badge badge-${b.status.toLowerCase()}`}>{b.status}</span>

          {b.status === 'ACCEPTED' && !b.rated && new Date(`${b.date}T${b.time}`) < new Date() && (
            <div style={{ marginTop: 8 }}>
              <p>Rate this session:</p>
              {[1, 2, 3, 4, 5].map((star) => (
                <button
                  key={star}
                  className="star-btn"
                  style={{ marginRight: 6 }}
                  onClick={() => rateBooking(b.id, star)}
                >
                  {star} ⭐
                </button>
              ))}
            </div>
          )}
          {b.status === 'ACCEPTED' && (
  <>
    <button className="back-link" onClick={() => setOpenChat(openChat === b.id ? null : b.id)}>
      {openChat === b.id ? 'Close chat' : 'Message mentor'}
    </button>
    {openChat === b.id && <Chat bookingId={b.id} senderRole="USER" />}
  </>
)}
          {b.status === 'ACCEPTED' && !b.rated && new Date(`${b.date}T${b.time}`) >= new Date() && (
  <p style={{ color: 'var(--text-muted)', fontSize: 14 }}>You can rate this after your session</p>
)}

          {b.rated && <p style={{ color: 'var(--text-muted)' }}>You rated this session</p>}
        </div>
      ))}

      {rateMsg && <p style={{ marginTop: 10 }}>{rateMsg}</p>}
    </div>
  );
}

export default MyBookings;