<template>
  <div class="kategorije-container">
    <h2>Moje Kategorije</h2>

    <!-- Forma za dodavanje nove kategorije -->
    <div class="add-form">
      <h3>Dodaj novu kategoriju</h3>
      <form @submit.prevent="handleCreateKategorija">
        <input type="text" v-model="newCategory.naziv" placeholder="Naziv kategorije" required>
        <select v-model="newCategory.tip" required>
          <option value="TROSAK">Trošak</option>
          <option value="PRIHOD">Prihod</option>
        </select>
        <button type="submit">Dodaj</button>
      </form>
    </div>

    <!-- Lista postojećih kategorija -->
    <div class="category-list">
      <h3>Postojeće kategorije</h3>
      <ul>
        <li v-for="cat in kategorije" :key="cat.id">
          <span>{{ cat.naziv }} ({{ cat.tip }})</span>
          <span v-if="cat.predefinisan" class="badge">Predefinisana</span>
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
      kategorije: [],
      newCategory: {
        naziv: '',
        tip: 'TROSAK'
      }
    };
  },
  async created() {
    this.fetchKategorije();
  },
  methods: {
    async fetchKategorije() {
      try {
        const response = await api.getKategorije();
        this.kategorije = response.data;
      } catch (error) {
        console.error('Greška pri dohvatanju kategorija:', error);
      }
    },
    async handleCreateKategorija() {
      try {
        await api.createKategorija(this.newCategory);
        this.newCategory.naziv = '';
        this.fetchKategorije(); // Osveži listu
      } catch (error) {
        console.error('Greška pri kreiranju kategorije:', error);
      }
    }
  }
};
</script>

<style scoped>
.kategorije-container { max-width: 800px; margin: auto; }
.add-form, .category-list { background: white; padding: 20px; border-radius: 5px; margin-bottom: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
input, select { margin-right: 10px; padding: 8px; }
li { display: flex; justify-content: space-between; align-items: center; list-style: none; padding: 10px; border-bottom: 1px solid #eee; }
.badge { background-color: #eee; color: #555; padding: 3px 6px; border-radius: 10px; font-size: 0.8em; }
</style>
