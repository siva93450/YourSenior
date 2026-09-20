import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';

function Login() {
  const [role, setRole] = useState('user'); // 'user' or 'mentor'
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const endpoint = role === 'user' ? '/users/login' : '/mentors/login';
      const res = await api.post(endpoint, { email, password });

      localStorage.setItem('token', res.data.token);
      localStorage.setItem('role', res.data.role);
      localStorage.setItem('name', res.data.name);
      localStorage.setItem('id', res.data.id);

      navigate('/browse'); // we'll build this page next
    } catch (err) {
      setError('Invalid email or password');
    }
  };

  return (
  <div className="auth-card">
    <h2>Login to Your Senior</h2>

    <div className="role-toggle">
      <button type="button" className={role === 'user' ? 'active' : ''} onClick={() => setRole('user')}>
        Student
      </button>
      <button type="button" className={role === 'mentor' ? 'active' : ''} onClick={() => setRole('mentor')}>
        Mentor
      </button>
    </div>

    <form onSubmit={handleLogin}>
      <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required />
      <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required />
      {error && <p className="error-text">{error}</p>}
      <button type="submit" className="btn-primary">Log in</button>
    </form>

    <p className="link-row">Don't have an account? <a href="/register">Register here</a></p>
  </div>
);
}

export default Login;