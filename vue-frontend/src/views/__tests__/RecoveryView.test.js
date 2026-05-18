import { mount } from '@vue/test-utils';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import RecoveryView from '../RecoveryView.vue';

const pushMock = vi.fn();
const vandirFetchMock = vi.fn();

vi.mock('vue-router', () => ({
    useRouter: () => ({ push: pushMock })
}));

vi.mock('../../utils/api', () => ({
    vandirFetch: (...args) => vandirFetchMock(...args),
    API_ENDPOINTS: {
        AUTH_RECOVERY: '/auth/recovery'
    }
}));

const flushPromises = () => new Promise((resolve) => setTimeout(resolve, 0));

describe('RecoveryView', () => {
    beforeEach(() => {
        pushMock.mockReset();
        vandirFetchMock.mockReset();
    });

    it('shows validation error when email is empty', async () => {
        const wrapper = mount(RecoveryView);

        await wrapper.find('form').trigger('submit');
        await flushPromises();

        expect(wrapper.text()).toContain('Ingresa un correo valido');
    });

    it('submits recovery request and shows success message', async () => {
        vandirFetchMock.mockResolvedValueOnce({
            ok: true,
            text: async () => JSON.stringify({ message: 'Instrucciones enviadas.' })
        });

        const wrapper = mount(RecoveryView);

        await wrapper.get('#recoveryEmail').setValue('demo@vandir.com');
        await wrapper.find('form').trigger('submit');
        await flushPromises();

        expect(vandirFetchMock).toHaveBeenCalledWith('/auth/recovery', expect.any(Object));
        expect(wrapper.text()).toContain('Instrucciones enviadas.');
    });

    it('navigates back to login', async () => {
        const wrapper = mount(RecoveryView);

        const backButton = wrapper.get('button[type="button"]');
        await backButton.trigger('click');

        expect(pushMock).toHaveBeenCalledWith({ name: 'login' });
    });
});
