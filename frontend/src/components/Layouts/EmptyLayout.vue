<template>
    <div class="empty-layout">
        <slot />
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
    name: 'EmptyLayout'
})
</script>

<style lang="scss" scoped>
.empty-layout {
    min-height: 100vh;
}
</style>