// -------------------- BASE DA API --------------------
(function () {
  const isLocal = location.hostname === "localhost" || location.hostname.startsWith("127.");

  const PROD = "https://mousetrack-erp.onrender.com";
  const DEV  = "http://localhost:8080";

  window.API_BASE = isLocal ? DEV : PROD;

  // Garante uma única barra entre base e path
  window.api = (path) => `${window.API_BASE.replace(/\/+$/, '')}/${String(path).replace(/^\/+/, '')}`;
})();

// -------------------- ROTAS DO FRONT --------------------
// (se mudar os nomes/locais das páginas, ajuste aqui)
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
  logout() {
    this.user = null;
    // replace evita voltar para a página privada no histórico
    location.replace(LOGIN_PATH);
  }
};

// ---------- Helpers globais usados em outras páginas ----------
/** Garante usuário logado; se não houver, alerta uma vez, redireciona e aborta a execução. */
window.requireAuth = function requireAuth() {
  const u = session.user;
  if (!u) {
    if (!window.__authWarned) {
      window.__authWarned = true;
      alert('Sessão expirada. Faça login novamente.');
    }
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
  const isRootOrIndex = p === '/' || p === '' || p.endsWith('/index') || p.endsWith('/index.html');
  const isLogin = isRootOrIndex;

  // Se já logado e abriu o login → manda para a home
  if (isLogin && session.user) {
    location.replace(HOME_PATH);
    return;
  }

  // Se não logado e tentou abrir página privada → volta pro login
  // Se não logado e em modo convidado (guest=1), permite acesso a páginas públicas
  const urlParams = new URLSearchParams(window.location.search);
  const isGuest = urlParams.get("guest") === "1";
  const isGuestPage =
    location.pathname.endsWith("processo.html") ||
    location.pathname.endsWith("chat.html");

  if (!isLogin && !session.user) {
    if (isGuest && isGuestPage) {
      return; // permite acesso ao convidado
    }
    location.replace(LOGIN_PATH);
  }
})();

// -------------------- HELPER PÓS-LOGIN --------------------
window.handleLoginResponse = function (res, data, email) {
  if (!res.ok) {
    alert((data && data.erro) || `Falha no login (${res.status})`);
    return;
  }
  // Esperamos { id, mensagem? }
  if (data && typeof data.id !== 'undefined') {
    session.user = { id: data.id, email };
  } else {
    console.warn('[login] Resposta sem id:', data);
  }
  location.href = HOME_PATH; // → /profile.html
};

// -------------------- LOGOUT GLOBAL --------------------
// Intercepta diversas formas de “Sair” na UI.
document.addEventListener('DOMContentLoaded', () => {
  // Se existir um botão/link com esses seletores, o clique fará logout.
  document.addEventListener('click', (ev) => {
    const el = ev.target.closest('#btnLogout, .btn-logout, a[href="#logout"], a.logout');
    if (!el) return;
    ev.preventDefault();
    ev.stopPropagation();
    session.logout();
  });
});

// -------------------- PRÉ-AQUECIMENTO DO BACKEND --------------------
(function warmBackend() {
  // Ignora erros (ex.: serviço dormindo). Só “acorda” o Render.
  fetch(api('/api/_health'), { cache: 'no-store' }).catch(() => {});
})();
