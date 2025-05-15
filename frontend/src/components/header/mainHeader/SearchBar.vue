<script setup>
import { ref } from 'vue'

const isFocused = ref(false)
const searchValue = ref('')


// searchValue
const handleInput = (e) => {
    searchValue.value = e.target.value
}
</script>

<template>
    <div class="search-bar-container" :class="{ focused: isFocused}" >
        <input class="search-bar-input-field input-field"
                placeholder="검색어를 입력하세요."
                :value="searchValue"
                @input="handleInput"
                @focus="isFocused = true"
                @blur="isFocused = false" />

        <font-awesome-icon :icon="['fas', 'magnifying-glass']" class="search-bar-icon" />
    </div>
</template>



<style lang="scss" scoped>
.search-bar-container {
    width: 28rem;
    height: 2.7rem;
    border: 1px solid #aaa;
    border-radius: 1rem;
    display: flex;
    gap: 0.8rem;
    align-items: center;
    padding: 0rem 20px;


    &.focused {
        border-width: 3px;
        border-color: #c09370;
        padding: 0 18px;
    }

    .search-bar-input-field {
        height: 1rem;
        width: 23rem;
        font-size: 1rem;
    }

    .search-bar-icon {
        font-size: 1.1rem;
        color: #454F5B;
    }
}
</style>