// ======================================
// CARREGA PROCESSOS DO LOCALSTORAGE
// ======================================
function loadProcessos() {
  return JSON.parse(localStorage.getItem("processos") || "[]");
}

// ======================================
// AGRUPA PROCESSOS POR TIPO E MODAL
// ======================================
function buildSeed() {
  const processos = loadProcessos();

  const seed = {
    importacao: { maritima: [], aerea: [] },
    exportacao: { maritima: [], aerea: [] },
  };

  processos.forEach((p) => {
    const tipo = p.tipo;       // importacao | exportacao
    const modal = p.modal;     // maritima | aerea

    seed[tipo][modal].push({
      id: p.codigo,
      desc: p.titulo,
      obs: p.observacao || "",
      anexos: p.arquivos || []
    });
  });

  return seed;
}

let seed = buildSeed();
let currentType = "importacao";

// ======================================
// ELEMENTOS DO DOM
// ======================================
const laneEls = {
  aerea: document.getElementById("laneAerea"),
  maritima: document.getElementById("laneMaritima")
};

const laneTitles = {
  aerea: document.getElementById("lane2Title"),
  maritima: document.getElementById("lane1Title")
};

// ======================================
// CHECKLISTS
// ======================================
const checklists = {
  importacao: {
    aerea: [
      "S.I Revisada/Recebida",
      "Reserva com Cia Aérea",
      "Carga Pronta",
      "Carga Coletada",
      "Carga Entregue: Aero Origem",
      "Tracking Feito",
      "LCL - Carga Solta Coletada",
      "Carga Entregue: Aero Destino"
    ],
    maritima: [
      "S.I Revisada/Recebida",
      "Reserva com Armador",
      "Carga Pronta",
      "Carga Coletada",
      "Carga Entregue: Porto Origem",
      "Tracking Feito",
      "FCL - Container Coletado",
      "LCL - Carga Solta Coletada",
      "Carga Entregue: Porto Destino"
    ]
  },
  exportacao: {
    aerea: [
      "Reserva com Cia Aérea",
      "Carga Pronta",
      "Carga Coletada",
      "Carga Entregue: Aero Origem",
      "AWB Entregue Cia Aérea",
      "DUE Liberada",
      "Tracking Feito",
      "Carga Entregue: Aero Destino",
      "LCL - Carga Solta Coletada"
    ],
    maritima: [
      "Reserva com Armador",
      "Carga Pronta",
      "Carga Coletada",
      "BL/Draft Entregue para Armador",
      "Carga Entregue: Porto Origem",
      "DUE Liberada",
      "Tracking Feito",
      "Carga Entregue: Porto Destino",
      "FCL - Container Coletado",
      "LCL - Carga Solta Coletada"
    ]
  }
};

// ======================================
// CRIA CARD
// ======================================
function cardEl(card, laneKey) {
  const el = document.createElement("article");
  el.className = "card";
  el.dataset.code = card.id;
  el.dataset.desc = card.desc.toLowerCase();
  el.dataset.lane = laneKey;

  el.innerHTML = `
    <div class="card-head">
      <span class="code">${card.id}</span>
    </div>
    <div class="desc">${card.desc}</div>
  `;

  el.addEventListener("click", () => openPopover(card, laneKey, el));
  return el;
}

// ======================================
// CHECKLIST NO POPOVER
// ======================================
function renderChecklist(tipo, modal) {
  const wrap = document.getElementById("pChecklist");
  wrap.innerHTML = "";

  checklists[tipo][modal].forEach((txt) => {
    wrap.innerHTML += `
      <label class="check">
        <input type="checkbox" class="pCheck">
        <span>${txt}</span>
      </label>
    `;
  });
}

// ======================================
// ABRE POPOVER
// ======================================
function openPopover(card, laneKey) {
  document.getElementById("popTitle").textContent = `${card.id} - ${card.desc}`;
  renderChecklist(currentType, laneKey);

  const popover = document.getElementById("cardPopover");
  popover.hidden = false;
}

// ======================================
// FECHAR POPOVER
// ======================================
document.getElementById("pClose").onclick = () => {
  document.getElementById("cardPopover").hidden = true;
};

// ======================================
// RENDERIZAÇÃO PRINCIPAL
// ======================================
function render() {
  seed = buildSeed();

  laneTitles.maritima.textContent =
    currentType === "importacao" ? "Importação Marítima" : "Exportação Marítima";

  laneTitles.aerea.textContent =
    currentType === "importacao" ? "Importação Aérea" : "Exportação Aérea";

  ["maritima", "aerea"].forEach((k) => {
    laneEls[k].innerHTML = "";
    seed[currentType][k].forEach((card) => {
      laneEls[k].appendChild(cardEl(card, k));
    });
  });
}

// ======================================
// TIPO IMPORTAÇÃO / EXPORTAÇÃO
// ======================================
const typeBtn = document.getElementById("typeBtn");
const typeMenu = document.getElementById("typeMenu");

typeBtn.onclick = () => typeMenu.toggleAttribute("hidden");

typeMenu.querySelectorAll("li").forEach((li) => {
  li.onclick = () => {
    currentType = li.dataset.type;

    document.getElementById("typeLabel").textContent = li.textContent;
    typeMenu.hidden = true;
    render();
  };
});

// ======================================
// SEARCH
// ======================================
document.getElementById("search").addEventListener("input", function () {
  const q = this.value.toLowerCase();

  document.querySelectorAll(".card").forEach((c) => {
    c.style.display =
      c.dataset.code.toLowerCase().includes(q) ||
      c.dataset.desc.includes(q)
        ? ""
        : "none";
  });
});

// ======================================
// INIT
// ======================================
render();
