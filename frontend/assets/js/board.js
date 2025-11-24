// board.js
const BASE_URL = 'https://mousetrack-erp.onrender.com';

// estrutura mínima de seed
const seed = {
  importacao: { aerea: [], maritima: [] },
  exportacao: { aerea: [], maritima: [] }
};

// elementos no DOM (exemplo): containers para cada lane
const importAereaContainer = document.getElementById('laneAerea');
const importMaritimaContainer = document.getElementById('laneMaritima');
const exportAereaContainer = document.getElementById('export-aerea');
const exportMaritimaContainer = document.getElementById('export-maritima');

function createCardElement(p) {
  const card = document.createElement('div');
  card.className = 'card';
  card.dataset.codigo = p.codigo;
  card.innerHTML = `<strong>${p.titulo}</strong><div>${p.codigo}</div>`;
  // clique para abrir (implemente modal de detalhe depois)
  card.addEventListener('click', () => {
    // abrir modal com detalhes (implemente UI)
    alert(`Abrir processo ${p.codigo}\nTítulo: ${p.titulo}`);
  });
  return card;
}

async function carregarProcessosRemotos() {
  try {
    const res = await fetch(`${BASE_URL}/api/processos`);
    if (!res.ok) throw new Error('Falha ao buscar processos remotos');
    const processos = await res.json();

    // limpa containers
    [importAereaContainer, importMaritimaContainer, exportAereaContainer, exportMaritimaContainer]
      .forEach(c => { if (c) c.innerHTML = ''; });

    processos.forEach(p => {
      const tipo = (p.tipo && p.tipo.toLowerCase().includes('export')) ? 'exportacao' : 'importacao';
      const lane = (p.modal && p.modal.toLowerCase().includes('marit')) ? 'maritima' : 'aerea';

      const el = createCardElement(p);
      if (tipo === 'importacao') {
        if (lane === 'aerea' && importAereaContainer) importAereaContainer.appendChild(el);
        if (lane === 'maritima' && importMaritimaContainer) importMaritimaContainer.appendChild(el);
      } else {
        if (lane === 'aerea' && exportAereaContainer) exportAereaContainer.appendChild(el);
        if (lane === 'maritima' && exportMaritimaContainer) exportMaritimaContainer.appendChild(el);
      }
    });

    // também soma localStorage se necessário
    const processosLoc = JSON.parse(localStorage.getItem('processos') || '[]');
    processosLoc.forEach(p => {
      // evita duplicatas por codigo
      if (!document.querySelector(`[data-codigo="${p.codigo}"]`)) {
        const el = createCardElement(p);
        const tipo = (p.tipo && p.tipo.toLowerCase().includes('export')) ? 'exportacao' : 'importacao';
        const lane = (p.modal && p.modal.toLowerCase().includes('marit')) ? 'maritima' : 'aerea';
        if (tipo === 'importacao') {
          if (lane === 'aerea' && importAereaContainer) importAereaContainer.appendChild(el);
          if (lane === 'maritima' && importMaritimaContainer) importMaritimaContainer.appendChild(el);
        } else {
          if (lane === 'aerea' && exportAereaContainer) exportAereaContainer.appendChild(el);
          if (lane === 'maritima' && exportMaritimaContainer) exportMaritimaContainer.appendChild(el);
        }
      }
    });

  } catch (err) {
    console.error('Erro ao carregar processos remotos:', err);
    // fallback: apenas render local
    renderLocalOnly();
  }
}

function renderLocalOnly() {
  const processosLoc = JSON.parse(localStorage.getItem('processos') || '[]');
  processosLoc.forEach(p => {
    const el = createCardElement(p);
    const tipo = (p.tipo && p.tipo.toLowerCase().includes('export')) ? 'exportacao' : 'importacao';
    const lane = (p.modal && p.modal.toLowerCase().includes('marit')) ? 'maritima' : 'aerea';
    if (tipo === 'importacao') {
      if (lane === 'aerea' && importAereaContainer) importAereaContainer.appendChild(el);
      if (lane === 'maritima' && importMaritimaContainer) importMaritimaContainer.appendChild(el);
    } else {
      if (lane === 'aerea' && exportAereaContainer) exportAereaContainer.appendChild(el);
      if (lane === 'maritima' && exportMaritimaContainer) exportMaritimaContainer.appendChild(el);
    }
  });
}

// chama ao carregar a página
carregarProcessosRemotos();
