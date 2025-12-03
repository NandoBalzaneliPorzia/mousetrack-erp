// -------------------------------
// VARIÁVEIS DO FILTRO
// -------------------------------
const input = document.getElementById('repoSearch');
let rows = []; // será preenchido depois

function applyFilter() {
  const q = (input?.value || '').trim().toLowerCase();
  rows.forEach(tr => {
    const hay = (tr.dataset.search || '').toLowerCase();
    tr.style.display = hay.includes(q) ? '' : 'none';
  });
}

input?.addEventListener('input', applyFilter);


// -------------------------------
// FUNÇÃO PARA CARREGAR PROCESSOS
// -------------------------------
async function carregarProcessos() {
  const tbody = document.getElementById("repoTbody");
  if (!tbody) {
    console.error("Elemento tbody com id 'repoTbody' não encontrado no HTML.");
    return;
  }

  try {
    const resp = await fetch(api("/api/processos"));
    if (!resp.ok) {
      tbody.innerHTML = `<tr><td colspan="6" style="text-align:center;color:red;">Erro ao carregar processos</td></tr>`;
      return;
    }
    const lista = await resp.json();
    tbody.innerHTML = "";

    if (!Array.isArray(lista) || lista.length === 0) {
      tbody.innerHTML = `<tr><td colspan="6" style="text-align:center;">Nenhum processo encontrado</td></tr>`;
      rows = [];
      return;
    }

    lista.forEach(p => {
      const quantidade = p.quantidadeArquivos ?? 0;

      const tr = document.createElement("tr");

      tr.dataset.href = `repository-doc.html?id=${p.codigo}`;
      tr.dataset.search = `
        ${p.codigo}
        ${p.titulo}
        ${p.responsavel || ""}
        ${p.dataCriacao || ""}
        ${quantidade}
      `.toLowerCase();

      tr.innerHTML = `
        <td class="col-icon">
          <img src="assets/img/icons/pastinha.svg" width="16">
        </td>
        <td>${p.codigo}</td>
        <td>${p.titulo}</td>
        <td>${p.responsavel || "-"}</td>
        <td>${p.dataCriacao ? p.dataCriacao.slice(0, 10) : "-"}</td>
        <td class="col-qty">${quantidade}</td>
      `;

      tr.addEventListener("click", () => {
        window.location.href = tr.dataset.href;
      });

      tbody.appendChild(tr);
    });

    // Atualiza lista usada pelo filtro
    rows = Array.from(document.querySelectorAll('#repoTbody tr'));
  } catch (e) {
    tbody.innerHTML = `<tr><td colspan="6" style="text-align:center;color:red;">Erro ao carregar processos</td></tr>`;
    console.error(e);
  }
}


// -------------------------------
// INICIALIZA
// -------------------------------
if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", carregarProcessos);
} else {
  carregarProcessos();
}