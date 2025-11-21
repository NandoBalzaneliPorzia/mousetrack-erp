// assets/js/chat.js
// Integração do chat com o backend Java (REST)

document.addEventListener('DOMContentLoaded', () => {
  // se requireAuth existir, aplica
  if (typeof requireAuth === 'function') {
    try { requireAuth(); } catch (e) { return; }
  }

  const threadListEl  = document.getElementById('threadList');
  const chatPanelEl   = document.getElementById('chatPanel');
  const messagesEl    = document.getElementById('messagesContainer');
  const contactNameEl = document.getElementById('chatContactName');
  const avatarEl      = document.getElementById('chatAvatar');
  const formEl        = document.getElementById('chatForm');
  const inputEl       = document.getElementById('messageInput');
  const searchEl      = document.getElementById('searchInput');

  let currentThreadId = null;
  let allThreads = [];

  if (!threadListEl || !messagesEl || !formEl) return;

  // Carrega lista de conversas ao abrir a tela
  loadThreads();

  // Envio de mensagem
  formEl.addEventListener('submit', async (ev) => {
    ev.preventDefault();
    const text = (inputEl.value || '').trim();
    if (!text || !currentThreadId) return;

    const submitBtn = formEl.querySelector('button[type="submit"]');
    if (submitBtn) submitBtn.disabled = true;

    try {
      const body = {
        autorId: getUsuarioId(),
        conteudo: text
      };

      const res = await fetch(api(`/api/chat/threads/${currentThreadId}/messages`), {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
        credentials: 'include'
      });

      if (!res.ok) {
        alert('Erro ao enviar mensagem.');
        return;
      }

      const msg = await res.json();
      appendMessageBubble(msg);
      inputEl.value = '';
      inputEl.focus();

      loadThreads(false);
    } catch (err) {
      console.error('[chat] erro ao enviar mensagem:', err);
      alert('Erro ao enviar mensagem.');
    } finally {
      if (submitBtn) submitBtn.disabled = false;
    }
  });

  // Filtro de pesquisa
  if (searchEl) {
    searchEl.addEventListener('input', () => {
      const term = searchEl.value.toLowerCase();
      const filtradas = allThreads.filter(t =>
        t.titulo.toLowerCase().includes(term)
      );
      renderThreadList(filtradas);
    });
  }

  // -----------------------------------------
  // FUNÇÕES PRINCIPAIS
  // -----------------------------------------

  async function loadThreads(selectFirst = true) {
    try {
      const res = await fetch(api('/api/chat/threads'), {
        method: 'GET',
        cache: 'no-store',
        credentials: 'include'
      });

      if (!res.ok) throw new Error('Falha ao carregar conversas');

      allThreads = await res.json();
      renderThreadList(allThreads);

      if (selectFirst && allThreads.length && !currentThreadId) {
        openThread(allThreads[0].id, allThreads[0].titulo);
      }
    } catch (err) {
      console.error('[chat] erro ao carregar threads:', err);
    }
  }

  function renderThreadList(list) {
    threadListEl.innerHTML = '';

    list.forEach(thread => {
      const btn = document.createElement('button');
      btn.type = 'button';
      btn.className = 'chat-list-item' +
        (thread.id === currentThreadId ? ' is-active' : '');
      btn.dataset.id = thread.id;

      const initials = getInitials(thread.titulo);

      btn.innerHTML = `
        <div class="chat-list-meta">
          <div class="chat-list-avatar">${initials}</div>
          <div class="chat-list-text">
            <span class="chat-list-name">${thread.titulo}</span>
            <span class="chat-list-preview">
              ${thread.ultimaMensagem ? thread.ultimaMensagem : '&nbsp;'}
            </span>
          </div>
        </div>
        <div class="chat-list-unread" ${thread.naoLidas ? '' : 'hidden'}>
          ${thread.naoLidas}
        </div>
      `;

      btn.addEventListener('click', () => openThread(thread.id, thread.titulo));
      threadListEl.appendChild(btn);
    });
  }

  async function openThread(id, titulo) {
    currentThreadId = id;

    if (titulo) {
      contactNameEl.textContent = titulo;
      if (avatarEl) avatarEl.textContent = getInitials(titulo);
    }

    chatPanelEl.classList.remove('hidden');

    threadListEl.querySelectorAll('.chat-list-item').forEach(btn => {
      const isActive = Number(btn.dataset.id) === id;
      btn.classList.toggle('is-active', isActive);
    });

    messagesEl.innerHTML = '<p class="chat-empty">Carregando...</p>';

    try {
      const res = await fetch(api(`/api/chat/threads/${id}/messages`), {
        method: 'GET',
        cache: 'no-store',
        credentials: 'include'
      });
      if (!res.ok) throw new Error('Falha ao buscar mensagens');

      const msgs = await res.json();
      messagesEl.innerHTML = '';
      msgs.forEach(appendMessageBubble);
      scrollMessagesToBottom();

      loadThreads(false);
    } catch (err) {
      console.error('[chat] erro ao abrir thread:', err);
      messagesEl.innerHTML =
        '<p class="chat-empty">Erro ao carregar mensagens.</p>';
    }
  }

  function appendMessageBubble(msg) {
    const isMine = String(msg.autorId) === String(getUsuarioId());

    const div = document.createElement('div');
    div.className = 'chat-bubble ' + (isMine ? 'me' : 'them');
    div.textContent = msg.conteudo;
    messagesEl.appendChild(div);
    scrollMessagesToBottom();
  }

  function scrollMessagesToBottom() {
    messagesEl.scrollTop = messagesEl.scrollHeight;
  }

  function getInitials(name) {
    return (name || '')
      .split(' ')
      .filter(Boolean)
      .slice(0, 2)
      .map(p => p[0].toUpperCase())
      .join('');
  }
});
