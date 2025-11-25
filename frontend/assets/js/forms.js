// assets/js/forms.js
document.addEventListener('DOMContentLoaded', () => {
  const input = document.getElementById('anexo');
  const fileText = document.getElementById('fileText');
  const form = document.getElementById('procForm');

  // Ajuste para seu backend
  const BASE_URL = 'https://mousetrack-erp.onrender.com';

  if (input) {
    input.addEventListener('change', () => {
      if (!input.files || input.files.length === 0) {
        if (fileText) fileText.textContent = '';
        return;
      }
      if (fileText) {
        fileText.textContent =
          input.files.length === 1
            ? input.files[0].name
            : `${input.files.length} arquivos selecionados`;
      }
    });
  }

  if (!form) return;

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const formData = new FormData();

    // campos do formulário — ajuste nomes conforme seu HTML
    formData.append("titulo", form.titulo?.value || "");
    formData.append("tipo", form.tipo?.value || "importacao");
    formData.append("modal", form.modal?.value || "maritimo");
    formData.append("observacao", form.observacao?.value || "");

    // arquivos (se houver)
    if (input && input.files && input.files.length > 0) {
      for (let i = 0; i < input.files.length; i++) {
        formData.append("arquivos", input.files[i]); // mesmo nome para MultipartFile[]
      }
    }

    // debug do FormData (útil)
    console.log("------ Enviando FormData ------");
    for (const pair of formData.entries()) {
      console.log(pair[0], pair[1]);
    }

    try {
      const res = await fetch(`${BASE_URL}/api/processos`, {
        method: 'POST',
        body: formData
      });

      const text = await res.text();
      let data;
      try { data = JSON.parse(text); } catch { data = text; }

      if (!res.ok) {
        console.error("Erro HTTP:", res.status, text);
        alert("❌ Erro ao criar processo: " + res.status);
        return;
      }

      // 'data' é o processo criado pelo backend. Normaliza campos para o board.
      const created = data || {};

      // garante campos mínimos esperados pelo board.js
      const procForBoard = {
        id: created.id || created._id || created.codigo || ('local-' + Math.random().toString(36).slice(2,9)),
        codigo: created.codigo || created.id || created._id || undefined,
        titulo: created.titulo || form.titulo?.value || '(Sem título)',
        tipo: created.tipo || form.tipo?.value || 'importacao',
        modal: created.modal || form.modal?.value || 'maritimo',
        observacao: created.observacao || form.observacao?.value || '',
        // se quiser guardar arquivos/metadata, adicione aqui
      };

      // salva no localStorage (array processos)
      const processos = JSON.parse(localStorage.getItem('processos') || '[]');
      processos.push(procForBoard);
      localStorage.setItem('processos', JSON.stringify(processos));

      // dispara evento storage para forçar listeners (board.js) a re-render
      window.dispatchEvent(new StorageEvent('storage', { key: 'processos', newValue: JSON.stringify(processos) }));

      alert(`✅ Processo criado!\nCódigo: ${procForBoard.codigo || procForBoard.id}`);

      form.reset();
      if (fileText) fileText.textContent = '';

    } catch (err) {
      console.error("❌ Erro ao criar processo:", err);
      alert("❌ Falha ao criar processo. Veja console.");
    }
  });
});
