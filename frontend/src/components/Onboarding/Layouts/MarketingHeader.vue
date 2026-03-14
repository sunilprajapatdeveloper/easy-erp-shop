<template>
    <header class="marketing-header">
        <div class="container">
            <div class="header-content">
                <!-- Logo -->
                <router-link to="/" class="logo">
                    <!-- <img src="@/assets/logo.svg" alt="YourERP"> -->
                    <span class="logo-text">EasyERPShop</span>
                </router-link>

                <!-- Desktop Navigation -->
                <nav class="desktop-nav">
                    <div class="nav-links">
                        <a href="#features" class="nav-link">Features</a>
                        <a href="#how-it-works" class="nav-link">How It Works</a>
                        <a href="#pricing" class="nav-link">Pricing</a>
                        <div class="dropdown">
                            <button class="nav-link dropdown-toggle">
                                Resources <i class="ri-arrow-down-s-line"></i>
                            </button>
                            <div class="dropdown-menu">
                                <a href="#" class="dropdown-item">Blog</a>
                                <a href="#" class="dropdown-item">Documentation</a>
                                <a href="#" class="dropdown-item">API Reference</a>
                                <a href="#" class="dropdown-item">Help Center</a>
                            </div>
                        </div>
                    </div>

                    <div class="header-actions">
                        <a href="/login" class="btn btn-text">Log in</a>
                        <router-link to="/signup" class="btn btn-primary startTrialBtn">
                            Start free trial
                        </router-link>
                    </div>
                </nav>

                <!-- Mobile Menu Toggle -->
                <button class="mobile-menu-toggle" @click="toggleMobileMenu">
                    <i :class="mobileMenuOpen ? 'ri-close-line' : 'ri-menu-line'"></i>
                </button>
            </div>

            <!-- Mobile Menu -->
            <div class="mobile-menu" :class="{ 'open': mobileMenuOpen }">
                <div class="mobile-nav-links">
                    <a href="#features" class="mobile-nav-link" @click="closeMobileMenu">Features</a>
                    <a href="#how-it-works" class="mobile-nav-link" @click="closeMobileMenu">How It Works</a>
                    <a href="#pricing" class="mobile-nav-link" @click="closeMobileMenu">Pricing</a>
                    <div class="mobile-dropdown">
                        <button class="mobile-nav-link" @click="toggleMobileDropdown">
                            Resources <i
                                :class="mobileDropdownOpen ? 'ri-arrow-up-s-line' : 'ri-arrow-down-s-line'"></i>
                        </button>
                        <div class="mobile-dropdown-menu" :class="{ 'open': mobileDropdownOpen }">
                            <a href="#" class="mobile-dropdown-item">Blog</a>
                            <a href="#" class="mobile-dropdown-item">Documentation</a>
                            <a href="#" class="mobile-dropdown-item">API Reference</a>
                            <a href="#" class="mobile-dropdown-item">Help Center</a>
                        </div>
                    </div>
                </div>
                <div class="mobile-actions">
                    <a href="/login" class="btn btn-outline btn-block">Log in</a>
                    <router-link to="/signup" class="btn btn-primary btn-block">
                        Start free trial
                    </router-link>
                </div>
            </div>
        </div>
    </header>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'

export default defineComponent({
    name: 'MarketingHeader',
    setup() {
        const mobileMenuOpen = ref(false)
        const mobileDropdownOpen = ref(false)

        const toggleMobileMenu = () => {
            mobileMenuOpen.value = !mobileMenuOpen.value
            if (mobileMenuOpen.value) {
                document.body.style.overflow = 'hidden'
            } else {
                document.body.style.overflow = ''
            }
        }

        const closeMobileMenu = () => {
            mobileMenuOpen.value = false
            document.body.style.overflow = ''
        }

        const toggleMobileDropdown = () => {
            mobileDropdownOpen.value = !mobileDropdownOpen.value
        }

        return {
            mobileMenuOpen,
            mobileDropdownOpen,
            toggleMobileMenu,
            closeMobileMenu,
            toggleMobileDropdown
        }
    }
})
</script>

