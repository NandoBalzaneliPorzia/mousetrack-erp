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
  const tbody = document.getElementById("docsTbody");
  const title = document.getElementById("folderTitle");
  if (!tbody) {
    console.error("Elemento tbody com id 'docsTbody' não encontrado no HTML.");
    return;
  }
  if (!title) {
    console.error("Elemento com id 'folderTitle' não encontrado no HTML.");
    return;
  }

  try {
    const resp = await fetch(api(`/api/processos/${codigo}/arquivos`));
    if (!resp.ok) {
      tbody.innerHTML = `<tr><td colspan="3" style="text-align:center;color:red;">Erro ao carregar documentos</td></tr>`;
      return;
    }
    const docs = await resp.json();

    title.textContent = `Processo ${codigo}`;
    tbody.innerHTML = "";

    if (!Array.isArray(docs) || docs.length === 0) {
      tbody.innerHTML = `<tr><td colspan="3" style="text-align:center;">Nenhum documento encontrado</td></tr>`;
      return;
    }

    docs.forEach(d => {
      const tr = document.createElement("tr");
      // Use o nome correto do campo do DTO (provavelmente nomeArquivo)
      tr.dataset.search = `${d.nomeArquivo} ${d.dataCriacao || ""}`.toLowerCase();

      tr.innerHTML = `
        <td class="col-icon">
          <img src="assets/img/icons/documento.svg" width="16">
        </td>
        <td class="doc-link" data-id="${d.id}" style="cursor:pointer; color:#007bff; text-decoration:underline;">
          ${d.nomeArquivo}
        </td>
        <td class="col-date">${d.dataCriacao ? d.dataCriacao.slice(0, 10) : "-"}</td>
      `;

      // Clique para download
      tr.querySelector(".doc-link").addEventListener("click", () => {
        window.location.href = api(`/api/processos/download/${d.id}`);
      });

      tbody.appendChild(tr);
    });
  } catch (e) {
    tbody.innerHTML = `<tr><td colspan="3" style="text-align:center;color:red;">Erro ao carregar documentos</td></tr>`;
    console.error(e);
  }
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