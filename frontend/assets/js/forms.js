// forms.js (corrigido e revisado)
document.addEventListener('DOMContentLoaded', () => {
  const input = document.getElementById('anexo');
  const fileText = document.getElementById('fileText');
  const form = document.getElementById('procForm');

  // 🔧 ATUALIZE para o domínio EXATO do seu backend
  const BASE_URL = 'https://mousetrack-erp.onrender.com';

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

      const formData = new FormData(form);

      console.log('--- Enviando FormData ---');
      for (const pair of formData.entries()) {
        console.log(pair[0], pair[1]);
      }

      try {
        const res = await fetch(`${BASE_URL}/api/processos`, {
          method: 'POST',
          body: formData,
        });

        console.log("Status:", res.status);
        console.log("Headers:", [...res.headers.entries()]);

        let rawBody = await res.text();
        console.log("Raw body:", rawBody);

        let parsed;
        try {
          parsed = JSON.parse(rawBody);
        } catch {
          parsed = rawBody;
        }

        if (!res.ok) {
          throw new Error(`Erro HTTP ${res.status}: ${rawBody}`);
        }

        const created = parsed;

        // Salvar localmente
        const processos = JSON.parse(localStorage.getItem('processos') || '[]');
        processos.push(created);
        localStorage.setItem('processos', JSON.stringify(processos));

        alert(`✅ Processo criado com sucesso!\nCódigo: ${created?.codigo || 'sem código'}`);

        form.reset();
        if (fileText) fileText.textContent = '';

      } catch (err) {
        console.error("❌ Erro ao criar processo:", err);
        alert("❌ Falha ao criar processo. Veja o console para detalhes.");
      }
    });
  }
});