<style lang="scss" scoped>
.marketing-header {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    box-shadow: 0 2px 20px rgba(0, 0, 0, 0.05);
    z-index: 1000;
    padding: 1rem 0;

    .header-content {
        display: flex;
        align-items: center;
        justify-content: space-between;
        height: 60px;
    }

    .logo {
        display: flex;
        align-items: center;
        gap: 10px;
        text-decoration: none;

        img {
            height: 32px;
            width: auto;
        }

        .logo-text {
            font-size: 1.5rem;
            font-weight: 700;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
    }

    .startTrialBtn {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        // -webkit-background-clip: text;
        // -webkit-text-fill-color: transparent;
    }

    .desktop-nav {
        display: flex;
        align-items: center;
        gap: 2rem;

        @media (max-width: 992px) {
            display: none;
        }

        .nav-links {
            display: flex;
            align-items: center;
            gap: 2rem;
        }

        .nav-link {
            color: var(--titleColor);
            text-decoration: none;
            font-weight: 500;
            transition: color 0.3s ease;
            position: relative;

            &:hover {
                color: var(--primaryColor);
            }

            &.dropdown-toggle {
                display: flex;
                align-items: center;
                gap: 4px;
                background: none;
                border: none;
                cursor: pointer;
                font-family: inherit;
                font-size: inherit;
            }
        }

        .dropdown {
            position: relative;

            &:hover {
                .dropdown-menu {
                    opacity: 1;
                    visibility: visible;
                    transform: translateY(0);
                }
            }

            .dropdown-menu {
                position: absolute;
                top: 100%;
                left: 0;
                min-width: 200px;
                background: white;
                box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
                border-radius: 12px;
                padding: 0.5rem 0;
                opacity: 0;
                visibility: hidden;
                transform: translateY(10px);
                transition: all 0.3s ease;
                z-index: 100;

                .dropdown-item {
                    display: block;
                    padding: 0.75rem 1.5rem;
                    color: var(--titleColor);
                    text-decoration: none;
                    transition: background 0.3s ease;

                    &:hover {
                        background: rgba(0, 0, 0, 0.03);
                        color: var(--primaryColor);
                    }
                }
            }
        }

        .header-actions {
            display: flex;
            align-items: center;
            gap: 1rem;
        }
    }

    .mobile-menu-toggle {
        display: none;
        background: none;
        border: none;
        font-size: 1.5rem;
        color: var(--titleColor);
        cursor: pointer;
        padding: 0.5rem;

        @media (max-width: 992px) {
            display: block;
        }
    }

    .mobile-menu {
        position: fixed;
        top: 80px;
        left: 0;
        right: 0;
        bottom: 0;
        background: white;
        padding: 2rem;
        transform: translateX(100%);
        transition: transform 0.3s ease;
        z-index: 999;
        overflow-y: auto;

        &.open {
            transform: translateX(0);
        }

        .mobile-nav-links {
            display: flex;
            flex-direction: column;
            gap: 1rem;
            margin-bottom: 2rem;
        }

        .mobile-nav-link {
            padding: 1rem;
            color: var(--titleColor);
            text-decoration: none;
            font-size: 1.125rem;
            border-radius: 8px;
            transition: background 0.3s ease;

            &:hover {
                background: rgba(0, 0, 0, 0.03);
            }
        }

        .mobile-dropdown {
            .mobile-dropdown-menu {
                max-height: 0;
                overflow: hidden;
                transition: max-height 0.3s ease;
                padding-left: 1rem;

                &.open {
                    max-height: 300px;
                }

                .mobile-dropdown-item {
                    display: block;
                    padding: 0.75rem;
                    color: var(--textColor);
                    text-decoration: none;

                    &:hover {
                        color: var(--primaryColor);
                    }
                }
            }
        }

        .mobile-actions {
            display: flex;
            flex-direction: column;
            gap: 1rem;
        }
    }
}
</style>