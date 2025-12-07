// assets/js/board.js
// ======================================
// CONFIG
// ======================================

const guestAccess = new URLSearchParams(location.search).get("processoId");
if (guestAccess) {
    window.location.href = `/processo.html?processoId=${guestAccess}`;
}


const lanes = {
  // ids que existem no seu HTML
  "importacao-maritimo": document.getElementById("imp-maritima"),
  "importacao-aereo": document.getElementById("imp-aerea"),
  "exportacao-maritimo": document.getElementById("exp-maritima"),
  "exportacao-aereo": document.getElementById("exp-aerea")
};

const popover  = document.getElementById('cardPopover');
const pTitle   = document.getElementById('popTitle');
const pChecklist = document.getElementById('pChecklist');
const pClose   = document.getElementById('pClose');
const pChatBtn = document.getElementById('pChatBtn');
const pInspect = document.getElementById('pInspect');

let selectedCard = null;
let currentType = "importacao"; // default

// ======================================
// CHECKLISTS
// ======================================
const checklists = {
  importacao: {
    aereo: [
      "S.I Revisada/Recebida", "Reserva com Cia Aérea", "Carga Pronta",
      "Carga Coletada", "Carga Entregue: Aero Origem", "Tracking Feito",
      "LCL - Carga Solta Coletada", "Carga Entregue: Aero Destino"
    ],
    maritimo: [
      "S.I Revisada/Recebida", "Reserva com Armador", "Carga Pronta",
      "Carga Coletada", "Carga Entregue: Porto Origem", "Tracking Feito",
      "FCL - Container Coletado", "LCL - Carga Solta Coletada",
      "Carga Entregue: Porto Destino"
    ]
  },
  exportacao: {
    aereo: [
      "Reserva com Cia Aérea", "Carga Pronta", "Carga Coletada",
      "Carga Entregue: Aero Origem", "AWB Entregue Cia Aérea",
      "DUE Liberada", "Tracking Feito", "Carga Entregue: Aero Destino",
      "LCL - Carga Solta Coletada"
    ],
    maritimo: [
      "Reserva com Armador", "Carga Pronta", "Carga Coletada",
      "BL/Draft Entregue para Armador", "Carga Entregue: Porto Origem",
      "DUE Liberada", "Tracking Feito", "Carga Entregue: Porto Destino",
      "FCL - Container Coletado", "LCL - Carga Solta Coletada"
    ]
  }
};

// ======================================
// HELPERS: normalização de tipo/modal
// ======================================
function normalizeTipo(raw) {
  if (!raw) return 'importacao';
  const s = String(raw).toLowerCase();
  if (s.includes('export')) return 'exportacao';
  if (s.includes('exp')) return 'exportacao';
  return 'importacao';
}

function normalizeModal(raw) {
  if (!raw) return 'maritimo';
  const s = String(raw).toLowerCase();
  if (s.includes('aer') || s.includes('aéreo') || s.includes('aereo')) return 'aereo';
  return 'maritimo';
}

function laneKeyFor(proc) {
  const tipo = normalizeTipo(proc.tipo);
  const modal = normalizeModal(proc.modal);
  return `${tipo}-${modal}`;
}

// ======================================
// CARD
// ======================================
function createCard(proc) {
  const el = document.createElement('article');
  el.className = 'card';
  el.dataset.id = proc.id || proc.codigo || Math.random().toString(36).slice(2,9);

  // exibição segura: usa código e título se existirem
  const codigo = proc.codigo || el.dataset.id;
  const titulo = proc.titulo || '(Sem título)';

  el.innerHTML = `
    <div class="card-head">
      <span class="code">${codigo}</span>
      <span class="icons"></span>
    </div>
    <div class="desc">${titulo}</div>
  `;

  el.addEventListener("click", () => openPopover(proc));
  return el;
}

// ======================================
// POPOVER
// ======================================
function renderChecklist(tipo, modal) {
  pChecklist.innerHTML = "";
  const t = normalizeTipo(tipo);
  const m = normalizeModal(modal);
  const list = (checklists[t] && checklists[t][m]) || [];
  list.forEach(txt => {
    const lab = document.createElement("label");
    lab.className = "check";
    lab.innerHTML = `
      <input type="checkbox">
      <span>${txt}</span>
    `;
    pChecklist.appendChild(lab);
  });
}

