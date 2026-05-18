<script setup>
import { ref, onMounted } from 'vue';
import { vandirFetch, API_ENDPOINTS } from '../utils/api';

const orders = ref([]);
const providers = ref([]);
const loadingOrders = ref(true);
const loadingProviders = ref(true);
const ordersError = ref('');
const providersError = ref('');
const formError = ref('');
const message = ref('');
const saving = ref(false);
const providerFormOpen = ref(false);
const providerFormError = ref('');
const providerMessage = ref('');
const providerSaving = ref(false);
const form = ref({
    providerId: '',
    estimatedTotal: ''
});

const providerForm = ref({
    name: '',
    contact: '',
    phone: '',
    email: ''
});

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

const getSession = () => {
    const str = localStorage.getItem('sesionVandir');
    if (!str) {
        return null;
    }

    try {
        return JSON.parse(str);
    } catch (e) {
        return null;
    }
};

const fetchOrders = async () => {
    loadingOrders.value = true;
    ordersError.value = '';

    try {
        const response = await vandirFetch(API_ENDPOINTS.ORDERS);
        if (response.ok) {
            orders.value = await response.json();
        } else {
            ordersError.value = 'No se pudieron cargar las ordenes de compra.';
        }
    } catch (e) {
        ordersError.value = e.message;
    } finally {
        loadingOrders.value = false;
    }
};

const fetchProviders = async () => {
    loadingProviders.value = true;
    providersError.value = '';

    try {
        const response = await vandirFetch(API_ENDPOINTS.PROVIDERS);
        if (response.ok) {
            providers.value = await response.json();
            if (providers.value.length > 0 && !form.value.providerId) {
                form.value.providerId = String(providers.value[0].id);
            }
            if (providers.value.length === 0) {
                providerFormOpen.value = true;
            }
        } else {
            providersError.value = 'No se pudo cargar el listado de proveedores.';
        }
    } catch (e) {
        providersError.value = e.message;
    } finally {
        loadingProviders.value = false;
    }
};

const readErrorMessage = async (response) => {
    try {
        const text = await response.text();
        return text || 'No se pudo registrar la orden.';
    } catch (e) {
        return 'No se pudo registrar la orden.';
    }
};

const resetProviderForm = () => {
    providerForm.value = {
        name: '',
        contact: '',
        phone: '',
        email: ''
    };
};

