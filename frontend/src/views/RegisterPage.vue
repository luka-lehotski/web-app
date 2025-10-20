<template>
  <div class="register-container">
    <h2>Registracija</h2>
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label for="username">Korisničko ime:</label>
        <input type="text" v-model="form.korisnickoIme" id="username" required />
      </div>
      <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" v-model="form.mejlAdresa" id="email" required />
      </div>
      <div class="form-group">
        <label for="password">Lozinka:</label>
        <input type="password" v-model="form.lozinka" id="password" required />
      </div>
      <button type="submit">Registruj se</button>
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      <p class="login-link">
        Već imaš nalog? <router-link to="/login">Prijavi se</router-link>
      </p>
    </form>
  </div>
</template>

<script>
import api from '../services/api';

export default {
  data() {
    return {
      form: {
        korisnickoIme: '',
        mejlAdresa: '',
        lozinka: ''
      },
      errorMessage: ''
    };
  },
  methods: {
    async handleRegister() {
      this.errorMessage = '';
      try {
        const response = await api.register(this.form.korisnickoIme, this.form.mejlAdresa, this.form.lozinka);
        
        // Nakon uspešne registracije, automatski ga prijavljujemo
        const { token, uloga, userId } = response.data;
        localStorage.setItem('token', token);
        localStorage.setItem('userRole', uloga);
        localStorage.setItem('userId', userId);

        // i preusmeravamo na dashboard
        this.$router.push('/dashboard');

      } catch (error) {
        if (error.response && error.response.data) {
          this.errorMessage = error.response.data;
        } else {
          this.errorMessage = 'Došlo je do greške prilikom registracije.';
        }
        console.error('Greška prilikom registracije:', error);
      }
    }
  }
};
</script>

<style scoped>
/* Koristimo iste stilove kao za login */
.register-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
.form-group { margin-bottom: 15px; }
label { display: block; margin-bottom: 5px; }
input { width: 100%; padding: 8px; box-sizing: border-box; }
button { width: 100%; padding: 10px; background-color: #42b983; color: white; border: none; border-radius: 5px; cursor: pointer; }
.error { color: red; margin-top: 10px; }
.login-link { margin-top: 15px; text-align: center; }
</style>
