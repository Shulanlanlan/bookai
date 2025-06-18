<template>
  <div class="min-h-screen flex items-center justify-center bg-blue-50">
    <div class="bg-white shadow-md rounded-2xl p-10 max-w-md text-center">
      <h1 class="text-3xl font-semibold text-blue-600 mb-6">欢迎使用BookAI</h1>
      <p class="text-gray-600 mb-8">请登录或注册以继续使用</p>
      <div class="flex flex-col gap-4">
        <button @click="handleLogin" class="bg-blue-600 hover:bg-blue-700 text-white py-2 rounded-full transition duration-300">
          登录
        </button>
        <button @click="handleRegister" class="border border-blue-600 text-blue-600 hover:bg-blue-50 py-2 rounded-full transition duration-300">
          注册
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "StartScreen",
  methods: {
    handleLogin() {
      this.$router.push("/login");
    },
    handleRegister() {
      this.$router.push("/register");
    },
  },
};
</script>
