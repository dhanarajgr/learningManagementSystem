// src/components/Navbar.jsx
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

const Navbar = () => {

    const navigate = useNavigate();
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    const name = localStorage.getItem('name');

    const handleLogout = () => {
        localStorage.clear();
        navigate('/login');
    };

    return (
        <nav style={styles.nav}>

            {/* logo */}
            <Link to="/" style={styles.logo}>
                LMS Portal
            </Link>

            {/* links */}
            <div style={styles.links}>

                <Link to="/" style={styles.link}>
                    Courses
                </Link>

                {/* student links */}
                {token && role === 'STUDENT' && (
                    <Link to="/student/dashboard"
                        style={styles.link}>
                        My Courses
                    </Link>
                )}

                {/* instructor links */}
                {token && role === 'INSTRUCTOR' && (
                    <>
                        <Link to="/instructor/dashboard"
                            style={styles.link}>
                            My Courses
                        </Link>
                        <Link to="/instructor/create-course"
                            style={styles.link}>
                            Create Course
                        </Link>
                    </>
                )}

                {/* admin links */}
                {token && role === 'ADMIN' && (
                    <Link to="/admin/dashboard"
                        style={styles.link}>
                        Admin Panel
                    </Link>
                )}

                {/* not logged in */}
                {!token && (
                    <>
                        <Link to="/login"
                            style={styles.link}>
                            Login
                        </Link>
                        <Link to="/register"
                            style={styles.link}>
                            Register
                        </Link>
                    </>
                )}

                {/* logged in */}
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
        textDecoration: 'none',
        fontSize: '22px',
        fontWeight: 'bold'
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