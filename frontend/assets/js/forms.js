// forms.js
document.addEventListener('DOMContentLoaded', () => {
  const input = document.getElementById('anexo');
  const fileText = document.getElementById('fileText');
  const form = document.getElementById('procForm');

  // URL do backend hospedado no Render
  const BASE_URL = 'https://mouse-track-backend.onrender.com';

  // Mostra nomes/quantidade de arquivos selecionados
  if (input) {
    input.addEventListener('change', () => {
      if (!input.files || input.files.length === 0) {
        fileText.textContent = '';
        return;
      }
      fileText.textContent = input.files.length === 1
        ? input.files[0].name
        : `${input.files.length} arquivos selecionados`;
    });
  }

  if (form) {
    form.addEventListener('submit', async (e) => {
      e.preventDefault();

      const formData = new FormData(form);

      try {
        const res = await fetch(`${BASE_URL}/api/processos`, {
          method: 'POST',
          body: formData // 👈 manda como multipart/form-data automaticamente
        });

        if (!res.ok) {
          const text = await res.text().catch(() => null);
          throw new Error(`Servidor retornou ${res.status} ${text || ''}`);
        }

        const created = await res.json();

        // Salva o processo no localStorage (para exibir no board)
        const processos = JSON.parse(localStorage.getItem('processos') || '[]');
        processos.push(created);
        localStorage.setItem('processos', JSON.stringify(processos));

        alert(`✅ Processo criado com sucesso!\nCódigo: ${created.codigo}`);
        form.reset();
        if (fileText) fileText.textContent = '';

        // Redireciona para o board (opcional)
        // window.location.href = '/board.html';

      } catch (err) {
        console.error('Erro ao criar processo:', err);
        alert('❌ Falha ao criar processo. Verifique o console para detalhes.');
      }
    });
  }
});
