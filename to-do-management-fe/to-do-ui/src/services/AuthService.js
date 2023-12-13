import axios from "axios";

const AUTH_BASE_URL = "http://localhost:8080/api/auth";

export const registerAPICall = (registerDto) => axios.post(AUTH_BASE_URL + '/register', registerDto);

export const loginAPICall = (emailOrUsername, password) => axios.post(AUTH_BASE_URL + '/login', { emailOrUsername, password });

export const storeToken = (token) => localStorage.setItem("token", token);

export const getToken = () => localStorage.getItem("token");

export const saveLoggedInUser = (username, role) => {
    sessionStorage.setItem("authenticatedUser", username);
    sessionStorage.setItem("role", role);
}

export const isUserLoggedIn = () => {
    const username = sessionStorage.getItem("authenticatedUser");

    if (username == null) {
        return false;
    } else {
        return true;
    }
}

export const getLoggedInUser = () => {
    const username = sessionStorage.getItem("authenticatedUser");
    return username;
}

export const logout = () => {
    localStorage.clear();
    sessionStorage.clear();
}

export const isAdminUser = () => {
    let role = sessionStorage.getItem("role");

    if (role != null && role === 'ROLE_ADMIN') {
        return true;
    } else {
        return false;
    }
}