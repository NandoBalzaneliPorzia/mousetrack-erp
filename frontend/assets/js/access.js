// ======================
//  LÓGICA DA PÁGINA DE ACESSO
// ======================

// Seleciona elementos
const form = document.getElementById('accForm');
const nameEl = document.getElementById('accName');
const emailEl = document.getElementById('accEmail');
const passEl = document.getElementById('accPass');
const cliSel = document.getElementById('accClient');
const feedbackEl = document.getElementById('accFeedback');
const genBtn = document.getElementById('btnGenPass');

const guestAccess = new URLSearchParams(location.search).get("processoId");
if (guestAccess) {
    window.location.href = `/processo.html?processoId=${guestAccess}`;
}


// feedback
function setFeedback(msg, error = false) {
  feedbackEl.textContent = msg;
  feedbackEl.style.color = error ? 'red' : 'green';
}

// gerar senha
function generatePassword(len = 12) {
  const upper = "ABCDEFGHJKLMNPQRSTUVWXYZ";
  const lower = "abcdefghijkmnopqrstuvwxyz";
  const numbers = "23456789";
  const symbols = "!@#$%&*?";
  const all = upper + lower + numbers + symbols;

  const pick = set => set[Math.floor(Math.random() * set.length)];

  let pwd = pick(upper) + pick(lower) + pick(numbers) + pick(symbols);
  while (pwd.length < len) pwd += pick(all);

  return pwd.split('').sort(() => Math.random() - 0.5).join('');
}

genBtn.addEventListener('click', () => {
  passEl.value = generatePassword();
  setFeedback('Senha gerada automaticamente.');
});

// carregar clientes
async function carregarClientes() {
  try {
    const resp = await fetch(api('/api/clientes'));
    const lista = await resp.json();

    cliSel.innerHTML = '<option value="">Selecione</option>';
    lista.forEach(c => {
      const opt = document.createElement('option');
      opt.value = c.id;
      opt.textContent = c.nome;
      cliSel.appendChild(opt);
    });

  } catch (err) {
    console.error(err);
    setFeedback('Falha ao carregar clientes.', true);
  }
}

carregarClientes();

// envio do form
form.addEventListener('submit', async e => {
  e.preventDefault();
  setFeedback('');

  const nome = nameEl.value.trim();
  const email = emailEl.value.trim();
  const senha = passEl.value.trim();
  const clienteId = cliSel.value;

  if (!nome || !email || !senha || !clienteId) {
    setFeedback('Preencha todos os campos.', true);
    return;
  }

  if (!/^\S+@\S+\.\S+$/.test(email)) {
    setFeedback('Email inválido.', true);
    return;
  }

  const submitBtn = form.querySelector('.btn.primary');
  submitBtn.disabled = true;
  setFeedback('Enviando…');

  try {
    const resp = await fetch(api('/api/usuarios'), {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        nome,
        email,
        senhaHash: senha,
        clienteId: Number(clienteId),
        tipoUsuario: 'colaborador'
      })
    });

    if (!resp.ok) throw new Error(await resp.text());

    setFeedback('Usuário cadastrado com sucesso!');
    form.reset();

  } catch (err) {
    setFeedback('Falha ao cadastrar.', true);
  } finally {
    submitBtn.disabled = false;
  }
});
