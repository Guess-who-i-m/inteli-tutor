<script setup>
    // 使用 Vue 3 的 <script setup> 语法糖
    // 这里导入了两个组件，但实际上在当前组件中并未直接使用
    // 这里只是做了导入，具体怎么显示内容则是由router进行控制
    // 在router/index.js中有相关的内容定义
    import LoginVue from './views/Login.vue';
    import LayoutVue from './views/Layout.vue';
</script>

<template>
    <!-- 这是 Vue Router 的视图出口 -->
    <!-- router-view 是一个动态组件，会根据当前路由路径显示对应的组件 -->
    <!-- 这是单页面应用(SPA)的核心机制，路由变化时只替换这部分内容 -->
    <router-view></router-view>
</template>

<style scoped>
    /* 
     * scoped 样式，意味着这些样式只作用于当前组件
     * 当前为空，表示没有组件特定的样式
     * scoped 是通过添加 data-v-xxx 属性选择器实现的样式隔离
     */
</style>
