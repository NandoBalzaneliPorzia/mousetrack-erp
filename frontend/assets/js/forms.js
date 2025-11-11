// Exibe nomes/quantidade dos arquivos selecionados e evita submit real por enquanto
document.addEventListener('DOMContentLoaded', () => {
  const input = document.getElementById('anexo');
  const fileText = document.getElementById('fileText');
  const form = document.getElementById('procForm');

  if (form) {
    form.addEventListener('submit', async (e) => {
      e.preventDefault();

      const data = Object.fromEntries(new FormData(form).entries());

      try {
        const response = await fetch('https://mouse-track-backend.onrender.com/api/forms/docx', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(data)
        });

        if (!response.ok) throw new Error('Erro ao gerar documento');

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'Form_Processo.docx';
        a.click();
        window.URL.revokeObjectURL(url);

      } catch (err) {
        alert('❌ Falha ao gerar documento.');
        console.error(err);
      }
    });
  }
});