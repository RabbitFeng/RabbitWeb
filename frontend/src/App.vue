<script setup>
import { ref } from 'vue'

const email = ref('')
const password = ref('')
const result = ref('')

// 演示:通过 /api 代理调用后端登录接口 (AccountController: POST /api/account/login)
async function login() {
  result.value = '请求中...'
  try {
    const resp = await fetch('/api/account/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: email.value, password: password.value })
    })
    const data = await resp.json()
    result.value = JSON.stringify(data, null, 2)
  } catch (e) {
    result.value = '请求失败: ' + e.message
  }
}
</script>

<template>
  <div class="container">
    <h1>R</h1>
    <div class="form">
      <input v-model="email" placeholder="email" />
      <input v-model="password" type="password" placeholder="password" />
      <button @click="login">登录</button>
    </div>
    <pre v-if="result">{{ result }}</pre>
  </div>
</template>

<style scoped>
.container {
  max-width: 360px;
  margin: 80px auto;
  font-family: system-ui, sans-serif;
}
.form {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
input, button {
  padding: 8px;
  font-size: 14px;
}
pre {
  background: #f5f5f5;
  padding: 12px;
  border-radius: 4px;
  overflow: auto;
}
</style>
