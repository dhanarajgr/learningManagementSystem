// src/pages/CreateCourse.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../api/axios';

const CreateCourse = () => {

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        title: '',
        description: '',
        price: ''
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            await API.post('/courses', {
                ...formData,
                price: parseFloat(formData.price)
            });
            alert('Course created successfully!');
            navigate('/instructor/dashboard');
        } catch (err) {
            setError(
                err.response?.data?.message ||
                'Failed to create course'
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>

                {/* header */}
                <div style={styles.header}>
                    <h2 style={styles.title}>
                        Create New Course
                    </h2>
                    <button
                        onClick={() =>
                            navigate('/instructor/dashboard')}
                        style={styles.backButton}
                    >
                        Back
                    </button>
                </div>

                {/* error */}
                {error && (
                    <div style={styles.error}>
                        {error}
                    </div>
                )}

                {/* form */}
                <form onSubmit={handleSubmit}>

                    {/* title */}
                    <div style={styles.formGroup}>
                        <label style={styles.label}>
                            Course Title
                        </label>
                        <input
                            type="text"
                            name="title"
                            placeholder="Enter course title"
                            value={formData.title}
                            onChange={handleChange}
                            style={styles.input}
                            required
                        />
                    </div>

                    {/* description */}
                    <div style={styles.formGroup}>
                        <label style={styles.label}>
                            Description
                        </label>
                        <textarea
                            name="description"
                            placeholder="Enter course description"
                            value={formData.description}
                            onChange={handleChange}
                            style={styles.textarea}
                            required
                        />
                    </div>

                    {/* price */}
                    <div style={styles.formGroup}>
                        <label style={styles.label}>
                            Price (Rs.)
                        </label>
                        <input
                            type="number"
                            name="price"
                            placeholder="Enter course price"
                            value={formData.price}
                            onChange={handleChange}
                            style={styles.input}
                            min="0"
                            required
                        />
                    </div>

                    {/* buttons */}
                    <div style={styles.buttons}>
                        <button
                            type="button"
                            onClick={() =>
                                navigate('/instructor/dashboard')}
                            style={styles.cancelButton}
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            style={styles.submitButton}
                            disabled={loading}
                        >
                            {loading
                                ? 'Creating...'
                                : 'Create Course'}
                        </button>
                    </div>

                </form>
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
        alignItems: 'center',
        padding: '30px'
    },
    card: {
        backgroundColor: 'white',
        borderRadius: '12px',
        padding: '40px',
        width: '100%',
        maxWidth: '600px',
        boxShadow: '0 4px 20px rgba(0,0,0,0.1)'
    },
    header: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '30px'
    },
    title: {
        fontSize: '24px',
        color: '#1a1a2e',
        margin: 0
    },
    backButton: {
        padding: '8px 16px',
        backgroundColor: 'transparent',
        border: '1px solid #1a1a2e',
        color: '#1a1a2e',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '14px'
    },
    error: {
        backgroundColor: '#ffe0e0',
        color: '#e74c3c',
        padding: '12px',
        borderRadius: '8px',
        marginBottom: '20px',
        fontSize: '14px'
    },
    formGroup: {
        marginBottom: '20px'
    },
    label: {
        display: 'block',
        marginBottom: '8px',
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
    textarea: {
        width: '100%',
        padding: '12px',
        borderRadius: '8px',
        border: '1px solid #ddd',
        fontSize: '14px',
        outline: 'none',
        minHeight: '150px',
        resize: 'vertical',
        boxSizing: 'border-box'
    },
    buttons: {
        display: 'flex',
        gap: '12px',
        justifyContent: 'flex-end',
        marginTop: '10px'
    },
    cancelButton: {
        padding: '12px 24px',
        backgroundColor: '#888',
        color: 'white',
        border: 'none',
        borderRadius: '8px',
        cursor: 'pointer',
        fontSize: '14px'
    },
    submitButton: {
        padding: '12px 24px',
        backgroundColor: '#1a1a2e',
        color: 'white',
        border: 'none',
        borderRadius: '8px',
        cursor: 'pointer',
        fontSize: '14px'
    }
};

export default CreateCourse;