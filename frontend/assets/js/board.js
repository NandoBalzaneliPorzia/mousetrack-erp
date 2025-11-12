// board.js (trecho de inicialização)
const BASE_URL = 'https://mouse-track-backend.onrender.com';

async function carregarProcessosRemotos() {
  try {
    const res = await fetch(`${BASE_URL}/api/processos`);
    if (!res.ok) throw new Error('Falha ao buscar processos');
    const processos = await res.json();

    // transforma processos em cards e insere em seed
    processos.forEach(p => {
      // p.tipo = 'importacao' ou 'exportacao'
      // p.modal = 'aereo' ou 'maritima'
      const lane = p.modal === 'maritimo' || p.modal === 'maritima' ? 'maritima' : 'aerea';
      const tipo = p.tipo || 'importacao';
      // converte nomes inconsistentes
      if (!seed[tipo]) seed[tipo] = { maritima: [], aerea: [] };
      // evita duplicatas (verifica código)
      const exists = seed[tipo][lane].some(c => c.id === p.codigo);
      if (!exists) seed[tipo][lane].push({ id: p.codigo, desc: p.titulo });
    });

    // também carrega processos que foram salvos no localStorage pelo forms (caso offline)
    const processosLoc = JSON.parse(localStorage.getItem('processos') || '[]');
    processosLoc.forEach(p => {
      const lane = p.modal === 'maritimo' || p.modal === 'maritima' ? 'maritima' : 'aerea';
      const tipo = p.tipo || 'importacao';
      const exists = seed[tipo][lane].some(c => c.id === p.codigo);
      if (!exists) seed[tipo][lane].push({ id: p.codigo, desc: p.titulo });
    });

    render();
  } catch (err) {
    console.error('Erro ao carregar processos remotos:', err);
    // fallback: usa somente seed local
    render();
  }
}

// chama carregarProcessosRemotos em vez de render() direto
carregarProcessosRemotos();
