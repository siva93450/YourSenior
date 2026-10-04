import { useNavigate } from 'react-router-dom';

function Navbar() {
  const navigate = useNavigate();
  const role = localStorage.getItem('role');
  const name = localStorage.getItem('name');
  const loggedIn = !!localStorage.getItem('token');

  const logout = () => {
    localStorage.clear();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand" onClick={() => navigate(loggedIn ? (role === 'MENTOR' ? '/mentor-dashboard' : '/browse') : '/login')}>
        Your Senior
      </div>

      {loggedIn && (
        <div className="navbar-links">
          {role === 'USER' && (
            <>
              <a onClick={() => navigate('/browse')}>Browse</a>
              <a onClick={() => navigate('/my-bookings')}>My Bookings</a>
            </>
          )}
          {role === 'MENTOR' && (
            <a onClick={() => navigate('/mentor-dashboard')}>Dashboard</a>
          )}
          <span className="navbar-name">Hi, {name}</span>
          <button className="navbar-logout" onClick={logout}>Logout</button>
        </div>
      )}
    </nav>
  );
}

export default Navbar;