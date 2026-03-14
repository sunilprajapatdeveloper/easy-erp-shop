<template>
    <div class="public-layout">
        <MarketingHeader />
        <main>
            <slot />
        </main>
        <MarketingFooter />
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import MarketingHeader from './MarketingHeader.vue'
import MarketingFooter from './MarketingFooter.vue'

export default defineComponent({
    name: 'PublicLayout',
    components: {
        MarketingHeader,
        MarketingFooter
    }
})
</script>

<style lang="scss" scoped>
.public-layout {
    min-height: 100vh;
    display: flex;
    flex-direction: column;

    main {
        flex: 1;
        padding-top: 80px; // Header height
    }
}
</style>