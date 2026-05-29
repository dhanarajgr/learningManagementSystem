// src/pages/Lessons.jsx
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import API from '../api/axios';

const Lessons = () => {

    const { courseId } = useParams();
    const navigate = useNavigate();

    const [lessons, setLessons] = useState([]);
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [activeLesson, setActiveLesson] = useState(null);
    const [showReviewForm, setShowReviewForm] = useState(false);
    const [reviewData, setReviewData] = useState({
        rating: 5,
        comment: ''
    });

    useEffect(() => {
        fetchLessons();
        fetchReviews();
    }, [courseId]);

    const fetchLessons = async () => {
        try {
            const res = await API.get(
                `/courses/${courseId}/lessons`);
            setLessons(res.data);
            if (res.data.length > 0) {
                setActiveLesson(res.data[0]);
            }
        } catch (err) {
            setError(
                err.response?.data?.message ||
                'Failed to load lessons'
            );
        } finally {
            setLoading(false);
        }
    };

    const fetchReviews = async () => {
        try {
            const res = await API.get(
                `/reviews/course/${courseId}`);
            setReviews(res.data);
        } catch (err) {
            setReviews([]);
        }
    };

    const handleReviewSubmit = async (e) => {
        e.preventDefault();
        try {
            await API.post(
                `/reviews/course/${courseId}`,
                reviewData);
            alert('Review submitted successfully!');
            setShowReviewForm(false);
            fetchReviews();
        } catch (err) {
            alert(
                err.response?.data?.message ||
                'Failed to submit review'
            );
        }
    };

    const goToPrevious = () => {
        const index = lessons.indexOf(activeLesson);
        if (index > 0) {
            setActiveLesson(lessons[index - 1]);
        }
    };

    const goToNext = () => {
        const index = lessons.indexOf(activeLesson);
        if (index < lessons.length - 1) {
            setActiveLesson(lessons[index + 1]);
        }
    };

    const isFirst = () => {
        return lessons.indexOf(activeLesson) === 0;
    };

    const isLast = () => {
        return lessons.indexOf(activeLesson) ===
            lessons.length - 1;
    };

    return (
        <div style={styles.container}>

            {/* back button */}
            <button
                onClick={() =>
                    navigate('/student/dashboard')}
                style={styles.backButton}
            >
                Back to Dashboard
            </button>

            {/* loading */}
            {loading && (
                <p style={styles.loading}>
                    Loading lessons...
                </p>
            )}

            {/* error */}
            {error && (
                <div style={styles.error}>
                    {error}
                </div>
            )}

            {!loading && !error && (
                <div style={styles.layout}>

                    {/* left sidebar — lesson list */}
                    <div style={styles.sidebar}>
                        <h3 style={styles.sidebarTitle}>
                            Course Lessons
                        </h3>

                        {lessons.length === 0 && (
                            <p style={styles.empty}>
                                No lessons yet
                            </p>
                        )}

                        {lessons.map((lesson, index) => (
                            <div
                                key={lesson.id}
                                onClick={() =>
                                    setActiveLesson(lesson)}
                                style={{
                                    ...styles.lessonItem,
                                    backgroundColor:
                                        activeLesson?.id ===
                                            lesson.id
                                            ? '#1a1a2e'
                                            : 'white',
                                    color:
                                        activeLesson?.id ===
                                            lesson.id
                                            ? 'white'
                                            : '#333'
                                }}
                            >
                                <span style={styles.lessonNumber}>
                                    {index + 1}
                                </span>
                                <span style={styles.lessonTitle}>
                                    {lesson.title}
                                </span>
                            </div>
                        ))}
                    </div>

                    {/* right — lesson content */}
                    <div style={styles.content}>

                        {activeLesson ? (
                            <div>

                                {/* lesson title */}
                                <h2 style={styles.lessonHeading}>
                                    {activeLesson.title}
                                </h2>

                                {/* video */}
                                {activeLesson.videoUrl && (
                                    <div style={styles.videoBox}>
                                        <a
                                            href={activeLesson.videoUrl}
                                            target="_blank"
                                            rel="noreferrer"
                                            style={styles.videoLink}
                                        >
                                            Watch Video
                                        </a>
                                    </div>
                                )}

                        {/* content */}
                        <div style={styles.lessonContent}>
                            <h4 style={styles.contentTitle}>
                                Lesson Content
                            </h4>
                            <p style={styles.contentText}>
                                {activeLesson.content}
                            </p>
                        </div>

                        {/* navigation */}
                        <div style={styles.navigation}>
                            <button
                                onClick={goToPrevious}
                                disabled={isFirst()}
                                style={{
                                    ...styles.navButton,
                                    opacity: isFirst()
                                        ? 0.5 : 1
                                }}
                            >
                                Previous
                            </button>

                            <span style={styles.pageInfo}>
                                {lessons.indexOf(
                                    activeLesson) + 1
                                } / {lessons.length}
                            </span>

                            <button
                                onClick={goToNext}
                                disabled={isLast()}
                                style={{
                                    ...styles.navButton,
                                    opacity: isLast()
                                        ? 0.5 : 1
                                }}
                            >
                                Next
                            </button>
                        </div>

                    </div>
                    ) : (
                    <p style={styles.empty}>
                        Select a lesson to view
                    </p>
                        )}

                    {/* reviews section */}
                    <div style={styles.reviewSection}>

                        <div style={styles.reviewHeader}>
                            <h3 style={styles.reviewTitle}>
                                Course Reviews
                            </h3>
                            <button
                                onClick={() =>
                                    setShowReviewForm(
                                        !showReviewForm)}
                                style={styles.addReviewButton}
                            >
                                {showReviewForm
                                    ? 'Cancel'
                                    : 'Add Review'}
                            </button>
                        </div>

                        {/* review form */}
                        {showReviewForm && (
                            <form
                                onSubmit={handleReviewSubmit}
                                style={styles.reviewForm}
                            >
                                <div style={styles.formGroup}>
                                    <label style={styles.label}>
                                        Rating (1-5)
                                    </label>
                                    <select
                                        value={reviewData.rating}
                                        onChange={(e) =>
                                            setReviewData({
                                                ...reviewData,
                                                rating: parseInt(
                                                    e.target.value)
                                            })}
                                        style={styles.select}
                                    >
                                        {[1, 2, 3, 4, 5].map(n => (
                                            <option
                                                key={n}
                                                value={n}
                                            >
                                                {n} Star
                                            </option>
                                        ))}
                                    </select>
                                </div>

                                <div style={styles.formGroup}>
                                    <label style={styles.label}>
                                        Comment
                                    </label>
                                    <textarea
                                        placeholder="Write your review..."
                                        value={reviewData.comment}
                                        onChange={(e) =>
                                            setReviewData({
                                                ...reviewData,
                                                comment:
                                                    e.target.value
                                            })}
                                        style={styles.textarea}
                                        required
                                    />
                                </div>

                                <button
                                    type="submit"
                                    style={styles.submitButton}
                                >
                                    Submit Review
                                </button>
                            </form>
                        )}

                        {/* reviews list */}
                        {reviews.length === 0 ? (
                            <p style={styles.empty}>
                                No reviews yet.
                                Be the first to review!
                            </p>
                        ) : (
                            reviews.map((review) => (
                                <div
                                    key={review.id}
                                    style={styles.reviewCard}
                                >
                                    <div style={styles.reviewTop}>
                                        <span style={styles.reviewer}>
                                            {review.studentName}
                                        </span>
                                        <span style={styles.rating}>
                                            {Array(review.rating)
                                                .fill('Star')
                                                .join(' ')}
                                            {' '}
                                            ({review.rating}/5)
                                        </span>
                                    </div>
                                    <p style={styles.comment}>
                                        {review.comment}
                                    </p>
                                </div>
                            ))
                        )}
                    </div>
                </div>
                </div>
    )
}
        </div >
    );
};

