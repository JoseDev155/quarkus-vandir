<script setup>
import { RouterView } from 'vue-router';
import Sidebar from './Sidebar.vue';
import Topbar from './Topbar.vue';
</script>

<template>
    <div class="dashboard-container">
        <Sidebar />
        
        <main class="main-content">
            <Topbar />
            
            <div class="workspace">
                <RouterView />
            </div>
        </main>
    </div>
</template>
