// src/App.jsx
import React from 'react';
import { BrowserRouter, Routes, Route, Navigate }
    from 'react-router-dom';

import Login from './pages/Login';
import Register from './pages/Register';
import Home from './pages/Home';
import StudentDashboard from './pages/StudentDashboard';
import Lessons from './pages/Lessons';
import InstructorDashboard from './pages/InstructorDashboard';
import CreateCourse from './pages/CreateCourse';
import AddLesson from './pages/AddLesson';
import AdminDashboard from './pages/AdminDashboard';
import Navbar from './components/Navbar';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
    return (
        <BrowserRouter>
            <Navbar />
            <Routes>

                {/* public routes */}
                <Route path="/"
                    element={<Home />} />
                <Route path="/login"
                    element={<Login />} />
                <Route path="/register"
                    element={<Register />} />

                {/* student routes */}
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

                {/* instructor routes */}
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
                <Route path="/instructor/add-lesson/:courseId"
                    element={
                        <ProtectedRoute role="INSTRUCTOR">
                            <AddLesson />
                        </ProtectedRoute>
                    }
                />

                {/* admin routes */}
                <Route path="/admin/dashboard"
                    element={
                        <ProtectedRoute role="ADMIN">
                            <AdminDashboard />
                        </ProtectedRoute>
                    }
                />

                {/* redirect unknown routes */}
                <Route path="*"
                    element={<Navigate to="/" />} />

            </Routes>
        </BrowserRouter>
    );
}

export default App;