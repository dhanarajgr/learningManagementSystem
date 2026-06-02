// src/pages/InstructorLogin.jsx
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import API from '../api/axios';

const InstructorLogin = () => {

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        email: '',
        password: ''
    });

    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            const res = await API.post(
                '/auth/login', formData);

            if (res.data.role !== 'INSTRUCTOR') {
                setError(
                    'Access denied. Instructor only.'
                );
                return;
            }

            localStorage.setItem('token', res.data.token);
            localStorage.setItem('role', res.data.role);
            localStorage.setItem('name', res.data.name);
            localStorage.setItem('email', res.data.email);

            navigate('/instructor/dashboard');

        } catch (err) {
            setError(
                err.response?.data?.message ||
                'Invalid email or password'
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>

                <div style={styles.badge}>
                    INSTRUCTOR
                </div>

                <h2 style={styles.title}>
                    Instructor Login
                </h2>
                <p style={styles.subtitle}>
                    Login to your instructor account
                </p>

                {error && (
                    <div style={styles.error}>
                        {error}
                    </div>
                )}

                <form onSubmit={handleLogin}>

                    <div style={styles.formGroup}>
                        <label style={styles.label}>
                            Email
                        </label>
                        <input
                            type="email"
                            name="email"
                            placeholder="Enter your email"
                            value={formData.email}
                            onChange={handleChange}
                            style={styles.input}
                            required
                        />
                    </div>

                    <div style={styles.formGroup}>
                        <label style={styles.label}>
                            Password
                        </label>
                        <input
                            type="password"
                            name="password"
                            placeholder="Enter your password"
                            value={formData.password}
                            onChange={handleChange}
                            style={styles.input}
                            required
                        />
                    </div>

                    <button
                        type="submit"
                        style={styles.button}
                        disabled={loading}
                    >
                        {loading
                            ? 'Logging in...'
                            : 'Login'}
                    </button>

                </form>

                <p style={styles.bottomText}>
                    No account?{' '}
                    <Link to="/instructor/register"
                        style={styles.link}>
                        Register here
                    </Link>
                </p>

            </div>
        </div>
    );
};

const styles = {
    container: {
        minHeight: '100vh',
        backgroundColor: '#f0f2f5',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center'
    },
    card: {
        backgroundColor: 'white',
        padding: '40px',
        borderRadius: '12px',
        boxShadow: '0 4px 20px rgba(0,0,0,0.1)',
        width: '100%',
        maxWidth: '420px'
    },
    badge: {
        backgroundColor: '#e0f2f1',
        color: '#00695c',
        padding: '6px 16px',
        borderRadius: '20px',
        fontSize: '12px',
        fontWeight: 'bold',
        textAlign: 'center',
        marginBottom: '16px',
        letterSpacing: '2px'
    },
    title: {
        textAlign: 'center',
        color: '#1a1a2e',
        fontSize: '26px',
        marginBottom: '8px'
    },
    subtitle: {
        textAlign: 'center',
        color: '#888',
        marginBottom: '24px',
        fontSize: '14px'
    },
    error: {
        backgroundColor: '#ffe0e0',
        color: '#e74c3c',
        padding: '10px',
        borderRadius: '6px',
        marginBottom: '16px',
        fontSize: '14px',
        textAlign: 'center'
    },
    formGroup: {
        marginBottom: '18px'
    },
    label: {
        display: 'block',
        marginBottom: '6px',
        color: '#333',
        fontSize: '14px',
        fontWeight: 'bold'
    },
    input: {
        width: '100%',
        padding: '12px',
        borderRadius: '8px',
        border: '1px solid #ddd',
        fontSize: '14px',
        outline: 'none',
        boxSizing: 'border-box'
    },
    button: {
        width: '100%',
        padding: '12px',
        backgroundColor: '#00695c',
        color: 'white',
        border: 'none',
        borderRadius: '8px',
        fontSize: '16px',
        cursor: 'pointer',
        marginTop: '8px'
    },
    bottomText: {
        textAlign: 'center',
        marginTop: '20px',
        fontSize: '14px',
        color: '#888'
    },
    link: {
        color: '#00695c',
        fontWeight: 'bold',
        textDecoration: 'none'
    }
};

export default InstructorLogin;