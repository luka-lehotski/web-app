<template>
  <div class="ponavljajuce-container">
    <h2>Ponavljajuće Transakcije (Šabloni)</h2>

    <!-- Forma za dodavanje novog šablona -->
    <div class="add-form">
      <h3>Dodaj novi šablon</h3>
      <form @submit.prevent="handleCreateSablon">
        <input type="text" v-model="newTemplate.naziv" placeholder="Naziv" required>
        <input type="number" v-model="newTemplate.iznos" placeholder="Iznos" required>
        <select v-model="newTemplate.tip" required>
          <option value="TROSAK">Trošak</option>
          <option value="PRIHOD">Prihod</option>
        </select>
        <select v-model="newTemplate.novcanikId" required>
          <option disabled value="">Izaberite novčanik</option>
          <option v-for="w in novcanici" :key="w.id" :value="w.id">{{ w.naziv }}</option>
        </select>
        <select v-model="newTemplate.kategorijaId" required>
          <option disabled value="">Izaberite kategoriju</option>
          <option v-for="k in kategorije" :key="k.id" :value="k.id">{{ k.naziv }}</option>
        </select>
        <select v-model="newTemplate.ucestalost" required>
          <option disabled value="">Učestalost</option>
          <option value="DNEVNO">Dnevno</option>
          <option value="NEDELJNO">Nedeljno</option>
          <option value="MESECNO">Mesečno</option>
          <option value="GODISNJE">Godišnje</option>
        </select>
        <button type="submit">Dodaj šablon</button>
      </form>
    </div>

    <!-- Lista postojećih šablona -->
    <div class="template-list">
      <h3>Postojeći šabloni</h3>
      <ul>
        <li v-for="sablon in sabloni" :key="sablon.id">
          <span>{{ sablon.naziv }} ({{ sablon.iznos }} {{ sablon.novcanik.valuta.naziv }}, {{ sablon.ucestalost }})</span>
          <button @click="toggleAktivnost(sablon)" :class="sablon.aktivna ? 'iskljuci-btn' : 'ukljuci-btn'">
            {{ sablon.aktivna ? 'Isključi' : 'Uključi' }}
          </button>
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
      sabloni: [],
      novcanici: [],
      kategorije: [],
      newTemplate: {
        naziv: '',
        iznos: null,
        tip: 'TROSAK',
        novcanikId: '',
        kategorijaId: '',
        ucestalost: ''
      }
    };
  },
  async created() {
    this.fetchSabloni();
    this.fetchNovcanici();
    this.fetchKategorije();
  },
  methods: {
    async fetchSabloni() {
      try {
        const response = await api.getSabloni();
        this.sabloni = response.data;
      } catch (error) {
        console.error('Greška pri dohvatanju šablona:', error);
      }
    },
    async fetchNovcanici() {
      try {
        const response = await api.getNovcanici();
        this.novcanici = response.data;
      } catch (error) {
        console.error('Greška pri dohvatanju novčanika:', error);
      }
    },
    async fetchKategorije() {
      try {
        const response = await api.getKategorije();
        this.kategorije = response.data;
      } catch (error) {
        console.error('Greška pri dohvatanju kategorija:', error);
      }
    },
    async handleCreateSablon() {
      const data = {
        naziv: this.newTemplate.naziv,
        iznos: this.newTemplate.iznos,
        tip: this.newTemplate.tip,
        ucestalost: this.newTemplate.ucestalost,
        novcanik: { id: this.newTemplate.novcanikId },
        kategorija: { id: this.newTemplate.kategorijaId }
      };
      try {
        await api.createSablon(data);
        this.fetchSabloni(); // Osveži listu
        // Resetuj formu
        Object.assign(this.newTemplate, { naziv: '', iznos: null, tip: 'TROSAK', novcanikId: '', kategorijaId: '', ucestalost: '' });
      } catch (error) {
        console.error('Greška pri kreiranju šablona:', error);
      }
    },
    async toggleAktivnost(sablon) {
      try {
        if (sablon.aktivna) {
          await api.iskljuciSablon(sablon.id);
        } else {
          await api.ukljuciSablon(sablon.id);
        }
        this.fetchSabloni(); // Osveži listu
      } catch (error) {
        console.error('Greška pri promeni statusa šablona:', error);
      }
    }
  }
};
</script>

<style scoped>
.ponavljajuce-container { max-width: 800px; margin: auto; }
.add-form, .template-list { background: white; padding: 20px; border-radius: 5px; margin-bottom: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
input, select { margin-right: 10px; padding: 8px; }
li { display: flex; justify-content: space-between; align-items: center; list-style: none; padding: 10px; border-bottom: 1px solid #eee; }
.ukljuci-btn { background-color: #4CAF50; color: white; }
.iskljuci-btn { background-color: #f44336; color: white; }
</style>
