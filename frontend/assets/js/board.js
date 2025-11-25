// ======================================
// CONFIGURAÇÃO BASE
// ======================================
const BASE_URL = 'https://mousetrack-erp.onrender.com';

// containers das lanes no DOM
const importAereaContainer = document.getElementById('laneAerea');
const importMaritimaContainer = document.getElementById('laneMaritima');
const exportAereaContainer = document.getElementById('export-aerea');
const exportMaritimaContainer = document.getElementById('export-maritima');

// ======================================
// FUNÇÃO PARA CRIAR CARDS (ATUALIZADA)
// ======================================
function createCardElement(p) {
  const card = document.createElement('div');
  card.className = 'card';

  card.dataset.codigo = p.codigo;
  card.dataset.titulo = p.titulo;

  card.innerHTML = `
    <div class="card-head">
      <span class="code">${p.codigo}</span>
    </div>
    <div class="desc">${p.titulo}</div>
  `;

  // ➜ Ao clicar → abrir popover
  card.addEventListener('click', () => abrirPopover(p));

  return card;
}

// ======================================
// FUNÇÃO PARA ABRIR O POPOVER (NOVA)
// ======================================
function abrirPopover(p) {
  const pop = document.getElementById("cardPopover");

  // preencher campos
  document.getElementById("popTitle").textContent = `${p.codigo} - ${p.titulo}`;
  document.getElementById("pStart").value = p.inicio || "";
  document.getElementById("pEnd").value = p.fim || "";
  document.getElementById("pStatus").value = p.status || "Em andamento";
  document.getElementById("pObs").value = p.observacao || "";

  // checklist dinâmico
  const checklist = document.getElementById("pChecklist");
  checklist.innerHTML = "";

<<<<<<< HEAD
<<<<<<< HEAD
  const lane = cardEl.closest('.lane');
  const laneRect = lane.getBoundingClientRect();
  const boxWidth = 560;
  const left = Math.round(laneRect.left + (laneRect.width - boxWidth)/2);
  const top  = Math.round(laneRect.top + 8);

  popover.style.left = `${left}px`;
  popover.style.top  = `${top}px`;
  popover.hidden = false;

  docsMenu.setAttribute('hidden','');
=======
    // limpar lanes
    [
      importAereaContainer,
      importMaritimaContainer,
      exportAereaContainer,
      exportMaritimaContainer
    ].forEach(c => c.innerHTML = '');

    // render remotos
    processos.forEach(p => renderProcesso(p));

    // render locais
    const processosLocais = JSON.parse(localStorage.getItem('processos') || '[]');
>>>>>>> parent of b1d53f0 (arrumando lanes)

  renderChecklist(currentType, laneKey);
}

// ======================================
// FECHAR POPOVER
// ======================================
pClose.addEventListener('click', ()=> popover.hidden = true);
document.addEventListener('keydown', e=>{ if(e.key==='Escape') popover.hidden = true; });
document.addEventListener('click', e=>{
  if(!popover.hidden && !popover.contains(e.target) && !e.target.closest('.card')) {
    popover.hidden = true;
  }
<<<<<<< HEAD
});
=======
  if (p.checklist && Array.isArray(p.checklist)) {
    p.checklist.forEach(item => {
      const li = document.createElement("label");
      li.className = "check";
      li.innerHTML = `
        <input type="checkbox" class="round" ${item.done ? "checked" : ""}>
        <span>${item.label}</span>
      `;
      checklist.appendChild(li);
    });
  }
>>>>>>> parent of 115da1b (ajustando card)

  // mostrar popover
  pop.hidden = false;

  // posicionar centralizado
  const width = 560;
  const x = (window.innerWidth - width) / 2;
  const y = window.scrollY + 120;

  pop.style.left = `${x}px`;
  pop.style.top = `${y}px`;
}

// ======================================
// BOTÃO FECHAR DO POPUP
// ======================================
document.getElementById("pClose").addEventListener("click", () => {
  document.getElementById("cardPopover").hidden = true;
});

// ======================================
// CARREGAR PROCESSOS REMOTOS + LOCAIS
// ======================================
async function carregarProcessosRemotos() {
  try {
    const res = await fetch(`${BASE_URL}/api/processos`);
    if (!res.ok) throw new Error('Falha ao buscar processos remotos');

    const processos = await res.json();

    [
      importAereaContainer,
      importMaritimaContainer,
      exportAereaContainer,
      exportMaritimaContainer
    ].forEach(c => c.innerHTML = '');

    processos.forEach(p => renderProcesso(p));

    const processosLocais = JSON.parse(localStorage.getItem('processos') || '[]');

    processosLocais.forEach(p => {
      if (!document.querySelector(`[data-codigo="${p.codigo}"]`)) {
        renderProcesso(p);
      }
    });

  } catch (err) {
    console.error("Erro ao carregar processos:", err);
    renderLocalOnly();
  }
}

function renderLocalOnly() {
  const processos = JSON.parse(localStorage.getItem('processos') || '[]');
  processos.forEach(p => renderProcesso(p));
}

