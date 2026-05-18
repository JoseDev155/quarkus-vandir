<script setup>
import { useRouter } from 'vue-router';
import { onMounted } from 'vue';

const router = useRouter();

// Componente puente que decide a qué dashboard llevar al usuario según su rol
onMounted(() => {
    const sessionStr = localStorage.getItem('sesionVandir');
    if (sessionStr) {
        const session = JSON.parse(sessionStr);
        if (session.rol === 'Vendedor') {
            router.replace({ name: 'sales' });
        } else if (session.rol === 'Gerente') {
            router.replace({ name: 'manager-summary' });
        } else if (session.rol === 'Administrador') {
            router.replace({ name: 'admin-users' });
        } else {
            localStorage.removeItem('sesionVandir');
            router.replace({ name: 'login' });
        }
    } else {
        router.push({ name: 'login' });
    }
});
</script>

<template>
    <div>Cargando dashboard...</div>
</template>
