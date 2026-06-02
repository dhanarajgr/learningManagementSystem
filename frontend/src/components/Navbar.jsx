// src/components/Navbar.jsx
import React from 'react';
import { Link, useNavigate, useLocation }
    from 'react-router-dom';

const Navbar = () => {

    const navigate = useNavigate();
    const location = useLocation();
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    const name = localStorage.getItem('name');

    // check which section user is in
    const isInstructor = location.pathname
        .startsWith('/instructor');
    const isAdmin = location.pathname
        .startsWith('/admin');
    const isStudent = !isInstructor && !isAdmin;

    const handleLogout = () => {
        localStorage.clear();
        if (isInstructor) {
            navigate('/instructor/login');
        } else if (isAdmin) {
            navigate('/admin');
        } else {
            navigate('/student/login');
        }
    };

    return (
        <nav style={styles.nav}>

            {/* logo */}
            <div style={styles.logo}>
                {isAdmin
                    ? 'LMS Admin'
                    : isInstructor
                        ? 'LMS Instructor'
                        : 'LMS Portal'}
            </div>

            {/* links */}
            <div style={styles.links}>

                {/* ── STUDENT NAVBAR ─────────────── */}
                {isStudent && (
                    <>
                        <Link to="/"
                            style={styles.link}>
                            Courses
                        </Link>

                        {token && role === 'STUDENT' && (
                            <Link
                                to="/student/dashboard"
                                style={styles.link}>
                                My Courses
                            </Link>
                        )}

                        {!token && (
                            <>
                                <Link
                                    to="/student/login"
                                    style={styles.link}>
                                    Login
                                </Link>
                                <Link
                                    to="/student/register"
                                    style={styles.registerLink}>
                                    Register
                                </Link>
                            </>
                        )}
                    </>
                )}

                {/* ── INSTRUCTOR NAVBAR ──────────── */}
                {isInstructor && (
                    <>
                        {token && role === 'INSTRUCTOR' && (
                            <>
                                <Link
                                    to="/instructor/dashboard"
                                    style={styles.link}>
                                    My Courses
                                </Link>
                                <Link
                                    to="/instructor/create-course"
                                    style={styles.link}>
                                    Create Course
                                </Link>
                            </>
                        )}

                        {!token && (
                            <>
                                <Link
                                    to="/instructor/login"
                                    style={styles.link}>
                                    Login
                                </Link>
                                <Link
                                    to="/instructor/register"
                                    style={styles.registerLink}>
                                    Register
                                </Link>
                            </>
                        )}
                    </>
                )}

                {/* ── ADMIN NAVBAR ───────────────── */}
                {isAdmin && (
                    <>
                        {token && role === 'ADMIN' && (
                            <Link
                                to="/admin/dashboard"
                                style={styles.link}>
                                Dashboard
                            </Link>
                        )}
                    </>
                )}

                {/* ── LOGGED IN USER ─────────────── */}
                {token && (
                    <>
                        <span style={styles.name}>
                            Hi, {name}
                        </span>
                        <button
                            onClick={handleLogout}
                            style={styles.logout}>
                            Logout
                        </button>
                    </>
                )}

            </div>
        </nav>
    );
};

const styles = {
    nav: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: '15px 30px',
        backgroundColor: '#1a1a2e',
        color: 'white'
    },
    logo: {
        color: 'white',
        fontSize: '22px',
        fontWeight: 'bold',
        cursor: 'pointer'
    },
    links: {
        display: 'flex',
        alignItems: 'center',
        gap: '20px'
    },
    link: {
        color: 'white',
        textDecoration: 'none',
        fontSize: '15px'
    },
    registerLink: {
        color: '#1a1a2e',
        textDecoration: 'none',
        fontSize: '14px',
        backgroundColor: '#00d4ff',
        padding: '8px 16px',
        borderRadius: '6px',
        fontWeight: 'bold'
    },
    name: {
        color: '#00d4ff',
        fontSize: '15px'
    },
    logout: {
        backgroundColor: '#e74c3c',
        color: 'white',
        border: 'none',
        padding: '8px 16px',
        borderRadius: '5px',
        cursor: 'pointer'
    }
};

export default Navbar;