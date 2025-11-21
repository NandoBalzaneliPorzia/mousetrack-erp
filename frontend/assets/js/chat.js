// assets/js/chat.js

// Dados fake só para a interface funcionar
const threadsMock = [
  {
    id: 1,
    nome: "Juliana Prado",
    preview: "Bom dia, tudo bem?",
    unread: 1,
    mensagens: [
      { id: 1, autor: "them", texto: "Bom dia!" },
      { id: 2, autor: "them", texto: "Tudo bem?" }
    ]
  },
  {
    id: 2,
    nome: "Ana Maranhão",
    preview: "Estou com uma dúvida.",
    unread: 3,
    mensagens: [
      { id: 1, autor: "them", texto: "Bom dia!" },
      { id: 2, autor: "them", texto: "Tudo bem?" },
      { id: 3, autor: "them", texto: "Estou com uma dúvida." }
    ]
  },
  {
    id: 3,
    nome: "Laura Nogueira",
    preview: "Vou verificar.",
    unread: 0,
    mensagens: [
      { id: 1, autor: "them", texto: "Vou verificar." }
    ]
  }
];

let currentThreadId = null;

document.addEventListener("DOMContentLoaded", () => {
  const threadListEl = document.getElementById("threadList");
  const messagesEl = document.getElementById("messagesContainer");
  const contactNameEl = document.getElementById("chatContactName");
  const formEl = document.getElementById("chatForm");
  const messageInputEl = document.getElementById("messageInput");
  const searchInputEl = document.getElementById("searchInput");

  let filteredThreads = [...threadsMock];

  // Render inicial
  renderThreadList(filteredThreads);
  renderEmptyState();

  // Clique em uma conversa
  threadListEl.addEventListener("click", (event) => {
    const item = event.target.closest("[data-thread-id]");
    if (!item) return;

    const id = Number(item.dataset.threadId);
    openThread(id);
  });

  // Enviar mensagem
  formEl.addEventListener("submit", (event) => {
    event.preventDefault();
    const text = messageInputEl.value.trim();
    if (!text || currentThreadId == null) return;

    const thread = threadsMock.find((t) => t.id === currentThreadId);
    if (!thread) return;

    // Cria mensagem "me"
    const msg = {
      id: Date.now(),
      autor: "me",
      texto: text
    };
    thread.mensagens.push(msg);
    thread.preview = text;
    thread.unread = 0; // você é quem está vendo, então zera

    messageInputEl.value = "";
    appendMessageBubble(msg);
    scrollMessagesToBottom();

    // Atualiza lista (preview)
    renderThreadList(filteredThreads);
  });

  // Filtro da busca
  if (searchInputEl) {
    searchInputEl.addEventListener("input", () => {
      const term = searchInputEl.value.toLowerCase();
      filteredThreads = threadsMock.filter((t) =>
        t.nome.toLowerCase().includes(term)
      );
      renderThreadList(filteredThreads);
    });
  }

  // ---------- FUNÇÕES ----------

  function renderThreadList(list) {
    threadListEl.innerHTML = "";

    list.forEach((thread) => {
      const btn = document.createElement("button");
      btn.type = "button";
      btn.className =
        "chat-list-item" +
        (thread.id === currentThreadId ? " is-active" : "");
      btn.dataset.threadId = thread.id;

      const initials = getInitials(thread.nome);

      btn.innerHTML = `
        <div class="chat-list-meta">
          <div class="chat-list-avatar">${initials}</div>
          <div class="chat-list-text">
            <span class="chat-list-name">${thread.nome}</span>
            <span class="chat-list-preview">${thread.preview || ""}</span>
          </div>
        </div>
        <div class="chat-list-unread" ${
          thread.unread ? "" : "hidden"
        }>${thread.unread}</div>
      `;

      threadListEl.appendChild(btn);
    });
  }

  function openThread(id) {
    currentThreadId = id;
    const thread = threadsMock.find((t) => t.id === id);
    if (!thread) return;

    // Zera não lidas
    thread.unread = 0;

    // Atualiza cabeçalho
    contactNameEl.textContent = thread.nome;
    document.querySelector(".chat-panel__avatar").textContent =
      getInitials(thread.nome);

    // Atualiza lista (highlight + contagem)
    renderThreadList(filteredThreads);

    // Render mensagens
    messagesEl.innerHTML = "";
    thread.mensagens.forEach(appendMessageBubble);
    scrollMessagesToBottom();
  }

  function appendMessageBubble(msg) {
    const div = document.createElement("div");
    div.className = "chat-bubble " + (msg.autor === "me" ? "me" : "them");
    div.textContent = msg.texto;
    messagesEl.appendChild(div);
  }

  function renderEmptyState() {
    messagesEl.innerHTML =
      '<p class="chat-empty">Selecione uma conversa na lista ao lado.</p>';
  }

  function scrollMessagesToBottom() {
    messagesEl.scrollTop = messagesEl.scrollHeight;
  }

  function getInitials(name) {
    return name
      .split(" ")
      .filter(Boolean)
      .slice(0, 2)
      .map((part) => part[0].toUpperCase())
      .join("");
  }
});
