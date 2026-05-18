<script setup>
import { ref, onMounted } from 'vue';
import { vandirFetch, API_ENDPOINTS } from '../utils/api';

const products = ref([]);
const loading = ref(true);
const error = ref('');

const fetchProducts = async () => {
    try {
        const response = await vandirFetch(API_ENDPOINTS.PRODUCTS);
        if (response.ok) {
            products.value = await response.json();
        } else {
            error.value = 'Error al cargar productos';
        }
    } catch (e) {
        error.value = e.message;
    } finally {
        loading.value = false;
    }
};

onMounted(() => {
    fetchProducts();
});
</script>

<template>
    <div class="workspace-header">
        <h2>Inventario Global</h2>
        <button class="btn btn-primary" v-if="products.length > 0">+ Nuevo Producto</button>
    </div>

    <div class="content-panel">
        <div class="search-panel">
            <input type="text" placeholder="Buscar por código o nombre...">
            <button class="btn btn-secondary">Buscar</button>
        </div>

        <div v-if="loading">Cargando datos...</div>
        <div v-else-if="error" class="text-danger">{{ error }}</div>

        <table class="data-table" v-else>
            <thead>
                <tr>
                    <th>Código</th>
                    <th>Nombre</th>
                    <th>Categoría</th>
                    <th>Stock Actual</th>
                    <th>Precio Unit.</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in products" :key="item.id">
                    <td>{{ item.code }}</td>
                    <td class="font-bold">{{ item.name }}</td>
                    <td>{{ item.categoryName || 'Sin Categoría' }}</td>
                    <td :class="{'text-danger font-bold': item.currentStock <= item.minStock}">
                        {{ item.currentStock }}
                    </td>
                    <td>${{ item.unitPrice.toFixed(2) }}</td>
                    <td>
                        <span :class="item.status === 'ACTIVE' ? 'text-success font-bold' : 'text-danger font-bold'">
                            {{ item.status }}
                        </span>
                    </td>
                    <td>
                        <button class="btn btn-secondary btn-small">Editar</button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>
