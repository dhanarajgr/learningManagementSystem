// src/App.jsx
import React from 'react';
import { BrowserRouter, Routes, Route, Navigate }
    from 'react-router-dom';

import Home from './pages/Home';
import Navbar from './components/Navbar';
import ProtectedRoute from './components/ProtectedRoute';

// student pages
import StudentLogin from './pages/StudentLogin';
import StudentRegister from './pages/StudentRegister';
import StudentDashboard from './pages/StudentDashboard';
import Lessons from './pages/Lessons';

// instructor pages
import InstructorLogin from './pages/InstructorLogin';
import InstructorRegister from './pages/InstructorRegister';
import InstructorDashboard from './pages/InstructorDashboard';
import CreateCourse from './pages/CreateCourse';
import AddLesson from './pages/AddLesson';

// admin pages
import AdminLogin from './pages/AdminLogin';
import AdminDashboard from './pages/AdminDashboard';

function App() {
    return (
        <BrowserRouter>
            <Navbar />
            <Routes>

                {/* ── PUBLIC ──────────────────────── */}
                <Route path="/"
                    element={<Home />} />

                {/* ── STUDENT ─────────────────────── */}
                <Route path="/student/register"
                    element={<StudentRegister />} />
                <Route path="/student/login"
                    element={<StudentLogin />} />
                <Route path="/student/dashboard"
                    element={
                        <ProtectedRoute role="STUDENT">
                            <StudentDashboard />
                        </ProtectedRoute>
                    }
                />
                <Route path="/student/lessons/:courseId"
                    element={
                        <ProtectedRoute role="STUDENT">
                            <Lessons />
                        </ProtectedRoute>
                    }
                />

                {/* ── INSTRUCTOR ──────────────────── */}
                <Route path="/instructor"
                    element={<Navigate to=
                        "/instructor/register" />}
                />
                <Route path="/instructor/register"
                    element={<InstructorRegister />} />
                <Route path="/instructor/login"
                    element={<InstructorLogin />} />
                <Route path="/instructor/dashboard"
                    element={
                        <ProtectedRoute role="INSTRUCTOR">
                            <InstructorDashboard />
                        </ProtectedRoute>
                    }
                />
                <Route path="/instructor/create-course"
                    element={
                        <ProtectedRoute role="INSTRUCTOR">
                            <CreateCourse />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/instructor/add-lesson/:courseId"
                    element={
                        <ProtectedRoute role="INSTRUCTOR">
                            <AddLesson />
                        </ProtectedRoute>
                    }
                />

                {/* ── ADMIN ───────────────────────── */}
                <Route path="/admin"
                    element={<AdminLogin />} />
                <Route path="/admin/dashboard"
                    element={
                        <ProtectedRoute role="ADMIN">
                            <AdminDashboard />
                        </ProtectedRoute>
                    }
                />

                {/* redirect unknown */}
                <Route path="*"
                    element={<Navigate to="/" />} />

            </Routes>
        </BrowserRouter>
    );
}

export default App;