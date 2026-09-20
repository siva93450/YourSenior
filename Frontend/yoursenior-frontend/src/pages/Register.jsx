import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';

function Register() {
  const [role, setRole] = useState('user');
  const [form, setForm] = useState({
    name: '', email: '', password: '', college: '', course: ''
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const endpoint = role === 'user' ? '/users/register' : '/mentors/register';
      // User has no college/course; Mentor does
      const payload = role === 'user'
        ? { name: form.name, email: form.email, password: form.password }
        : form;

      await api.post(endpoint, payload);
      navigate('/login');
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed');
    }
  };

  return (
  <div className="auth-card">
    <h2>Register</h2>

    <div className="role-toggle">
      <button type="button" className={role === 'user' ? 'active' : ''} onClick={() => setRole('user')}>
        Student
      </button>
      <button type="button" className={role === 'mentor' ? 'active' : ''} onClick={() => setRole('mentor')}>
        Mentor
      </button>
    </div>

    <form onSubmit={handleRegister}>
      <input
        name="name" placeholder="Full Name" value={form.name}
        onChange={handleChange} required
      />
      <input
        name="email" type="email" placeholder="Email" value={form.email}
        onChange={handleChange} required
      />
      <input
        name="password" type="password" placeholder="Password" value={form.password}
        onChange={handleChange} required
      />

      {role === 'mentor' && (
        <>
          <input
            name="college" placeholder="College" value={form.college}
            onChange={handleChange} required
          />
          <input
            name="course" placeholder="Course / Department" value={form.course}
            onChange={handleChange} required
          />
        </>
      )}

      {error && <p className="error-text">{error}</p>}

      <button type="submit" className="btn-primary">Create account</button>
    </form>

    <p className="link-row">Already have an account? <a href="/login">Login here</a></p>
  </div>
);
}

export default Register;