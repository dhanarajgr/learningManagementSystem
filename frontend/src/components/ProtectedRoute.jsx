// src/components/ProtectedRoute.jsx
import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children, role }) => {

    const token = localStorage.getItem('token');
    const userRole = localStorage.getItem('role');

    // not logged in
    if (!token) {
        return <Navigate to="/login" />;
    }

    // wrong role
    if (role && userRole !== role) {
        return <Navigate to="/" />;
    }

    // correct role — show page
    return children;
};

export default ProtectedRoute;