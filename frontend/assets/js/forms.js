// forms.js
document.addEventListener('DOMContentLoaded', () => {
  const input = document.getElementById('anexo');
  const fileText = document.getElementById('fileText');
  const form = document.getElementById('procForm');

  // ATENÇÃO: ajuste para seu backend correto (com/sem hífen conforme deploy)
  const BASE_URL = 'https://mousetrack-backend.onrender.com';

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

      const formData = new FormData(form); // pega todos os campos com os names corretos

      try {
        const res = await fetch(`${BASE_URL}/api/processos`, {
          method: 'POST',
          body: formData,
          // NÃO setar Content-Type — o browser faz o boundary automaticamente
        });

        if (!res.ok) {
          const text = await res.text().catch(() => null);
          throw new Error(`Servidor retornou ${res.status} - ${text || 'sem corpo'}`);
        }

        const created = await res.json();
        const processos = JSON.parse(localStorage.getItem('processos') || '[]');
        processos.push(created);
        localStorage.setItem('processos', JSON.stringify(processos));

        alert(`✅ Processo criado com sucesso!\nCódigo: ${created.codigo}`);
        form.reset();
        if (fileText) fileText.textContent = '';

      } catch (err) {
        console.error('Erro ao criar processo:', err);
        alert('❌ Falha ao criar processo. Veja console do navegador para detalhes.');
      }
    });
  }
});
