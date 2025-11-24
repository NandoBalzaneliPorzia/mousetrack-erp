// ======================================
// CONFIGURAÇÃO BASE
// ======================================
const BASE_URL = 'https://mousetrack-erp.onrender.com';

// containers
const importAereaContainer = document.getElementById('laneAerea');
const importMaritimaContainer = document.getElementById('laneMaritima');
const exportAereaContainer = document.getElementById('export-aerea');
const exportMaritimaContainer = document.getElementById('export-maritima');


// ======================================
// FUNÇÃO PARA CRIAR CARDS
// ======================================
function createCardElement(p) {
  const card = document.createElement('div');
  card.className = 'card';
  card.dataset.codigo = p.codigo;
  card.dataset.tipo = (p.tipo && p.tipo.toLowerCase().includes("export")) ? "exportacao" : "importacao";

  card.innerHTML = `
    <strong>${p.titulo}</strong>
    <div>${p.codigo}</div>
  `;

  card.addEventListener("click", () => {
    alert(`Abrir processo ${p.codigo}\nTítulo: ${p.titulo}`);
  });

  return card;
}


// =========================================
// RENDERIZA UM PROCESSO NA LANE CORRETA
// =========================================
function renderProcesso(p) {
  const tipo = (p.tipo && p.tipo.toLowerCase().includes('export')) ? 'exportacao' : 'importacao';
  const lane = (p.modal && p.modal.toLowerCase().includes('marit')) ? 'maritima' : 'aerea';

  const card = createCardElement(p);

  if (tipo === 'importacao') {
    if (lane === 'aerea') return importAereaContainer.appendChild(card);
    if (lane === 'maritima') return importMaritimaContainer.appendChild(card);
  } else {
    if (lane === 'aerea') return exportAereaContainer.appendChild(card);
    if (lane === 'maritima') return exportMaritimaContainer.appendChild(card);
  }
}


// =========================================
// CARREGAR PROCESSOS REMOTOS + LOCAIS
// =========================================
async function carregarProcessosRemotos() {
  try {
    const res = await fetch(`${BASE_URL}/api/processos`);
    if (!res.ok) throw new Error("Falha ao buscar processos remotos");

    const processos = await res.json();

    // limpar lanes
    [
      importAereaContainer,
      importMaritimaContainer,
      exportAereaContainer,
      exportMaritimaContainer
    ].forEach(c => c.innerHTML = "");

    // remotos
    processos.forEach(p => renderProcesso(p));

    // locais
    const locais = JSON.parse(localStorage.getItem("processos") || "[]");

    locais.forEach(p => {
      if (!document.querySelector(`[data-codigo="${p.codigo}"]`)) {
        renderProcesso(p);
      }
    });

  } catch (err) {
    console.error("Erro:", err);
    renderLocalOnly();
  }
}

function renderLocalOnly() {
  const processos = JSON.parse(localStorage.getItem('processos') || '[]');
  processos.forEach(p => renderProcesso(p));
}

// carregar ao abrir
carregarProcessosRemotos();


// ========================================================================
//  SISTEMA DE ALTERNAÇÃO IMPORTAÇÃO / EXPORTAÇÃO
// ========================================================================

const typeBtn = document.getElementById("typeBtn");
const typeLabel = document.getElementById("typeLabel");
const typeMenu = document.getElementById("typeMenu");

const lane1Title = document.getElementById("lane1Title");
const lane2Title = document.getElementById("lane2Title");


// abrir / fechar menu
typeBtn.addEventListener("click", () => {
  typeMenu.hidden = !typeMenu.hidden;
});


// ================================
// MOSTRAR APENAS IMPORTAÇÃO OU EXPORTAÇÃO
// ================================
function atualizarVisibilidadeLanes(tipoAtual) {
  const lanesImport = document.querySelectorAll('[data-group="importacao"]');
  const lanesExport = document.querySelectorAll('[data-group="exportacao"]');

  if (tipoAtual === "importacao") {
    lanesImport.forEach(l => l.style.display = "block");
    lanesExport.forEach(l => l.style.display = "none");
  } else {
    lanesImport.forEach(l => l.style.display = "none");
    lanesExport.forEach(l => l.style.display = "block");
  }
}


// ================================
// FILTRAR CARDS PELO PREFIXO
// ================================
function atualizarVisibilidadeCards(tipoAtual) {
  const cards = document.querySelectorAll(".card");

  cards.forEach(card => {
    const codigo = card.dataset.codigo.toUpperCase();
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


// ================================
// EVENTO DO MENU
// ================================
typeMenu.querySelectorAll("li").forEach(item => {
  item.addEventListener("click", () => {

    const tipo = item.dataset.type; // importacao | exportacao

    typeMenu.querySelectorAll("li").forEach(li =>
      li.classList.remove("active")
    );
    item.classList.add("active");

    typeLabel.textContent = item.textContent;
    typeMenu.hidden = true;

    if (tipo === "importacao") {
      lane1Title.textContent = "Importação Marítima";
      lane2Title.textContent = "Importação Aérea";
    } else {
      lane1Title.textContent = "Exportação Marítima";
      lane2Title.textContent = "Exportação Aérea";
    }

    atualizarVisibilidadeLanes(tipo);
    atualizarVisibilidadeCards(tipo);
  });
});
