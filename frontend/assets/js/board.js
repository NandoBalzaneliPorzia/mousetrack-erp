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

let selectedCard = null;
let currentType = "importacao"; // default

// ======================================
// CHECKLISTS
// (mantive as no seu código original)
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

  // Monta um título amigável pro chat usando o processo
  const codigo = selectedCard.codigo || selectedCard.id || "";
  const tituloProc = selectedCard.titulo || "";
  const tituloThread = (codigo ? `${codigo} - ${tituloProc || "Processo"}` : (tituloProc || "Processo"));

  try {
    // Usa seu endpoint já existente: POST /api/chat/threads
    const res = await fetch(api('/api/chat/threads'), {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({ titulo: tituloThread })
    });

    if (!res.ok) {
      console.error("Erro ao criar thread de chat:", res.status, res.statusText);
      alert("Não foi possível abrir o chat para este processo.");
      return;
    }

    const thread = await res.json();

    // Redireciona pra tela de chat já apontando pra essa thread
    window.location.href = `chat.html?threadId=${thread.id}`;
  } catch (err) {
    console.error("Erro ao abrir chat a partir do card:", err);
    alert("Erro ao abrir o chat. Tente novamente.");
  }
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
    // assegura campos mínimos
    if (!proc.id) proc.id = (proc.codigo || Math.random().toString(36).slice(2,9));
    if (!proc.codigo) proc.codigo = proc.id;

    const key = laneKeyFor(proc);
    // se a lane existir e for do tipo atualmente visível -> anexa
    if (lanes[key]) {
      lanes[key].appendChild(createCard(proc));
    } else {
      // se não existirem, opcionalmente logamos pra debug
      // console.info("Lane não encontrada para", key, proc);
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
