(function () {
  const isLocal = location.hostname === "localhost" || location.hostname.startsWith("127.");
  
  // URLs do backend
  const PROD = "https://mousetrack-erp.onrender.com";
  const DEV  = "http://localhost:8080";
  
  // Define qual usar
  window.API_BASE = isLocal ? DEV : PROD;

  // Função helper que garante a barra "/" no meio
  window.api = (path) => {
    return `${window.API_BASE.replace(/\/+$/, '')}/${String(path).replace(/^\/+/, '')}`;
  };
})();
