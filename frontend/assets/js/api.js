// -------------------- BASE DA API --------------------
(function () {
  const isLocal = location.hostname === "localhost" || location.hostname.startsWith("127.");

  // URLs do backend
  const PROD = "https://mousetrack-erp.onrender.com";
  const DEV  = "http://localhost:8080";

  // Define qual usar
  window.API_BASE = isLocal ? DEV : PROD;

  // Helper que garante a barra "/" no meio
  window.api = (path) => {
    return `${window.API_BASE.replace(/\/+$/, '')}/${String(path).replace(/^\/+/, '')}`;
  };
})();

// -------------------- ROTAS DO FRONT --------------------
// Sua tela de login é index.html (não existe login.html)
const LOGIN_PATH = '/index.html';
const HOME_PATH  = '/profile.html';

// -------------------- SESSÃO (mínima, sem JWT) --------------------
window.session = {
  get user() {
    try { return JSON.parse(localStorage.getItem('user') || 'null'); } catch { return null; }
  },
  set user(u) {
    if (u) localStorage.setItem('user', JSON.stringify(u));
    else   localStorage.removeItem('user');
  },
  logout() { this.user = null; location.href = LOGIN_PATH; }
};

// ---------- Helpers globais usados em outras páginas ----------
/** Garante usuário logado; se não houver, redireciona e aborta a execução. */
window.requireAuth = function requireAuth() {
  const u = session.user;
  if (!u) {
    // Volta pro login e interrompe a execução do restante da página
    location.replace(LOGIN_PATH);
    throw new Error('[auth] Usuário não autenticado');
  }
  return u;
};

/** Retorna o id do usuário salvo na sessão. */
window.getUsuarioId = function getUsuarioId() {
  return session.user?.id ?? null;
};

// -------------------- GUARD AUTOMÁTICO --------------------
(function guard() {
  const p = (location.pathname || '').toLowerCase();

  // considera raiz e index como "página de login"
  const isRootOrIndex =
    p === '/' || p === '' || p.endsWith('/index') || p.endsWith('/index.html');

  const isLogin = isRootOrIndex;

  // Se já logado e abriu o login → vai pra home
  if (isLogin && session.user) {
    location.replace(HOME_PATH);
    return;
  }

  // Se não logado e tentou abrir página privada → volta pro login
  if (!isLogin && !session.user) {
    location.replace(LOGIN_PATH);
  }
})();

// -------------------- HELPER PÓS-LOGIN --------------------
window.handleLoginResponse = function (res, data, email) {
  if (!res.ok) {
    alert((data && data.erro) || `Falha no login (${res.status})`);
    return;
  }
  // backend retorna { id, ... }
  if (data && typeof data.id !== 'undefined') {
    session.user = { id: data.id, email };
  } else {
    console.warn('[login] Resposta sem id:', data);
  }
  location.href = HOME_PATH; // -> /profile.html
};

// -------------------- LOGOUT GLOBAL (botão opcional) --------------------
document.addEventListener('DOMContentLoaded', () => {
  const btn = document.getElementById('btnLogout');
  if (btn) btn.addEventListener('click', () => session.logout());
});

// -------------------- PRÉ-AQUECIMENTO DO BACKEND --------------------
(function warmBackend() {
  fetch(api('/api/_health'), { cache: 'no-store' }).catch(() => {});
})();
