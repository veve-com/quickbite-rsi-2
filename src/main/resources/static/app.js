const rp = n => 'Rp' + Number(n).toLocaleString('id-ID');
const esc = s => String(s).replace(/[&<>"']/g, c =>
  ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));

async function api(method, url, body) {
  const res = await fetch(url, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: body ? JSON.stringify(body) : undefined
  });
  let data = null;
  try { data = await res.json(); } catch (e) {}
  return { ok: res.ok, status: res.status, data };
}

function pesan(ok, html) {
  const el = document.getElementById('hasil');
  el.className = ok ? 'ok' : 'err';
  el.innerHTML = html;
  el.style.display = 'block';
}
function tampilkanHalamanSukses(html) { pesan(true, html); }
function tampilkanPesanError(html) { pesan(false, html); }
function galat(r) {
  const d = r.data;
  tampilkanPesanError('Gagal (' + r.status + ')' +
    (d && d.code ? ' ' + esc(d.code) + ': ' + esc(d.message) : ''));
}