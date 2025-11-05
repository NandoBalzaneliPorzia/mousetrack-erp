document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('loginForm');
  const submitBtn = form.querySelector('button[type="submit"]');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const email = document.getElementById('email').value.trim();
    const senha = document.getElementById('senha').value.trim();

    // UX: evita duplo clique
    submitBtn.disabled = true;

    try {
      const res = await fetch(api('/api/login'), {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, senha })
      });

      // tenta ler JSON; se falhar, usa objeto vazio
      let data = {};
      try { data = await res.json(); } catch (_) {}

      if (res.ok) {
        // Backend retorna { mensagem, id }
        if (data && typeof data.id !== 'undefined') {
          localStorage.setItem('usuarioId', String(data.id));
        } else {
          console.warn('Resposta sem id:', data);
        }

        alert(data.mensagem || 'Login realizado com sucesso!');
        // Redireciona após login
        window.location.href = 'profile.html'; // ou 'access.html' se preferir
        return;
      }

      // Erro de autenticação/validação
      alert(data.erro || `Falha no login (${res.status})`);
    } catch (err) {
      console.error('Erro na requisição:', err);
      alert('Erro ao conectar ao servidor.');
    } finally {
      submitBtn.disabled = false;
    }
  });
});