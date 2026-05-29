// src/pages/StudentDashboard.jsx
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../api/axios';

const StudentDashboard = () => {

    const navigate = useNavigate();

    const [enrollments, setEnrollments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    const name = localStorage.getItem('name');

    // fetch enrollments on load
    useEffect(() => {
        fetchEnrollments();
    }, []);

    // get my enrollments
    const fetchEnrollments = async () => {
        try {
            const res = await API.get(
                '/enrollments/my-courses');
            setEnrollments(res.data);
        } catch (err) {
            setError('Failed to load enrollments');
        } finally {
            setLoading(false);
        }
    };

    // unenroll from course
    const handleUnenroll = async (courseId) => {
        if (!window.confirm(
            'Are you sure you want to unenroll?')) {
            return;
        }
        try {
            await API.delete(`/enrollments/${courseId}`);
            alert('Unenrolled successfully');
            fetchEnrollments();
        } catch (err) {
            alert(
                err.response?.data?.message ||
                'Unenroll failed'
            );
        }
    };

    // complete course
    const handleComplete = async (courseId) => {
        try {
            await API.put(
                `/enrollments/${courseId}/complete`);
            alert('Course marked as completed!');
            fetchEnrollments();
        } catch (err) {
            alert(
                err.response?.data?.message ||
                'Already completed'
            );
        }
    };

    return (
        <div style={styles.container}>

            {/* header */}
            <div style={styles.header}>
                <div>
                    <h2 style={styles.title}>
                        My Dashboard
                    </h2>
                    <p style={styles.subtitle}>
                        Welcome back, {name}!
                    </p>
                </div>
                <button
                    onClick={() => navigate('/')}
                    style={styles.browseButton}>
                    Browse Courses
                </button>
            </div>

            {/* stats */}
            <div style={styles.stats}>
                <div style={styles.statCard}>
                    <h3 style={styles.statNumber}>
                        {enrollments.length}
                    </h3>
                    <p style={styles.statLabel}>
                        Enrolled Courses
                    </p>
                </div>
                <div style={styles.statCard}>
                    <h3 style={styles.statNumber}>
                        {enrollments.filter(
                            e => e.completed).length}
                    </h3>
                    <p style={styles.statLabel}>
                        Completed
                    </p>
                </div>
                <div style={styles.statCard}>
                    <h3 style={styles.statNumber}>
                        {enrollments.filter(
                            e => !e.completed).length}
                    </h3>
                    <p style={styles.statLabel}>
                        In Progress
                    </p>
                </div>
            </div>

            {/* enrollments */}
            <div style={styles.section}>
                <h3 style={styles.sectionTitle}>
                    My Courses
                </h3>

                {/* loading */}
                {loading && (
                    <p style={styles.loading}>
                        Loading...
                    </p>
                )}

                {/* error */}
                {error && (
                    <p style={styles.error}>{error}</p>
                )}

                {/* empty */}
                {!loading && enrollments.length === 0 && (
                    <div style={styles.empty}>
                        <p>You have not enrolled
                            in any course yet</p>
                        <button
                            onClick={() => navigate('/')}
                            style={styles.browseButton}>
                            Browse Courses
                        </button>
                    </div>
                )}

                {/* enrollment list */}
                <div style={styles.grid}>
                    {enrollments.map((enrollment) => (
                        <div key={enrollment.id}
                            style={styles.card}>

                            {/* course title */}
                            <h3 style={styles.courseTitle}>
                                {enrollment.courseTitle}
                            </h3>

                            {/* status badge */}
                            <span style={{
                                ...styles.badge,
                                backgroundColor:
                                    enrollment.completed
                                        ? '#e8f5e9'
                                        : '#fff3e0',
                                color:
                                    enrollment.completed
                                        ? '#2e7d32'
                                        : '#e65100'
                            }}>
                                {enrollment.completed
                                    ? '✅ Completed'
                                    : '📖 In Progress'}
                            </span>

                            {/* enrolled date */}
                            <p style={styles.date}>
                                Enrolled:{' '}
                                {new Date(enrollment.enrolledAt)
                                    .toLocaleDateString()}
                            </p>

                            {/* action buttons */}
                            <div style={styles.actions}>

                                {/* view lessons */}
                                <button
                                    onClick={() => navigate(
                                        `/student/lessons/${enrollment.courseId}`
                                    )}
                                    style={styles.lessonsButton}>
                                    View Lessons
                                </button>

                                {/* complete button */}
                                {!enrollment.completed && (
                                    <button
                                        onClick={() =>
                                            handleComplete(
                                                enrollment.courseId
                                            )}
                                        style={styles.completeButton}>
                                        Mark Complete
                                    </button>
                                )}

                                {/* unenroll button */}
                                <button
                                    onClick={() =>
                                        handleUnenroll(
                                            enrollment.courseId
                                        )}
                                    style={styles.unenrollButton}>
                                    Unenroll
                                </button>

                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

const styles = {
    container: {
        minHeight: '100vh',
        backgroundColor: '#f0f2f5',
        padding: '30px'
    },
    header: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '30px'
    },
    title: {
        fontSize: '28px',
        color: '#1a1a2e',
        margin: 0
    },
    subtitle: {
        color: '#888',
        margin: '4px 0 0 0'
    },
    browseButton: {
        padding: '10px 20px',
        backgroundColor: '#1a1a2e',
        color: 'white',
        border: 'none',
        borderRadius: '8px',
        cursor: 'pointer',
        fontSize: '14px'
    },
    stats: {
        display: 'grid',
        gridTemplateColumns: 'repeat(3, 1fr)',
        gap: '20px',
        marginBottom: '30px'
    },
    statCard: {
        backgroundColor: 'white',
        padding: '24px',
        borderRadius: '12px',
        textAlign: 'center',
        boxShadow: '0 2px 12px rgba(0,0,0,0.08)'
    },
    statNumber: {
        fontSize: '36px',
        color: '#1a1a2e',
        margin: 0
    },
    statLabel: {
        color: '#888',
        margin: '8px 0 0 0',
        fontSize: '14px'
    },
    section: {
        backgroundColor: 'white',
        borderRadius: '12px',
        padding: '24px',
        boxShadow: '0 2px 12px rgba(0,0,0,0.08)'
    },
    sectionTitle: {
        fontSize: '20px',
        color: '#1a1a2e',
        marginBottom: '20px'
    },
    loading: {
        textAlign: 'center',
        color: '#888'
    },
    error: {
        textAlign: 'center',
        color: '#e74c3c'
    },
    empty: {
        textAlign: 'center',
        color: '#888',
        padding: '40px'
    },
    grid: {
        display: 'grid',
        gridTemplateColumns:
            'repeat(auto-fill, minmax(280px, 1fr))',
        gap: '20px'
    },
    card: {
        border: '1px solid #eee',
        borderRadius: '12px',
        padding: '20px',
        display: 'flex',
        flexDirection: 'column',
        gap: '12px'
    },
    courseTitle: {
        fontSize: '18px',
        color: '#1a1a2e',
        margin: 0
    },
    badge: {
        padding: '4px 12px',
        borderRadius: '20px',
        fontSize: '13px',
        fontWeight: 'bold',
        width: 'fit-content'
    },
    date: {
        color: '#888',
        fontSize: '13px',
        margin: 0
    },
    actions: {
        display: 'flex',
        gap: '8px',
        flexWrap: 'wrap',
        marginTop: 'auto'
    },
    lessonsButton: {
        padding: '8px 14px',
        backgroundColor: '#1a1a2e',
        color: 'white',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '13px'
    },
    completeButton: {
        padding: '8px 14px',
        backgroundColor: '#2e7d32',
        color: 'white',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '13px'
    },
    unenrollButton: {
        padding: '8px 14px',
        backgroundColor: '#e74c3c',
        color: 'white',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '13px'
    }
};

export default StudentDashboard;