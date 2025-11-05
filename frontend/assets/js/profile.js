// profile.js
document.addEventListener('DOMContentLoaded', () => {
  // bloqueia a página se não estiver logado
  requireAuth();

  const form = document.getElementById('perfilForm');
  const senha = document.getElementById('senha');
  const confirmar = document.getElementById('confirmar');
  const telefone = document.getElementById('telefone');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const usuarioId = getUsuarioId(); // já garantido pelo requireAuth()

    // validação simples
    if (senha.value !== confirmar.value) {
      alert('As senhas não coincidem.');
      return;
    }

    try {
      // exemplo de PATCH/PUT para atualizar perfil
      const resp = await fetch(api(`/api/usuarios/${usuarioId}`), {
        method: 'PUT', // ou PATCH, conforme seu controller
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          telefone: telefone.value || null,
          senha: senha.value || null
        })
      });

      if (!resp.ok) {
        const msg = await resp.text();
        alert(`Falha ao salvar perfil (${resp.status}): ${msg || 'tente novamente'}`);
        return;
        }

      alert('Perfil atualizado com sucesso!');
      // opcional: limpar campos de senha
      senha.value = '';
      confirmar.value = '';
    } catch (err) {
      console.error(err);
      alert('Erro de rede ao salvar perfil.');
    }
  });
});
