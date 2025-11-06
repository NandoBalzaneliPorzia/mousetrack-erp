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
const LOGIN_PATH = '/frontend/login.html';
const HOME_PATH  = '/frontend/profile.html';

// -------------------- SESSÃO (mínima, sem JWT) --------------------
window.session = {
  get user() {
    try {
      return JSON.parse(localStorage.getItem('user') || 'null');
    } catch {
      return null;
    }
  },
  set user(u) {
    if (u) {
      localStorage.setItem('user', JSON.stringify(u));
    } else {
      localStorage.removeItem('user');
    }
  },
  logout() {
    this.user = null;
    location.href = LOGIN_PATH;
  }
};

// -------------------- GUARD AUTOMÁTICO --------------------
(function guard() {
  const p = (location.pathname || '').toLowerCase();
  const isLogin = p.endsWith('/login') || p.endsWith('/login.html');

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

  // backend retorna { id, mensagem, ... }
  if (data && typeof data.id !== 'undefined') {
    session.user = { id: data.id, email };
  }

  // Redireciona para página privada (profile.html)
  location.href = HOME_PATH;
};

// -------------------- LOGOUT GLOBAL (botão opcional) --------------------
document.addEventListener('DOMContentLoaded', () => {
  const btn = document.getElementById('btnLogout');
  if (btn) btn.addEventListener('click', () => session.logout());
});
