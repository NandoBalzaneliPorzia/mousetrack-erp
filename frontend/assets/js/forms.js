// assets/js/forms.js
document.addEventListener('DOMContentLoaded', () => {

  const BASE_URL = 'https://mousetrack-erp.onrender.com';

  const form = document.getElementById('procForm');
  const input = document.getElementById('anexo');
  const fileText = document.getElementById('fileText');

const guestAccess = new URLSearchParams(location.search).get("processoId");
if (guestAccess) {
    window.location.href = `/processo.html?processoId=${guestAccess}`;
}


  if (input) {
    input.addEventListener('change', () => {
      if (!input.files?.length) {
        fileText.textContent = '';
        return;
      }
      fileText.textContent =
        input.files.length === 1
          ? input.files[0].name
          : `${input.files.length} arquivos selecionados`;
    });
  }

  if (!form) return;

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const titulo = form.titulo?.value || "";
    const tipo = form.tipo?.value || "importacao";
    const modal = form.modal?.value || "maritimo";
    const observacao = form.observacao?.value || "";

    const formData = new FormData();
    formData.append("titulo", titulo);
    formData.append("tipo", tipo);
    formData.append("modal", modal);
    formData.append("observacao", observacao);

    if (input && input.files && input.files.length > 0) {
      for (let i = 0; i < input.files.length; i++) {
        formData.append("arquivos", input.files[i]); // nome esperado pelo backend
      }
    }

    console.log("------ Enviando FormData ------");
    for (const [key, value] of formData.entries()) {
      console.log(key, value);
    }

    try {
      const res = await fetch(`${BASE_URL}/api/processos`, {
        method: 'POST',
        body: formData
      });

      const rawText = await res.text();
      let data;
      try { data = JSON.parse(rawText); } catch { data = {}; }

      if (!res.ok) {
        console.error("Erro HTTP:", res.status, rawText);
        alert("❌ Erro ao criar processo!");
        return;
      }

      const procId = data.id || data.codigo || ("local-" + Date.now());

      const novoProcesso = {
        id: procId,
        codigo: data.codigo || procId,
        titulo,
        tipo,
        modal,
        observacao,
      };

      const processos = JSON.parse(localStorage.getItem("processos") || "[]");
      processos.push(novoProcesso);
      localStorage.setItem("processos", JSON.stringify(processos));

      window.dispatchEvent(new StorageEvent('storage', {
        key: 'processos',
        newValue: JSON.stringify(processos),
      }));

      alert(`🎯 Processo criado com sucesso!\nCódigo: ${novoProcesso.codigo}`);

      form.reset();
      if (fileText) fileText.textContent = "";

    } catch (err) {
      console.error("❌ Falha ao criar processo:", err);
      alert("❌ Falha ao criar processo. Verifique o console!");
    }
  });
});
