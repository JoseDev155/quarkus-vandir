import { mount } from '@vue/test-utils';
import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import LoginView from '../LoginView.vue';

const pushMock = vi.fn();

vi.mock('vue-router', () => ({
    useRouter: () => ({ push: pushMock })
}));

const flushPromises = () => new Promise((resolve) => setTimeout(resolve, 0));

describe('LoginView', () => {
    const originalFetch = global.fetch;

    beforeEach(() => {
        localStorage.clear();
        pushMock.mockReset();
        global.fetch = vi.fn();
    });

    afterEach(() => {
        if (originalFetch) {
            global.fetch = originalFetch;
        } else {
            delete global.fetch;
        }
    });

    it('loads remembered email on mount', async () => {
        localStorage.setItem('vandirRememberEmail', 'demo@vandir.com');

        const wrapper = mount(LoginView);
        await wrapper.vm.$nextTick();

        const emailInput = wrapper.get('#email');
        const rememberInput = wrapper.get('input[type="checkbox"]');

        expect(emailInput.element.value).toBe('demo@vandir.com');
        expect(rememberInput.element.checked).toBe(true);
    });

    it('submits login and stores session', async () => {
        global.fetch.mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                token: 'token-123',
                rol: 'Vendedor',
                name: 'Carlos',
                userId: 5
            })
        });

        const wrapper = mount(LoginView);

        await wrapper.get('#email').setValue('carlos@vandir.com');
        await wrapper.get('#password').setValue('secret');
        await wrapper.get('input[type="checkbox"]').setValue(true);

        await wrapper.find('form').trigger('submit');
        await flushPromises();

        const session = JSON.parse(localStorage.getItem('sesionVandir'));
        expect(session).toMatchObject({
            token: 'token-123',
            rol: 'Vendedor',
            name: 'Carlos',
            userId: 5,
            email: 'carlos@vandir.com'
        });
        expect(localStorage.getItem('vandirRememberEmail')).toBe('carlos@vandir.com');
        expect(pushMock).toHaveBeenCalledWith({ name: 'dashboard' });
    });

    it('shows an error when credentials are invalid', async () => {
        global.fetch.mockResolvedValueOnce({
            ok: false
        });

        const wrapper = mount(LoginView);

        await wrapper.get('#email').setValue('bad@vandir.com');
        await wrapper.get('#password').setValue('bad');
        await wrapper.find('form').trigger('submit');
        await flushPromises();

        expect(wrapper.text()).toContain('Correo o');
        expect(pushMock).not.toHaveBeenCalled();
    });
});
