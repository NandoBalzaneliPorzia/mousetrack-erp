(function () {
  const isLocal = location.hostname === "localhost" || location.hostname.startsWith("127.");
  // Troque pelo domínio REAL do seu backend no Render:
  const PROD = "https://mousetrack-erp.onrender.com";
  const DEV  = "http://localhost:8080";
  window.API_BASE = isLocal ? DEV : PROD;
  window.api = (path) => `${window.API_BASE}${path}`;
})();
