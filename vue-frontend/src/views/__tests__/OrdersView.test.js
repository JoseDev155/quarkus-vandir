import { mount } from '@vue/test-utils';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import OrdersView from '../OrdersView.vue';

const vandirFetchMock = vi.fn();

vi.mock('../../utils/api', () => ({
    vandirFetch: (...args) => vandirFetchMock(...args),
    API_ENDPOINTS: {
        ORDERS: '/orders',
        PROVIDERS: '/providers'
    }
}));

const flushPromises = () => new Promise((resolve) => setTimeout(resolve, 0));

describe('OrdersView', () => {
    beforeEach(() => {
        localStorage.clear();
        vandirFetchMock.mockReset();
    });

    it('shows provider empty state and form', async () => {
        vandirFetchMock.mockImplementation((endpoint) => {
            if (endpoint === '/orders') {
                return Promise.resolve({ ok: true, json: async () => [] });
            }
            if (endpoint === '/providers') {
                return Promise.resolve({ ok: true, json: async () => [] });
            }
            return Promise.resolve({ ok: true, json: async () => [] });
        });

        const wrapper = mount(OrdersView);
        await flushPromises();

        expect(wrapper.text()).toContain('No hay proveedores registrados');
        expect(wrapper.find('#providerName').exists()).toBe(true);
    });

    it('creates a provider and updates the list', async () => {
        let providersCall = 0;

        vandirFetchMock.mockImplementation((endpoint, options = {}) => {
            if (endpoint === '/orders') {
                return Promise.resolve({ ok: true, json: async () => [] });
            }

            if (endpoint === '/providers' && (!options.method || options.method === 'GET')) {
                providersCall += 1;
                if (providersCall === 1) {
                    return Promise.resolve({ ok: true, json: async () => [] });
                }
                return Promise.resolve({ ok: true, json: async () => ([{ id: 5, name: 'Proveedor X' }]) });
            }

            if (endpoint === '/providers' && options.method === 'POST') {
                return Promise.resolve({ ok: true, json: async () => ({ id: 5, name: 'Proveedor X' }) });
            }

            return Promise.resolve({ ok: true, json: async () => [] });
        });

        const wrapper = mount(OrdersView);
        await flushPromises();

        await wrapper.get('#providerName').setValue('Proveedor X');

        const buttons = wrapper.findAll('button');
        const saveButton = buttons.find((button) => button.text().includes('Guardar Proveedor'));
        expect(saveButton).toBeTruthy();
        await saveButton.trigger('click');
        await flushPromises();
        await flushPromises();

        const providerSelect = wrapper.get('#providerSelect');
        expect(providerSelect.element.value).toBe('5');
        expect(wrapper.text()).toContain('Proveedor X');
    });

    it('creates an order with a selected provider', async () => {
        localStorage.setItem('sesionVandir', JSON.stringify({ userId: 12 }));

        vandirFetchMock.mockImplementation((endpoint, options = {}) => {
            if (endpoint === '/orders' && (!options.method || options.method === 'GET')) {
                return Promise.resolve({ ok: true, json: async () => [] });
            }
            if (endpoint === '/orders' && options.method === 'POST') {
                return Promise.resolve({ ok: true, json: async () => ({ orderCode: 'PO-123' }) });
            }
            if (endpoint === '/providers') {
                return Promise.resolve({ ok: true, json: async () => ([{ id: 3, name: 'Proveedor A' }]) });
            }
            return Promise.resolve({ ok: true, json: async () => [] });
        });

        const wrapper = mount(OrdersView);
        await flushPromises();

        await wrapper.get('#estimatedTotal').setValue('150.50');

        const buttons = wrapper.findAll('button');
        const registerButton = buttons.find((button) => button.text().includes('Registrar Orden'));
        expect(registerButton).toBeTruthy();
        await registerButton.trigger('click');
        await flushPromises();

        const orderCall = vandirFetchMock.mock.calls.find(([endpoint, options]) => {
            return endpoint === '/orders' && options?.method === 'POST';
        });
        expect(orderCall).toBeTruthy();

        const payload = JSON.parse(orderCall[1].body);
        expect(payload).toMatchObject({
            providerId: 3,
            managerId: 12,
            estimatedTotal: 150.5
        });
        expect(wrapper.text()).toContain('Orden registrada: PO-123');
    });
});
