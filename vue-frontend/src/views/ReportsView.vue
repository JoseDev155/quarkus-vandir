<script setup>
import { ref, onMounted, computed } from 'vue';
import { vandirFetch, API_ENDPOINTS } from '../utils/api';

const sales = ref([]);
const loading = ref(true);
const error = ref('');
const search = ref('');

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

const filteredSales = computed(() => {
    if (!search.value) {
        return sales.value;
    }

    const term = search.value.toLowerCase();
    return sales.value.filter((sale) => {
        return `${sale.ticketCode} ${sale.customerName || ''}`.toLowerCase().includes(term);
    });
});

const fetchSales = async () => {
    try {
        const response = await vandirFetch(API_ENDPOINTS.SALES);
        if (response.ok) {
            sales.value = await response.json();
        } else {
            error.value = 'No se pudieron cargar los reportes.';
        }
    } catch (e) {
        error.value = e.message;
    } finally {
        loading.value = false;
    }
};

onMounted(() => {
    fetchSales();
});
</script>

<template>
    <div class="workspace-header">
        <h2>Reportes de Ventas</h2>
    </div>

    <div class="content-panel">
        <div class="search-panel">
            <input v-model="search" type="text" placeholder="Buscar por ticket o cliente..." />
            <button class="btn btn-secondary">Filtrar</button>
        </div>

        <div v-if="loading">Cargando reportes...</div>
        <div v-else-if="error" class="text-danger">{{ error }}</div>

        <table v-else class="data-table">
            <thead>
                <tr>
                    <th>Ticket</th>
                    <th>Cliente</th>
                    <th>Subtotal</th>
                    <th>IVA</th>
                    <th>Total</th>
                    <th>Estado</th>
                    <th>Fecha</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="sale in filteredSales" :key="sale.id">
                    <td>{{ sale.ticketCode }}</td>
                    <td>{{ sale.customerName || 'Mostrador' }}</td>
                    <td>{{ formatCurrency(sale.subtotal) }}</td>
                    <td>{{ formatCurrency(sale.totalVat) }}</td>
                    <td class="font-bold">{{ formatCurrency(sale.totalSale) }}</td>
                    <td>{{ sale.status }}</td>
                    <td>{{ formatDate(sale.saleDate) }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>
