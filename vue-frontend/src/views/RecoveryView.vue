<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { vandirFetch, API_ENDPOINTS } from '../utils/api';

const router = useRouter();
const email = ref('');
const errorMessage = ref('');
const successMessage = ref('');
const loading = ref(false);

const readResponseMessage = async (response) => {
    const fallback = 'Si el correo existe, recibiras instrucciones de recuperacion.';

    try {
        const text = await response.text();
        if (!text) {
            return fallback;
        }

        try {
            const data = JSON.parse(text);
            return data?.message || text;
        } catch (e) {
            return text;
        }
    } catch (e) {
        return fallback;
    }
};

const handleRecovery = async () => {
    loading.value = true;
    errorMessage.value = '';
    successMessage.value = '';

    if (!email.value.trim()) {
        errorMessage.value = 'Ingresa un correo valido para continuar.';
        loading.value = false;
        return;
    }

    try {
        const response = await vandirFetch(API_ENDPOINTS.AUTH_RECOVERY, {
            method: 'POST',
            body: JSON.stringify({
                email: email.value.trim()
            })
        });

        if (response.ok) {
            successMessage.value = await readResponseMessage(response);
        } else {
            errorMessage.value = await readResponseMessage(response);
        }
    } catch (e) {
        errorMessage.value = e.message;
    } finally {
        loading.value = false;
    }
};

const goBack = () => {
    router.push({ name: 'login' });
};
</script>

<template>
    <div class="login-wrapper">
        <div class="login-card">
            <div class="login-header">
                <h1>Recuperar acceso</h1>
                <p>Ingresa tu correo para recibir instrucciones</p>
            </div>

            <form @submit.prevent="handleRecovery" class="login-form">
                <div v-if="errorMessage" class="form-message text-danger font-bold">
                    {{ errorMessage }}
                </div>
                <div v-if="successMessage" class="form-message text-success font-bold">
                    {{ successMessage }}
                </div>

                <div class="input-group">
                    <label for="recoveryEmail">Correo Electronico</label>
                    <input
                        id="recoveryEmail"
                        type="email"
                        v-model="email"
                        placeholder="usuario@vandirstore.com"
                        required
                        :disabled="loading"
                    />
                </div>

                <button type="submit" class="btn-login" :disabled="loading">
                    {{ loading ? 'Enviando...' : 'Enviar instrucciones' }}
                </button>
            </form>

            <div class="recovery-actions">
                <button type="button" class="btn btn-secondary btn-small" @click="goBack">
                    Volver al inicio de sesion
                </button>
            </div>

            <div class="login-footer">
                <p>¿Necesitas acceso? Contacta al administrador.</p>
            </div>
        </div>
    </div>
</template>
