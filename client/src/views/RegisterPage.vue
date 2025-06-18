<template>
  <div class="min-h-screen flex items-center justify-center bg-blue-50">
    <div class="bg-white shadow-md rounded-2xl p-10 max-w-md w-full text-center">
      <h2 class="text-3xl font-semibold text-blue-600 mb-6">注册账号</h2>
      <form @submit.prevent="register" class="flex flex-col gap-4 text-left">
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
        <div>
          <label for="confirm" class="block mb-1 font-medium text-gray-700">确认密码</label>
          <input
            type="password"
            id="confirm"
            v-model="confirm"
            placeholder="请再次输入密码"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
          />
        </div>
        <button
          type="submit"
          class="bg-blue-600 hover:bg-blue-700 text-white py-2 rounded-full transition duration-300"
        >
          注册
        </button>
        <div v-if="error" class="text-red-500 text-center text-sm mt-2">{{ error }}</div>
      </form>
      <div class="text-sm text-gray-600 mt-4">
        已有账号？
        <router-link to="/login" class="text-blue-600 hover:underline">点此登录</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "RegisterPage",
  data() {
    return {
      username: "",
      password: "",
      confirm: "",
      error: "",
    };
  },
  methods: {
    async register() {
      this.error = "";
      if (!this.username || !this.password || !this.confirm) {
        this.error = "请填写所有字段";
        return;
      }
      if (this.password !== this.confirm) {
        this.error = "两次密码不一致";
        return;
      }

      try {
        const res = await fetch("/api/user/register", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            username: this.username,
            password: this.password,
          }),
        });
        const data = await res.json();

        if (data.code === 1) {
          alert("注册成功，请登录");
          this.$router.push("/login");
        } else {
          this.error = data.message || "注册失败";
        }
      } catch (err) {
        console.error(err);
        this.error = "服务器错误";
      }
    },
  },
};
</script>
