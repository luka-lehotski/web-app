<template>
  <div class="ciljevi-container">
    <h2>Moji Ciljevi Štednje</h2>

    <!-- Forma za dodavanje novog cilja -->
    <div class="add-form">
      <h3>Dodaj novi cilj</h3>
      <form @submit.prevent="handleCreateCilj">
        <input type="text" v-model="newGoal.naziv" placeholder="Naziv cilja" required>
        <input type="number" v-model="newGoal.zeljeniIznos" placeholder="Željeni iznos" required>
        <input type="date" v-model="newGoal.rok">
        <select v-model="newGoal.novcanikId" required>
          <option disabled value="">Izaberite štedni novčanik</option>
          <option v-for="wallet in stedniNovcanici" :key="wallet.id" :value="wallet.id">
            {{ wallet.naziv }}
          </option>
        </select>
        <button type="submit">Dodaj cilj</button>
      </form>
    </div>

    <!-- Lista postojećih ciljeva -->
    <div class="goal-list">
      <h3>Postojeći ciljevi</h3>
      <ul>
        <li v-for="goal in ciljevi" :key="goal.id">
          <span>{{ goal.naziv }} ({{ goal.novcanik.trenutnoStanje }} / {{ goal.zeljeniIznos }} {{ goal.novcanik.valuta.naziv }})</span>
          <div class="progress-bar">
            <div class="progress" :style="{ width: calculateProgress(goal) + '%' }"></div>
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
      ciljevi: [],
      stedniNovcanici: [],
      newGoal: {
        naziv: '',
        zeljeniIznos: null,
        rok: null,
        novcanikId: ''
      }
    };
  },
  async created() {
    this.fetchCiljevi();
    this.fetchStedniNovcanici();
  },
  methods: {
    async fetchCiljevi() {
      try {
        const response = await api.getCiljevi();
        this.ciljevi = response.data;
      } catch (error) {
        console.error('Greška pri dohvatanju ciljeva:', error);
      }
    },
    async fetchStedniNovcanici() {
      try {
        const response = await api.getNovcanici();
        // Filtriramo samo one novčanike koji su označeni kao štedni
        this.stedniNovcanici = response.data.filter(wallet => wallet.stedni);
      } catch (error) {
        console.error('Greška pri dohvatanju novčanika:', error);
      }
    },
    async handleCreateCilj() {
      try {
        await api.createCilj(this.newGoal);
        this.newGoal.naziv = '';
        this.newGoal.zeljeniIznos = null;
        this.newGoal.rok = null;
        this.newGoal.novcanikId = '';
        this.fetchCiljevi(); // Osveži listu
      } catch (error) {
        console.error('Greška pri kreiranju cilja:', error);
      }
    },
    calculateProgress(goal) {
      if (!goal.zeljeniIznos || goal.zeljeniIznos <= 0) return 0;
      const progress = (goal.novcanik.trenutnoStanje / goal.zeljeniIznos) * 100;
      return Math.min(progress, 100); // Ne može biti preko 100%
    }
  }
};
</script>

<style scoped>
.ciljevi-container { max-width: 800px; margin: auto; }
.add-form, .goal-list { background: white; padding: 20px; border-radius: 5px; margin-bottom: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
input, select { margin-right: 10px; padding: 8px; }
li { list-style: none; padding: 10px; border-bottom: 1px solid #eee; }
.progress-bar { background-color: #eee; border-radius: 5px; height: 20px; width: 100%; margin-top: 5px; }
.progress { background-color: #4CAF50; height: 100%; border-radius: 5px; }
</style>
