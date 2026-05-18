import { mount } from '@vue/test-utils';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import SalesView from '../SalesView.vue';

const vandirFetchMock = vi.fn();

vi.mock('../../utils/api', () => ({
    vandirFetch: (...args) => vandirFetchMock(...args),
    API_ENDPOINTS: {
        PRODUCTS: '/products',
        CUSTOMERS: '/customers',
        SALES: '/sales'
    }
}));

const flushPromises = () => new Promise((resolve) => setTimeout(resolve, 0));

const productResponse = {
    ok: true,
    json: async () => ([
        { id: 1, code: 'A001', name: 'Taladro', unitPrice: 120 }
    ])
};

const customerResponse = {
    ok: true,
    json: async () => ([
        { id: 10, name: 'Cliente Demo' }
    ])
};

describe('SalesView', () => {
    beforeEach(() => {
        localStorage.clear();
        vandirFetchMock.mockReset();
    });

    it('adds items and submits a sale', async () => {
        localStorage.setItem('sesionVandir', JSON.stringify({ userId: 7 }));

        vandirFetchMock.mockImplementation((endpoint, options = {}) => {
            if (endpoint === '/products') {
                return Promise.resolve(productResponse);
            }
            if (endpoint === '/customers') {
                return Promise.resolve(customerResponse);
            }
            if (endpoint === '/sales' && options.method === 'POST') {
                return Promise.resolve({
                    ok: true,
                    json: async () => ({ ticketCode: 'TK-123' })
                });
            }
            return Promise.resolve({ ok: true, json: async () => [] });
        });

        const wrapper = mount(SalesView);
        await flushPromises();

        await wrapper.find('button.btn-small.btn-secondary').trigger('click');
        await wrapper.find('button.btn-sell').trigger('click');
        await flushPromises();

        const salesCall = vandirFetchMock.mock.calls.find(([endpoint]) => endpoint === '/sales');
        expect(salesCall).toBeTruthy();

        const payload = JSON.parse(salesCall[1].body);
        expect(payload).toMatchObject({
            sellerId: 7,
            paymentMethod: 'CASH'
        });
        expect(payload.items).toHaveLength(1);

        expect(wrapper.text()).toContain('Venta registrada: TK-123');
        expect(wrapper.text()).toContain('No hay productos agregados.');
    });

    it('shows validation error when seller is missing', async () => {
        vandirFetchMock.mockImplementation((endpoint) => {
            if (endpoint === '/products') {
                return Promise.resolve(productResponse);
            }
            if (endpoint === '/customers') {
                return Promise.resolve(customerResponse);
            }
            return Promise.resolve({ ok: true, json: async () => [] });
        });

        const wrapper = mount(SalesView);
        await flushPromises();

        await wrapper.find('button.btn-small.btn-secondary').trigger('click');
        await wrapper.find('button.btn-sell').trigger('click');
        await flushPromises();

        expect(wrapper.text()).toContain('No se pudo identificar al vendedor');
    });

    it('disables submit when cart is empty', async () => {
        localStorage.setItem('sesionVandir', JSON.stringify({ userId: 9 }));

        vandirFetchMock.mockImplementation((endpoint) => {
            if (endpoint === '/products') {
                return Promise.resolve(productResponse);
            }
            if (endpoint === '/customers') {
                return Promise.resolve(customerResponse);
            }
            return Promise.resolve({ ok: true, json: async () => [] });
        });

        const wrapper = mount(SalesView);
        await flushPromises();

        const submitButton = wrapper.get('button.btn-sell');
        expect(submitButton.element.disabled).toBe(true);
    });
});
