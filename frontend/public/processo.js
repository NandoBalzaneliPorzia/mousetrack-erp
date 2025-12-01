async function carregarProcesso() {
  const processoId = new URLSearchParams(location.search).get("processoId");

  if (!processoId) {
    document.body.innerHTML = "<h3>Processo inválido</h3>";
    return;
  }

  const resp = await fetch(`https://mousetrack-erp.onrender.com/api/processos/${processoId}`);

  if (!resp.ok) {
    document.body.innerHTML = "<h3>Processo não encontrado</h3>";
    return;
  }

  const proc = await resp.json();

  document.getElementById("processo").innerHTML = `
    <h2>${proc.codigo} - ${proc.titulo}</h2>
    <p><strong>Tipo:</strong> ${proc.tipo}</p>
    <p><strong>Modal:</strong> ${proc.modal}</p>
    <p><strong>Observação:</strong> ${proc.observacao || '—'}</p>
  `;

  document.getElementById("btnChat").addEventListener("click", abrirChat);
}

function abrirChat() {
  const processoId = new URLSearchParams(location.search).get("processoId");

  fetch(`https://mousetrack-erp.onrender.com/api/chat/threads/processo/${processoId}`, {
    method: "POST",
    credentials: "include"
  })
  .then(r => r.json())
  .then(thread => {
    window.location.href = `/chat.html?threadId=${thread.id}`;
  });
}

carregarProcesso();
