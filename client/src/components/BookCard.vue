<template>
  <div class="border rounded-lg p-4 bg-white shadow">
    <h3 class="text-lg font-bold text-blue-700">{{ book.title }}</h3>
    <p class="text-sm text-gray-600 mb-2">作者：{{ book.author }}</p>
    <p class="text-xs text-gray-500 mb-2 line-clamp-2">{{ book.description }}</p>
    <div class="text-xs text-blue-500 mb-2">
      标签：
      <span v-for="t in book.tags" :key="t" class="mr-1">#{{ t }}</span>
    </div>
    <button
      :class="[
        'text-sm px-3 py-1 rounded-full',
        isAdded
          ? 'bg-gray-300 text-gray-600 cursor-not-allowed'
          : 'bg-blue-500 text-white hover:bg-blue-600'
      ]"
      :disabled="isAdded"
      @click="addToFavorite"
    >
      {{ isAdded ? '已添加' : '➕ 加入兴趣单' }}
    </button>
  </div>
</template>

<script>
export default {
  props: {
    book: Object,
    added: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      isAdded: this.added,
    };
  },
  methods: {
    async addToFavorite() {
      const token = localStorage.getItem("token");
      try {
        const res = await fetch("/api/book/favorite/add", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify([this.book.id]), // 接口要求是 id 数组
        });

        const result = await res.json();
        if (result.code === 1) {
          this.isAdded = true;
          this.$emit("add", this.book); // 通知父组件
        } else {
          alert("添加失败：" + (result.message || "未知错误"));
        }
      } catch (e) {
        console.error("请求错误", e);
        alert("请求失败");
      }
    },
  },
  watch: {
    added(val) {
      this.isAdded = val; // 父组件改变时同步
    },
  },
};
</script>
