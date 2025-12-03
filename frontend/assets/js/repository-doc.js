// -----------------------------
// PEGAR ID DO PROCESSO DA URL
// -----------------------------
function getParam(name) {
  return new URL(window.location.href).searchParams.get(name);
}

let codigo = getParam("id");  // agora só "id" é necessário

if (!codigo) {
  alert("Código do processo não encontrado na URL.");
  throw new Error("Código não encontrado");
}


// -----------------------------
// CARREGAR DOCUMENTOS DO BACKEND
// -----------------------------
async function carregarDocumentos() {
  const resp = await fetch(api(`/api/processos/${codigo}/arquivos`));
  const docs = await resp.json();

  const title = document.getElementById("folderTitle");
  title.textContent = `Processo ${codigo}`;

  const tbody = document.getElementById("docsTbody");
  tbody.innerHTML = "";

  docs.forEach(d => {
    const tr = document.createElement("tr");
    tr.dataset.search = `${d.nome} ${d.dataCriacao || ""}`.toLowerCase();

    tr.innerHTML = `
      <td class="col-icon">
        <img src="assets/img/icons/documento.svg" width="16">
      </td>
      <td class="doc-link" data-id="${d.id}">
        ${d.nome}
      </td>
      <td class="col-date">${d.dataCriacao ? d.dataCriacao.slice(0, 10) : "-"}</td>
    `;

    // Clique para download
    tr.querySelector(".doc-link").addEventListener("click", () => {
      window.location.href = api(`/api/processos/download/${d.id}`);
    });

    tbody.appendChild(tr);
  });
}

carregarDocumentos();


// -----------------------------
// FILTRO DE DOCUMENTOS
// -----------------------------
document.getElementById("docSearch").addEventListener("input", () => {
  const q = document.getElementById("docSearch").value.toLowerCase();

  document.querySelectorAll("#docsTbody tr").forEach(tr => {
    tr.style.display = tr.dataset.search.includes(q) ? "" : "none";
  });
});
