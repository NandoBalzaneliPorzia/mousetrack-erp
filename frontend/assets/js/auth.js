// auth.js

// Verifica se o usuário está logado
window.requireAuth = function () {
  const usuarioId = localStorage.getItem('usuarioId');
  if (!usuarioId) {
    // Se não estiver logado, volta para a página de login
    alert('Sessão expirada. Faça login novamente.');
    window.location.href = 'index.html';
  }
};

// Retorna o ID do usuário logado
window.getUsuarioId = function () {
  return localStorage.getItem('usuarioId');
};

// Faz logout e limpa informações do navegador
window.logout = function () {
  localStorage.removeItem('usuarioId');
  window.location.href = 'index.html';
};
