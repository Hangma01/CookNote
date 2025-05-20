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
  font-family: "Noto Sans KR", Helvetica, "Helvetica Neue", Arial, "sans-serif";
}

a{
  color: black;
  text-decoration: none;
}

ul {
  list-style: none;
}

.input-field {
  border: none;
  outline: none;
}

/* Chrome, Safari, Edge, Opera */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox  */
input[type="number"] {
  -webkit-appearance: none;
  -moz-appearance: textfield;
  appearance: none;
}

.v-field--error:not(.v-field--disabled), 
.v-messages__message {
  color: #ff3f3f;
}

.v-field--error:not(.v-field--disabled) .v-label.v-field-label {
  color: #ff3f3f !important; /* Tomato 색상 (예시) */
}

.v-label.v-field-label {
  font-size: 0.85rem;
}


.v-field__input {
  font-size: 0.9rem;
}
</style>

