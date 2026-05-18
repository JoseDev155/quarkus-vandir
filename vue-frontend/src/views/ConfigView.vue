<script setup>
import { ref, onMounted } from 'vue';
import { vandirFetch, API_ENDPOINTS } from '../utils/api';

const config = ref({
    companyName: '',
    contactPhone: '',
    vatTax: '',
    currency: ''
});

const loading = ref(true);
const saving = ref(false);
const error = ref('');
const message = ref('');

const loadConfig = async () => {
    try {
        const response = await vandirFetch(API_ENDPOINTS.CONFIG);
        if (response.ok) {
            const data = await response.json();
            config.value = {
                companyName: data.companyName || '',
                contactPhone: data.contactPhone || '',
                vatTax: data.vatTax ?? '',
                currency: data.currency || ''
            };
        } else {
            error.value = 'No se pudo cargar la configuracion.';
        }
    } catch (e) {
        error.value = e.message;
    } finally {
        loading.value = false;
    }
};

const saveConfig = async () => {
    saving.value = true;
    error.value = '';
    message.value = '';

    try {
        const payload = {
            companyName: config.value.companyName,
            contactPhone: config.value.contactPhone,
            vatTax: config.value.vatTax === '' ? null : Number(config.value.vatTax),
            currency: config.value.currency
        };

        const response = await vandirFetch(API_ENDPOINTS.CONFIG, {
            method: 'PUT',
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            message.value = 'Configuracion actualizada correctamente.';
        } else {
            error.value = 'No se pudo actualizar la configuracion.';
        }
    } catch (e) {
        error.value = e.message;
    } finally {
        saving.value = false;
    }
};

onMounted(() => {
    loadConfig();
});
</script>

<template>
    <div class="workspace-header">
        <h2>Configuracion General</h2>
        <button class="btn btn-primary" :disabled="saving" @click="saveConfig">
            {{ saving ? 'Guardando...' : 'Guardar Cambios' }}
        </button>
    </div>

    <div class="content-panel">
        <div v-if="loading">Cargando configuracion...</div>
        <div v-else>
            <div v-if="error" class="status-message text-danger">{{ error }}</div>
            <div v-if="message" class="status-message text-success">{{ message }}</div>

            <div class="input-group">
                <label for="companyName">Nombre de la Empresa</label>
                <input id="companyName" v-model="config.companyName" type="text" placeholder="Vandir System" />
            </div>

            <div class="input-group">
                <label for="contactPhone">Telefono de Contacto</label>
                <input id="contactPhone" v-model="config.contactPhone" type="text" placeholder="+503 0000-0000" />
            </div>

            <div class="input-group">
                <label for="vatTax">IVA (%)</label>
                <input id="vatTax" v-model="config.vatTax" type="number" step="0.01" placeholder="13" />
            </div>

            <div class="input-group">
                <label for="currency">Moneda</label>
                <input id="currency" v-model="config.currency" type="text" placeholder="USD" />
            </div>
        </div>
    </div>
</template>
