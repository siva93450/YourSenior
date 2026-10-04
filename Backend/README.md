Your Senior

A peer-to-peer mentorship platform connecting prospective college students with current students (mentors) at their target colleges — browse by college and department, book a session, chat privately, and rate your experience.

The Problem

Students choosing a college often rely on marketing brochures, YouTube campus tours, or word of mouth from acquaintances. Your Senior connects them directly with current students at the colleges they're considering, for honest, first-hand guidance — without exposing either side's personal contact details.

Features
Separate Student and Mentor accounts with independent registration and login
Browse flow: College → Department → Mentor list, each showing average rating and live availability
Booking system: students request a session (date/time); mentors accept or reject requests
Availability toggle: mentors mark themselves free/busy in real time
Rating system: students rate a mentor only after an accepted session has actually taken place
In-app private messaging: student and mentor can chat once a booking is accepted, without either side's email/phone being revealed
JWT-based authentication: stateless, role-aware (USER / MENTOR), with public browsing and protected booking/messaging endpoints
Tech Stack
Backend
Technology	Purpose
Java 25	Language
Spring Boot 4.1.1	Application framework
Spring Web (REST)	RESTful API layer
Spring Data JPA	Database access via Hibernate
Hibernate 7.4.5	ORM
MySQL	Relational database
Spring Security 7	Authentication & authorization
JJWT (io.jsonwebtoken) 0.12.6	JWT token generation & validation
BCrypt	Password hashing
Jakarta Bean Validation	Request payload validation (@NotBlank, @Email, etc.)
Maven	Build & dependency management
Frontend
Technology	Purpose
React 19	UI library
Vite	Build tool / dev server
React Router DOM 7	Client-side routing
Axios	HTTP client, with a request interceptor that auto-attaches the JWT to every call
Custom CSS	Design system (navy/amber theme, Fraunces + Inter typefaces)
Architecture
Layered backend: Controller → Service → Repository → Entity, consistently applied across all four modules (User, Mentor, Booking, Message)
Stateless REST API — no server-side sessions; every authenticated request carries its own JWT
CORS configured for local frontend-backend communication during development
API Overview
Module	Key Endpoints
Users	               POST /api/users/register, POST /api/users/login, GET /api/users/{id}
Mentors	               POST /api/mentors/register, POST /api/mentors/login, GET /api/mentors/search?college=&course=, GET /api/mentors/colleges, GET /api/mentors/colleges/{college}/courses, PUT /api/mentors/{id}/availability
Bookings	           POST /api/bookings/create?userId=&mentorId=, GET /api/bookings/user/{userId}, GET /api/bookings/mentor/{mentorId}, PUT /api/bookings/{id}/status, PUT /api/bookings/{id}/rate
Messages	           POST /api/messages/{bookingId}, GET /api/messages/{bookingId}

All endpoints except registration, login, and mentor browsing require a valid Authorization: Bearer <token> header.

Project Structure
YourSenior/
├── backend/          # Spring Boot application
│   └── src/main/java/com/One/YourSenior/
│       ├── model/        # JPA entities
│       ├── repository/   # Spring Data repositories
│       ├── service/       # Service interfaces
│       ├── service/impl/  # Service implementations
│       ├── controller/    # REST controllers
│       ├── security/      # JWT filter, JwtUtil, SecurityConfig
│       └── dto/           # Login request/response DTOs
└── frontend/         # React (Vite) application
    └── src/
        ├── pages/         # Login, Register, Browse, MyBookings, MentorDashboard
        ├── components/    # Navbar, Chat
        └── api/           # Axios instance with JWT interceptor
Running Locally

Backend:

bash
cd backend
# Create backend/src/main/resources/application.properties
# using application.properties.example as a template
mvn spring-boot:run

Frontend:

bash
cd frontend
npm install
npm run dev

Backend runs on http://localhost:8080, frontend on http://localhost:5173.

Roadmap / Not Yet Implemented
Payment integration (Razorpay) for paid mentorship sessions — deliberately scoped out to prioritize a complete, working core platform within the project timeline
Real-time messaging via WebSockets (current implementation uses polling)
Email verification on signup
Author

Built by Siva, 3rd-year CSE student at Sri Sairam Engineering College, as part of coursework and placement portfolio preparation.