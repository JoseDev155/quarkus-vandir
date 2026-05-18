<script setup>
import { computed } from 'vue';

const session = computed(() => {
    const str = localStorage.getItem('sesionVandir');
    return str ? JSON.parse(str) : { rol: 'Invitado' };
});

const role = computed(() => session.value?.rol || 'Invitado');

const menuSectionTitle = computed(() => {
    if (role.value === 'Administrador') {
        return 'Sistema';
    }

    if (role.value === 'Gerente') {
        return 'Gestión';
    }

    if (role.value === 'Vendedor') {
        return 'Ventas';
    }

    return 'Menú';
});

const menuItems = computed(() => {
    if (role.value === 'Administrador') {
        return [
            { name: 'Usuarios y Permisos', routeName: 'admin-users', icon: '🔐' },
            { name: 'Configuración General', routeName: 'admin-config', icon: '⚙️' },
            { name: 'Respaldos de Base de Datos', routeName: 'admin-backups', icon: '💾' }
        ];
    }

    if (role.value === 'Gerente') {
        return [
            { name: 'Resumen', routeName: 'manager-summary', icon: '📊' },
            { name: 'Reportes de Ventas', routeName: 'reports', icon: '📈' },
            { name: 'Inventario Global', routeName: 'inventory', icon: '📋' },
            { name: 'Órdenes de Compra', routeName: 'orders', icon: '🛒' }
        ];
    }

    if (role.value === 'Vendedor') {
        return [
            { name: 'Nueva Venta', routeName: 'sales', icon: '🛒' },
            { name: 'Consultar Precios', routeName: 'inventory', icon: '📦' },
            { name: 'Mis Clientes', routeName: 'customers', icon: '👥' }
        ];
    }

    return [{ name: 'Inicio', routeName: 'dashboard', icon: '🏠' }];
});
</script>

<template>
    <aside class="sidebar">
        <div class="sidebar-header">
            <h2>Vandir System</h2>
            <div class="user-role">{{ role }}</div>
        </div>

        <ul class="sidebar-menu">
            <li class="menu-section">{{ menuSectionTitle }}</li>
            <li v-for="item in menuItems" :key="item.routeName">
                <router-link :to="{ name: item.routeName }">
                    <span class="menu-icon" aria-hidden="true">{{ item.icon }}</span>
                    <span>{{ item.name }}</span>
                </router-link>
            </li>
        </ul>
    </aside>
</template>
