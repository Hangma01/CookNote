<script setup>
import { ref } from 'vue';

const dialog = ref(false);
</script>

<template>
    <div class="text-center pa-4">
        <v-btn @click="dialog = true"> Open Dialog </v-btn>

        <v-dialog v-model="dialog" width="auto">
            <v-card
                max-width="400"
                prepend-icon="mdi-update"
                text="Your application will relaunch automatically after the update is complete."
                title="제재 사유"
            >
                <template v-slot:actions>
                    <v-btn class="ms-auto" text="Ok" @click="dialog = false"></v-btn>
                </template>
            </v-card>
        </v-dialog>
    </div>
</template>



<style lang="scss" scoped>
</style>