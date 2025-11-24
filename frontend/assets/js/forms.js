// forms.js (corrigido e revisado DEFINITIVO)
document.addEventListener('DOMContentLoaded', () => {
  const input = document.getElementById('anexo');
  const fileText = document.getElementById('fileText');
  const form = document.getElementById('procForm');

  // 🔧 SEU BACKEND
  const BASE_URL = 'https://mousetrack-erp.onrender.com';

  // Exibe nome(s) dos arquivos
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

  // =============================
  // ENVIO DO FORMULÁRIO
  // =============================
  if (form) {
    form.addEventListener('submit', async (e) => {
      e.preventDefault();

      // NÃO USAR new FormData(form) (quebra MultipartFile[])
      const formData = new FormData();

      // Campos simples
      formData.append("titulo", form.titulo.value);
      formData.append("tipo", form.tipo.value);
      formData.append("modal", form.modal.value);
      formData.append("observacao", form.observacao.value);

      // Arquivos (forma correta p/ MultipartFile[])
      const files = input.files;
      if (files && files.length > 0) {
        for (let i = 0; i < files.length; i++) {
          formData.append("arquivos", files[i]); // MESMO NOME!
        }
      }

      // Debug
      console.log("------ Enviando FormData ------");
      for (const pair of formData.entries()) {
        console.log(pair[0], pair[1]);
      }

      try {
        const res = await fetch(`${BASE_URL}/api/processos`, {
          method: 'POST',
          body: formData,
        });

        console.log("Status:", res.status);

        const raw = await res.text();
        let parsed = raw;

        try { parsed = JSON.parse(raw); } catch {}

        if (!res.ok) {
          throw new Error(`Erro HTTP ${res.status}: ${raw}`);
        }

        const created = parsed;

        // Salva no localStorage (cards)
        const processos = JSON.parse(localStorage.getItem('processos') || '[]');
        processos.push(created);
        localStorage.setItem('processos', JSON.stringify(processos));

        alert(`✅ Processo criado!\nCódigo: ${created?.codigo || '(sem código)'}`);

        form.reset();
        if (fileText) fileText.textContent = '';

      } catch (err) {
        console.error("❌ Erro ao criar processo:", err);
        alert("❌ Falha ao criar processo. Veja o console.");
      }
    });
  }
});
