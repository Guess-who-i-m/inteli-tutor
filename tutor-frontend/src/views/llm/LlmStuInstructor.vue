<template>
  <div class="ai-chat-container">
    <!-- 聊天头部 -->
    <!-- <el-card class="chat-header" shadow="never">
      <div class="header-content">
        <el-avatar :size="20" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
        <div class="header-info">
          <h3>AI助手</h3>
          <el-tag size="small" type="success">在线</el-tag>
        </div>
        
      </div>
    </el-card> -->

    <!-- 消息列表 -->
    <div class="messages-container" ref="messagesContainer">
      <div v-if="messages.length === 0" class="empty-state">
        <el-empty description="开始你的第一次对话吧！" />
      </div>
      
      <div v-else class="messages-list">
        <div 
          v-for="message in messages" 
          :key="message.id"
          class="message-item"
          :class="message.role"
        >
          <div class="message-avatar">
            <el-avatar 
              :size="32" 
              :src="message.role === 'user' ? userAvatar : aiAvatar"
            />
          </div>
          
          <div class="message-content">
            <div class="message-header">
              <span class="message-role">
                {{ message.role === 'user' ? '你' : 'AI助手' }}
              </span>
              <!-- <span class="message-time">{{ formatTime(message.timestamp) }}</span> -->
            </div>
            
            <div class="message-body">
              <div v-if="message.loading" class="loading-message">
                <el-icon class="is-loading"><Loading /></el-icon>
                <span>正在思考中...</span>
              </div>
              <div v-else class="message-text">
                {{ message.content }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="input-area">
      <el-card shadow="never">
        <div class="input-container">
          <el-input
            v-model="inputMessage"
            type="textarea"
            :rows="3"
            placeholder="输入你的问题..."
            :disabled="isLoading"
            @keydown.enter="sendMessage"
            class="message-input"
          />
          
          <div class="input-actions">
            <div class="input-tips">
              <el-text size="small" type="info">
                Enter 发送
              </el-text>
            </div>
            
            <div class="action-buttons">

              <el-button @click="clearChat" size="small"  type="danger" plain>
                <el-icon><Delete /></el-icon>
              </el-button>

              <el-button 
                @click="sendMessage" 
                type="primary" 
                size="small"
                :loading="isLoading"
                :disabled="!inputMessage.trim()"
              >
                <el-icon><Position /></el-icon>
              </el-button>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, nextTick, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Delete, Loading, Position } from '@element-plus/icons-vue'

// 响应式数据
const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const messagesContainer = ref(null)

// 头像URLs
const userAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
const aiAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

// 生成唯一ID
const generateId = () => {
  return Date.now() + Math.random().toString(36).substr(2, 9)
}

