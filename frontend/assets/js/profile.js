document.addEventListener('DOMContentLoaded', () => {
  // bloqueia a página se não estiver logado
  const user = requireAuth();
  console.log('[profile] user =', user);

  const form      = document.getElementById('perfilForm');
  const senha     = document.getElementById('senha');
  const confirmar = document.getElementById('confirmar');
  const telefone  = document.getElementById('telefone');

  if (!form) {
    console.error('[profile] Form #perfilForm não encontrado');
    return;
  }

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const usuarioId = getUsuarioId(); // já garantido pelo requireAuth()

    if (!usuarioId) {
      alert('Sessão inválida. Faça login novamente.');
      session.logout();
      return;
    }

    // validação simples
    if (senha && confirmar && senha.value !== confirmar.value) {
      alert('As senhas não coincidem.');
      return;
    }

    const payload = {
      telefone: telefone?.value || null,
      senha:    senha?.value     || null
    };

    try {
      const resp = await fetch(api(`/api/usuarios/${usuarioId}`), {
        method: 'PUT', // mude para PATCH se seu controller pedir
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!resp.ok) {
        const msg = await resp.text();
        alert(`Falha ao salvar perfil (${resp.status}): ${msg || 'tente novamente'}`);
        return;
      }

      alert('Perfil atualizado com sucesso!');

      // opcional: limpar campos de senha
      if (senha)     senha.value = '';
      if (confirmar) confirmar.value = '';
    } catch (err) {
      console.error('[profile] erro de rede:', err);
      alert('Erro de rede ao salvar perfil.');
    }
  });
});
