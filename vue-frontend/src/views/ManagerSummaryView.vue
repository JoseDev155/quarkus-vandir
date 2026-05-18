<script setup>
import { ref, onMounted, computed } from 'vue';
import { vandirFetch, API_ENDPOINTS } from '../utils/api';

const sales = ref([]);
const lowStock = ref([]);
const loading = ref(true);
const error = ref('');

const formatCurrency = (value) => {
    if (value === null || value === undefined) {
        return '$0.00';
    }

    const number = Number(value);
    return Number.isNaN(number) ? '$0.00' : `$${number.toFixed(2)}`;
};

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

const totalSales = computed(() => {
    return sales.value.reduce((acc, item) => acc + Number(item.totalSale || 0), 0);
});

const recentSales = computed(() => {
    return [...sales.value]
        .sort((a, b) => new Date(b.saleDate) - new Date(a.saleDate))
        .slice(0, 5);
});

const loadSummary = async () => {
    try {
        const [salesResponse, lowStockResponse] = await Promise.all([
            vandirFetch(API_ENDPOINTS.SALES),
            vandirFetch(API_ENDPOINTS.PRODUCTS_LOW_STOCK)
        ]);

        if (salesResponse.ok) {
            sales.value = await salesResponse.json();
        } else {
            error.value = 'No se pudieron cargar las ventas recientes.';
        }

        if (lowStockResponse.ok) {
            lowStock.value = await lowStockResponse.json();
        }
    } catch (e) {
        error.value = e.message;
    } finally {
        loading.value = false;
    }
};

onMounted(() => {
    loadSummary();
});
</script>

<template>
    <div class="workspace-header">
        <h2>Resumen del Dia</h2>
    </div>

    <div v-if="loading">Cargando datos...</div>
    <div v-else-if="error" class="text-danger">{{ error }}</div>

    <div v-else>
        <div class="stats-grid">
            <div class="stat-card">
                <h3>Ventas Totales</h3>
                <p class="stat-number">{{ formatCurrency(totalSales) }}</p>
            </div>
            <div class="stat-card">
                <h3>Transacciones</h3>
                <p class="stat-number">{{ sales.length }}</p>
            </div>
            <div class="stat-card">
                <h3 class="text-danger">Alertas de Stock</h3>
                <p class="stat-number alert">{{ lowStock.length }} Productos</p>
            </div>
        </div>

        <h2>Ultimas Ventas Realizadas</h2>
        <table class="data-table">
            <thead>
                <tr>
                    <th>Ticket</th>
                    <th>Cliente</th>
                    <th>Total</th>
                    <th>Fecha</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="sale in recentSales" :key="sale.id">
                    <td>{{ sale.ticketCode }}</td>
                    <td>{{ sale.customerName || 'Mostrador' }}</td>
                    <td>{{ formatCurrency(sale.totalSale) }}</td>
                    <td>{{ formatDate(sale.saleDate) }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>