function openPopover(proc) {
  selectedCard = proc;

  const codigo = proc.codigo || proc.id || '(sem código)';
  pTitle.textContent = `${codigo} - ${proc.titulo || '(Sem título)'}`;
  renderChecklist(proc.tipo, proc.modal);

  const cardEl = document.querySelector(`[data-id="${proc.id}"]`);
  if (!cardEl) return;

  popover.hidden = false;

  // FORÇA A RENDERIZAÇÃO DO TAMANHO
  const box = document.querySelector(".pop-box");
  const boxWidth = box.offsetWidth;
  const boxHeight = box.offsetHeight;

  const rect = cardEl.getBoundingClientRect();

  // Centralizado acima
  let left = rect.left + rect.width / 2 - boxWidth / 2;
  let top = rect.top - boxHeight - 14;

  if (left < 10) left = 10;
  if (left + boxWidth > window.innerWidth - 10)
    left = window.innerWidth - boxWidth - 10;

  if (top < 10) top = rect.bottom + 14;

  popover.style.left = `${left}px`;
  popover.style.top = `${top}px`;
}
pClose.addEventListener("click", () => {
  popover.hidden = true;
  selectedCard = null;
});

// BOTÃO DE CHAT DO CARD
if (pChatBtn) {
  pChatBtn.addEventListener("click", handleOpenChatFromCard);
}

async function handleOpenChatFromCard() {
  if (!selectedCard) {
    alert("Nenhum processo selecionado.");
    return;
  }

  // id do processo no banco (é o que o backend espera)
  const processoId = selectedCard.id;

  if (processoId == null) {
    alert("Processo sem ID. Recarregue a página.");
    console.error("selectedCard.id ausente:", selectedCard);
    return;
  }

  try {
    const res = await fetch(api(`/api/chat/threads/processo/${processoId}`), {
      method: "POST"
    });

    if (!res.ok) {
      const errorText = await res.text().catch(() => "");
      console.error("Erro ao criar/obter thread de chat:", res.status, res.statusText, errorText);
      alert("Não foi possível abrir o chat para este processo.");
      return;
    }

    const thread = await res.json();
    window.location.href = `chat.html?threadId=${thread.id}`;
  } catch (err) {
    console.error("Erro ao abrir chat a partir do card:", err);
    alert("Erro ao abrir o chat. Tente novamente.");
  }
}

// BOTÃO INSPECT - lupa: ir para repository-doc do processo selecionado
if (pInspect) {
  pInspect.addEventListener("click", () => {
    if (!selectedCard || !selectedCard.codigo) {
      alert("Nenhum processo selecionado ou processo sem código.");
      return;
    }

    const codigo = selectedCard.codigo;
    // abre a página de documentos do processo pelo
    window.location.href = `repository-doc.html?id=${encodeURIComponent(codigo)}`;
  });
}

// BOTÃO INSPECT - docs
const pInspectDocs = document.getElementById('pInspectDocs');

if (pInspectDocs) {
  pInspectDocs.addEventListener("click", () => {
    if (!selectedCard || !selectedCard.codigo) {
      alert("Nenhum processo selecionado ou processo sem código.");
      return;
    }

    const codigo = selectedCard.codigo;
    // abre a página de documentos do processo
    window.location.href = `repository-doc.html?id=${encodeURIComponent(codigo)}`;
  });
}

// BOTÃO DOWNLOAD DOCS
const pDownloadDocs = document.getElementById('pDownloadDocs');

if (pDownloadDocs) {
  pDownloadDocs.addEventListener("click", async () => {
    if (!selectedCard || !selectedCard.codigo) {
      alert("Nenhum processo selecionado ou processo sem código.");
      return;
    }

    const codigo = selectedCard.codigo;

    try {
      // Busca a lista de arquivos do processo
      const resp = await fetch(api(`/api/processos/${codigo}/arquivos`));
      if (!resp.ok) {
        alert("Erro ao carregar documentos do processo.");
        return;
      }

      const docs = await resp.json();

      if (!Array.isArray(docs) || docs.length === 0) {
        alert("Nenhum documento encontrado para este processo.");
        return;
      }

      // Se só tem 1 arquivo, baixa direto
      if (docs.length === 1) {
        window.location.href = api(`/api/processos/download/${docs[0].id}`);
        return;
      }

      // Se tem vários, pergunta qual baixar
      const lista = docs
        .map((d, i) => `${i + 1} - ${d.nomeArquivo}`)
        .join("\n");

      const escolha = prompt(
        `Arquivos deste processo:\n${lista}\n\nDigite o número do arquivo que deseja baixar:`
      );

      const index = Number(escolha) - 1;
      if (isNaN(index) || index < 0 || index >= docs.length) {
        return; // usuário cancelou ou digitou algo inválido
      }

      const docEscolhido = docs[index];
      window.location.href = api(`/api/processos/download/${docEscolhido.id}`);

    } catch (err) {
      console.error("Erro ao buscar/baixar documentos:", err);
      alert("Erro ao buscar documentos. Tente novamente.");
    }
  });
}

// BOTÃO "+" DO POPOVER ABRE INPUT DE E-MAIL
const pAdd = document.getElementById("pAdd");
const emailBox = document.getElementById("emailBox");
const emailInput = document.getElementById("emailInput");
const emailSendBtn = document.getElementById("emailSendBtn");

pAdd.addEventListener("click", () => {
  // alternar visibilidade do box
  emailBox.hidden = !emailBox.hidden;

  if (!emailBox.hidden) {
    emailInput.focus();
  }
});

