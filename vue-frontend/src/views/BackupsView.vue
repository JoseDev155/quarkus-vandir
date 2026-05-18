<script setup>
import { ref, onMounted } from 'vue';
import { vandirFetch, API_ENDPOINTS } from '../utils/api';

const backups = ref([]);
const loading = ref(true);
const error = ref('');

const formatDate = (value) => {
    if (!value) {
        return 'N/D';
    }

    const date = new Date(value);
    if (Number.isNaN(date.getTime())) {
        return value;
    }

    return date.toLocaleString();
};

const formatSize = (value) => {
    if (value === null || value === undefined) {
        return 'N/D';
    }

    return `${Number(value).toFixed(2)} MB`;
};

const fetchBackups = async () => {
    try {
        const response = await vandirFetch(API_ENDPOINTS.CONFIG_BACKUPS);
        if (response.ok) {
            backups.value = await response.json();
        } else {
            error.value = 'No se pudo cargar el historial de respaldos.';
        }
    } catch (e) {
        error.value = e.message;
    } finally {
        loading.value = false;
    }
};

onMounted(() => {
    fetchBackups();
});
</script>

<template>
    <div class="workspace-header">
        <h2>Respaldos de Base de Datos</h2>
    </div>

    <div class="content-panel">
        <div v-if="loading">Cargando respaldos...</div>
        <div v-else-if="error" class="text-danger">{{ error }}</div>

        <table v-else class="data-table">
            <thead>
                <tr>
                    <th>Archivo</th>
                    <th>Tipo</th>
                    <th>Generado</th>
                    <th>Tamano</th>
                    <th>Responsable</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="backup in backups" :key="backup.id">
                    <td>{{ backup.fileName }}</td>
                    <td>{{ backup.type }}</td>
                    <td>{{ formatDate(backup.generatedAt) }}</td>
                    <td>{{ formatSize(backup.sizeMb) }}</td>
                    <td>{{ backup.generatedByName || 'N/D' }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>
