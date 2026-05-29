// src/pages/InstructorDashboard.jsx
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../api/axios';

const InstructorDashboard = () => {

    const navigate = useNavigate();

    const [courses, setCourses] = useState([]);
    const [students, setStudents] = useState([]);
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [editCourse, setEditCourse] = useState(null);
    const [editData, setEditData] = useState({
        title: '',
        description: '',
        price: ''
    });

    const name = localStorage.getItem('name');

    useEffect(() => {
        fetchMyCourses();
    }, []);

    // get instructor courses
    const fetchMyCourses = async () => {
        try {
            const res = await API.get('/courses/my-courses');
            setCourses(res.data);
        } catch (err) {
            setError('Failed to load courses');
        } finally {
            setLoading(false);
        }
    };

    // get enrolled students
    const fetchStudents = async (courseId) => {
        try {
            const res = await API.get(
                `/enrollments/course/${courseId}`);
            setStudents(res.data);
            setSelectedCourse(courseId);
        } catch (err) {
            setStudents([]);
            setSelectedCourse(courseId);
        }
    };

    // delete course
    const handleDelete = async (courseId) => {
        if (!window.confirm(
            'Are you sure you want to delete this course?' +
            ' All lessons and enrollments will be deleted.')) {
            return;
        }
        try {
            await API.delete(`/courses/${courseId}`);
            alert('Course deleted successfully');
            fetchMyCourses();
            setSelectedCourse(null);
            setStudents([]);
        } catch (err) {
            alert(
                err.response?.data?.message ||
                'Delete failed'
            );
        }
    };

    // open edit form
    const handleEditOpen = (course) => {
        setEditCourse(course.id);
        setEditData({
            title: course.title,
            description: course.description,
            price: course.price
        });
    };

    // update course
    const handleUpdate = async (e) => {
        e.preventDefault();
        try {
            await API.put(
                `/courses/${editCourse}`, editData);
            alert('Course updated successfully');
            setEditCourse(null);
            fetchMyCourses();
        } catch (err) {
            alert(
                err.response?.data?.message ||
                'Update failed'
            );
        }
    };

    return (
        <div style={styles.container}>

            {/* header */}
            <div style={styles.header}>
                <div>
                    <h2 style={styles.title}>
                        Instructor Dashboard
                    </h2>
                    <p style={styles.subtitle}>
                        Welcome, {name}!
                    </p>
                </div>
                <button
                    onClick={() =>
                        navigate('/instructor/create-course')}
                    style={styles.createButton}
                >
                    Create New Course
                </button>
            </div>

            {/* stats */}
            <div style={styles.stats}>
                <div style={styles.statCard}>
                    <h3 style={styles.statNumber}>
                        {courses.length}
                    </h3>
                    <p style={styles.statLabel}>
                        Total Courses
                    </p>
                </div>
                <div style={styles.statCard}>
                    <h3 style={styles.statNumber}>
                        {courses.reduce((total, course) =>
                            total + course.totalLessons, 0)}
                    </h3>
                    <p style={styles.statLabel}>
                        Total Lessons
                    </p>
                </div>
                <div style={styles.statCard}>
                    <h3 style={styles.statNumber}>
                        {students.length}
                    </h3>
                    <p style={styles.statLabel}>
                        Enrolled Students
                    </p>
                </div>
            </div>

            {/* loading */}
            {loading && (
                <p style={styles.loading}>Loading...</p>
            )}

            {/* error */}
            {error && (
                <p style={styles.error}>{error}</p>
            )}

            <div style={styles.layout}>

                {/* left — courses list */}
                <div style={styles.courseSection}>
                    <h3 style={styles.sectionTitle}>
                        My Courses
                    </h3>

                    {!loading && courses.length === 0 && (
                        <div style={styles.empty}>
                            <p>No courses yet</p>
                            <button
                                onClick={() => navigate(
                                    '/instructor/create-course')}
                                style={styles.createButton}
                            >
                                Create First Course
                            </button>
                        </div>
                    )}

                    {courses.map((course) => (
                        <div key={course.id}
                            style={{
                                ...styles.courseCard,
                                border: selectedCourse ===
                                    course.id
                                    ? '2px solid #1a1a2e'
                                    : '1px solid #eee'
                            }}
                        >
                            {/* edit form */}
                            {editCourse === course.id ? (
                                <form onSubmit={handleUpdate}>

                                    <div style={styles.formGroup}>
                                        <label style={styles.label}>
                                            Title
                                        </label>
                                        <input
                                            type="text"
                                            value={editData.title}
                                            onChange={(e) =>
                                                setEditData({
                                                    ...editData,
                                                    title: e.target.value
                                                })}
                                            style={styles.input}
                                            required
                                        />
                                    </div>

                                    <div style={styles.formGroup}>
                                        <label style={styles.label}>
                                            Description
                                        </label>
                                        <textarea
                                            value={editData.description}
                                            onChange={(e) =>
                                                setEditData({
                                                    ...editData,
                                                    description:
                                                        e.target.value
                                                })}
                                            style={styles.textarea}
                                            required
                                        />
                                    </div>

                                    <div style={styles.formGroup}>
                                        <label style={styles.label}>
                                            Price
                                        </label>
                                        <input
                                            type="number"
                                            value={editData.price}
                                            onChange={(e) =>
                                                setEditData({
                                                    ...editData,
                                                    price: e.target.value
                                                })}
                                            style={styles.input}
                                            required
                                        />
                                    </div>

                                    <div style={styles.formActions}>
                                        <button
                                            type="submit"
                                            style={styles.saveButton}
                                        >
                                            Save
                                        </button>
                                        <button
                                            type="button"
                                            onClick={() =>
                                                setEditCourse(null)}
                                            style={styles.cancelButton}
                                        >
                                            Cancel
                                        </button>
                                    </div>
                                </form>
                            ) : (
                                <>
                                    {/* course info */}
                                    <h4 style={styles.courseTitle}>
                                        {course.title}
                                    </h4>
                                    <p style={styles.courseDesc}>
                                        {course.description
                                            .substring(0, 80)}...
                                    </p>

                                    <div style={styles.courseMeta}>
                                        <span>
                                            Rs.{course.price}
                                        </span>
                                        <span>
                                            {course.totalLessons} Lessons
                                        </span>
                                        <span>
                                            {course.averageRating > 0
                                                ? course.averageRating
                                                    .toFixed(1) + ' Rating'
                                                : 'No ratings'}
                                        </span>
                                    </div>

                                    {/* action buttons */}
                                    <div style={styles.actions}>
                                        <button
                                            onClick={() =>
                                                fetchStudents(course.id)}
                                            style={styles.studentsButton}
                                        >
                                            View Students
                                        </button>
                                        <button
                                            onClick={() => navigate(
                                                `/instructor/add-lesson/${course.id}`
                                            )}
                                            style={styles.lessonButton}
                                        >
                                            Add Lesson
                                        </button>
                                        <button
                                            onClick={() =>
                                                handleEditOpen(course)}
                                            style={styles.editButton}
                                        >
                                            Edit
                                        </button>
                                        <button
                                            onClick={() =>
                                                handleDelete(course.id)}
                                            style={styles.deleteButton}
                                        >
                                            Delete
                                        </button>
                                    </div>
                                </>
                            )}
                        </div>
                    ))}
                </div>

                {/* right — enrolled students */}
                {selectedCourse && (
                    <div style={styles.studentSection}>
                        <h3 style={styles.sectionTitle}>
                            Enrolled Students
                        </h3>

                        {students.length === 0 ? (
                            <p style={styles.empty}>
                                No students enrolled yet
                            </p>
                        ) : (
                            students.map((enrollment) => (
                                <div
                                    key={enrollment.id}
                                    style={styles.studentCard}
                                >
                                    <div style={styles.studentInfo}>
                                        <span style={styles.studentName}>
                                            {enrollment.studentName}
                                        </span>
                                        <span style={{
                                            ...styles.statusBadge,
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
                                                ? 'Completed'
                                                : 'In Progress'}
                                        </span>
                                    </div>
                                    <p style={styles.enrollDate}>
                                        Enrolled:{' '}
                                        {new Date(enrollment.enrolledAt)
                                            .toLocaleDateString()}
                                    </p>
                                </div>
                            ))
                        )}
                    </div>
                )}
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
    createButton: {
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
    loading: {
        textAlign: 'center',
        color: '#888'
    },
    error: {
        textAlign: 'center',
        color: '#e74c3c'
    },
    layout: {
        display: 'grid',
        gridTemplateColumns: selectedCourse =>
            selectedCourse ? '1fr 1fr' : '1fr',
        gap: '24px'
    },
    courseSection: {
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
    empty: {
        textAlign: 'center',
        color: '#888',
        padding: '40px'
    },
    courseCard: {
        borderRadius: '10px',
        padding: '16px',
        marginBottom: '16px'
    },
    courseTitle: {
        fontSize: '16px',
        color: '#1a1a2e',
        margin: '0 0 8px 0'
    },
    courseDesc: {
        color: '#666',
        fontSize: '13px',
        margin: '0 0 12px 0'
    },
    courseMeta: {
        display: 'flex',
        gap: '16px',
        fontSize: '13px',
        color: '#888',
        marginBottom: '12px'
    },
    actions: {
        display: 'flex',
        gap: '8px',
        flexWrap: 'wrap'
    },
    studentsButton: {
        padding: '6px 12px',
        backgroundColor: '#1a1a2e',
        color: 'white',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '12px'
    },
    lessonButton: {
        padding: '6px 12px',
        backgroundColor: '#2e7d32',
        color: 'white',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '12px'
    },
    editButton: {
        padding: '6px 12px',
        backgroundColor: '#e65100',
        color: 'white',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '12px'
    },
    deleteButton: {
        padding: '6px 12px',
        backgroundColor: '#e74c3c',
        color: 'white',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '12px'
    },
    formGroup: {
        marginBottom: '14px'
    },
    label: {
        display: 'block',
        marginBottom: '6px',
        color: '#333',
        fontSize: '13px',
        fontWeight: 'bold'
    },
    input: {
        width: '100%',
        padding: '10px',
        borderRadius: '6px',
        border: '1px solid #ddd',
        fontSize: '14px',
        boxSizing: 'border-box'
    },
    textarea: {
        width: '100%',
        padding: '10px',
        borderRadius: '6px',
        border: '1px solid #ddd',
        fontSize: '14px',
        minHeight: '80px',
        resize: 'vertical',
        boxSizing: 'border-box'
    },
    formActions: {
        display: 'flex',
        gap: '8px'
    },
    saveButton: {
        padding: '8px 16px',
        backgroundColor: '#2e7d32',
        color: 'white',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '13px'
    },
    cancelButton: {
        padding: '8px 16px',
        backgroundColor: '#888',
        color: 'white',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '13px'
    },
    studentSection: {
        backgroundColor: 'white',
        borderRadius: '12px',
        padding: '24px',
        boxShadow: '0 2px 12px rgba(0,0,0,0.08)'
    },
    studentCard: {
        border: '1px solid #eee',
        borderRadius: '8px',
        padding: '14px',
        marginBottom: '12px'
    },
    studentInfo: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '6px'
    },
    studentName: {
        fontWeight: 'bold',
        color: '#1a1a2e',
        fontSize: '14px'
    },
    statusBadge: {
        padding: '3px 10px',
        borderRadius: '20px',
        fontSize: '12px',
        fontWeight: 'bold'
    },
    enrollDate: {
        color: '#888',
        fontSize: '12px',
        margin: 0
    }
};

export default InstructorDashboard;