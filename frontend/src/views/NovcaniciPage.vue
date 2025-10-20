<template>
  <div class="novcanici-container">
    <h2>Moji Novčanici</h2>

    <!-- Forma za dodavanje novog novčanika -->
    <div class="add-form">
      <h3>Dodaj novi novčanik</h3>
      <form @submit.prevent="handleCreateNovcanik">
        <input type="text" v-model="newWallet.naziv" placeholder="Naziv novčanika" required>
        <input type="number" v-model="newWallet.pocetnoStanje" placeholder="Početno stanje" required>
        <select v-model="newWallet.valutaKod" required>
          <option disabled value="">Izaberite valutu</option>
          <option v-for="valuta in dostupneValute" :key="valuta.id" :value="valuta.naziv">
            {{ valuta.naziv }}
          </option>
        </select>
        <button type="submit">Dodaj</button>
      </form>
    </div>

    <!-- Lista postojećih novčanika -->
    <div class="wallet-list">
      <h3>Postojeći novčanici</h3>
      <ul>
        <li v-for="wallet in wallets" :key="wallet.id" class="wallet-item">
          
          <!-- Prikaz ili Edit mod -->
          <div v-if="editingWalletId !== wallet.id" class="wallet-info">
            <router-link :to="`/dashboard/novcanici/${wallet.id}`" class="wallet-name-link">
              {{ wallet.naziv }}
            </router-link>
            <span>{{ wallet.trenutnoStanje }} {{ wallet.valuta.naziv }}</span>
            <div class="actions">
              <button @click="enterEditMode(wallet)" class="edit-btn">Izmeni</button>
              <button @click="handleDeleteNovcanik(wallet.id)" class="delete-btn">Obriši</button>
            </div>
          </div>

          <!-- Edit mod -->
          <div v-else class="wallet-edit">
            <input type="text" v-model="editingWalletNaziv">
            <div class="actions">
              <button @click="handleUpdateNovcanik(wallet.id)" class="save-btn">Sačuvaj</button>
              <button @click="cancelEditMode">Otkaži</button>
            </div>
          </div>

        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import api from '../services/api';

export default {
  data() {
    return {
      wallets: [],
      dostupneValute: [],
      newWallet: {
        naziv: '',
        pocetnoStanje: 0,
        valutaKod: ''
      },
      editingWalletId: null, // ID novčanika koji se trenutno menja
      editingWalletNaziv: '' // Novi naziv za novčanik koji se menja
    };
  },
  async created() {
    this.fetchWallets();
    this.fetchCurrencies();
  },
  methods: {
    async fetchWallets() {
      try {
        const response = await api.getNovcanici();
        this.wallets = response.data;
      } catch (error) {
        console.error('Greška pri dohvatanju novčanika:', error);
      }
    },
    async fetchCurrencies() {
      try {
        const response = await api.getValute();
        this.dostupneValute = response.data;
      } catch (error) {
        console.error('Greška pri dohvatanju valuta:', error);
      }
    },
    async handleCreateNovcanik() {
      try {
        await api.createNovcanik(this.newWallet);
        this.newWallet.naziv = '';
        this.newWallet.pocetnoStanje = 0;
        this.newWallet.valutaKod = '';
        this.fetchWallets();
      } catch (error) {
        console.error('Greška pri kreiranju novčanika:', error);
      }
    },
    async handleDeleteNovcanik(walletId) {
      if (confirm('Da li ste sigurni da želite da obrišete ovaj novčanik?')) {
        try {
          await api.deleteNovcanik(walletId);
          this.fetchWallets(); // Osveži listu nakon brisanja
        } catch (error) {
          console.error('Greška pri brisanju novčanika:', error);
          alert('Nije moguće obrisati novčanik. Proverite da li ima transakcije.');
        }
      }
    },
    enterEditMode(wallet) {
      this.editingWalletId = wallet.id;
      this.editingWalletNaziv = wallet.naziv;
    },
    cancelEditMode() {
      this.editingWalletId = null;
      this.editingWalletNaziv = '';
    },
    async handleUpdateNovcanik(walletId) {
      try {
        const updatedData = { naziv: this.editingWalletNaziv };
        await api.updateNovcanik(walletId, updatedData);
        this.cancelEditMode();
        this.fetchWallets(); // Osveži listu
      } catch (error) {
        console.error('Greška pri izmeni novčanika:', error);
      }
    }
  }
};
</script>

<style scoped>
.novcanici-container { max-width: 800px; margin: auto; }
.add-form, .wallet-list { background: white; padding: 20px; border-radius: 5px; margin-bottom: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
input, select { margin-right: 10px; padding: 8px; }
li { list-style: none; padding: 10px; border-bottom: 1px solid #eee; }
.wallet-item { display: flex; justify-content: space-between; align-items: center; }
.wallet-info, .wallet-edit { display: flex; justify-content: space-between; align-items: center; width: 100%; }
.wallet-name-link { text-decoration: none; color: #2c3e50; font-weight: bold; }
.wallet-name-link:hover { text-decoration: underline; }
.actions button { margin-left: 10px; cursor: pointer; }
.edit-btn { background-color: #ffc107; } 
.delete-btn { background-color: #f44336; color: white; }
.save-btn { background-color: #4CAF50; color: white; }
</style>
