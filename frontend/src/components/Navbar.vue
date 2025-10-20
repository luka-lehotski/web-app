<template>
  <nav class="navbar">
    <div class="navbar-brand">FinanceApp</div>
    <div class="navbar-links">
      <router-link to="/dashboard">Početna</router-link>
      <router-link to="/dashboard/novcanici">Novčanici</router-link>
      <router-link to="/dashboard/kategorije">Kategorije</router-link>
      <router-link to="/dashboard/ciljevi">Ciljevi</router-link>
      <router-link to="/dashboard/ponavljajuce">Ponavljajuće</router-link> <!-- DODATO -->
    </div>
    <div class="navbar-user">
      <button @click="logout">Odjavi se</button>
    </div>
  </nav>
</template>

<script>
export default {
  methods: {
    logout() {
      localStorage.removeItem('token');
      localStorage.removeItem('userRole');
      localStorage.removeItem('userId');
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #333;
  color: white;
  padding: 10px 20px;
}
.navbar-brand {
  font-weight: bold;
  font-size: 1.5em;
}
.navbar-links a {
  color: white;
  text-decoration: none;
  margin: 0 10px;
}
.navbar-links a:hover {
  text-decoration: underline;
}
.navbar-user button {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
}
</style>
