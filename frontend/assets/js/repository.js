// Filtro simples
const input = document.getElementById('repoSearch');
const rows  = Array.from(document.querySelectorAll('#repoTbody tr'));

const guestAccess = new URLSearchParams(location.search).get("processoId");
if (guestAccess) {
    window.location.href = `/processo.html?processoId=${guestAccess}`;
}


function applyFilter(){
  const q = (input?.value || '').trim().toLowerCase();
  rows.forEach(tr => {
    const hay = (tr.dataset.search || '').toLowerCase();
    tr.style.display = hay.includes(q) ? '' : 'none';
  });
}
input?.addEventListener('input', applyFilter);

// Navegação para a página de documentos (clicar na linha)
rows.forEach(tr => {
  const href = tr.dataset.href;
  if(!href) return;
  tr.addEventListener('click', () => {
    window.location.href = href;
  });
});
