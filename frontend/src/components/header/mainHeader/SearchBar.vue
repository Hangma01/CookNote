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
    height: 3rem;
    border: 0.1rem solid #aaa;
    border-radius: 1rem;
    display: flex;
    gap: 0.8rem;
    align-items: center;
    padding: 0 1.2rem;


    &.focused {
        border-width: 0.2rem;
        border-color: #c09370;
        padding: 0 1.1rem;
    }

    .search-bar-input-field {
        height: 2rem;
        width: 24rem;
        font-size: 1.2rem;
    }

    .search-bar-icon {
        font-size: 1.4rem;
        color: #454F5B;
    }
}
</style>