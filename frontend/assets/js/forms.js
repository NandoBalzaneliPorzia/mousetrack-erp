// forms.js (corrigido e revisado)
document.addEventListener('DOMContentLoaded', () => {
  const input = document.getElementById('anexo');
  const fileText = document.getElementById('fileText');
  const form = document.getElementById('procForm');

  // 🔧 ATUALIZE para o domínio EXATO do seu backend
  const BASE_URL = 'https://mousetrack-backend.onrender.com/'; 

  // Exibe nome ou quantidade de arquivos selecionados
  if (input) {
    input.addEventListener('change', () => {
      if (!input.files || input.files.length === 0) {
        fileText.textContent = '';
        return;
      }
      fileText.textContent =
        input.files.length === 1
          ? input.files[0].name
          : `${input.files.length} arquivos selecionados`;
    });
  }

  // Envio do formulário
  if (form) {
    form.addEventListener('submit', async (e) => {
      e.preventDefault();

      // 🔧 Cria um FormData para incluir arquivos + campos
      const formData = new FormData(form);

      // Debug opcional — mostra o conteúdo antes de enviar
      console.log('--- Enviando FormData ---');
      for (const pair of formData.entries()) {
        console.log(pair[0], pair[1]);
      }

      try {
        // 🔧 Envio sem headers — o próprio FormData define o Content-Type
        const res = await fetch(`${BASE_URL}/api/processos`, {
          method: 'POST',
          body: formData,
        });

        if (!res.ok) {
          const text = await res.text().catch(() => null);
          throw new Error(`Servidor retornou ${res.status}: ${text || 'sem corpo'}`);
        }

        const created = await res.json();

        // 🔧 Salva localmente para consulta offline
        const processos = JSON.parse(localStorage.getItem('processos') || '[]');
        processos.push(created);
        localStorage.setItem('processos', JSON.stringify(processos));

        alert(`✅ Processo criado com sucesso!\nCódigo: ${created.codigo || 'sem código'}`);
        form.reset();
        if (fileText) fileText.textContent = '';

      } catch (err) {
        console.error('❌ Erro ao criar processo:', err);
        alert('❌ Falha ao criar processo. Veja o console para detalhes.');
      }
    });
  }
});
