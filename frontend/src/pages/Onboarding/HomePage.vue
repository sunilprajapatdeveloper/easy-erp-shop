<template>
    <MarketingHeader />
    <HeroSection />
    <TrustBadges />
    <FeaturesGrid />
    <DashboardPreview />
    <PosPreview />
    <HowItWorks />
    <TestimonialsSection />
    <PricingPreview />
    <FaqSection />
    <FinalCta />
    <MarketingFooter />
</template>

<script lang="ts">
import { defineComponent, onMounted, onUnmounted } from 'vue'
import MarketingHeader from '@/components/Onboarding/Layouts/MarketingHeader.vue'
import MarketingFooter from '@/components/Onboarding/Layouts/MarketingFooter.vue'
import HeroSection from '@/components/Onboarding/Home/HeroSection.vue'
import TrustBadges from '@/components/Onboarding/Home/TrustBadges.vue'
import FeaturesGrid from '@/components/Onboarding/Home/FeaturesGrid.vue'
import DashboardPreview from '@/components/Onboarding/Home/DashboardPreview.vue'
import PosPreview from '@/components/Onboarding/Home/PosPreview.vue'
import HowItWorks from '@/components/Onboarding/Home/HowItWorks.vue'
import TestimonialsSection from '@/components/Onboarding/Home/TestimonialsSection.vue'
import PricingPreview from '@/components/Onboarding/Home/PricingPreview.vue'
import FaqSection from '@/components/Onboarding/Home/FaqSection.vue'
import FinalCta from '@/components/Onboarding/Home/FinalCta.vue'

export default defineComponent({
    name: 'HomePage',
    components: {
        MarketingHeader,
        MarketingFooter,
        HeroSection,
        TrustBadges,
        FeaturesGrid,
        DashboardPreview,
        PosPreview,
        HowItWorks,
        TestimonialsSection,
        PricingPreview,
        FaqSection,
        FinalCta
    },
    setup() {
        // Add scroll animations
        const handleScrollAnimations = () => {
            const elements = document.querySelectorAll('.animate-on-scroll')
            elements.forEach(element => {
                const position = element.getBoundingClientRect()
                if (position.top < window.innerHeight - 100) {
                    element.classList.add('animated')
                }
            })
        }

        onMounted(() => {
            document.body.classList.add('marketing-page')
            window.addEventListener('scroll', handleScrollAnimations)
            handleScrollAnimations() // Initial check
        })

        onUnmounted(() => {
            document.body.classList.remove('marketing-page')
            window.removeEventListener('scroll', handleScrollAnimations)
        })

        return {}
    }
})
</script>

<style lang="scss" scoped>
// Page-specific styles
.marketing-page {
    overflow-x: hidden;
}

// Global animations for marketing page
::v-deep {
    .animate-on-scroll {
        opacity: 0;
        transform: translateY(30px);
        transition: opacity 0.6s ease, transform 0.6s ease;

        &.animated {
            opacity: 1;
            transform: translateY(0);
        }
    }

    .delay-1 {
        transition-delay: 0.1s;
    }

    .delay-2 {
        transition-delay: 0.2s;
    }

    .delay-3 {
        transition-delay: 0.3s;
    }

    .delay-4 {
        transition-delay: 0.4s;
    }
}
</style>