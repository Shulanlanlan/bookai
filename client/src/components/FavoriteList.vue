<template>
  <div>
    <h4 class="text-lg font-semibold mb-2 text-blue-600">兴趣书单</h4>
    <ul class="text-sm text-gray-700 space-y-1 max-h-40 overflow-auto">
      <li v-for="book in favorites" :key="book.id" class="truncate flex justify-between items-center">
        <span class="truncate max-w-[70%]">📘 {{ book.title }}</span>
        <button
          @click="removeBook(book)"
          class="text-red-500 hover:text-red-700 text-xs px-2 py-0.5 rounded border border-red-300 hover:border-red-600"
          title="移除"
        >
          ×
        </button>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  props: {
    favorites: Array,
  },
  methods: {
    async removeBook(book) {
      const token = localStorage.getItem("token");
      try {
        const res = await fetch("/api/book/favorite/remove", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(book.id),
        });

        const result = await res.json();
        if (result.code === 1) {
          this.$emit("remove", book); // 通知父组件移除本地数据
        } else {
          alert("移除失败：" + (result.message || "未知错误"));
        }
      } catch (e) {
        console.error("请求失败", e);
        alert("请求失败");
      }
    },
  },
};
</script>
