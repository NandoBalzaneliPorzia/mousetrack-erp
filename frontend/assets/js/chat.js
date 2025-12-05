// -------------------- MODO GUEST --------------------
const params = new URLSearchParams(window.location.search);
const isGuest = params.get("guest") === "1";
const processoGuest = params.get("processo");
const nomeGuest = decodeURIComponent(params.get("nome") || "Convidado");

// Se convidado, remover verificação de autenticação
if (!isGuest && typeof requireAuth === "function") {
  try { requireAuth(); } catch (e) {
    console.warn("RequireAuth bloqueou usuário: ", e);
  }
}

// Elementos da UI — declarados ANTES de usar!
const threadListEl  = document.getElementById('threadList');
const chatPanelEl   = document.getElementById('chatPanel');
const messagesEl    = document.getElementById('messagesContainer');
const contactNameEl = document.getElementById('chatContactName');
const avatarEl      = document.getElementById('chatAvatar');
const formEl        = document.getElementById('chatForm');
const inputEl       = document.getElementById('messageInput');

// Esconde interface não utilizada pelo guest
if (isGuest) {
  const sidebar = document.querySelector(".sidebar");
  if (sidebar) sidebar.style.display = "none";

  const wrapper = threadListEl?.parentElement;
  if (wrapper) wrapper.style.display = "none";
}

// Thread atual
let currentThreadId = null;

// -------------------- GUEST: Criar thread do processo --------------------
if (isGuest && processoGuest) {

  window.__pendingRedirect = true;

  fetch(api(`/api/chat/threads/processo/${processoGuest}`), {
    method: "POST"
  })
  .then(r => r.json())
  .then(thread => {
    location.href =
      `chat.html?threadId=${thread.id}&guest=1&nome=${encodeURIComponent(nomeGuest)}`;
  })
  .catch(err => {
    alert("Não foi possível abrir o chat deste processo (guest)");
    console.error(err);
  });
}

// -------------------- Normal (ou guest já redirecionado) --------------------
document.addEventListener("DOMContentLoaded", () => {

  if (window.__pendingRedirect) return;

  const threadId = Number(params.get("threadId"));
  if (!threadId) {
    messagesEl.innerHTML = "<p>Nenhuma thread encontrada.</p>";
    return;
  }

  currentThreadId = threadId;
  chatPanelEl.classList.remove('hidden');
  loadMessages();
});

// -------------------- Carregar mensagens --------------------
async function loadMessages() {
  messagesEl.innerHTML = "<p class='chat-empty'>Carregando...</p>";

  try {
    const res = await fetch(api(`/api/chat/threads/${currentThreadId}/messages`));
    if (!res.ok) throw new Error("Erro ao buscar mensagens");

    const msgs = await res.json();
    messagesEl.innerHTML = "";
    msgs.forEach(appendMessageBubble);
    scrollMessagesToBottom();

  } catch (err) {
    messagesEl.innerHTML = "<p class='chat-empty'>Erro ao carregar mensagens.</p>";
    console.error(err);
  }
}

// -------------------- Enviar mensagem --------------------
formEl.addEventListener("submit", async (ev) => {
  ev.preventDefault();
  const text = inputEl.value.trim();
  if (!text) return;

  const body = isGuest
    ? { autorNome: nomeGuest, conteudo: text }
    : { autorId: getUsuarioId(), conteudo: text };

  try {
    const res = await fetch(api(`/api/chat/threads/${currentThreadId}/messages`), {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });

    if (!res.ok) throw new Error("Erro ao enviar mensagem");

    const msg = await res.json();
    appendMessageBubble(msg);
    inputEl.value = "";
    scrollMessagesToBottom();

  } catch (err) {
    alert("Erro ao enviar mensagem");
    console.error(err);
  }
});

// -------------------- Interface visual --------------------
function appendMessageBubble(msg) {
  const isMine = isGuest
    ? msg.autorNome === nomeGuest
    : String(msg.autorId) === String(getUsuarioId());

  const div = document.createElement("div");
  div.className = `chat-bubble ${isMine ? 'me' : 'them'}`;
  div.textContent = msg.conteudo;
  messagesEl.appendChild(div);
}

function scrollMessagesToBottom() {
  messagesEl.scrollTop = messagesEl.scrollHeight;
}
