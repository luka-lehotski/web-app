<template>
  <div>
    <Navbar />
    <main class="main-content">
      <router-view /> <!-- Ovde će se prikazivati sadržaj pod-stranica -->
    </main>
  </div>
</template>

<script>
import Navbar from '../components/Navbar.vue';

export default {
  components: {
    Navbar
  }
};
</script>

<style scoped>
.main-content {
  padding: 20px;
}
</style>