// ===============================
// BOTÃO DE UPLOAD (pUpload)
// ===============================
const pUpload = document.getElementById("pUpload");

if (pUpload) {
  pUpload.addEventListener("click", () => {
    if (!selectedCard) {
      alert("Nenhum processo selecionado.");
      return;
    }

    const input = document.createElement("input");
    input.type = "file";
    input.multiple = true;
    input.accept = "*/*";

    input.addEventListener("change", () => {
      const files = Array.from(input.files);
      if (!files.length) return;

      console.log("Arquivos:", files);
      console.log("Processo:", selectedCard);
      const formData = new FormData();
files.forEach(f => formData.append("arquivos", f));

fetch(api(`/api/processos/${selectedCard.codigo}/arquivos`), {
  method: "POST",
  body: formData
})
.then(r => r.json())
.then(res => {
  alert("Arquivos enviados com sucesso!");
})
.catch(err => {
  console.error(err);
  alert("Erro ao enviar arquivos");
});
    });

    input.click();
  });
}

// 🔺🔺🔺 FIM DO CÓDIGO DO pUpload 🔺🔺🔺

// BOTÃO DE ENVIO DO E-MAIL (AGORA REAL!)
emailSendBtn.addEventListener("click", async () => {
  const email = emailInput.value.trim();
  if (!email) {
    alert("Digite um e-mail válido.");
    return;
  }

  // link único do processo
  const link = `${location.origin}/processo.html?codigo=${encodeURIComponent(selectedCard.codigo)}&guest=1`;

  try {
    const resp = await fetch("https://mousetrack-erp.onrender.com/email/enviar", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
        para: email,
        assunto: "Teste",
        mensagem: "Olá ! Segue o link do processo: " + link
      })
    });

    if (!resp.ok) {
      throw new Error("Erro ao enviar e-mail");
    }

    alert(`Link enviado para ${email}!`);
    emailInput.value = "";
    emailBox.hidden = true;

  } catch (err) {
    console.error("Erro ao enviar e-mail:", err);
    alert("Falha ao enviar o e-mail. Tente novamente.");
  }
});

// ======================================
// RENDER
// ======================================
function clearAllLanes() {
  Object.values(lanes).forEach(l => {
    if (l) l.innerHTML = "";
  });
}

function renderBoard() {
  clearAllLanes();

  const processosRaw = localStorage.getItem("processos") || "[]";
  let processos = [];
  try {
    processos = JSON.parse(processosRaw);
  } catch (e) {
    console.error("processos inválido no localStorage:", e);
    processos = [];
  }

processos.forEach(proc => {
  // NÃO inventar id; ele deve vir do backend.
  // Apenas garante que exista um código para exibir.
  if (!proc.codigo) {
    proc.codigo = proc.id || Math.random().toString(36).slice(2,9);
  }

  const key = laneKeyFor(proc);
  if (lanes[key]) {
    lanes[key].appendChild(createCard(proc));
  }
});

  // atualiza visibilidade das lanes conforme currentType
  updateLaneVisibility();
}

// ======================================
// TIPO SWITCH (mostrar só importacao OU exportacao)
// ======================================
const typeBtn = document.getElementById('typeBtn');
const typeLabel = document.getElementById('typeLabel');
const typeMenu = document.getElementById('typeMenu');

function updateLaneVisibility() {
  // mostra apenas as lanes do currentType
  Object.keys(lanes).forEach(k => {
    const element = lanes[k].closest('.lane') || lanes[k]; // pega container da lane
    const isMatch = k.startsWith(currentType);
    if (element) element.style.display = isMatch ? '' : 'none';
  });

  // atualiza label do botão
  if (typeLabel) {
    typeLabel.textContent = currentType === 'importacao' ? 'Importação' : 'Exportação';
  }
}

// abertura do menu e escolha
if (typeBtn && typeMenu) {
  typeBtn.addEventListener('click', () => {
    typeMenu.hidden = !typeMenu.hidden;
  });

  typeMenu.addEventListener('click', (ev) => {
    const li = ev.target.closest('li[data-type]');
    if (!li) return;
    currentType = li.dataset.type === 'exportacao' ? 'exportacao' : 'importacao';
    // fecha menu
    typeMenu.hidden = true;
    // re-render
    renderBoard();
  });
}

// esconde menu ao clicar fora
document.addEventListener('click', (e) => {
  if (!typeBtn) return;
  if (!typeBtn.contains(e.target) && !typeMenu.contains(e.target)) {
    if (typeMenu) typeMenu.hidden = true;
  }
});

// -------------------------------------------------
// Ouvir storage para atualizações vindas de outras abas/scripts
// -------------------------------------------------
window.addEventListener('storage', (ev) => {
  if (ev.key === 'processos' || ev.key === null) {
    renderBoard();
  }
});

// inicializa
renderBoard();
