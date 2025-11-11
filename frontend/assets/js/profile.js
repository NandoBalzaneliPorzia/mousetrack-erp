// profile.js
document.addEventListener('DOMContentLoaded', () => {
  // bloqueia a página se não estiver logado
  const user = requireAuth();
  console.log('[profile] user =', user);

  const form = document.getElementById('perfilForm');
  const senha = document.getElementById('senha');
  const confirmar = document.getElementById('confirmar');
  const telefone = document.getElementById('telefone');
  const inputImagem = document.getElementById('inputImagem');
  const btnAlterarImagem = document.getElementById('btnAlterarImagem');

  if (!form) {
    console.error('[profile] Form #perfilForm não encontrado');
    return;
  }

  // 👉 Evento de envio do formulário
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
      senha: senha?.value || null
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
      if (senha) senha.value = '';
      if (confirmar) confirmar.value = '';
    } catch (err) {
      console.error('[profile] erro de rede:', err);
      alert('Erro de rede ao salvar perfil.');
    }
  });

  // 👉 Evento de alteração da imagem
  btnAlterarImagem.addEventListener('click', () => {
    inputImagem.click(); // abre o seletor de arquivos
  });

  inputImagem.addEventListener('change', (e) => {
    const arquivo = e.target.files[0];
    if (arquivo) {
      const reader = new FileReader();
      reader.onload = (event) => {
        // troca o avatar para mostrar a nova imagem escolhida
        const avatarIcon = document.querySelector('.avatar-icon');
        avatarIcon.outerHTML = `
          <img src="${event.target.result}" 
               alt="Avatar" 
               class="avatar-icon" 
               style="width:72px;height:72px;border-radius:50%;object-fit:cover;">
        `;
      };
      reader.readAsDataURL(arquivo);
    }
  });
});
