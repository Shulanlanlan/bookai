<template>
  <div
      class="fixed top-0 left-0 w-full bg-blue-600 text-white text-sm px-6 py-3 z-50 shadow-md flex justify-between items-center">
    <span>当前用户：{{ username || '加载中...' }}</span>
    <button
        @click="logout"
        class="bg-white text-blue-600 text-xs font-medium px-3 py-1 rounded hover:bg-gray-100"
    >
      退出登录
    </button>
  </div>
  <div class="flex min-h-screen pt-12">
    <!-- 左侧图书列表 -->
    <div class="w-2/3 p-6 border-r bg-white">
      <div class="flex items-center gap-4 mb-4">
        <input
            v-model="search"
            placeholder="搜索图书或作者"
            class="flex-1 px-4 py-2 border rounded-md"
        />
        <select v-model="selectedTag" class="border px-2 py-1 rounded-md">
          <option value="">全部标签</option>
          <option v-for="tag in allTags" :key="tag" :value="tag">{{ tag }}</option>
        </select>
      </div>
      <div class="grid grid-cols-2 gap-4">
        <BookCard
            v-for="book in filteredBooks"
            :key="book.id"
            :book="book"
            :added="isFavorite(book)"
            @add="addToFavorites"
        />
      </div>
    </div>

    <!-- 右侧区域 -->
    <div class="w-1/3 bg-blue-50 relative">
      <!-- 悬浮兴趣单：固定在页面右上角 -->
      <div class="fixed top-6 right-6 w-1/4 max-h-64 overflow-auto bg-white shadow-lg rounded-xl p-4 z-50 mt-7">
        <FavoriteList
            :favorites="favoriteBooks"
            @remove="removeFromFavorites"
        />
      </div>

      <!-- 悬浮聊天框：固定在页面右下角 -->
      <div class="fixed bottom-6 right-6 w-1/4 max-h-[400px] overflow-auto bg-white shadow-lg rounded-xl p-4 z-50">
        <ChatBox
            :selectedBooks="favoriteBooks"
            :allBooks="books"
            :selectedTag="selectedTag"
            @add-favorite="addAllToFavorites"
            @remove-favorite="removeFromFavorites"
            @update:selectedTag="val => selectedTag = val"
        />
      </div>
    </div>
  </div>
</template>

<script>
import BookCard from "@/components/BookCard.vue";
import FavoriteList from "@/components/FavoriteList.vue";
import ChatBox from "@/components/ChatBox.vue";

export default {
  name: "BookChatPage",
  components: {BookCard, FavoriteList, ChatBox},
  data() {
    return {
      search: "",
      selectedTag: "",
      allTags: ["科幻", "历史", "技术", "文学", "科学"], // 你可以补充标签
      books: [],
      favoriteBooks: [],
      username: null,  // 新增字段
    };
  },
  computed: {
    filteredBooks() {
      return this.books.filter((book) => {
        const matchesSearch =
            book.title.includes(this.search) || book.author.includes(this.search);
        const matchesTag = !this.selectedTag || book.tags.includes(this.selectedTag);
        return matchesSearch && matchesTag;
      });
    },
  },
  methods: {
    addToFavorites(book) {
      this.favoriteBooks.push(book);
    },
    addAllToFavorites(books) {
      this.favoriteBooks.push(...books);
    },
    removeFromFavorites(book) {
      this.favoriteBooks = this.favoriteBooks.filter((b) => b.id !== book.id);
    },
    removeFromFavoritesById(id) {
      this.favoriteBooks = this.favoriteBooks.filter((b) => b.id !== id);
    },
    isFavorite(book) {
      return this.favoriteBooks.some((b) => b.id === book.id);
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
    logout() {
      // 示例：清除本地 token 并跳转到登录页
      localStorage.removeItem('token');
      this.$router.push('/login');
    },
    async fetchAllBooks() {
      try {
        const token = localStorage.getItem("token");
        const res = await fetch("/api/book/list", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            ...(token ? {Authorization: `Bearer ${token}`} : {}),
          },
        });
        if (!res.ok) throw new Error(`请求失败: ${res.status}`);

        const json = await res.json();

        if (json.code === 1) {
          this.books = json.data || [];

          // 根据所有书籍的 tags 动态生成 allTags
          const tagSet = new Set();
          this.books.forEach(book => {
            if (book.tags && Array.isArray(book.tags)) {
              book.tags.forEach(tag => tagSet.add(tag));
            }
          });
          this.allTags = Array.from(tagSet);

        } else {
          console.error("接口返回错误:", json.message);
          this.books = [];
          this.allTags = [];
        }
      } catch (err) {
        console.error("获取书籍失败:", err);
        this.books = [];
        this.allTags = [];
      }
    },
    fetchFavorites(token) {
      fetch('/api/book/favorite/list', {
        method: 'POST',
        headers: {
          'Authorization': 'Bearer ' + token,
          'Content-Type': 'application/json',
        },
      })
          .then(res => res.json())
          .then(json => {
            if (json.code === 1) {
              this.favoriteBooks = json.data;
            } else {
              console.error("获取兴趣书单失败：", json.message);
            }
          });
    },
  },
  mounted() {
    this.books = [
      {id: 1, title: "三体", author: "刘慈欣", description: "科幻巨著", tags: ["科幻"]},
      {id: 2, title: "时间简史", author: "霍金", description: "宇宙探索", tags: ["科学"]},
      {id: 3, title: "史记", author: "司马迁", description: "历史经典", tags: ["历史"]},
      {id: 4, title: "程序员修炼之道", author: "Andrew Hunt", description: "技术书籍", tags: ["技术"]},
      // 其他书籍
    ];
    const token = localStorage.getItem("token");
    if (token) {
      this.username = this.getUsernameFromToken(token);
    }
    this.fetchAllBooks();
    this.fetchFavorites(token);
  },
};
</script>
