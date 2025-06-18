<template>
  <div
      class="fixed bottom-6 right-6 w-96 max-h-[400px] bg-white rounded-xl shadow-lg p-4 flex flex-col z-50"
  >
    <!-- 聊天记录区域 -->
    <div ref="msgList" class="flex-1 overflow-auto text-sm text-gray-700 mb-4">
      <div v-for="(msg, i) in messages" :key="i" class="mb-2">
        <span class="font-semibold">{{ msg.role }}：</span>{{ msg.text }}
      </div>
    </div>

    <!-- 附加输入，展示选中书列表 -->
    <div class="mb-2 p-2 bg-gray-100 rounded text-xs text-gray-600 overflow-x-auto whitespace-nowrap">
      当前选择的书：
      <span
          v-for="book in selectedBooks"
          :key="book.id"
          class="inline-block mr-2 px-2 py-0.5 bg-blue-200 text-blue-800 rounded"
      >
        {{ book.title }}
      </span>
      <span v-if="selectedBooks.length === 0" class="text-gray-400">暂无选择</span>
    </div>

    <!-- 输入框区域 -->
    <div class="flex items-center gap-2">
      <input
          v-model="input"
          :disabled="inputDisabled"
          placeholder="输入你的问题..."
          class="flex-1 px-3 py-2 border rounded-md"
          @keydown.enter.prevent="send"
      />
      <button
          @click="send"
          :disabled="inputDisabled"
          class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 disabled:bg-gray-400 disabled:cursor-not-allowed"
      >
        发送
      </button>
    </div>
  </div>
</template>

<script>
// 注意：请先安装并配置好OpenAI SDK（npm install openai）
// 并在项目根目录创建 .env 文件，写入 VITE_OPENAI_API_KEY=你的apikey
import {OpenAI} from "openai";

export default {
  props: {
    selectedBooks: {
      type: Array,
      default: () => [],
    },
    // 额外传入页面全部书籍列表，方便给AI做上下文
    allBooks: {
      type: Array,
      default: () => [],
    },
    selectedTag: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      input: "",
      messages: [],
      inputDisabled: false,
    };
  },
  methods: {
    async send() {
      if (!this.input.trim()) return;

      this.messages.push({role: "你", text: this.input});
      this.inputDisabled = true;

      // 准备上下文字符串，控制长度仅传基础字段
      const favoriteBooksJson = JSON.stringify(
          this.selectedBooks.map(b => ({id: b.id, title: b.title}))
      );
      const allBooksJson = JSON.stringify(
          this.allBooks.map(b => ({id: b.id, title: b.title, tags: b.tags || []}))
      );

      const systemPrompt = `
          你是一个图书兴趣管理助手。用户输入可以包含三类操作,返回JSON格式(一定要符合，不需要返回错误内容）：

          1. 筛选图书，：
          {"action":"filter","tag":"标签名称","message":"说明文字"}

          2. 添加兴趣书单
          {"action":"add","bookIds":[书籍ID列表],"message":"说明文字"}

          3. 移除兴趣书单条目
          {"action":"remove","bookIds":[书籍ID列表],"message":"说明文字"}

          如果输入无法匹配以上三类
          {"action":"error","message":"出错了，请重新问我。"}

          请严格只输出JSON字符串。

          ---

          当前上下文信息：
          - 当前选中兴趣书单（id和title）：${favoriteBooksJson}
          - 当前页面图书列表（id,title,tags）：${allBooksJson}
          - 当前选中标签：${this.selectedTag || ""}
          你可以根据当前选择的标签，推测出用户可能会喜欢哪些书籍
                `.trim();

      const userPrompt = `用户输入：${this.input}`;

      // // 初始化OpenAI客户端（环境变量）
      // const openai = new OpenAI({
      //   baseURL: 'https://api.deepseek.com',
      //   apiKey: import.meta.env.VITE_OPENAI_API_KEY,
      //   dangerouslyAllowBrowser: true,  // 注意风险！
      // });

      try {
        const res = await fetch("/api/chat/ask", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
          body: JSON.stringify({
            systemPrompt,
            userPrompt,
          }),
        });

        const result = await res.json();
        const rawReply = result.choices?.[0]?.message?.content?.trim();
        console.log(rawReply)

        //TODO 不输出原回答
        // this.messages.push({role: "助手", text: rawReply});
        console.log(rawReply);
        const match = rawReply.match(/{[\s\S]*}/); // 匹配最外层的大括号及其内容
        if (!match) {
          this.messages.push({role: "助手", text: "解析AI回复失败，请输入更明确的问题。"})
          return;
        }
        // 尝试解析AI返回的JSON
        let data;
        try {
          data = JSON.parse(match[0]);
        } catch (e) {
          this.messages.push({role: "助手", text: "解析AI回复失败，请输入更明确的问题。"});
          this.inputDisabled = false;
          this.scrollToBottom();
          return;
        }

        switch (data.action) {
          case "filter":
            // 触发父组件修改标签（用事件）
            this.$emit("update:selectedTag", data.tag);
            this.messages.push({role: "助手", text: `已切换标签至「${data.tag}」`});
            break;

          case "add":
            await this.addToFavoritesByIds(data.bookIds);
            this.messages.push({role: "助手", text: data.message || "已添加兴趣书单"});
            break;

          case "remove":
            this.removeFromFavoritesByIds(data.bookIds);
            this.messages.push({role: "助手", text: data.message || "已移除兴趣书单"});
            break;

          case "error":
          default:
            this.messages.push({role: "助手", text: data.message || "出错了，请重新问我。"});
            break;
        }
      } catch (err) {
        this.messages.push({role: "助手", text: "请求AI接口失败，请稍后再试。"});
      } finally {
        this.input = "";
        this.inputDisabled = false;
        this.scrollToBottom();
      }
    },

    async addToFavoritesByIds(ids) {
      // 过滤已存在的，添加新选中的兴趣书单
      const newBooks = this.allBooks.filter(b => ids.includes(b.id) && !this.selectedBooks.some(sb => sb.id === b.id));
      const newBookIds = newBooks.map(b => b.id);
      const token = localStorage.getItem("token");
      try {
        const res = await fetch("/api/book/favorite/add", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(newBookIds), // 接口要求是 id 数组
        });

        const result = await res.json();
        if (result.code === 1) {
          this.$emit("add-favorite", newBooks); // 通知父组件
        } else {
          alert("添加失败：" + (result.message || "未知错误"));
        }
      } catch (e) {
        console.error("请求错误", e);
        alert("请求失败");
      }
    },
    removeFromFavoritesByIds(ids) {
      // 通过事件通知父组件移除
      ids.forEach(id => {
        let book = this.allBooks.find(b => b.id === id);
        this.removeBook(book);
      });
    },
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
          this.$emit("remove-favorite", book); // 通知父组件移除本地数据
        } else {
          alert("移除失败：" + (result.message || "未知错误"));
        }
      } catch (e) {
        console.error("请求失败", e);
        alert("请求失败");
      }
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const list = this.$refs.msgList;
        if (list) list.scrollTop = list.scrollHeight;
      });
    },
  },
};
</script>