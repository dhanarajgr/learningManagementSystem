// src/pages/Home.jsx
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../api/axios';

const Home = () => {

    const navigate = useNavigate();

    const [courses, setCourses] = useState([]);
    const [search, setSearch] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    const role = localStorage.getItem('role');
    const token = localStorage.getItem('token');

    // fetch all courses on load
    useEffect(() => {
        fetchCourses();
    }, []);

    // get all courses
    const fetchCourses = async () => {
        try {
            const res = await API.get('/courses');
            setCourses(res.data);
        } catch (err) {
            setError('Failed to load courses');
        } finally {
            setLoading(false);
        }
    };

    // search courses
    const handleSearch = async (e) => {
        e.preventDefault();
        if (!search.trim()) {
            fetchCourses();
            return;
        }
        try {
            setLoading(true);
            const res = await API.get(
                `/courses/search?title=${search}`);
            setCourses(res.data);
        } catch (err) {
            setError('No courses found');
            setCourses([]);
        } finally {
            setLoading(false);
        }
    };

    // handle enroll
    const handleEnroll = async (courseId) => {
        if (!token) {
            navigate('/login');
            return;
        }
        try {
            await API.post(`/enrollments/${courseId}`);
            alert('Enrolled successfully!');
            navigate('/student/dashboard');
        } catch (err) {
            alert(
                err.response?.data?.message ||
                'Enrollment failed'
            );
        }
    };

    return (
        <div style={styles.container}>

            {/* hero section */}
            <div style={styles.hero}>
                <h1 style={styles.heroTitle}>
                    Learn Without Limits
                </h1>
                <p style={styles.heroSubtitle}>
                    Explore top courses from expert instructors
                </p>

                {/* search bar */}
                <form onSubmit={handleSearch}
                    style={styles.searchForm}>
                    <input
                        type="text"
                        placeholder="Search courses..."
                        value={search}
                        onChange={(e) =>
                            setSearch(e.target.value)}
                        style={styles.searchInput}
                    />
                    <button type="submit"
                        style={styles.searchButton}>
                        Search
                    </button>
                </form>
            </div>

            {/* courses section */}
            <div style={styles.section}>

                <h2 style={styles.sectionTitle}>
                    All Courses
                </h2>

                {/* loading */}
                {loading && (
                    <p style={styles.loading}>
                        Loading courses...
                    </p>
                )}

                {/* error */}
                {error && (
                    <p style={styles.error}>{error}</p>
                )}

                {/* courses grid */}
                {!loading && courses.length === 0 && (
                    <p style={styles.empty}>
                        No courses found
                    </p>
                )}

                <div style={styles.grid}>
                    {courses.map((course) => (
                        <div key={course.id}
                            style={styles.card}>

                            {/* course header */}
                            <div style={styles.cardHeader}>
                                <h3 style={styles.cardTitle}>
                                    {course.title}
                                </h3>
                                <span style={styles.price}>
                                    ₹{course.price}
                                </span>
                            </div>

                            {/* description */}
                            <p style={styles.description}>
                                {course.description
                                    .substring(0, 100)}...
                            </p>

                            {/* course info */}
                            <div style={styles.info}>
                                <span style={styles.infoItem}>
                                    👨‍🏫 {course.instructorName}
                                </span>
                                <span style={styles.infoItem}>
                                    📚 {course.totalLessons} Lessons
                                </span>
                                <span style={styles.infoItem}>
                                    ⭐ {course.averageRating > 0
                                        ? course.averageRating
                                            .toFixed(1)
                                        : 'No ratings'}
                                </span>
                            </div>

                            {/* enroll button */}
                            {role === 'STUDENT' && (
                                <button
                                    onClick={() =>
                                        handleEnroll(course.id)}
                                    style={styles.enrollButton}>
                                    Enroll Now
                                </button>
                            )}

                            {/* not logged in */}
                            {!token && (
                                <button
                                    onClick={() =>
                                        navigate('/login')}
                                    style={styles.enrollButton}>
                                    Login to Enroll
                                </button>
                            )}

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
        backgroundColor: '#f0f2f5'
    },
    hero: {
        backgroundColor: '#1a1a2e',
        padding: '60px 20px',
        textAlign: 'center'
    },
    heroTitle: {
        color: 'white',
        fontSize: '42px',
        marginBottom: '12px'
    },
    heroSubtitle: {
        color: '#aaa',
        fontSize: '18px',
        marginBottom: '30px'
    },
    searchForm: {
        display: 'flex',
        justifyContent: 'center',
        gap: '10px',
        maxWidth: '500px',
        margin: '0 auto'
    },
    searchInput: {
        flex: 1,
        padding: '12px 16px',
        borderRadius: '8px',
        border: 'none',
        fontSize: '15px',
        outline: 'none'
    },
    searchButton: {
        padding: '12px 24px',
        backgroundColor: '#00d4ff',
        color: '#1a1a2e',
        border: 'none',
        borderRadius: '8px',
        fontSize: '15px',
        fontWeight: 'bold',
        cursor: 'pointer'
    },
    section: {
        padding: '40px 30px'
    },
    sectionTitle: {
        fontSize: '26px',
        color: '#1a1a2e',
        marginBottom: '24px'
    },
    loading: {
        textAlign: 'center',
        color: '#888',
        fontSize: '16px'
    },
    error: {
        textAlign: 'center',
        color: '#e74c3c',
        fontSize: '16px'
    },
    empty: {
        textAlign: 'center',
        color: '#888',
        fontSize: '16px'
    },
    grid: {
        display: 'grid',
        gridTemplateColumns:
            'repeat(auto-fill, minmax(300px, 1fr))',
        gap: '24px'
    },
    card: {
        backgroundColor: 'white',
        borderRadius: '12px',
        padding: '24px',
        boxShadow: '0 2px 12px rgba(0,0,0,0.08)',
        display: 'flex',
        flexDirection: 'column',
        gap: '12px'
    },
    cardHeader: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'flex-start'
    },
    cardTitle: {
        fontSize: '18px',
        color: '#1a1a2e',
        margin: 0,
        flex: 1
    },
    price: {
        backgroundColor: '#e8f5e9',
        color: '#2e7d32',
        padding: '4px 10px',
        borderRadius: '20px',
        fontSize: '14px',
        fontWeight: 'bold',
        whiteSpace: 'nowrap'
    },
    description: {
        color: '#666',
        fontSize: '14px',
        lineHeight: '1.5'
    },
    info: {
        display: 'flex',
        flexDirection: 'column',
        gap: '6px'
    },
    infoItem: {
        fontSize: '13px',
        color: '#555'
    },
    enrollButton: {
        padding: '10px',
        backgroundColor: '#1a1a2e',
        color: 'white',
        border: 'none',
        borderRadius: '8px',
        fontSize: '14px',
        cursor: 'pointer',
        marginTop: 'auto'
    }
};

export default Home;