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
      // tenta ler JSON de erro, senão mostra mensagem padrão
      const err = await resp.json().catch(() => null);
      console.warn("Erro ao buscar processo:", err);
      processoEl.innerHTML = "<h3>Processo não encontrado</h3>";
      return;
    }

    const proc = await resp.json();

    processoEl.innerHTML = `
      <h2>${escapeHtml(proc.codigo)} - ${escapeHtml(proc.titulo)}</h2>
      <p><strong>Tipo:</strong> ${escapeHtml(proc.tipo || '')}</p>
      <p><strong>Modal:</strong> ${escapeHtml(proc.modal || '')}</p>
      <p><strong>Observação:</strong> ${escapeHtml(proc.observacao) || '—'}</p>
    `;

    // habilita botão chat para convidados
    if (guest && btnChat) {
      btnChat.style.display = "flex";
      // (re)define listener
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

  const lista = arquivos.map(a => {
    const nome = escapeHtml(a.nomeArquivo || "sem-nome");
    const data = a.dataCriacao ? escapeHtml(String(a.dataCriacao).slice(0,10)) : "";
    // link absoluto para o backend (download)
    const href = `${BACKEND}/api/processos/download/${encodeURIComponent(a.id)}`;
    return `<li>
      <a href="${href}" target="_blank" rel="noopener noreferrer">${nome}</a>
      ${data ? ` <small>(${data})</small>` : ""}
    </li>`;
  }).join("");

  arquivosContainer.innerHTML = `
    <h3>Arquivos do Processo</h3>
    <ul>${lista}</ul>
  `;
}

function abrirChat(processoId) {
  const nome = email ? encodeURIComponent(email) : "Convidado";
  const link = `https://mousetrack-frontend.onrender.com/chat.html?processo=${encodeURIComponent(processoId)}&guest=1&nome=${nome}`;
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