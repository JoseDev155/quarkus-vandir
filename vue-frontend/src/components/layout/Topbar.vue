<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const session = computed(() => {
    const str = localStorage.getItem('sesionVandir');
    return str ? JSON.parse(str) : null;
});

const greeting = computed(() => {
    if (!session.value) {
        return 'Hola, Usuario';
    }

    const email = session.value.email;
    const name = session.value.name;
    const role = session.value.rol;
    const displayName = name || email;
    const normalizedRole = role ? role.toLowerCase() : '';

    if (normalizedRole === 'administrador') {
        return displayName ? `Hola, ${displayName}` : 'Hola, Administrador';
    }

    if (normalizedRole === 'gerente') {
        return displayName ? `Hola, ${displayName} (Gerencia)` : 'Hola, Gerencia';
    }

    if (normalizedRole === 'vendedor') {
        return displayName ? `Hola, ${displayName} (Caja 1)` : 'Hola, Caja 1';
    }

    if (displayName) {
        return `Hola, ${displayName}`;
    }

    if (role) {
        return `Hola, ${role}`;
    }

    return 'Hola, Usuario';
});

const handleLogout = () => {
    localStorage.removeItem('sesionVandir');
    router.push({ name: 'login' });
};
</script>

<template>
    <div class="topbar">
        <div class="user-profile">
            <span>{{ greeting }}</span>
        </div>
        <button class="logout-btn" @click="handleLogout">Salir</button>
    </div>
</template>