// 格式化时间
const formatTime = (timestamp) => {
  const date = new Date(timestamp)
  const now = new Date()
  const diffMs = now - date
  const diffMins = Math.floor(diffMs / 60000)
  
  if (diffMins < 1) return '刚刚'
  if (diffMins < 60) return `${diffMins}分钟前`
  if (diffMins < 1440) return `${Math.floor(diffMins / 60)}小时前`
  
  return date.toLocaleString('zh-CN', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}


import { useTokenStore } from '@/stores/token'

const tokenStore = useTokenStore();

// 发送消息
const sendMessage = async () => {
  if (!inputMessage.value.trim() || isLoading.value) return;
  const prompt = inputMessage.value.trim();
  // 1. 将用户消息添加到列表中
  messages.value.push({
    id: Date.now() + '-user',
    role: 'user',
    content: prompt,
  });
  // ******** 新增：准备要发送到后端的会话历史 ********
  // 我们发送包含最新用户消息在内的所有历史记录
  // 使用 map 来创建一个不包含 'id' 的新数组，因为后端不需要它
  const conversationHistory = messages.value.map(({ role, content }) => ({
    role,
    content,
  }));
  // 清空输入框
  inputMessage.value = '';
  // 立即滚动到底部
  scrollToBottom();
  // 2. 创建一个空的AI消息占位符
  const aiMessageId = Date.now() + '-ai';
  messages.value.push({
    id: aiMessageId,
    role: 'assistant',
    content: '', // 初始内容为空
  });
  
  isLoading.value = true;
  try {
    // 3. 使用 Fetch API 发起请求
    const response = await fetch('/api/llm/stream', { // 确保URL正确
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': tokenStore.token // 假设 tokenStore 已定义
      },
      // ******** 修改：发送整个会话历史 ********
      // 之前: body: JSON.stringify({ prompt: prompt }),
      // 现在:
      body: JSON.stringify(conversationHistory), // 直接发送消息数组
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    // 4. 处理数据流 (这部分代码无需改动)
    const reader = response.body.getReader();
    const decoder = new TextDecoder('utf-8');
    let streaming = true;
    while (streaming) {
      const { done, value } = await reader.read();
      if (done) {
        streaming = false;
        break;
      }
      const chunk = decoder.decode(value, { stream: true });
      const aiMessage = messages.value.find(m => m.id === aiMessageId);
      if (aiMessage) {
        aiMessage.content += chunk;
        scrollToBottom();
      }
    }
  } catch (error) {
    console.error('流式请求失败:', error);
    const aiMessage = messages.value.find(m => m.id === aiMessageId);
    if (aiMessage) {
      aiMessage.content = '抱歉，出错了，请稍后再试。';
    }
  } finally {
    // 6. 请求结束
    isLoading.value = false;
  }
};

// 清空对话
const clearChat = () => {
  messages.value = []
  ElMessage.success('对话已清空')
}

// 组件挂载时的初始化
// onMounted(() => {
//   // 可以在这里添加一些初始化逻辑
//   ElMessage({
//     message: '欢迎使用AI对话助手！',
//     type: 'success',
//     duration: 2000
//   })
// })
</script>

<style scoped>
.ai-chat-container {
  display: flex;
  flex-direction: column;
  height: 680px;
  max-width: 1200px;
  margin: 0 auto;
  background: #f5f7fa;
  border-radius: 8px;
  overflow: hidden;
}

.chat-header {
  border-top: 0px;
  flex-shrink: 0;
  border-radius: 0;
  border-bottom: 0px solid #e4e7ed;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.header-info h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background: #ffffff;
}

.empty-state {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.messages-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message-item {
  display: flex;
  gap: 12px;
  animation: fadeIn 0.3s ease-in;
}

.message-item.user {
  flex-direction: row-reverse;
}

.message-item.user .message-content {
  background: #409eff;
  color: white;
  border-radius: 18px 18px 4px 18px;
}

.message-item.assistant .message-content {
  background: #f4f4f5;
  color: #303133;
  border-radius: 18px 18px 18px 4px;
}

.message-avatar {
  flex-shrink: 0;
  align-self: flex-end;
}

.message-content {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 18px;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
  font-size: 12px;
  opacity: 0.8;
}

.message-role {
  font-weight: 600;
}

.message-time {
  font-size: 11px;
}

.message-body {
  line-height: 1.5;
}

.loading-message {
  display: flex;
  align-items: center;
  gap: 8px;
  font-style: italic;
  opacity: 0.7;
}

.message-text {
  white-space: pre-wrap;
  word-break: break-word;
}

.input-area {
  flex-shrink: 0;
  padding: 0px;
  background: #f5f7fa;
}

.input-area .el-card {
  border-radius: 0px;
}

.input-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message-input {
  resize: none;
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 滚动条样式 */
.messages-container::-webkit-scrollbar {
  width: 6px;
}

.messages-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.messages-container::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.messages-container::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .ai-chat-container {
    height: 100vh;
    max-width: 100%;
    border-radius: 0;
  }
  
  .message-content {
    max-width: 85%;
  }
  
  .input-area {
    padding: 12px;
  }
}
</style>