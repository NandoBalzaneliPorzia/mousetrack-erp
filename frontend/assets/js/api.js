(function () {
  const isLocal = location.hostname === "localhost" || location.hostname.startsWith("127.");
  
  // URLs do backend
  const PROD = "https://mousetrack-erp.onrender.com";
  const DEV  = "http://localhost:8080";
  
  // Define qual usar
  window.API_BASE = isLocal ? DEV : PROD;

  // Função helper que garante a barra "/" no meio
  window.api = (path) => {
    return `${window.API_BASE.replace(/\/+$/, '')}/${String(path).replace(/^\/+/, '')}`;
  };
})();

// --- Sessão mínima (sem JWT por enquanto) ---
window.session = {
  get user() {
    try { return JSON.parse(localStorage.getItem('user') || 'null'); } catch { return null; }
  },
  set user(u) {
    if (u) localStorage.setItem('user', JSON.stringify(u));
    else localStorage.removeItem('user');
  },
  logout() { this.user = null; window.location.href = '/login.html'; }
};

// --- Guard automático de rotas ---
// Regras:
// - login.html é pública
// - qualquer outra página exige session.user
(function routeGuard() {
  const path = (location.pathname || '').toLowerCase();
  const isLogin = path.endsWith('/login') || path.endsWith('/login.html');
  if (!isLogin && !session.user) {
    // sem mexer no HTML: redireciona direto
    window.location.replace('/login.html');
  }
})();

// --- Helper de login para reaproveitar no seu login.js atual ---
window.handleLoginResponse = function(res, data, email) {
  if (!res.ok) {
    alert((data && data.erro) || `Falha no login (${res.status})`);
    return;
  }
  // backend retorna { id, mensagem, ... }
  if (data && typeof data.id !== 'undefined') {
    session.user = { id: data.id, email };
  }
  // redireciona para uma página privada já existente
  window.location.href = '/dashboard.html'; // troque se sua home privada tiver outro nome
};

// --- Logout progressivo (só funciona se existir um botão com esse id) ---
document.addEventListener('DOMContentLoaded', () => {
  const btn = document.getElementById('btnLogout');
  if (btn) btn.addEventListener('click', () => session.logout());
});
