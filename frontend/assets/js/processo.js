const params = new URLSearchParams(location.search);
const codigo = params.get("codigo");
const guest = params.get("guest") === "1"; // se for convidado
const email = params.get("email"); // nome no chat
const urlParams = new URLSearchParams(window.location.search); //front chama endpoint link email

async function carregarProcesso() {

    if (!codigo) {
        document.getElementById("processo").innerHTML =
            "<h3>Código do processo não informado.</h3>";
        return;
    }

    const resp = await fetch(`https://mousetrack-erp.onrender.com/api/processos/codigo/${codigo}`);
    if (!resp.ok) {
        document.getElementById("processo").innerHTML =
            "<h3>Processo não encontrado</h3>";
        return;
    }

    const proc = await resp.json();

    document.getElementById("processo").innerHTML = `
        <h2>${proc.codigo} - ${proc.titulo}</h2>
        <p><strong>Tipo:</strong> ${proc.tipo}</p>
        <p><strong>Modal:</strong> ${proc.modal}</p>
        <p><strong>Observação:</strong> ${proc.observacao || '—'}</p>
    `;

    // Se for convidado → exibe botão do chat
    if (guest) {
        document.getElementById("btnChat").style.display = "flex";
    }

    document.getElementById("btnChat").addEventListener("click", () => abrirChat(proc.id));
}

function abrirChat(processoId) {

    // Garante email no chat (nome do convidado)
    const nome = email ? encodeURIComponent(email) : "Convidado";

    const link = `https://mousetrack-frontend.onrender.com/chat.html?processo=${processoId}&guest=1&nome=${nome}`;

    window.location.href = link;
}

carregarProcesso();

    fetch(`/api/processos/codigo/${codigo}`)
    .then(resp => {
        if (!resp.ok) throw new Error('Processo não encontrado');
        return resp.json();
    })
    .then(data => {
        // renderiza o card do processo com os dados recebidos
    })
    .catch(err => {
        // mostra "Processo não encontrado"
    });

// buscando dados do processo 
fetch(`/api/processos/codigo/${codigo}`)
  .then(resp => resp.json())
  .then(processo => {


    // buscando arquivos associados ao processo
    fetch(`/api/processos/${codigo}/arquivos`)
      .then(resp => resp.json())
      .then(arquivos => {
        renderizarArquivos(arquivos);
      });
  });

function renderizarArquivos(arquivos) {
  const container = document.getElementById('arquivosContainer');
  if (!container) return;

  if (!arquivos.length) {
    container.innerHTML = "<p>Nenhum arquivo associado.</p>";
    return;
  }

  // Monta a lista de arquivos com link para download
  container.innerHTML = `
    <h3>Arquivos do Processo</h3>
    <ul>
      ${arquivos.map(a => `
        <li>
          <a href="/api/processos/download/${a.id}" target="_blank">${a.nomeArquivo}</a>
          (${a.dataCriacao ? a.dataCriacao.slice(0,10) : ''})
        </li>
      `).join('')}
    </ul>
  `;
}
