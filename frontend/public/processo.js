const params = new URLSearchParams(location.search);
const codigo = params.get("codigo");
const guest = params.get("guest") === "1"; // se for convidado
const email = params.get("email"); // nome no chat

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
