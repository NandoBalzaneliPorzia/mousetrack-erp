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

// exibe apenas as lanes compatíveis
function atualizarLanes(tipo) {
  const lanes = document.querySelectorAll(".lane");
  lanes.forEach(lane => {
    const laneTipo = lane.dataset.type;
    lane.style.display = laneTipo === tipo ? "block" : "none";
  });
}

// filtra cards por tipo (import/export)
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

// click no item do menu
typeMenu.querySelectorAll("li").forEach(item => {
  item.addEventListener("click", () => {
    const tipo = item.dataset.type;

    typeLabel.textContent = item.textContent;
    typeMenu.hidden = true;

    atualizarLanes(tipo);
    atualizarCards(tipo);
  });
});

// estado inicial
atualizarLanes("importacao");
