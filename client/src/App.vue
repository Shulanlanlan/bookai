<template>
  <router-view />
</template>

<script>
import StartScreen from './components/WelcomeScreen.vue';

export default {
  name: 'App',
  components: {
    StartScreen
  }
}
</script>

<style>
/* 全局样式可以在这里添加 */
body {
  margin: 0;
  font-family: 'Helvetica Neue', sans-serif;
}
</style>
