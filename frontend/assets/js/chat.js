/* -----------------------------------------------------------
 *  Responsável: Nando Balzaneli
 * ----------------------------------------------------------- */

console.log("⚠ Modo apresentação: Chat fictício ativo!");

// Lê parâmetros só para mostrar nome do convidado
const params = new URLSearchParams(window.location.search);
const nomeGuest = decodeURIComponent(params.get("nome") || "Convidado");

// Elementos principais
const chatPanelEl = document.getElementById("chatPanel");
const messagesEl = document.getElementById("messagesContainer");
const contactNameEl = document.getElementById("chatContactName");
const formEl = document.getElementById("chatForm");
const inputEl = document.getElementById("messageInput");

// Esconde sidebar e lista de conversas
document.querySelector(".sidebar")?.remove();
document.getElementById("threadList")?.remove();

// Chat fictício fixo
const threadDemo = {
  mensagens: [
    {
      autor: "Atendente",
      conteudo: "Olá! Tudo bem? 👋",
      enviadoEm: "15:30"
    },
    {
      autor: nomeGuest,
      conteudo: "Olá! Estou aqui para apresentar meu chat! 😄",
      enviadoEm: "15:31"
    },
    {
      autor: "Atendente",
      conteudo: "Que ótimo! Manda ver! 🚀",
      enviadoEm: "15:32"
    }
  ]
};

// Renderizar todas as mensagens
function renderMessages() {
  if (!messagesEl) return;

  messagesEl.innerHTML = "";

  threadDemo.mensagens.forEach(msg => {
    const bubble = document.createElement("div");
    bubble.className = `chat-bubble ${
      msg.autor === nomeGuest ? "me" : "them"
    }`;

    bubble.innerHTML = `
      <p>${msg.conteudo}</p>
      <span class="time">${msg.enviadoEm}</span>
    `;

    messagesEl.appendChild(bubble);
  });

  scrollMessagesToBottom();
}

// Enviar nova mensagem simulada
formEl?.addEventListener("submit", ev => {
  ev.preventDefault();

  const texto = inputEl.value.trim();
  if (!texto) return;

  threadDemo.mensagens.push({
    autor: nomeGuest,
    conteudo: texto,
    enviadoEm: new Date().toLocaleTimeString().slice(0, 5)
  });

  inputEl.value = "";
  renderMessages();
});

// Exibir painel sempre
chatPanelEl?.classList.remove("hidden");
contactNameEl.textContent = "Atendente";

// Rola automático para última mensagem
function scrollMessagesToBottom() {
  messagesEl.scrollTop = messagesEl.scrollHeight;
}

// Primeira exibição
renderMessages();
