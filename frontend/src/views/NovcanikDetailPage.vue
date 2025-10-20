<template>
  <div v-if="wallet" class="detail-container">
    <button @click="$router.back()" class="back-button">Nazad na listu</button>
    
    <h2>Detalji novčanika: {{ wallet.naziv }}</h2>
    <p><strong>Trenutno stanje:</strong> {{ wallet.trenutnoStanje }} {{ wallet.valuta.naziv }}</p>

    <hr>

    <!-- Forma za dodavanje nove transakcije -->
    <div class="add-transaction-form">
      <h3>Dodaj novu transakciju</h3>
      <form @submit.prevent="handleCreateTransakcija">
        <input type="text" v-model="newTransaction.naziv" placeholder="Naziv transakcije" required>
        <input type="number" v-model="newTransaction.iznos" placeholder="Iznos" required>
        <select v-model="newTransaction.tip" required>
          <option value="TROSAK">Trošak</option>
          <option value="PRIHOD">Prihod</option>
        </select>
        <select v-model="newTransaction.kategorijaId" required>
          <option disabled value="">Izaberite kategoriju</option>
          <option v-for="kat in dostupneKategorije" :key="kat.id" :value="kat.id">
            {{ kat.naziv }}
          </option>
        </select>
        <button type="submit">Dodaj transakciju</button>
        <p v-if="transactionError" class="error">{{ transactionError }}</p>
      </form>
    </div>

    <hr>

    <h3>Transakcije</h3>
    <ul class="transaction-list">
      <li v-for="t in transactions" :key="t.id" :class="t.tip === 'PRIHOD' ? 'prihod' : 'trosak'">
        <span>{{ new Date(t.datumTransakcije).toLocaleDateString() }}</span>
        <span>{{ t.naziv }}</span>
        <span>{{ t.tip === 'PRIHOD' ? '+' : '-' }}{{ t.iznos }} {{ wallet.valuta.naziv }}</span>
      </li>
      <li v-if="transactions.length === 0">
        Nema transakcija za ovaj novčanik.
      </li>
    </ul>
  </div>
  <div v-else>
    <p>Učitavanje podataka o novčaniku...</p>
  </div>
</template>

<script>
import api from '../services/api';

export default {
  data() {
    return {
      wallet: null,
      transactions: [],
      dostupneKategorije: [],
      newTransaction: {
        naziv: '',
        iznos: null,
        tip: 'TROSAK',
        kategorijaId: ''
      },
      transactionError: ''
    };
  },
  async created() {
    const novcanikId = this.$route.params.id;
    this.fetchWalletDetails(novcanikId);
    this.fetchTransactions(novcanikId);
    this.fetchKategorije();
  },
  methods: {
    async fetchWalletDetails(id) {
      try {
        const response = await api.getNovcanikById(id);
        this.wallet = response.data;
      } catch (error) {
        console.error('Greška pri dohvatanju detalja novčanika:', error);
      }
    },
    async fetchTransactions(id) {
      try {
        const response = await api.getTransakcijeForNovcanik(id);
        this.transactions = response.data;
      } catch (error) {
        console.error('Greška pri dohvatanju transakcija:', error);
      }
    },
    async fetchKategorije() {
      try {
        const response = await api.getKategorije();
        this.dostupneKategorije = response.data;
      } catch (error) {
        console.error('Greška pri dohvatanju kategorija:', error);
      }
    },
    async handleCreateTransakcija() {
      this.transactionError = '';
      const novcanikId = this.wallet.id;
      const { naziv, iznos, tip, kategorijaId } = this.newTransaction;
      try {
        await api.createTransakcija(novcanikId, kategorijaId, { naziv, iznos, tip });
        
        this.newTransaction.naziv = '';
        this.newTransaction.iznos = null;
        this.newTransaction.kategorijaId = '';

        this.fetchWalletDetails(novcanikId);
        this.fetchTransactions(novcanikId);

      } catch (error) {
        if (error.response && error.response.data) {
          this.transactionError = error.response.data;
        } else {
          this.transactionError = 'Došlo je do neočekivane greške.';
        }
        console.error('Greška pri kreiranju transakcije:', error);
      }
    }
  }
};
</script>

<style scoped>
.detail-container { max-width: 800px; margin: auto; background: white; padding: 20px; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
.back-button { margin-bottom: 20px; padding: 8px 12px; cursor: pointer; }
.add-transaction-form { padding: 15px; border: 1px solid #ddd; border-radius: 5px; margin-top: 20px; }
.transaction-list li { display: flex; justify-content: space-between; padding: 10px; border-bottom: 1px solid #eee; }
.prihod { color: green; }
.trosak { color: red; }
.error { color: red; margin-top: 10px; }
</style>
