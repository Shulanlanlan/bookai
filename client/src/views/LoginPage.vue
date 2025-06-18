<template>
  <!-- 顶部用户名 Banner -->
  <div class="min-h-screen flex items-center justify-center bg-blue-50">
    <div class="bg-white shadow-md rounded-2xl p-10 max-w-md w-full text-center">
      <h2 class="text-3xl font-semibold text-blue-600 mb-6">用户登录</h2>
      <form @submit.prevent="login" class="flex flex-col gap-4 text-left">
        <div>
          <label for="username" class="block mb-1 font-medium text-gray-700">用户名</label>
          <input
              type="text"
              id="username"
              v-model="username"
              placeholder="请输入用户名"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
        </div>
        <div>
          <label for="password" class="block mb-1 font-medium text-gray-700">密码</label>
          <input
              type="password"
              id="password"
              v-model="password"
              placeholder="请输入密码"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
        </div>
        <button
            type="submit"
            class="bg-blue-600 hover:bg-blue-700 text-white py-2 rounded-full transition duration-300"
        >
          登录
        </button>
        <div v-if="error" class="text-red-500 text-center text-sm mt-2">{{ error }}</div>
      </form>
      <div class="text-sm text-gray-600 mt-4">
        没有账号？
        <router-link to="/register" class="text-blue-600 hover:underline">点此注册</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "LoginPage",
  data() {
    return {
      username: "",
      password: "",
      error: "",
    };
  },
  methods: {
    async login() {
      this.error = "";
      if (!this.username || !this.password) {
        this.error = "用户名和密码不能为空";
        return;
      }

      try {
        const res = await fetch("/api/user/login", {
          method: "POST",
          headers: {"Content-Type": "application/json"},
          body: JSON.stringify({username: this.username, password: this.password}),
        });

        const data = await res.json();

        if (data.code === 1) {
          const token = data.data;
          localStorage.setItem("token", token);
          this.$router.push("/bookai");
        } else {
          this.error = data.message || "登录失败";
        }
      } catch (err) {
        console.error(err);
        this.error = "服务器异常，请稍后再试";
      }
    },
    getUsernameFromToken(token) {
    try {
      const payload = token.split('.')[1];
      const decoded = atob(payload);
      const json = JSON.parse(decoded);
      return json.sub || null;
    } catch (e) {
      console.error("无法解析 token：", e);
      return null;
    }
  },
  },
};
</script>
