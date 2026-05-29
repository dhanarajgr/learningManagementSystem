// src/pages/Register.jsx
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import API from '../api/axios';

const Register = () => {

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        role: 'STUDENT'
    });

    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    // handle input change
    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    // handle register submit
    const handleRegister = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            const res = await API.post(
                '/auth/register', formData);

            // save to localStorage
            localStorage.setItem('token', res.data.token);
            localStorage.setItem('role', res.data.role);
            localStorage.setItem('name', res.data.name);
            localStorage.setItem('email', res.data.email);

            // redirect based on role
            if (res.data.role === 'STUDENT') {
                navigate('/student/dashboard');
            } else if (res.data.role === 'INSTRUCTOR') {
                navigate('/instructor/dashboard');
            }

        } catch (err) {
            setError(
                err.response?.data?.message ||
                'Registration failed. Try again.'
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>

                {/* header */}
                <h2 style={styles.title}>Create Account</h2>
                <p style={styles.subtitle}>
                    Join LMS Portal today
                </p>

                {/* error message */}
                {error && (
                    <div style={styles.error}>
                        {error}
                    </div>
                )}

                {/* form */}
                <form onSubmit={handleRegister}>

                    {/* name */}
                    <div style={styles.formGroup}>
                        <label style={styles.label}>
                            Full Name
                        </label>
                        <input
                            type="text"
                            name="name"
                            placeholder="Enter your full name"
                            value={formData.name}
                            onChange={handleChange}
                            style={styles.input}
                            required
                        />
                    </div>

                    {/* email */}
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

                    {/* password */}
                    <div style={styles.formGroup}>
                        <label style={styles.label}>
                            Password
                        </label>
                        <input
                            type="password"
                            name="password"
                            placeholder="Min 6 characters"
                            value={formData.password}
                            onChange={handleChange}
                            style={styles.input}
                            required
                        />
                    </div>

                    {/* role */}
                    <div style={styles.formGroup}>
                        <label style={styles.label}>
                            Register As
                        </label>
                        <select
                            name="role"
                            value={formData.role}
                            onChange={handleChange}
                            style={styles.select}>
                            <option value="STUDENT">
                                Student
                            </option>
                            <option value="INSTRUCTOR">
                                Instructor
                            </option>
                        </select>
                    </div>

                    <button
                        type="submit"
                        style={styles.button}
                        disabled={loading}>
                        {loading ? 'Registering...' : 'Register'}
                    </button>

                </form>

                {/* login link */}
                <p style={styles.loginText}>
                    Already have an account?{' '}
                    <Link to="/login"
                        style={styles.loginLink}>
                        Login here
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
    select: {
        width: '100%',
        padding: '12px',
        borderRadius: '8px',
        border: '1px solid #ddd',
        fontSize: '14px',
        outline: 'none',
        boxSizing: 'border-box',
        backgroundColor: 'white',
        cursor: 'pointer'
    },
    button: {
        width: '100%',
        padding: '12px',
        backgroundColor: '#1a1a2e',
        color: 'white',
        border: 'none',
        borderRadius: '8px',
        fontSize: '16px',
        cursor: 'pointer',
        marginTop: '8px'
    },
    loginText: {
        textAlign: 'center',
        marginTop: '20px',
        fontSize: '14px',
        color: '#888'
    },
    loginLink: {
        color: '#1a1a2e',
        fontWeight: 'bold',
        textDecoration: 'none'
    }
};

export default Register;