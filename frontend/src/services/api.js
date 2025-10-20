import axios from 'axios';

// Osnovna konfiguracija za axios
const apiClient = axios.create({
  // baseURL je uklonjen - sada će se koristiti proxy iz vite.config.js
  headers: {
    'Content-Type': 'application/json'
  }
});

// Interceptor koji će dodavati JWT token u svaki zahtev
apiClient.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, error => {
  return Promise.reject(error);
});

// Pomoćna funkcija za dobijanje ID-a korisnika
const getUserId = () => {
  return localStorage.getItem('userId');
};


// --- Metode za pozivanje endpoint-a ---

export default {
  // AuthController
  login(usernameOrEmail, lozinka) {
    return apiClient.post('/api/auth/login', { usernameOrEmail, lozinka });
  },
  register(korisnickoIme, mejlAdresa, lozinka) {
    return apiClient.post('/api/auth/register', { korisnickoIme, mejlAdresa, lozinka });
  },

  // PublicController
  getBrojKorisnika() {
    return apiClient.get('/api/public/users/count');
  },
  getValute() {
    return apiClient.get('/api/public/currencies');
  },

  // KorisnikController
  getProfil() {
    const korisnikId = getUserId();
    return apiClient.get(`/api/users/${korisnikId}/profil`);
  },

  // NovcanikController
  getNovcanici() {
    const korisnikId = getUserId();
    return apiClient.get(`/api/novcanici/${korisnikId}`);
  },
  createNovcanik(novcanikData) {
    const korisnikId = getUserId();
    return apiClient.post(`/api/novcanici/${korisnikId}`, novcanikData);
  },
  getNovcanikById(novcanikId) {
    const korisnikId = getUserId();
    // ISPRAVKA: URL sada sadrži i korisnikId i novcanikId
    return apiClient.get(`/api/novcanici/${korisnikId}/${novcanikId}`);
  },
  updateNovcanik(novcanikId, novcanikData) {
    const korisnikId = getUserId();
    return apiClient.put(`/api/novcanici/${korisnikId}/${novcanikId}`, novcanikData);
  },
  deleteNovcanik(novcanikId) {
    const korisnikId = getUserId();
    return apiClient.delete(`/api/novcanici/${korisnikId}/${novcanikId}`);
  },

  // KategorijaController
  getKategorije() {
    const korisnikId = getUserId();
    return apiClient.get(`/api/kategorije/${korisnikId}`);
  },
  createKategorija(kategorijaData) {
    const korisnikId = getUserId();
    return apiClient.post(`/api/kategorije/${korisnikId}`, kategorijaData);
  },

  // CiljStednjeController
  getCiljevi() {
    const korisnikId = getUserId();
    return apiClient.get(`/api/ciljevi/${korisnikId}`);
  },
  createCilj(ciljData) {
    const korisnikId = getUserId();
    const params = new URLSearchParams();
    params.append('novcanikId', ciljData.novcanikId);
    params.append('naziv', ciljData.naziv);
    params.append('zeljeniIznos', ciljData.zeljeniIznos);
    if (ciljData.rok) {
      params.append('rok', ciljData.rok);
    }
    return apiClient.post(`/api/ciljevi/${korisnikId}`, null, { params });
  },

  // TransakcijaController
  getTransakcijeForNovcanik(novcanikId, page = 0, size = 10) {
    const korisnikId = getUserId();
    return apiClient.get(`/api/transakcije/${korisnikId}/novcanik/${novcanikId}/stranica?page=${page}&size=${size}`);
  },
  createTransakcija(novcanikId, kategorijaId, transakcijaData) {
    const korisnikId = getUserId();
    return apiClient.post(`/api/transakcije/${korisnikId}/${novcanikId}/${kategorijaId}`, transakcijaData);
  }
};
