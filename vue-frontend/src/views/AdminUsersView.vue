<script setup>
import { ref, onMounted } from 'vue';
import { vandirFetch, API_ENDPOINTS } from '../utils/api';

const users = ref([]);
const loading = ref(true);
const error = ref('');

const fetchUsers = async () => {
    try {
        const response = await vandirFetch(API_ENDPOINTS.USERS);
        if (response.ok) {
            users.value = await response.json();
        } else {
            error.value = 'No se pudo cargar la lista de usuarios.';
        }
    } catch (e) {
        error.value = e.message;
    } finally {
        loading.value = false;
    }
};

const statusClass = (status) => {
    if (!status) {
        return '';
    }

    return ['ACTIVE', 'Activo'].includes(status)
        ? 'text-success font-bold'
        : 'text-danger font-bold';
};

onMounted(() => {
    fetchUsers();
});
</script>

<template>
    <div class="workspace-header">
        <h2>Usuarios y Permisos</h2>
        <button class="btn btn-primary">+ Nuevo Usuario</button>
    </div>

    <div class="content-panel">
        <div v-if="loading">Cargando usuarios...</div>
        <div v-else-if="error" class="text-danger">{{ error }}</div>

        <table v-else class="data-table">
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Correo</th>
                    <th>Rol</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.id">
                    <td>{{ user.name }}</td>
                    <td>{{ user.email }}</td>
                    <td>{{ user.role }}</td>
                    <td :class="statusClass(user.status)">{{ user.status }}</td>
                    <td>
                        <button class="btn btn-small btn-secondary">Editar</button>
                        <button class="btn btn-small btn-danger">Bloquear</button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>
