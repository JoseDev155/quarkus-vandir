<script setup>
import { ref, onMounted } from 'vue';
import { API_BASE_URL, API_ENDPOINTS } from '../utils/api';
import { useRouter } from 'vue-router';

const router = useRouter();
const email = ref('');
const password = ref('');
const errorMessage = ref('');
const loading = ref(false);
const rememberMe = ref(false);

const loadRememberedEmail = () => {
    const savedEmail = localStorage.getItem('vandirRememberEmail');
    if (savedEmail) {
        email.value = savedEmail;
        rememberMe.value = true;
    }
};

const handleLogin = async () => {
    loading.value = true;
    errorMessage.value = '';

    try {
        const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.AUTH_LOGIN}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                email: email.value,
                password: password.value
            })
        });

        if (!response.ok) {
            throw new Error('Correo o contraseña incorrectos');
        }

        const data = await response.json();

        localStorage.setItem('sesionVandir', JSON.stringify({
            token: data.token,
            rol: data.rol,
            email: email.value,
            name: data.name,
            userId: data.userId
        }));

        if (rememberMe.value) {
            localStorage.setItem('vandirRememberEmail', email.value);
        } else {
            localStorage.removeItem('vandirRememberEmail');
        }

        router.push({ name: 'dashboard' });

    } catch (error) {
        errorMessage.value = error.message;
    } finally {
        loading.value = false;
    }
};

onMounted(() => {
    loadRememberedEmail();
});
</script>

<template>
    <div class="login-wrapper">
        <div class="login-card">

            <div class="login-header">
                <h1>Vandir Store</h1>
                <p>Ingresa tus credenciales para continuar</p>
            </div>

            <form @submit.prevent="handleLogin" class="login-form">
                <div v-if="errorMessage" class="form-message text-danger font-bold">
                    {{ errorMessage }}
                </div>

                <div class="input-group">
                    <label for="email">Correo Electrónico</label>
                    <input type="email" id="email" v-model="email" placeholder="usuario@vandirstore.com" required :disabled="loading">
                </div>

                <div class="input-group">
                    <label for="password">Contraseña</label>
                    <input type="password" id="password" v-model="password" placeholder="••••••••" required :disabled="loading">
                </div>

                <div class="options">
                    <label class="remember-me">
                        <input type="checkbox" v-model="rememberMe" :disabled="loading">
                        Recordarme
                    </label>
                    <router-link class="forgot-password" :to="{ name: 'recovery' }">
                        ¿Olvidaste tu contraseña?
                    </router-link>
                </div>

                <button type="submit" class="btn-login" :disabled="loading">
                    {{ loading ? 'Iniciando...' : 'Iniciar Sesión' }}
                </button>
            </form>

            <div class="login-footer">
                <p>¿Necesitas acceso? Contacta al administrador.</p>
            </div>

        </div>
    </div>
</template>
