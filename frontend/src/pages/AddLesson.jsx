// src/pages/AddLesson.jsx
import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import API from '../api/axios';

const AddLesson = () => {

    const navigate = useNavigate();
    const { courseId } = useParams();

    const [formData, setFormData] = useState({
        title: '',
        content: '',
        videoUrl: '',
        lessonOrder: ''
    });

    const [lessons, setLessons] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [editLesson, setEditLesson] = useState(null);
    const [editData, setEditData] = useState({
        title: '',
        content: '',
        videoUrl: '',
        lessonOrder: ''
    });

    useEffect(() => {
        fetchLessons();
    }, [courseId]);

    // get all lessons of this course
    const fetchLessons = async () => {
        try {
            const res = await API.get(
                `/courses/${courseId}/lessons`);
            setLessons(res.data);
        } catch (err) {
            setLessons([]);
        }
    };

    // handle input change
    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    // add lesson
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            await API.post(
                `/courses/${courseId}/lessons`,
                {
                    ...formData,
                    lessonOrder: parseInt(
                        formData.lessonOrder)
                }
            );
            alert('Lesson added successfully!');
            setFormData({
                title: '',
                content: '',
                videoUrl: '',
                lessonOrder: ''
            });
            fetchLessons();
        } catch (err) {
            setError(
                err.response?.data?.message ||
                'Failed to add lesson'
            );
        } finally {
            setLoading(false);
        }
    };

    // open edit form
    const handleEditOpen = (lesson) => {
        setEditLesson(lesson.id);
        setEditData({
            title: lesson.title,
            content: lesson.content,
            videoUrl: lesson.videoUrl || '',
            lessonOrder: lesson.lessonOrder
        });
    };

    // update lesson
    const handleUpdate = async (e) => {
        e.preventDefault();
        try {
            await API.put(
                `/courses/${courseId}/lessons/${editLesson}`,
                {
                    ...editData,
                    lessonOrder: parseInt(
                        editData.lessonOrder)
                }
            );
            alert('Lesson updated successfully!');
            setEditLesson(null);
            fetchLessons();
        } catch (err) {
            alert(
                err.response?.data?.message ||
                'Update failed'
            );
        }
    };

    // delete lesson
    const handleDelete = async (lessonId) => {
        if (!window.confirm(
            'Are you sure you want to delete this lesson?'
        )) {
            return;
        }
        try {
            await API.delete(
                `/courses/${courseId}/lessons/${lessonId}`
            );
            alert('Lesson deleted successfully!');
            fetchLessons();
        } catch (err) {
            alert(
                err.response?.data?.message ||
                'Delete failed'
            );
        }
    };

    return (
        <div style={styles.container}>

            {/* header */}
            <div style={styles.header}>
                <h2 style={styles.title}>
                    Manage Lessons
                </h2>
                <button
                    onClick={() =>
                        navigate('/instructor/dashboard')}
                    style={styles.backButton}
                >
                    Back to Dashboard
                </button>
            </div>

            <div style={styles.layout}>

                {/* left — add lesson form */}
                <div style={styles.formCard}>
                    <h3 style={styles.formTitle}>
                        Add New Lesson
                    </h3>

                    {/* error */}
                    {error && (
                        <div style={styles.error}>
                            {error}
                        </div>
                    )}

                    <form onSubmit={handleSubmit}>

                        {/* title */}
                        <div style={styles.formGroup}>
                            <label style={styles.label}>
                                Lesson Title
                            </label>
                            <input
                                type="text"
                                name="title"
                                placeholder="Enter lesson title"
                                value={formData.title}
                                onChange={handleChange}
                                style={styles.input}
                                required
                            />
                        </div>

                        {/* content */}
                        <div style={styles.formGroup}>
                            <label style={styles.label}>
                                Content
                            </label>
                            <textarea
                                name="content"
                                placeholder="Enter lesson content"
                                value={formData.content}
                                onChange={handleChange}
                                style={styles.textarea}
                                required
                            />
                        </div>

                        {/* video url */}
                        <div style={styles.formGroup}>
                            <label style={styles.label}>
                                Video URL (optional)
                            </label>
                            <input
                                type="text"
                                name="videoUrl"
                                placeholder="Enter video URL"
                                value={formData.videoUrl}
                                onChange={handleChange}
                                style={styles.input}
                            />
                        </div>

                        {/* lesson order */}
                        <div style={styles.formGroup}>
                            <label style={styles.label}>
                                Lesson Order
                            </label>
                            <input
                                type="number"
                                name="lessonOrder"
                                placeholder="Enter lesson order"
                                value={formData.lessonOrder}
                                onChange={handleChange}
                                style={styles.input}
                                min="1"
                                required
                            />
                            {lessons.length > 0 && (
                                <p style={styles.hint}>
                                    Next suggested order:{' '}
                                    {lessons.length + 1}
                                </p>
                            )}
                        </div>

                        <button
                            type="submit"
                            style={styles.submitButton}
                            disabled={loading}
                        >
                            {loading
                                ? 'Adding...'
                                : 'Add Lesson'}
                        </button>

                    </form>
                </div>

                {/* right — lessons list */}
                <div style={styles.lessonsCard}>
                    <h3 style={styles.formTitle}>
                        Course Lessons ({lessons.length})
                    </h3>

                    {lessons.length === 0 ? (
                        <p style={styles.empty}>
                            No lessons added yet
                        </p>
                    ) : (
                        lessons.map((lesson, index) => (
                            <div
                                key={lesson.id}
                                style={styles.lessonCard}
                            >
                                {/* edit form */}
                                {editLesson === lesson.id ? (
                                    <form
                                        onSubmit={handleUpdate}
                                    >
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
                                                        title:
                                                            e.target.value
                                                    })}
                                                style={styles.input}
                                                required
                                            />
                                        </div>

                                        <div style={styles.formGroup}>
                                            <label style={styles.label}>
                                                Content
                                            </label>
                                            <textarea
                                                value={editData.content}
                                                onChange={(e) =>
                                                    setEditData({
                                                        ...editData,
                                                        content:
                                                            e.target.value
                                                    })}
                                                style={styles.textarea}
                                                required
                                            />
                                        </div>

                                        <div style={styles.formGroup}>
                                            <label style={styles.label}>
                                                Video URL
                                            </label>
                                            <input
                                                type="text"
                                                value={editData.videoUrl}
                                                onChange={(e) =>
                                                    setEditData({
                                                        ...editData,
                                                        videoUrl:
                                                            e.target.value
                                                    })}
                                                style={styles.input}
                                            />
                                        </div>

                                        <div style={styles.formGroup}>
                                            <label style={styles.label}>
                                                Lesson Order
                                            </label>
                                            <input
                                                type="number"
                                                value={editData.lessonOrder}
                                                onChange={(e) =>
                                                    setEditData({
                                                        ...editData,
                                                        lessonOrder:
                                                            e.target.value
                                                    })}
                                                style={styles.input}
                                                min="1"
                                                required
                                            />
                                        </div>

                                        <div style={styles.editActions}>
                                            <button
                                                type="submit"
                                                style={styles.saveButton}
                                            >
                                                Save
                                            </button>
                                            <button
                                                type="button"
                                                onClick={() =>
                                                    setEditLesson(null)}
                                                style={styles.cancelButton}
                                            >
                                                Cancel
                                            </button>
                                        </div>
                                    </form>
                                ) : (
                                    <>
                                        {/* lesson info */}
                                        <div style={styles.lessonTop}>
                                            <span style={styles.orderBadge}>
                                                {lesson.lessonOrder}
                                            </span>
                                            <h4 style={styles.lessonTitle}>
                                                {lesson.title}
                                            </h4>
                                        </div>

                                        <p style={styles.lessonContent}>
                                            {lesson.content
                                                .substring(0, 80)}...
                                        </p>

                                        {lesson.videoUrl && (
                                            <p style={styles.videoUrl}>
                                                Video: {lesson.videoUrl}
                                            </p>
                                        )}

                                        {/* action buttons */}
                                        <div style={styles.lessonActions}>
                                            <button
                                                onClick={() =>
                                                    handleEditOpen(lesson)}
                                                style={styles.editButton}
                                            >
                                                Edit
                                            </button>
                                            <button
                                                onClick={() =>
                                                    handleDelete(lesson.id)}
                                                style={styles.deleteButton}
                                            >
                                                Delete
                                            </button>
                                        </div>
                                    </>
                                )}
                            </div>
                        ))
                    )}
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
        fontSize: '26px',
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
    layout: {
        display: 'grid',
        gridTemplateColumns: '1fr 1fr',
        gap: '24px',
        alignItems: 'start'
    },
    formCard: {
        backgroundColor: 'white',
        borderRadius: '12px',
        padding: '28px',
        boxShadow: '0 2px 12px rgba(0,0,0,0.08)'
    },
    lessonsCard: {
        backgroundColor: 'white',
        borderRadius: '12px',
        padding: '28px',
        boxShadow: '0 2px 12px rgba(0,0,0,0.08)'
    },
    formTitle: {
        fontSize: '18px',
        color: '#1a1a2e',
        marginBottom: '20px'
    },
    error: {
        backgroundColor: '#ffe0e0',
        color: '#e74c3c',
        padding: '12px',
        borderRadius: '8px',
        marginBottom: '16px',
        fontSize: '14px'
    },
    formGroup: {
        marginBottom: '16px'
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
        padding: '10px 12px',
        borderRadius: '8px',
        border: '1px solid #ddd',
        fontSize: '14px',
        outline: 'none',
        boxSizing: 'border-box'
    },
    textarea: {
        width: '100%',
        padding: '10px 12px',
        borderRadius: '8px',
        border: '1px solid #ddd',
        fontSize: '14px',
        outline: 'none',
        minHeight: '120px',
        resize: 'vertical',
        boxSizing: 'border-box'
    },
    hint: {
        color: '#888',
        fontSize: '12px',
        margin: '4px 0 0 0'
    },
    submitButton: {
        width: '100%',
        padding: '12px',
        backgroundColor: '#1a1a2e',
        color: 'white',
        border: 'none',
        borderRadius: '8px',
        fontSize: '15px',
        cursor: 'pointer',
        marginTop: '8px'
    },
    empty: {
        textAlign: 'center',
        color: '#888',
        padding: '30px'
    },
    lessonCard: {
        border: '1px solid #eee',
        borderRadius: '10px',
        padding: '16px',
        marginBottom: '14px'
    },
    lessonTop: {
        display: 'flex',
        alignItems: 'center',
        gap: '12px',
        marginBottom: '8px'
    },
    orderBadge: {
        width: '28px',
        height: '28px',
        borderRadius: '50%',
        backgroundColor: '#1a1a2e',
        color: 'white',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        fontSize: '13px',
        fontWeight: 'bold',
        flexShrink: 0
    },
    lessonTitle: {
        fontSize: '15px',
        color: '#1a1a2e',
        margin: 0
    },
    lessonContent: {
        color: '#666',
        fontSize: '13px',
        margin: '0 0 8px 0'
    },
    videoUrl: {
        color: '#00d4ff',
        fontSize: '12px',
        margin: '0 0 8px 0',
        wordBreak: 'break-all'
    },
    lessonActions: {
        display: 'flex',
        gap: '8px'
    },
    editButton: {
        padding: '6px 14px',
        backgroundColor: '#e65100',
        color: 'white',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '12px'
    },
    deleteButton: {
        padding: '6px 14px',
        backgroundColor: '#e74c3c',
        color: 'white',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '12px'
    },
    editActions: {
        display: 'flex',
        gap: '8px',
        marginTop: '8px'
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
    }
};

export default AddLesson;