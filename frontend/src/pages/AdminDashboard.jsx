// src/pages/AdminDashboard.jsx
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../api/axios';

const AdminDashboard = () => {

    const navigate = useNavigate();

    const [users, setUsers] = useState([]);
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [activeTab, setActiveTab] = useState('users');

    const name = localStorage.getItem('name');

    useEffect(() => {
        fetchUsers();
        fetchCourses();
    }, []);

    // get all users
    const fetchUsers = async () => {
        try {
            const res = await API.get('/users');
            setUsers(res.data);
        } catch (err) {
            setError('Failed to load users');
        } finally {
            setLoading(false);
        }
    };

    // get all courses
    const fetchCourses = async () => {
        try {
            const res = await API.get('/courses');
            setCourses(res.data);
        } catch (err) {
            setError('Failed to load courses');
        }
    };

    // delete user
    const handleDeleteUser = async (userId) => {
        if (!window.confirm(
            'Are you sure you want to delete this user?'
        )) {
            return;
        }
        try {
            await API.delete(`/users/${userId}`);
            alert('User deleted successfully');
            fetchUsers();
        } catch (err) {
            alert(
                err.response?.data?.message ||
                'Delete failed'
            );
        }
    };

    // delete course
    const handleDeleteCourse = async (courseId) => {
        if (!window.confirm(
            'Are you sure you want to delete this course?'
        )) {
            return;
        }
        try {
            await API.delete(`/courses/${courseId}`);
            alert('Course deleted successfully');
            fetchCourses();
        } catch (err) {
            alert(
                err.response?.data?.message ||
                'Delete failed'
            );
        }
    };

    // get role badge color
    const getRoleColor = (role) => {
        if (role === 'ADMIN') return {
            bg: '#e8eaf6', color: '#3949ab'
        };
        if (role === 'INSTRUCTOR') return {
            bg: '#e0f2f1', color: '#00695c'
        };
        return {
            bg: '#e3f2fd', color: '#1565c0'
        };
    };

    return (
        <div style={styles.container}>

            {/* header */}
            <div style={styles.header}>
                <div>
                    <h2 style={styles.title}>
                        Admin Dashboard
                    </h2>
                    <p style={styles.subtitle}>
                        Welcome, {name}!
                    </p>
                </div>
            </div>

            {/* stats */}
            <div style={styles.stats}>
                <div style={styles.statCard}>
                    <h3 style={styles.statNumber}>
                        {users.length}
                    </h3>
                    <p style={styles.statLabel}>
                        Total Users
                    </p>
                </div>
                <div style={styles.statCard}>
                    <h3 style={styles.statNumber}>
                        {users.filter(
                            u => u.role === 'STUDENT'
                        ).length}
                    </h3>
                    <p style={styles.statLabel}>
                        Students
                    </p>
                </div>
                <div style={styles.statCard}>
                    <h3 style={styles.statNumber}>
                        {users.filter(
                            u => u.role === 'INSTRUCTOR'
                        ).length}
                    </h3>
                    <p style={styles.statLabel}>
                        Instructors
                    </p>
                </div>
                <div style={styles.statCard}>
                    <h3 style={styles.statNumber}>
                        {courses.length}
                    </h3>
                    <p style={styles.statLabel}>
                        Total Courses
                    </p>
                </div>
            </div>

            {/* tabs */}
            <div style={styles.tabs}>
                <button
                    onClick={() => setActiveTab('users')}
                    style={{
                        ...styles.tab,
                        backgroundColor:
                            activeTab === 'users'
                                ? '#1a1a2e'
                                : 'white',
                        color:
                            activeTab === 'users'
                                ? 'white'
                                : '#1a1a2e'
                    }}
                >
                    Users ({users.length})
                </button>
                <button
                    onClick={() => setActiveTab('courses')}
                    style={{
                        ...styles.tab,
                        backgroundColor:
                            activeTab === 'courses'
                                ? '#1a1a2e'
                                : 'white',
                        color:
                            activeTab === 'courses'
                                ? 'white'
                                : '#1a1a2e'
                    }}
                >
                    Courses ({courses.length})
                </button>
            </div>

            {/* loading */}
            {loading && (
                <p style={styles.loading}>Loading...</p>
            )}

            {/* error */}
            {error && (
                <p style={styles.error}>{error}</p>
            )}

            {/* users tab */}
            {activeTab === 'users' && !loading && (
                <div style={styles.tableCard}>
                    <h3 style={styles.tableTitle}>
                        All Users
                    </h3>

                    {users.length === 0 ? (
                        <p style={styles.empty}>
                            No users found
                        </p>
                    ) : (
                        <table style={styles.table}>
                            <thead>
                                <tr style={styles.tableHead}>
                                    <th style={styles.th}>ID</th>
                                    <th style={styles.th}>Name</th>
                                    <th style={styles.th}>Email</th>
                                    <th style={styles.th}>Role</th>
                                    <th style={styles.th}>Joined</th>
                                    <th style={styles.th}>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {users.map((user) => (
                                    <tr
                                        key={user.id}
                                        style={styles.tableRow}
                                    >
                                        <td style={styles.td}>
                                            {user.id}
                                        </td>
                                        <td style={styles.td}>
                                            {user.name}
                                        </td>
                                        <td style={styles.td}>
                                            {user.email}
                                        </td>
                                        <td style={styles.td}>
                                            <span style={{
                                                ...styles.roleBadge,
                                                backgroundColor:
                                                    getRoleColor(user.role).bg,
                                                color:
                                                    getRoleColor(user.role).color
                                            }}>
                                                {user.role}
                                            </span>
                                        </td>
                                        <td style={styles.td}>
                                            {new Date(user.createdAt)
                                                .toLocaleDateString()}
                                        </td>
                                        <td style={styles.td}>
                                            {user.role !== 'ADMIN' && (
                                                <button
                                                    onClick={() =>
                                                        handleDeleteUser(
                                                            user.id)}
                                                    style={styles.deleteButton}
                                                >
                                                    Delete
                                                </button>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            )}

            {/* courses tab */}
            {activeTab === 'courses' && !loading && (
                <div style={styles.tableCard}>
                    <h3 style={styles.tableTitle}>
                        All Courses
                    </h3>

                    {courses.length === 0 ? (
                        <p style={styles.empty}>
                            No courses found
                        </p>
                    ) : (
                        <table style={styles.table}>
                            <thead>
                                <tr style={styles.tableHead}>
                                    <th style={styles.th}>ID</th>
                                    <th style={styles.th}>Title</th>
                                    <th style={styles.th}>
                                        Instructor
                                    </th>
                                    <th style={styles.th}>Price</th>
                                    <th style={styles.th}>
                                        Lessons
                                    </th>
                                    <th style={styles.th}>
                                        Rating
                                    </th>
                                    <th style={styles.th}>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {courses.map((course) => (
                                    <tr
                                        key={course.id}
                                        style={styles.tableRow}
                                    >
                                        <td style={styles.td}>
                                            {course.id}
                                        </td>
                                        <td style={styles.td}>
                                            {course.title}
                                        </td>
                                        <td style={styles.td}>
                                            {course.instructorName}
                                        </td>
                                        <td style={styles.td}>
                                            Rs.{course.price}
                                        </td>
                                        <td style={styles.td}>
                                            {course.totalLessons}
                                        </td>
                                        <td style={styles.td}>
                                            {course.averageRating > 0
                                                ? course.averageRating
                                                    .toFixed(1)
                                                : 'No ratings'}
                                        </td>
                                        <td style={styles.td}>
                                            <button
                                                onClick={() =>
                                                    handleDeleteCourse(
                                                        course.id)}
                                                style={styles.deleteButton}
                                            >
                                                Delete
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            )}
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
    stats: {
        display: 'grid',
        gridTemplateColumns: 'repeat(4, 1fr)',
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
    tabs: {
        display: 'flex',
        gap: '12px',
        marginBottom: '24px'
    },
    tab: {
        padding: '10px 24px',
        border: '1px solid #1a1a2e',
        borderRadius: '8px',
        cursor: 'pointer',
        fontSize: '14px',
        fontWeight: 'bold'
    },
    loading: {
        textAlign: 'center',
        color: '#888'
    },
    error: {
        textAlign: 'center',
        color: '#e74c3c'
    },
    tableCard: {
        backgroundColor: 'white',
        borderRadius: '12px',
        padding: '24px',
        boxShadow: '0 2px 12px rgba(0,0,0,0.08)',
        overflowX: 'auto'
    },
    tableTitle: {
        fontSize: '18px',
        color: '#1a1a2e',
        marginBottom: '20px'
    },
    table: {
        width: '100%',
        borderCollapse: 'collapse'
    },
    tableHead: {
        backgroundColor: '#f8f9fa'
    },
    th: {
        padding: '12px 16px',
        textAlign: 'left',
        fontSize: '13px',
        color: '#555',
        fontWeight: 'bold',
        borderBottom: '2px solid #eee'
    },
    tableRow: {
        borderBottom: '1px solid #eee'
    },
    td: {
        padding: '12px 16px',
        fontSize: '14px',
        color: '#333'
    },
    roleBadge: {
        padding: '3px 10px',
        borderRadius: '20px',
        fontSize: '12px',
        fontWeight: 'bold'
    },
    empty: {
        textAlign: 'center',
        color: '#888',
        padding: '40px'
    },
    deleteButton: {
        padding: '6px 12px',
        backgroundColor: '#e74c3c',
        color: 'white',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '12px'
    }
};

export default AdminDashboard;