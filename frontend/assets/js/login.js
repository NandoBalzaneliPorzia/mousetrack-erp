//Responsável: Laura Pereira
// assets/js/login.js
document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('loginForm');
  if (!form) return;

const guestAccess = new URLSearchParams(location.search).get("processoId");
if (guestAccess) {
    window.location.href = `/processo.html?processoId=${guestAccess}`;
}


  const submitBtn = form.querySelector('button[type="submit"]');
  const emailEl = document.getElementById('email');
  const senhaEl = document.getElementById('senha');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    submitBtn.disabled = true;

    const email = (emailEl?.value || '').trim();
    const senha = (senhaEl?.value || '').trim();

    try {
      const res = await fetch(api('/api/login'), {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, senha })
      });

      let data = {};
      try { data = await res.json(); } catch {}

      // >>> ESSENCIAL: grava session.user e redireciona para /profile.html
      handleLoginResponse(res, data, email);
    } catch (err) {
      console.error('[login] erro de rede:', err);
      alert('Erro ao conectar ao servidor.');
    } finally {
      submitBtn.disabled = false;
    }
  });
});
