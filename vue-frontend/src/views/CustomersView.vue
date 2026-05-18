<script setup>
import { ref, onMounted } from 'vue';
import { vandirFetch, API_ENDPOINTS } from '../utils/api';

const customers = ref([]);
const loading = ref(true);
const error = ref('');

const fetchCustomers = async () => {
    try {
        const response = await vandirFetch(API_ENDPOINTS.CUSTOMERS);
        if (response.ok) {
            customers.value = await response.json();
        } else {
            error.value = 'No se pudo cargar la lista de clientes.';
        }
    } catch (e) {
        error.value = e.message;
    } finally {
        loading.value = false;
    }
};

onMounted(() => {
    fetchCustomers();
});
</script>

<template>
    <div class="workspace-header">
        <h2>Mis Clientes</h2>
        <button class="btn btn-primary">+ Nuevo Cliente</button>
    </div>

    <div class="content-panel">
        <div v-if="loading">Cargando clientes...</div>
        <div v-else-if="error" class="text-danger">{{ error }}</div>

        <table v-else class="data-table">
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Telefono</th>
                    <th>Correo</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="customer in customers" :key="customer.id">
                    <td>{{ customer.name }}</td>
                    <td>{{ customer.phone }}</td>
                    <td>{{ customer.email }}</td>
                    <td>
                        <button class="btn btn-small btn-secondary">Editar</button>
                        <button class="btn btn-small btn-danger">Eliminar</button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>
