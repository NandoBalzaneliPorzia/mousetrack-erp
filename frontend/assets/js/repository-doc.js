// -----------------------------
// PEGAR ID DO PROCESSO DA URL
// -----------------------------
function getParam(name) {
  return new URL(window.location.href).searchParams.get(name);
}

const codigo = getParam("id"); // EX: ABC12345


// -----------------------------
// CARREGAR DOCUMENTOS DO BACKEND
// -----------------------------
async function carregarDocumentos() {
  const resp = await fetch(`http://localhost:8080/api/processos/${codigo}/arquivos`);
  const docs = await resp.json();

  // Atualiza o título da página
  const title = document.getElementById("folderTitle");
  title.textContent = `Processo ${codigo}`;

  const tbody = document.getElementById("docsTbody");
  tbody.innerHTML = "";

  docs.forEach(d => {
    const tr = document.createElement("tr");

    tr.dataset.search = `${d.nomeArquivo} ${d.dataCriacao || ""}`.toLowerCase();

    tr.innerHTML = `
      <td class="col-icon">
        <img src="assets/img/icons/documento.svg" width="16">
      </td>
      <td class="doc-link" data-file="${d.nomeArquivo}">
        ${d.nomeArquivo}
      </td>
      <td class="col-date">${d.dataCriacao || "-"}</td>
    `;

    // Clique para download
    tr.querySelector(".doc-link").addEventListener("click", (e) => {
      e.stopPropagation();
      window.location.href = `http://localhost:8080/api/processos/${codigo}/download/${d.nomeArquivo}`;
    });

    tbody.appendChild(tr);
  });
}

carregarDocumentos();


// -----------------------------
// FILTRO DE DOCUMENTOS
// -----------------------------
document.getElementById("docSearch").addEventListener("input", () => {
  const q = docSearch.value.toLowerCase();
  document.querySelectorAll("#docsTbody tr").forEach(tr => {
    tr.style.display = tr.dataset.search.includes(q) ? "" : "none";
  });
});
