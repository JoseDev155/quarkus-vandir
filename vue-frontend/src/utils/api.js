export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '';

export const API_ENDPOINTS = {
    AUTH_LOGIN: '/auth/login',
    AUTH_RECOVERY: '/auth/recovery',
    USERS: '/users',
    CONFIG: '/config',
    CONFIG_BACKUPS: '/config/backups',
    PRODUCTS: '/products',
    PRODUCTS_LOW_STOCK: '/products/low-stock',
    ORDERS: '/orders',
    CUSTOMERS: '/customers',
    SALES: '/sales',
    CATEGORIES: '/categories',
    PROVIDERS: '/providers'
};

export const vandirFetch = async (endpoint, options = {}) => {
    const sessionStr = localStorage.getItem('sesionVandir');
    let token = '';

    if (sessionStr) {
        try {
            const session = JSON.parse(sessionStr);
            token = session.token;
        } catch (e) {
            console.error("Session parse error");
        }
    }

    const defaultHeaders = {
        'Content-Type': 'application/json',
    };

    if (token) {
        defaultHeaders['Authorization'] = `Bearer ${token}`;
    }

    const config = {
        ...options,
        headers: {
            ...defaultHeaders,
            ...options.headers
        }
    };

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, config);

        if (response.status === 401 || response.status === 403) {
            localStorage.removeItem('sesionVandir');
            window.location.href = '/login';
            throw new Error('Sesión expirada o no autorizada');
        }

        return response;
    } catch (error) {
        throw error;
    }
};
