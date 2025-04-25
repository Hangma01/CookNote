<script setup>
import { RouterView } from 'vue-router'
</script>

<template>
    <RouterView />
</template>

<style>

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

ul {
  list-style: none;
}

.input-field {
  border: none;
  outline: none;
}

.v-field--error:not(.v-field--disabled), 
.v-messages__message {
  color: #ff3f3f;
}
.v-field--error:not(.v-field--disabled) .v-label.v-field-label {
  color: #ff3f3f !important; /* Tomato 색상 (예시) */
}
</style>

