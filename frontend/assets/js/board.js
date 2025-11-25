// ======================================
// CONFIG
// ======================================
const BASE_URL = 'https://mousetrack-erp.onrender.com';

// lanes no DOM
const lanes = {
  "importacao-maritimo": document.getElementById("laneMaritima"),
  "importacao-aereo": document.getElementById("laneAerea"),
  "exportacao-maritimo": document.getElementById("export-maritima"),
  "exportacao-aereo": document.getElementById("export-aerea")
};

const laneTitles = {
  "importacao-maritimo": document.getElementById("lane1Title"),
  "importacao-aereo": document.getElementById("lane2Title")
};

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

let currentType = "importacao";

// ======================================
// POPUP ELEMENTOS
// ======================================
const popover  = document.getElementById('cardPopover');
const pTitle   = document.getElementById('popTitle');
const pChecklist = document.getElementById('pChecklist');
const pClose   = document.getElementById('pClose');

let selectedCard = null;

// ======================================
// CARD
// ======================================
function createCard(proc) {
  const el = document.createElement('article');
  el.className = 'card';
  el.dataset.id = proc.id;

  el.innerHTML = `
    <div class="card-head">
      <span class="code">${proc.codigo}</span>
      <span class="icons">⚙︎ ＋</span>
    </div>
    <div class="desc">${proc.titulo}</div>
  `;

  el.addEventListener("click", () => openPopover(proc));

  return el;
}

// ======================================
// POPOVER
// ======================================
function renderChecklist(tipo, modal) {
  pChecklist.innerHTML = "";
  checklists[tipo][modal].forEach(txt => {
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

  pTitle.textContent = `${proc.codigo} - ${proc.titulo}`;

  renderChecklist(proc.tipo, proc.modal);

  popover.hidden = false;
}

pClose.addEventListener("click", () => popover.hidden = true);

// ======================================
// RENDER
// ======================================
function renderBoard() {
  Object.values(lanes).forEach(l => l.innerHTML = "");

  const processos = JSON.parse(localStorage.getItem("processos") || "[]");

  processos.forEach(proc => {
    const key = `${proc.tipo}-${proc.modal}`;
    if (lanes[key]) {
      lanes[key].appendChild(createCard(proc));
    }
  });
}

// ======================================
// INIT
// ======================================
renderBoard();
