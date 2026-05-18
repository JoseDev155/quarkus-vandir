import { mount } from '@vue/test-utils';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import InventoryView from '../InventoryView.vue';

const vandirFetchMock = vi.fn();

vi.mock('../../utils/api', () => ({
    vandirFetch: (...args) => vandirFetchMock(...args),
    API_ENDPOINTS: {
        PRODUCTS: '/products'
    }
}));

const flushPromises = () => new Promise((resolve) => setTimeout(resolve, 0));

describe('InventoryView', () => {
    beforeEach(() => {
        vandirFetchMock.mockReset();
    });

    it('renders products after loading', async () => {
        vandirFetchMock.mockResolvedValueOnce({
            ok: true,
            json: async () => ([
                {
                    id: 1,
                    code: 'A001',
                    name: 'Taladro',
                    categoryName: 'Herramientas',
                    currentStock: 8,
                    minStock: 2,
                    unitPrice: 120,
                    status: 'ACTIVE'
                }
            ])
        });

        const wrapper = mount(InventoryView);
        await flushPromises();

        expect(vandirFetchMock).toHaveBeenCalledWith('/products');
        expect(wrapper.text()).toContain('Taladro');

        const buttons = wrapper.findAll('button');
        const hasNewButton = buttons.some((button) => button.text().includes('+ Nuevo Producto'));
        expect(hasNewButton).toBe(true);
    });

    it('shows an error message when products fail to load', async () => {
        vandirFetchMock.mockResolvedValueOnce({
            ok: false
        });

        const wrapper = mount(InventoryView);
        await flushPromises();

        expect(wrapper.text()).toContain('Error al cargar productos');
    });
});
