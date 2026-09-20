import { useState, useEffect } from 'react';
import api from '../api/axios';

function Browse() {
    const [bookingMentorId, setBookingMentorId] = useState(null);
const [date, setDate] = useState('');
const [time, setTime] = useState('');
const [bookingMsg, setBookingMsg] = useState('');

const submitBooking = async (mentorId) => {
  setBookingMsg('');
  const userId = localStorage.getItem('id');
  const role = localStorage.getItem('role');

  if (!localStorage.getItem('token') || role !== 'USER') {
    setBookingMsg('Please log in as a student to book.');
    return;
  }

  try {
    await api.post('/bookings/create', { date, time }, {
      params: { userId, mentorId }
    });
    setBookingMsg('Booking requested! Check status under My Bookings.');
    setBookingMentorId(null);
    setDate('');
    setTime('');
  } catch (err) {
    setBookingMsg(err.response?.data?.message || 'Booking failed — mentor may be unavailable.');
  }
};
  const [colleges, setColleges] = useState([]);
  const [courses, setCourses] = useState([]);
  const [mentors, setMentors] = useState([]);
  const [selectedCollege, setSelectedCollege] = useState(null);
  const [selectedCourse, setSelectedCourse] = useState(null);

  useEffect(() => {
    api.get('/mentors/colleges').then(res => setColleges(res.data));
  }, []);

  const pickCollege = async (college) => {
    setSelectedCollege(college);
    setSelectedCourse(null);
    setMentors([]);
    const res = await api.get(`/mentors/colleges/${college}/courses`);
    setCourses(res.data);
  };

  const pickCourse = async (course) => {
    setSelectedCourse(course);
    const res = await api.get('/mentors/search', {
      params: { college: selectedCollege, course }
    });
    setMentors(res.data);
  };

 return (
  <div className="browse-wrap">
    <h2>Find a Mentor</h2>

    {!selectedCollege && (
      <div className="pick-list">
        <h3>Select a college</h3>
        {colleges.map((c) => (
          <button key={c} onClick={() => pickCollege(c)}>{c}</button>
        ))}
      </div>
    )}

    {selectedCollege && !selectedCourse && (
      <div>
        <button className="back-link" onClick={() => setSelectedCollege(null)}>&larr; Back</button>
        <h3>{selectedCollege} — select a department</h3>
        <div className="pick-list">
          {courses.map((c) => (
            <button key={c} onClick={() => pickCourse(c)}>{c}</button>
          ))}
        </div>
      </div>
    )}

    {selectedCourse && (
      <div>
        <button className="back-link" onClick={() => setSelectedCourse(null)}>&larr; Back</button>
        <h3>{selectedCollege} — {selectedCourse}</h3>
        {mentors.length === 0 && <p>No mentors found yet.</p>}
        {mentors.map((m) => (
  <div key={m.id} className="mentor-card">
    <h3>{m.name}</h3>
    <p>{m.bio || 'No bio yet'}</p>
    <p>⭐ {m.averageRating.toFixed(1)} ({m.ratingCount} ratings)</p>
    <p className={m.available ? 'status-available' : 'status-busy'}>
      {m.available ? 'Available' : 'Busy'}
    </p>

    {m.available && (
      bookingMentorId === m.id ? (
        <div style={{ marginTop: 10 }}>
          <input type="date" value={date} onChange={(e) => setDate(e.target.value)} />
          <input type="time" value={time} onChange={(e) => setTime(e.target.value)} />
          <button className="btn-primary" onClick={() => submitBooking(m.id)}>
            Confirm booking
          </button>
          <button className="back-link" onClick={() => setBookingMentorId(null)}>Cancel</button>
        </div>
      ) : (
        <button className="btn-primary" style={{ marginTop: 8 }} onClick={() => setBookingMentorId(m.id)}>
          Request session
        </button>
      )
    )}
  </div>
))}

{bookingMsg && <p style={{ marginTop: 10 }}>{bookingMsg}</p>}
      </div>
    )}
  </div>
);

}

export default Browse;