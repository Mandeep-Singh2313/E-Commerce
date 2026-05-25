import axios from "axios";

// Helper function to extract token from auth data
const getTokenFromAuth = () => {
    try {
        const authData = localStorage.getItem("auth");
        
        if (!authData) {
            console.log("❌ No auth data in localStorage");
            return null;
        }
        
        const parsed = JSON.parse(authData);
        
        let token = parsed.jwtToken || parsed.token || parsed.accessToken;
        
        if (!token || typeof token !== 'string') {
            console.log("❌ No valid token found");
            return null;
        }
        
        // Clean up the token - extract just the JWT part
        if (token.includes(';')) {
            token = token.split(';')[0];
        }
        if (token.includes('=')) {
            const parts = token.split('=');
            token = parts[parts.length - 1]; // Get the last part after = (handles multiple = signs)
        }
        
        console.log("✅ Token extracted successfully");
        return token;
    } catch (error) {
        console.error("❌ Error extracting token:", error);
        return null;
    }
};

const api = axios.create({
    baseURL: `${import.meta.env.VITE_BACK_END_URL}/api`,
    withCredentials: true,
    headers: {
        'Access-Control-Allow-Origin': '*',
    }
});

// Add request interceptor to include auth token
api.interceptors.request.use(
    (config) => {
        const token = getTokenFromAuth();
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Add response interceptor for better error logging
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            console.error("⚠️ 401 Unauthorized - Authentication failed");
            console.error("Check if:");
            console.error("1. Token is still valid (not expired)");
            console.error("2. Backend expects token in Authorization header");
            console.error("3. Token format is correct (should be just JWT, no cookie metadata)");
            console.error("Response:", error.response?.data);
        }
        return Promise.reject(error);
    }
);

export default api;
export { getTokenFromAuth };