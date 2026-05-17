// Esperamos a que todo el HTML cargue antes de ejecutar el código
document.addEventListener('DOMContentLoaded', () => {
    
    // ==========================================================================
    // 1. SEGURIDAD Y CONTROL DE SESIÓN
    // ==========================================================================
    const sesionActual = JSON.parse(localStorage.getItem('sesionVandir'));
    const esPaginaLogin = window.location.href.includes('login.html') || window.location.pathname.endsWith('/');

    // A. Si NO hay sesión iniciada y NO está en el login, lo expulsamos al login
    if (!sesionActual && !esPaginaLogin) {
        window.location.href = 'login.html';
    }

    // B. Si YA hay sesión iniciada pero intenta entrar al login, lo regresamos a su panel
    if (sesionActual && esPaginaLogin) {
        const rutas = {
            'Administrador': 'admin.html',
            'Gerente': 'gerente.html',
            'Vendedor': 'vendedor.html'
        };
        window.location.href = rutas[sesionActual.rol] || 'login.html';
    }


    // ==========================================================================
    // 2. LÓGICA DE INICIO DE SESIÓN (LOGIN)
    // ==========================================================================
    const loginForm = document.querySelector('.login-form');
    
    // Solo ejecutamos esto si detectamos que existe el formulario de login en la pantalla
    if (loginForm) {
        loginForm.addEventListener('submit', (e) => {
            e.preventDefault(); // Evita que la página parpadee o se recargue

            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            // Simulamos la base de datos
            const usuariosBD = {
                'admin@vandir.com': { pass: 'admin123', rol: 'Administrador', url: 'admin.html' },
                'gerente@vandir.com': { pass: 'gerente123', rol: 'Gerente', url: 'gerente.html' },
                'vendedor@vandir.com': { pass: 'vendedor123', rol: 'Vendedor', url: 'vendedor.html' }
            };

            const usuario = usuariosBD[email];

            // Validamos credenciales
            if (usuario && usuario.pass === password) {
                localStorage.setItem('sesionVandir', JSON.stringify({ email: email, rol: usuario.rol }));
                window.location.href = usuario.url;
            } else {
                alert('⚠️ Correo o contraseña incorrectos. Intenta de nuevo.');
            }
        });
    }


    // ==========================================================================
    // 3. CERRAR SESIÓN (GLOBAL PARA TODOS LOS PANELES)
    // ==========================================================================
    const botonesSalir = document.querySelectorAll('.logout-btn');
    botonesSalir.forEach(btn => {
        btn.addEventListener('click', () => {
            localStorage.removeItem('sesionVandir'); // Borramos los datos
            window.location.href = 'login.html';     // Lo mandamos al login
        });
    });


    // ==========================================================================
    // 4. LÓGICA DEL PUNTO DE VENTA (SOLO PANTALLA DEL VENDEDOR)
    // ==========================================================================
    const posContainer = document.querySelector('.pos-container');
    
    // Solo ejecutamos esto si detectamos que estamos en el Punto de Venta
    if (posContainer) {
        let carrito = [];
        const botonesAgregar = document.querySelectorAll('.btn-add-product');
        const listaCarrito = document.getElementById('lista-carrito');
        const totalVenta = document.getElementById('total-venta');
        const btnCobrar = document.querySelector('.btn-sell');

        // Dibuja los productos en la lista lateral
        function actualizarCarrito() {
            listaCarrito.innerHTML = '';
            let total = 0;

            carrito.forEach((item, index) => {
                total += item.precio;
                
                const divItem = document.createElement('div');
                divItem.classList.add('cart-item');
                divItem.innerHTML = `
                    <span>1x ${item.nombre}</span>
                    <span>$${item.precio.toFixed(2)}</span>
                    <button class="btn btn-danger btn-small" onclick="eliminarDelCarrito(${index})" style="padding: 2px 5px; font-size: 0.7rem;">X</button>
                `;
                listaCarrito.appendChild(divItem);
            });

            totalVenta.innerText = `$${total.toFixed(2)}`;
        }

        // Función para quitar un producto de la lista
        window.eliminarDelCarrito = function(index) {
            carrito.splice(index, 1);
            actualizarCarrito();
        };

        // Escucha los clics en los botones de "Agregar" de la tabla
        botonesAgregar.forEach(btn => {
            btn.addEventListener('click', (e) => {
                const fila = e.target.closest('tr');
                const nombre = fila.children[1].innerText;
                const precio = parseFloat(fila.children[2].innerText.replace('$', '')); 

                carrito.push({ nombre, precio });
                actualizarCarrito();
            });
        });

        // Evento para el botón de cobrar
        if (btnCobrar) {
            btnCobrar.addEventListener('click', () => {
                if (carrito.length === 0) {
                    alert('⚠️ El carrito está vacío. Agrega productos primero.');
                    return;
                }
                alert(`✅ Venta cobrada con éxito. Total: ${totalVenta.innerText}`);
                carrito = []; // Vaciamos el carrito tras pagar
                actualizarCarrito(); // Refrescamos
            });
        }
    }
});