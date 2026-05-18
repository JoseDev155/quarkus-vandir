import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/recuperacion',
      name: 'recovery',
      component: () => import('../views/RecoveryView.vue')
    },
    {
      path: '/',
      component: () => import('../components/layout/MainLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'dashboard',
          component: () => import('../views/DashboardRouter.vue') // Rutas dinámicas según rol
        },
        {
          path: 'usuarios',
          name: 'admin-users',
          component: () => import('../views/AdminUsersView.vue'),
          meta: { roles: ['Administrador'] }
        },
        {
          path: 'configuracion',
          name: 'admin-config',
          component: () => import('../views/ConfigView.vue'),
          meta: { roles: ['Administrador'] }
        },
        {
          path: 'respaldos',
          name: 'admin-backups',
          component: () => import('../views/BackupsView.vue'),
          meta: { roles: ['Administrador'] }
        },
        {
          path: 'resumen',
          name: 'manager-summary',
          component: () => import('../views/ManagerSummaryView.vue'),
          meta: { roles: ['Gerente'] }
        },
        {
          path: 'reportes',
          name: 'reports',
          component: () => import('../views/ReportsView.vue'),
          meta: { roles: ['Administrador', 'Gerente'] }
        },
        {
          path: 'inventario',
          name: 'inventory',
          component: () => import('../views/InventoryView.vue'),
          meta: { roles: ['Administrador', 'Gerente', 'Vendedor'] }
        },
        {
          path: 'compras',
          name: 'orders',
          component: () => import('../views/OrdersView.vue'),
          meta: { roles: ['Administrador', 'Gerente'] }
        },
        {
          path: 'venta',
          name: 'sales',
          component: () => import('../views/SalesView.vue'),
          meta: { roles: ['Vendedor'] }
        },
        {
          path: 'clientes',
          name: 'customers',
          component: () => import('../views/CustomersView.vue'),
          meta: { roles: ['Administrador', 'Gerente', 'Vendedor'] }
        }
        // Iremos añadiendo el resto de vistas aquí
      ]
    }
  ]
})

// Navigation Guard Global para JWT
router.beforeEach((to, from, next) => {
  const session = JSON.parse(localStorage.getItem('sesionVandir'))

  if (to.meta.requiresAuth && !session) {
    next({ name: 'login' })
  } else if (to.name === 'login' && session) {
    next({ name: 'dashboard' })
  } else if (to.meta.roles && session) {
    if (to.meta.roles.includes(session.rol)) {
      next()
    } else {
      next({ name: 'dashboard' }) // Redirige al dashboard si no tiene permisos
    }
  } else {
    next()
  }
})

export default router
