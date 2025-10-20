<template>
  <div class="login-container">
    <h2>Prijava</h2>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="username">Korisničko ime ili Email:</label>
        <input type="text" v-model="username" id="username" required />
      </div>
      <div class="form-group">
        <label for="password">Lozinka:</label>
        <input type="password" v-model="password" id="password" required />
      </div>
      <button type="submit">Prijavi se</button>
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      <!-- DODATO: Link ka stranici za registraciju -->
      <p class="register-link">
        Nemaš nalog? <router-link to="/register">Registruj se</router-link>
      </p>
    </form>
  </div>
</template>

<script>
import api from '../services/api';

export default {
  data() {
    return {
      username: '',
      password: '',
      errorMessage: ''
    };
  },
  methods: {
    async handleLogin() {
      this.errorMessage = '';
      try {
        const response = await api.login(this.username, this.password);
        const { token, uloga, userId } = response.data;
        localStorage.setItem('token', token);
        localStorage.setItem('userRole', uloga);
        localStorage.setItem('userId', userId);
        this.$router.push('/dashboard');
      } catch (error) {
        this.errorMessage = 'Pogrešno korisničko ime ili lozinka.';
        console.error('Greška prilikom prijave:', error);
      }
    }
  }
};
</script>

<style scoped>
/* ... (stilovi ostaju isti) ... */
.login-container { max-width: 400px; margin: 50px auto; padding: 20px; border: 1px solid #ccc; border-radius: 5px; }
.form-group { margin-bottom: 15px; }
label { display: block; margin-bottom: 5px; }
input { width: 100%; padding: 8px; box-sizing: border-box; }
button { width: 100%; padding: 10px; background-color: #42b983; color: white; border: none; border-radius: 5px; cursor: pointer; }
.error { color: red; margin-top: 10px; }
.register-link { margin-top: 15px; text-align: center; }
</style>