const styles = {
    container: {
        minHeight: '100vh',
        backgroundColor: '#f0f2f5',
        padding: '20px 30px'
    },
    backButton: {
        backgroundColor: 'transparent',
        border: '1px solid #1a1a2e',
        color: '#1a1a2e',
        padding: '8px 16px',
        borderRadius: '6px',
        cursor: 'pointer',
        marginBottom: '20px',
        fontSize: '14px'
    },
    loading: {
        textAlign: 'center',
        color: '#888',
        marginTop: '50px'
    },
    error: {
        textAlign: 'center',
        color: '#e74c3c',
        backgroundColor: '#ffe0e0',
        padding: '16px',
        borderRadius: '8px',
        marginTop: '20px'
    },
    layout: {
        display: 'grid',
        gridTemplateColumns: '280px 1fr',
        gap: '24px',
        alignItems: 'start'
    },
    sidebar: {
        backgroundColor: 'white',
        borderRadius: '12px',
        padding: '20px',
        boxShadow: '0 2px 12px rgba(0,0,0,0.08)'
    },
    sidebarTitle: {
        fontSize: '16px',
        color: '#1a1a2e',
        marginBottom: '16px'
    },
    lessonItem: {
        display: 'flex',
        alignItems: 'center',
        gap: '12px',
        padding: '12px',
        borderRadius: '8px',
        cursor: 'pointer',
        marginBottom: '8px'
    },
    lessonNumber: {
        width: '24px',
        height: '24px',
        borderRadius: '50%',
        backgroundColor: '#00d4ff',
        color: '#1a1a2e',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        fontSize: '12px',
        fontWeight: 'bold',
        flexShrink: 0
    },
    lessonTitle: {
        fontSize: '14px'
    },
    content: {
        backgroundColor: 'white',
        borderRadius: '12px',
        padding: '28px',
        boxShadow: '0 2px 12px rgba(0,0,0,0.08)'
    },
    lessonHeading: {
        fontSize: '24px',
        color: '#1a1a2e',
        marginBottom: '20px'
    },
    videoBox: {
        backgroundColor: '#1a1a2e',
        borderRadius: '8px',
        padding: '20px',
        textAlign: 'center',
        marginBottom: '24px'
    },
    videoLink: {
        color: '#00d4ff',
        textDecoration: 'none',
        fontSize: '16px',
        fontWeight: 'bold'
    },
    lessonContent: {
        backgroundColor: '#f8f9fa',
        borderRadius: '8px',
        padding: '20px',
        marginBottom: '24px'
    },
    contentTitle: {
        color: '#1a1a2e',
        marginBottom: '12px'
    },
    contentText: {
        color: '#555',
        lineHeight: '1.7',
        fontSize: '15px'
    },
    navigation: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '30px'
    },
    navButton: {
        padding: '10px 20px',
        backgroundColor: '#1a1a2e',
        color: 'white',
        border: 'none',
        borderRadius: '8px',
        cursor: 'pointer',
        fontSize: '14px'
    },
    pageInfo: {
        color: '#888',
        fontSize: '14px'
    },
    reviewSection: {
        borderTop: '1px solid #eee',
        paddingTop: '24px'
    },
    reviewHeader: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '16px'
    },
    reviewTitle: {
        fontSize: '18px',
        color: '#1a1a2e',
        margin: 0
    },
    addReviewButton: {
        padding: '8px 16px',
        backgroundColor: '#1a1a2e',
        color: 'white',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '13px'
    },
    reviewForm: {
        backgroundColor: '#f8f9fa',
        padding: '20px',
        borderRadius: '8px',
        marginBottom: '20px'
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
    select: {
        width: '100%',
        padding: '10px',
        borderRadius: '6px',
        border: '1px solid #ddd',
        fontSize: '14px'
    },
    textarea: {
        width: '100%',
        padding: '10px',
        borderRadius: '6px',
        border: '1px solid #ddd',
        fontSize: '14px',
        minHeight: '100px',
        resize: 'vertical',
        boxSizing: 'border-box'
    },
    submitButton: {
        padding: '10px 20px',
        backgroundColor: '#2e7d32',
        color: 'white',
        border: 'none',
        borderRadius: '6px',
        cursor: 'pointer',
        fontSize: '14px'
    },
    reviewCard: {
        border: '1px solid #eee',
        borderRadius: '8px',
        padding: '16px',
        marginBottom: '12px'
    },
    reviewTop: {
        display: 'flex',
        justifyContent: 'space-between',
        marginBottom: '8px'
    },
    reviewer: {
        fontWeight: 'bold',
        color: '#1a1a2e',
        fontSize: '14px'
    },
    rating: {
        color: '#f59e0b',
        fontSize: '14px'
    },
    comment: {
        color: '#555',
        fontSize: '14px',
        margin: 0
    },
    empty: {
        color: '#888',
        textAlign: 'center',
        padding: '20px'
    }
};

export default Lessons;