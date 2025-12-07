//Responsável: Nando Balzaneli
const BACKEND = "https://mousetrack-erp.onrender.com"; // Base URL do backend

const params = new URLSearchParams(window.location.search);
const codigo = params.get("codigo");
const guest = params.get("guest") === "1";
const email = params.get("email");

// Elementos
const processoEl = document.getElementById("processo");
const arquivosContainer = document.getElementById("arquivosContainer");
const btnChat = document.getElementById("btnChat");

async function carregarProcesso() {
  if (!codigo) {
    processoEl.innerHTML = "<h3>Código do processo não informado.</h3>";
    return;
  }

  try {
    const resp = await fetch(`${BACKEND}/api/processos/codigo/${encodeURIComponent(codigo)}`);
    if (!resp.ok) {
      const err = await resp.json().catch(() => null);
      console.warn("Erro ao buscar processo:", err);
      processoEl.innerHTML = "<h3>Processo não encontrado</h3>";
      return;
    }

    const proc = await resp.json();

    // Render do card do processo usando as classes esperadas pelo CSS
    processoEl.innerHTML = `
      <h2>${escapeHtml(proc.codigo)} - ${escapeHtml(proc.titulo)}</h2>
      <div class="meta-row" aria-hidden="true">
        <div class="meta"><strong>Tipo:</strong> ${escapeHtml(proc.tipo || '')}</div>
        <div class="meta"><strong>Modal:</strong> ${escapeHtml(proc.modal || '')}</div>
      </div>
      <div class="observacao"><strong>Observação:</strong> ${escapeHtml(proc.observacao) || '—'}</div>
    `;

    // habilita botão chat para convidados
    if (guest && btnChat) {
      btnChat.style.display = "flex";
      btnChat.onclick = () => abrirChat(proc.id);
    }

    // carregar arquivos deste processo
    await carregarArquivosDoProcesso(codigo);

  } catch (e) {
    console.error("Erro ao carregar processo:", e);
    processoEl.innerHTML = "<h3>Erro ao carregar o processo. Tente novamente mais tarde.</h3>";
  }
}

async function carregarArquivosDoProcesso(codigoDoProcesso) {
  if (!arquivosContainer) return;

  arquivosContainer.innerHTML = "<p>Carregando arquivos...</p>";

  try {
    const resp = await fetch(`${BACKEND}/api/processos/${encodeURIComponent(codigoDoProcesso)}/arquivos`);
    if (!resp.ok) {
      const err = await resp.json().catch(() => null);
      console.warn("Erro ao buscar arquivos:", err);
      arquivosContainer.innerHTML = "<p>Não foi possível carregar os arquivos.</p>";
      return;
    }

    const arquivos = await resp.json();
    renderizarArquivos(arquivos);
  } catch (e) {
    console.error("Erro ao carregar arquivos:", e);
    arquivosContainer.innerHTML = "<p>Erro ao carregar arquivos.</p>";
  }
}

function renderizarArquivos(arquivos) {
  if (!arquivosContainer) return;

  if (!Array.isArray(arquivos) || arquivos.length === 0) {
    arquivosContainer.innerHTML = "<p>Nenhum arquivo associado.</p>";
    return;
  }

  const itens = arquivos.map(a => {
    const nome = escapeHtml(a.nomeArquivo || "sem-nome");
    const data = a.dataCriacao ? escapeHtml(String(a.dataCriacao).slice(0,10)) : "";
    const id = encodeURIComponent(a.id);
    const hrefDownload = `${BACKEND}/api/processos/download/${id}`;

    // ícone simples com extensão (ex: XML, PDF, XLS)
    const ext = (a.nomeArquivo || "").split('.').pop().toUpperCase().slice(0,4) || "FILE";
    const icon = escapeHtml(ext);

    return `
      <li>
        <div class="file-left">
          <span class="file-icon" aria-hidden="true">${icon}</span>
          <a class="file-name" href="${hrefDownload}" target="_blank" rel="noopener noreferrer">${nome}</a>
        </div>

        <div class="file-actions">
          <span class="file-meta">${data}</span>
          <a class="btn-download" href="${hrefDownload}" target="_blank" rel="noopener noreferrer" title="Download ${nome}">Baixar</a>
        </div>
      </li>
    `;
  }).join("");

  arquivosContainer.innerHTML = `
    <h3>Arquivos do Processo</h3>
    <ul>${itens}</ul>
  `;
}

function abrirChat(processoId) {
  const nome = email ? encodeURIComponent(email) : "Convidado";
  // agora usamos processoId, que é o que o chat.js já procura
  const link = `https://mousetrack-frontend.onrender.com/chat.html?processoId=${encodeURIComponent(processoId)}&guest=1&nome=${nome}`;
  window.location.href = link;
}

/** Pequena função para escapar HTML ao injetar strings no innerHTML */
function escapeHtml(str) {
  if (str == null) return "";
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}

// Inicializa a página
carregarProcesso();