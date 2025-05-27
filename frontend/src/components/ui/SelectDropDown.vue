<script setup>
import { computed } from 'vue';

const props = defineProps({
    modelValue: Object,
    items: Array,
    label: String,
    itemTitle: String,
    itemValue: String,
})

const emit = defineEmits(['update:modelValue'])

const localValue = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})
</script>

<template>
    <v-select
        v-model="localValue"
        :items="items"
        :label="label"
        :item-title="itemTitle"
        :item-value="itemValue"  
        return-object
        variant="outlined"
        hide-details
        density="compact"
        single-line
    />
</template>


