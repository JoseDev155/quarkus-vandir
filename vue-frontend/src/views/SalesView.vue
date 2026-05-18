<script setup>
import { ref, onMounted, computed } from 'vue';
import { vandirFetch, API_ENDPOINTS } from '../utils/api';

const products = ref([]);
const customers = ref([]);
const loading = ref(true);
const loadError = ref('');
const customersError = ref('');
const submitError = ref('');
const submitSuccess = ref('');
const submitting = ref(false);
const search = ref('');
const cartItems = ref([]);
const selectedCustomerId = ref('');
const paymentMethod = ref('CASH');

const paymentOptions = [
    { value: 'CASH', label: 'Efectivo' },
    { value: 'CREDIT_CARD', label: 'Tarjeta de Credito' },
    { value: 'TRANSFER', label: 'Transferencia' }
];

const formatCurrency = (value) => {
    if (value === null || value === undefined) {
        return '$0.00';
    }

    const number = Number(value);
    return Number.isNaN(number) ? '$0.00' : `$${number.toFixed(2)}`;
};

const filteredProducts = computed(() => {
    if (!search.value) {
        return products.value;
    }

    const term = search.value.toLowerCase();
    return products.value.filter((product) => {
        return `${product.code} ${product.name}`.toLowerCase().includes(term);
    });
});

const cartTotal = computed(() => {
    return cartItems.value.reduce((acc, item) => acc + item.unitPrice * item.quantity, 0);
});

const addToCart = (product) => {
    const index = cartItems.value.findIndex((item) => item.id === product.id);
    if (index >= 0) {
        cartItems.value[index].quantity += 1;
        return;
    }

    cartItems.value.push({
        id: product.id,
        name: product.name,
        unitPrice: Number(product.unitPrice || 0),
        quantity: 1
    });
};

const removeFromCart = (productId) => {
    cartItems.value = cartItems.value.filter((item) => item.id !== productId);
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

const fetchProducts = async () => {
    try {
        const response = await vandirFetch(API_ENDPOINTS.PRODUCTS);
        if (response.ok) {
            products.value = await response.json();
        } else {
            loadError.value = 'No se pudo cargar el catalogo de productos.';
        }
    } catch (e) {
        loadError.value = e.message;
    }
};

const fetchCustomers = async () => {
    try {
        const response = await vandirFetch(API_ENDPOINTS.CUSTOMERS);
        if (response.ok) {
            customers.value = await response.json();
        } else {
            customersError.value = 'No se pudo cargar la lista de clientes.';
        }
    } catch (e) {
        customersError.value = e.message;
    }
};

const loadData = async () => {
    loading.value = true;
    loadError.value = '';
    customersError.value = '';

    await Promise.all([fetchProducts(), fetchCustomers()]);
    loading.value = false;
};

const readErrorMessage = async (response) => {
    try {
        const text = await response.text();
        return text || 'No se pudo registrar la venta.';
    } catch (e) {
        return 'No se pudo registrar la venta.';
    }
};

const submitSale = async () => {
    submitError.value = '';
    submitSuccess.value = '';

    const session = getSession();
    if (!session?.userId) {
        submitError.value = 'No se pudo identificar al vendedor.';
        return;
    }

    if (cartItems.value.length === 0) {
        submitError.value = 'Agrega al menos un producto.';
        return;
    }

    submitting.value = true;

    try {
        const payload = {
            sellerId: session.userId,
            customerId: selectedCustomerId.value ? Number(selectedCustomerId.value) : null,
            paymentMethod: paymentMethod.value,
            items: cartItems.value.map((item) => ({
                productId: item.id,
                quantity: item.quantity
            }))
        };

        const response = await vandirFetch(API_ENDPOINTS.SALES, {
            method: 'POST',
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const sale = await response.json();
            submitSuccess.value = `Venta registrada: ${sale.ticketCode}`;
            cartItems.value = [];
            await fetchProducts();
        } else {
            submitError.value = await readErrorMessage(response);
        }
    } catch (e) {
        submitError.value = e.message;
    } finally {
        submitting.value = false;
    }
};

onMounted(() => {
    loadData();
});
</script>

<template>
    <div class="workspace-header">
        <h2>Nueva Venta</h2>
    </div>

    <div class="pos-container">
        <div class="pos-products">
            <div class="input-group">
                <input v-model="search" type="text" placeholder="Buscar producto por codigo o nombre..." />
            </div>

            <div v-if="loading">Cargando productos...</div>
            <div v-else-if="loadError" class="text-danger">{{ loadError }}</div>

            <table v-else class="data-table">
                <thead>
                    <tr>
                        <th>Codigo</th>
                        <th>Producto</th>
                        <th>Precio</th>
                        <th>Accion</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="product in filteredProducts" :key="product.id">
                        <td>{{ product.code }}</td>
                        <td>{{ product.name }}</td>
                        <td>{{ formatCurrency(product.unitPrice) }}</td>
                        <td>
                            <button class="btn btn-small btn-secondary" @click="addToCart(product)">
                                Agregar
                            </button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="pos-cart">
            <h3>Detalle de Venta</h3>

            <div class="pos-form">
                <div class="input-group">
                    <label for="customerSelect">Cliente</label>
                    <select id="customerSelect" v-model="selectedCustomerId" :disabled="loading">
                        <option value="">Mostrador</option>
                        <option v-for="customer in customers" :key="customer.id" :value="customer.id">
                            {{ customer.name }}
                        </option>
                    </select>
                </div>

                <div v-if="customersError" class="status-message text-danger">
                    {{ customersError }}
                </div>

                <div class="input-group">
                    <label for="paymentMethod">Metodo de Pago</label>
                    <select id="paymentMethod" v-model="paymentMethod">
                        <option v-for="option in paymentOptions" :key="option.value" :value="option.value">
                            {{ option.label }}
                        </option>
                    </select>
                </div>
            </div>

            <div v-if="submitError" class="status-message text-danger">{{ submitError }}</div>
            <div v-if="submitSuccess" class="status-message text-success">{{ submitSuccess }}</div>

            <hr class="cart-divider" />

            <div v-if="cartItems.length === 0" class="text-muted">
                No hay productos agregados.
            </div>

            <div v-else class="cart-items">
                <div v-for="item in cartItems" :key="item.id" class="cart-item">
                    <span>{{ item.quantity }}x {{ item.name }}</span>
                    <span>{{ formatCurrency(item.unitPrice * item.quantity) }}</span>
                    <button class="btn btn-small btn-danger" @click="removeFromCart(item.id)">
                        Quitar
                    </button>
                </div>
            </div>

            <hr class="cart-divider" />
            <div class="cart-total">Total: {{ formatCurrency(cartTotal) }}</div>
            <button class="btn-sell" :disabled="cartItems.length === 0 || submitting" @click="submitSale">
                {{ submitting ? 'Procesando...' : 'Cobrar Venta' }}
            </button>
        </div>
    </div>
</template>
