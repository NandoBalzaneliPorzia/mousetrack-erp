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
  const avatarContainer = document.querySelector('.avatar-container');

  const guestAccess = new URLSearchParams(location.search).get("processoId");
  if (guestAccess) {
      window.location.href = `/processo.html?processoId=${guestAccess}`;
  }


  if (!form) {
    console.error('[profile] Form #perfilForm não encontrado');
    return;
  }

  // 👉 Exibir imagem salva (caso já exista no localStorage)
  const imagemSalva = localStorage.getItem('userAvatar');
  if (imagemSalva) {
    avatarContainer.innerHTML = `
      <img src="${imagemSalva}" 
           alt="Avatar" 
           class="avatar-icon" 
           style="width:120px;height:120px;border-radius:50%;object-fit:cover;">
    `;
  }

  // 👉 Evento de alteração da imagem
  btnAlterarImagem.addEventListener('click', () => {
    inputImagem.click();
  });

  inputImagem.addEventListener('change', (e) => {
    const arquivo = e.target.files[0];
    if (arquivo) {
      const reader = new FileReader();
      reader.onload = (event) => {
        const novaImagem = event.target.result;

        // exibe e salva no localStorage
        avatarContainer.innerHTML = `
          <img src="${novaImagem}" 
               alt="Avatar" 
               class="avatar-icon" 
               style="width:120px;height:120px;border-radius:50%;object-fit:cover;">
        `;
        localStorage.setItem('userAvatar', novaImagem);
      };
      reader.readAsDataURL(arquivo);
    }
  });

  // 👉 Evento de envio do formulário
  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const usuarioId = getUsuarioId();
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
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!resp.ok) {
        const msg = await resp.text();
        alert(`Falha ao salvar perfil (${resp.status}): ${msg || 'tente novamente'}`);
        return;
      }

      alert('Perfil atualizado com sucesso!');
      if (senha) senha.value = '';
      if (confirmar) confirmar.value = '';
    } catch (err) {
      console.error('[profile] erro de rede:', err);
      alert('Erro de rede ao salvar perfil.');
    }
  });
});