const createProvider = async () => {
    providerFormError.value = '';
    providerMessage.value = '';

    if (!providerForm.value.name.trim()) {
        providerFormError.value = 'El nombre del proveedor es obligatorio.';
        return;
    }

    providerSaving.value = true;

    try {
        const payload = {
            name: providerForm.value.name.trim(),
            contact: providerForm.value.contact.trim() || null,
            phone: providerForm.value.phone.trim() || null,
            email: providerForm.value.email.trim() || null
        };

        const response = await vandirFetch(API_ENDPOINTS.PROVIDERS, {
            method: 'POST',
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const created = await response.json();
            providerMessage.value = `Proveedor registrado: ${created.name}`;
            resetProviderForm();
            providerFormOpen.value = false;
            await fetchProviders();
            if (created?.id) {
                form.value.providerId = String(created.id);
            }
        } else {
            providerFormError.value = await readErrorMessage(response);
        }
    } catch (e) {
        providerFormError.value = e.message;
    } finally {
        providerSaving.value = false;
    }
};

const createOrder = async () => {
    formError.value = '';
    message.value = '';

    const session = getSession();
    if (!session?.userId) {
        formError.value = 'No se pudo identificar al responsable.';
        return;
    }

    if (providers.value.length === 0) {
        formError.value = 'No hay proveedores registrados.';
        providerFormOpen.value = true;
        return;
    }

    if (!form.value.providerId) {
        formError.value = 'Seleccione un proveedor.';
        return;
    }

    const total = Number(form.value.estimatedTotal);
    if (!total || total <= 0) {
        formError.value = 'Ingrese un total estimado valido.';
        return;
    }

    saving.value = true;

    try {
        const payload = {
            providerId: Number(form.value.providerId),
            managerId: session.userId,
            estimatedTotal: total
        };

        const response = await vandirFetch(API_ENDPOINTS.ORDERS, {
            method: 'POST',
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const created = await response.json();
            message.value = `Orden registrada: ${created.orderCode}`;
            form.value = { providerId: '', estimatedTotal: '' };
            await fetchOrders();
        } else {
            formError.value = await readErrorMessage(response);
        }
    } catch (e) {
        formError.value = e.message;
    } finally {
        saving.value = false;
    }
};

onMounted(() => {
    fetchOrders();
    fetchProviders();
});
</script>

<template>
    <div class="workspace-header">
        <h2>Ordenes de Compra</h2>
    </div>

    <div class="panel-stack">
        <div class="content-panel">
            <h3 class="panel-title">Registrar Orden</h3>

            <div v-if="formError" class="status-message text-danger">{{ formError }}</div>
            <div v-if="message" class="status-message text-success">{{ message }}</div>

            <div class="input-group">
                <label for="providerSelect">Proveedor</label>
                <select id="providerSelect" v-model="form.providerId" :disabled="loadingProviders">
                    <option value="">
                        {{ loadingProviders ? 'Cargando proveedores...' : 'Seleccione proveedor' }}
                    </option>
                    <option v-for="provider in providers" :key="provider.id" :value="provider.id">
                        {{ provider.name }}
                    </option>
                </select>
            </div>

            <div v-if="providersError" class="status-message text-danger">{{ providersError }}</div>

            <div class="input-group">
                <label for="estimatedTotal">Total Estimado</label>
                <input
                    id="estimatedTotal"
                    v-model="form.estimatedTotal"
                    type="number"
                    step="0.01"
                    placeholder="0.00"
                />
            </div>

            <div class="form-actions">
                <button class="btn btn-primary" :disabled="saving" @click="createOrder">
                    {{ saving ? 'Registrando...' : 'Registrar Orden' }}
                </button>
            </div>

            <div class="panel-divider"></div>

            <div class="panel-header">
                <h4 class="panel-subtitle">Proveedor nuevo</h4>
                <button
                    type="button"
                    class="btn btn-small btn-secondary"
                    @click="providerFormOpen = !providerFormOpen"
                >
                    {{ providerFormOpen ? 'Ocultar' : 'Agregar Proveedor' }}
                </button>
            </div>

            <div v-if="providers.length === 0 && !loadingProviders" class="status-message text-warning">
                No hay proveedores registrados. Agrega uno para continuar.
            </div>

            <div v-if="providerFormOpen" class="form-grid">
                <div v-if="providerFormError" class="status-message text-danger">
                    {{ providerFormError }}
                </div>
                <div v-if="providerMessage" class="status-message text-success">
                    {{ providerMessage }}
                </div>

                <div class="input-group">
                    <label for="providerName">Nombre del Proveedor</label>
                    <input id="providerName" v-model="providerForm.name" type="text" placeholder="Proveedor X" />
                </div>

                <div class="input-group">
                    <label for="providerContact">Contacto</label>
                    <input id="providerContact" v-model="providerForm.contact" type="text" placeholder="Nombre contacto" />
                </div>

                <div class="input-group">
                    <label for="providerPhone">Telefono</label>
                    <input id="providerPhone" v-model="providerForm.phone" type="text" placeholder="0000-0000" />
                </div>

                <div class="input-group">
                    <label for="providerEmail">Correo</label>
                    <input id="providerEmail" v-model="providerForm.email" type="email" placeholder="correo@proveedor.com" />
                </div>

                <div class="form-actions">
                    <button class="btn btn-primary" :disabled="providerSaving" @click="createProvider">
                        {{ providerSaving ? 'Guardando...' : 'Guardar Proveedor' }}
                    </button>
                </div>
            </div>
        </div>

        <div class="content-panel">
            <div v-if="loadingOrders">Cargando ordenes...</div>
            <div v-else-if="ordersError" class="text-danger">{{ ordersError }}</div>

            <table v-else class="data-table">
                <thead>
                    <tr>
                        <th>Codigo</th>
                        <th>Proveedor</th>
                        <th>Responsable</th>
                        <th>Total Est.</th>
                        <th>Estado</th>
                        <th>Fecha</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="order in orders" :key="order.id">
                        <td>{{ order.orderCode }}</td>
                        <td>{{ order.providerName }}</td>
                        <td>{{ order.managerName }}</td>
                        <td>{{ formatCurrency(order.estimatedTotal) }}</td>
                        <td>{{ order.status }}</td>
                        <td>{{ formatDate(order.requestDate) }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>
