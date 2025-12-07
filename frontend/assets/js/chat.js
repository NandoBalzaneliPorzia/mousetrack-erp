//Responsável: Nando Balzaneli
// -------------------- MODO GUEST --------------------
const params = new URLSearchParams(window.location.search);

const isGuest = params.get("guest") === "1";
// aceita processo, processoId ou processoid
const processoGuest =
  params.get("processo") ||
  params.get("processoId") ||
  params.get("processoid");

const nomeGuest = decodeURIComponent(params.get("nome") || "Convidado");

// Se convidado, remover verificação de autenticação
if (!isGuest && typeof requireAuth === "function") {
  try {
    requireAuth();
  } catch (e) {
    console.warn("RequireAuth bloqueou usuário: ", e);
  }
}

// Elementos da UI
const threadListEl  = document.getElementById("threadList");
const chatPanelEl   = document.getElementById("chatPanel");
const messagesEl    = document.getElementById("messagesContainer");
const contactNameEl = document.getElementById("chatContactName");
const avatarEl      = document.getElementById("chatAvatar");
const formEl        = document.getElementById("chatForm");
const inputEl       = document.getElementById("messageInput");

// Esconde partes que o convidado não usa
if (isGuest) {
  const sidebar = document.querySelector(".sidebar");
  if (sidebar) sidebar.style.display = "none";

  const wrapper = threadListEl?.parentElement;
  if (wrapper) wrapper.style.display = "none";
}

// Thread atual
let currentThreadId = null;

// -------------------- GUEST: criar/pegar thread do processo --------------------
if (isGuest && processoGuest) {
  // marcamos que vamos redirecionar para não rodar o resto no meio
  window.__pendingRedirect = true;

  fetch(api(`/api/chat/threads/processo/${processoGuest}`), {
    method: "POST"
  })
    .then(r => {
      if (!r.ok) throw new Error("Falha ao criar/obter thread");
      return r.json();
    })
    .then(thread => {
      // redireciona já com threadId
      const url = new URL(window.location.href);
      url.searchParams.set("threadId", thread.id);
      url.searchParams.set("guest", "1");
      url.searchParams.set("nome", nomeGuest);
      // opcional: removemos o processo/Id da URL
      url.searchParams.delete("processo");
      url.searchParams.delete("processoId");
      url.searchParams.delete("processoid");

      window.location.href = url.toString();
    })
    .catch(err => {
      console.error("Erro ao criar thread para guest:", err);
      alert("Não foi possível abrir o chat desse processo.");
      window.__pendingRedirect = false;
    });
}

// -------------------- CARREGAR CHAT (guest já com threadId ou usuário logado) --------------------
document.addEventListener("DOMContentLoaded", () => {
  // se estamos esperando redirect, não faz nada
  if (window.__pendingRedirect) return;

  const threadIdParam = params.get("threadId");
  const threadId = threadIdParam ? Number(threadIdParam) : null;

  if (!threadId) {
    if (messagesEl) {
      messagesEl.innerHTML =
        "<p class='chat-empty'>Nenhuma conversa encontrada.</p>";
    }
    return;
  }

  currentThreadId = threadId;
  if (chatPanelEl) chatPanelEl.classList.remove("hidden");

  loadMessages();
});

// -------------------- Carregar mensagens --------------------
async function loadMessages() {
  if (!messagesEl) return;

  messagesEl.innerHTML = "<p class='chat-empty'>Carregando...</p>";

  try {
    const res = await fetch(api(`/api/chat/threads/${currentThreadId}/messages`));
    if (!res.ok) throw new Error("Erro ao buscar mensagens");

    const msgs = await res.json();
    messagesEl.innerHTML = "";
    msgs.forEach(appendMessageBubble);
    scrollMessagesToBottom();
  } catch (err) {
    console.error("Erro ao carregar mensagens:", err);
    messagesEl.innerHTML =
      "<p class='chat-empty'>Erro ao carregar mensagens.</p>";
  }
}

// -------------------- Enviar mensagem --------------------
if (formEl) {
  formEl.addEventListener("submit", async ev => {
    ev.preventDefault();
    const text = (inputEl.value || "").trim();
    if (!text || !currentThreadId) return;

    const body = isGuest
      ? { autorNome: nomeGuest, conteudo: text }
      : { autorId: getUsuarioId(), conteudo: text };

    try {
      const res = await fetch(
        api(`/api/chat/threads/${currentThreadId}/messages`),
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(body)
        }
      );

      if (!res.ok) throw new Error("Erro ao enviar mensagem");

      const msg = await res.json();
      appendMessageBubble(msg);
      inputEl.value = "";
      scrollMessagesToBottom();
    } catch (err) {
      console.error("Erro ao enviar mensagem:", err);
      alert("Erro ao enviar mensagem.");
    }
  });
}

// -------------------- Interface visual --------------------
function appendMessageBubble(msg) {
  if (!messagesEl) return;

  const meuId = getUsuarioId?.();
  const isMine = isGuest
    ? msg.autorNome === nomeGuest || msg.autorGuest === nomeGuest
    : msg.autorId != null && String(msg.autorId) === String(meuId);

  const div = document.createElement("div");
  div.className = `chat-bubble ${isMine ? "me" : "them"}`;
  div.textContent = msg.conteudo;
  messagesEl.appendChild(div);
}

function scrollMessagesToBottom() {
  if (!messagesEl) return;
  messagesEl.scrollTop = messagesEl.scrollHeight;
}