// ======================================
// RENDERIZA UM PROCESSO NA LANE CORRETA
// ======================================
function renderProcesso(p) {
  const tipo = (p.tipo && p.tipo.toLowerCase().includes('export'))
    ? 'exportacao'
    : 'importacao';

  const lane = (p.modal && p.modal.toLowerCase().includes('marit'))
    ? 'maritima'
    : 'aerea';

  const card = createCardElement(p);

  if (tipo === 'importacao') {
    if (lane === 'aerea') return importAereaContainer.appendChild(card);
    if (lane === 'maritima') return importMaritimaContainer.appendChild(card);
  } else {
    if (lane === 'aerea') return exportAereaContainer.appendChild(card);
    if (lane === 'maritima') return exportMaritimaContainer.appendChild(card);
  }
}

carregarProcessosRemotos();

// ========================================================================
// ALTERNAÇÃO IMPORTAÇÃO / EXPORTAÇÃO
// ========================================================================
const typeBtn = document.getElementById("typeBtn");
const typeLabel = document.getElementById("typeLabel");
const typeMenu = document.getElementById("typeMenu");

// abre/fecha menu
typeBtn.addEventListener("click", () => {
  typeMenu.hidden = !typeMenu.hidden;
});
<<<<<<< HEAD
typeMenu.querySelectorAll('li').forEach(li=>{
  li.addEventListener('click', ()=>{
    typeMenu.querySelectorAll('li').forEach(x=>x.classList.remove('active'));
    li.classList.add('active');
    currentType = li.dataset.type;
    typeLbl.textContent = li.textContent;
    typeMenu.setAttribute('hidden','');
    render();
=======
}

function renderLocalOnly() {
  const processos = JSON.parse(localStorage.getItem('processos') || '[]');
  processos.forEach(p => renderProcesso(p));
}

// ======================================
// RENDERIZA UM PROCESSO NA LANE CORRETA
// ======================================
function renderProcesso(p) {
  const tipo = (p.tipo && p.tipo.toLowerCase().includes('export'))
    ? 'exportacao'
    : 'importacao';

  const lane = (p.modal && p.modal.toLowerCase().includes('marit'))
    ? 'maritima'
    : 'aerea';

  const card = createCardElement(p);

  if (tipo === 'importacao') {
    if (lane === 'aerea') return importAereaContainer.appendChild(card);
    if (lane === 'maritima') return importMaritimaContainer.appendChild(card);
  } else {
    if (lane === 'aerea') return exportAereaContainer.appendChild(card);
    if (lane === 'maritima') return exportMaritimaContainer.appendChild(card);
  }
}

carregarProcessosRemotos();

// ========================================================================
// NOVO SISTEMA DE ALTERNAÇÃO IMPORTAÇÃO / EXPORTAÇÃO
// ========================================================================
const typeBtn = document.getElementById("typeBtn");
const typeLabel = document.getElementById("typeLabel");
const typeMenu = document.getElementById("typeMenu");

const lane1Title = document.getElementById("lane1Title");
const lane2Title = document.getElementById("lane2Title");

// abre/fecha menu
typeBtn.addEventListener("click", () => {
  typeMenu.hidden = !typeMenu.hidden;
});

// ---------------------------------------------------------------------
// NOVO SISTEMA DE EXIBIÇÃO DE LANES
// ---------------------------------------------------------------------
function atualizarLanes(tipo) {
  const lanes = document.querySelectorAll(".lane");

  lanes.forEach(lane => {
    const titulo = lane.querySelector("h2").textContent.toLowerCase();

    const isImport = titulo.includes("importação");
    const isExport = titulo.includes("exportação");

    if (tipo === "importacao") {
      lane.style.display = isImport ? "block" : "none";
    } else {
      lane.style.display = isExport ? "block" : "none";
    }
  });
}

=======

// exibe apenas as lanes compatíveis
function atualizarLanes(tipo) {
  const lanes = document.querySelectorAll(".lane");
  lanes.forEach(lane => {
    const laneTipo = lane.dataset.type;
    lane.style.display = laneTipo === tipo ? "block" : "none";
  });
}

// filtra cards por tipo (import/export)
>>>>>>> parent of 115da1b (ajustando card)
function atualizarCards(tipoAtual) {
  const cards = document.querySelectorAll(".card");

  cards.forEach(card => {
    const codigo = card.dataset.codigo?.toUpperCase() || "";

    const ehExport = codigo.startsWith("EX");
    const ehImport = codigo.startsWith("IN");

    if (tipoAtual === "exportacao" && ehExport) {
      card.style.display = "block";
    } else if (tipoAtual === "importacao" && ehImport) {
      card.style.display = "block";
    } else {
      card.style.display = "none";
    }
  });
}

<<<<<<< HEAD
// ---------------------------------------------------------------------
// MENU DE ALTERNAÇÃO
// ---------------------------------------------------------------------
=======
// click no item do menu
>>>>>>> parent of 115da1b (ajustando card)
typeMenu.querySelectorAll("li").forEach(item => {
  item.addEventListener("click", () => {
    const tipo = item.dataset.type;

    typeLabel.textContent = item.textContent;
    typeMenu.hidden = true;

<<<<<<< HEAD
    if (tipo === "importacao") {
      lane1Title.textContent = "Importação Marítima";
      lane2Title.textContent = "Importação Aérea";
    } else {
      lane1Title.textContent = "Exportação Marítima";
      lane2Title.textContent = "Exportação Aérea";
    }

    atualizarLanes(tipo);
    atualizarCards(tipo);
>>>>>>> parent of b1d53f0 (arrumando lanes)
=======
    atualizarLanes(tipo);
    atualizarCards(tipo);
>>>>>>> parent of 115da1b (ajustando card)
  });
});

// estado inicial
atualizarLanes("importacao");
