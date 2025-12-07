//Responsável: Juliana Prado
// nav.js — injeta o menu.html + ativa o item da sidebar + botão Sair

// Carrega o menu.html no ponto de montagem
async function mountMenu(where = '#sidebarMount') {
  const mount = document.querySelector(where);
  if (!mount) return;

  try {
    const resp = await fetch('menu.html', { cache: 'no-store' });
    const html = await resp.text();
    mount.innerHTML = html;

    // Ativa o item da sidebar conforme o data-page do <body>
    highlightCurrentMenu();

    // Liga o botão de logout (chama logout() do auth.js)
    const btnLogout = document.getElementById('btnLogout');
    if (btnLogout) {
      btnLogout.addEventListener('click', (e) => {
        e.preventDefault();
        logout(); // função que você já tem em auth.js
      });
    }
  } catch (err) {
    console.error('Erro ao montar menu:', err);
  }
}

// Função para destacar o item ativo
function highlightCurrentMenu() {
  const current = document.body.dataset.page; // ex.: "profile" ou "shipping"
  if (!current) return;

  document.querySelectorAll('.menu .menu-item').forEach(a => {
    const key = a.dataset.key; // definido no HTML (data-key="profile", etc.)
    const isActive = key === current;
    a.classList.toggle('is-active', isActive);
    if (isActive) {
      a.setAttribute('aria-current', 'page');
    } else {
      a.removeAttribute('aria-current');
    }
  });
}

// Inicializa automaticamente ao carregar a página
document.addEventListener('DOMContentLoaded', () => {
  if (typeof requireAuth === 'function') requireAuth(); // se existir
  mountMenu('#sidebarMount');
});
