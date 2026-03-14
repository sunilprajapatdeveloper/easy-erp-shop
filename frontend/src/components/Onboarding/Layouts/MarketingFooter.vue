<template>
    <footer class="marketing-footer">
        <!-- Main Footer -->
        <div class="footer-main">
            <div class="container">
                <div class="footer-grid">
                    <!-- Brand Column -->
                    <div class="footer-brand">
                        <router-link to="/" class="logo">
                            <!-- <img src="@/assets/logo.svg" alt="YourERP"> -->
                            <span class="logo-text">EasyERPShop</span>
                        </router-link>
                        <p class="brand-description">
                            Complete POS & ERP solution for multi-branch businesses.
                            Manage inventory, sales, purchases, and accounting in one platform.
                        </p>
                        <div class="social-links">
                            <a href="#" class="social-link" aria-label="Twitter">
                                <i class="ri-twitter-fill"></i>
                            </a>
                            <a href="#" class="social-link" aria-label="Facebook">
                                <i class="ri-facebook-fill"></i>
                            </a>
                            <a href="#" class="social-link" aria-label="LinkedIn">
                                <i class="ri-linkedin-fill"></i>
                            </a>
                            <a href="#" class="social-link" aria-label="Instagram">
                                <i class="ri-instagram-fill"></i>
                            </a>
                            <a href="#" class="social-link" aria-label="YouTube">
                                <i class="ri-youtube-fill"></i>
                            </a>
                        </div>
                    </div>

                    <!-- Product Links -->
                    <div class="footer-column">
                        <h4 class="column-title">Product</h4>
                        <ul class="footer-links">
                            <li><a href="#features">Features</a></li>
                            <li><a href="#how-it-works">How It Works</a></li>
                            <li><a href="#pricing">Pricing</a></li>
                            <li><a href="#">Updates</a></li>
                            <li><a href="#">Roadmap</a></li>
                            <li><a href="#">Mobile App</a></li>
                        </ul>
                    </div>

                    <!-- Solutions Links -->
                    <div class="footer-column">
                        <h4 class="column-title">Solutions</h4>
                        <ul class="footer-links">
                            <li><a href="#">Retail Stores</a></li>
                            <li><a href="#">Wholesale & Distribution</a></li>
                            <li><a href="#">Manufacturing</a></li>
                            <li><a href="#">Restaurants</a></li>
                            <li><a href="#">E-commerce</a></li>
                            <li><a href="#">Multi-location Businesses</a></li>
                        </ul>
                    </div>

                    <!-- Resources Links -->
                    <div class="footer-column">
                        <h4 class="column-title">Resources</h4>
                        <ul class="footer-links">
                            <li><a href="#">Documentation</a></li>
                            <li><a href="#">API Reference</a></li>
                            <li><a href="#">Help Center</a></li>
                            <li><a href="#">Blog</a></li>
                            <li><a href="#">Webinars</a></li>
                            <li><a href="#">Community</a></li>
                        </ul>
                    </div>

                    <!-- Company Links -->
                    <div class="footer-column">
                        <h4 class="column-title">Company</h4>
                        <ul class="footer-links">
                            <li><a href="#">About Us</a></li>
                            <li><a href="#">Careers</a></li>
                            <li><a href="#">Partners</a></li>
                            <li><a href="#">Contact</a></li>
                            <li><a href="#">Press</a></li>
                            <li><a href="#">Legal</a></li>
                        </ul>
                    </div>

                    <!-- Newsletter -->
                    <div class="footer-newsletter">
                        <h4 class="column-title">Stay Updated</h4>
                        <p class="newsletter-description">
                            Get the latest updates, tips, and industry insights.
                        </p>
                        <form class="newsletter-form" @submit.prevent="subscribeNewsletter">
                            <div class="input-group">
                                <input type="email" v-model="email" placeholder="Enter your email" required>
                                <button type="submit" class="btn btn-primary">
                                    <i class="ri-send-plane-fill"></i>
                                </button>
                            </div>
                            <p class="newsletter-note">
                                By subscribing, you agree to our Privacy Policy
                            </p>
                        </form>
                    </div>
                </div>

                <!-- App Downloads -->
                <div class="app-downloads">
                    <h4>Get the App</h4>
                    <div class="download-buttons">
                        <a href="#" class="app-button">
                            <i class="ri-apple-fill"></i>
                            <div class="app-info">
                                <span class="app-label">Download on the</span>
                                <span class="app-store">App Store</span>
                            </div>
                        </a>
                        <a href="#" class="app-button">
                            <i class="ri-google-play-fill"></i>
                            <div class="app-info">
                                <span class="app-label">Get it on</span>
                                <span class="app-store">Google Play</span>
                            </div>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Footer Bottom -->
        <div class="footer-bottom">
            <div class="container">
                <div class="bottom-content">
                    <div class="copyright">
                        © {{ currentYear }} YourERP. All rights reserved.
                    </div>
                    <div class="legal-links">
                        <a href="#">Privacy Policy</a>
                        <a href="#">Terms of Service</a>
                        <a href="#">Cookie Policy</a>
                        <a href="#">GDPR</a>
                        <a href="#">Sitemap</a>
                    </div>
                    <div class="language-selector">
                        <i class="ri-global-line"></i>
                        <select v-model="selectedLanguage">
                            <option value="en">English</option>
                            <option value="es">Español</option>
                            <option value="fr">Français</option>
                            <option value="de">Deutsch</option>
                            <option value="hi">हिंदी</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>

        <!-- Back to Top -->
        <button class="back-to-top" @click="scrollToTop" :class="{ visible: showBackToTop }" aria-label="Back to top">
            <i class="ri-arrow-up-line"></i>
        </button>
    </footer>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, onUnmounted } from 'vue'

export default defineComponent({
    name: 'MarketingFooter',
    setup() {
        const email = ref('')
        const currentYear = new Date().getFullYear()
        const selectedLanguage = ref('en')
        const showBackToTop = ref(false)

        const subscribeNewsletter = () => {
            console.log('Subscribing email:', email.value)
            // API call would go here
            email.value = ''
        }

        const checkScroll = () => {
            showBackToTop.value = window.scrollY > 500
        }

        const scrollToTop = () => {
            window.scrollTo({ top: 0, behavior: 'smooth' })
        }

        onMounted(() => {
            window.addEventListener('scroll', checkScroll)
        })

        onUnmounted(() => {
            window.removeEventListener('scroll', checkScroll)
        })

        return {
            email,
            currentYear,
            selectedLanguage,
            showBackToTop,
            subscribeNewsletter,
            scrollToTop
        }
    }
})
</script>

<style lang="scss" scoped>
.marketing-footer {
    background: #1a1a2e;
    color: #e5e7eb;
    position: relative;

    .footer-main {
        padding: 80px 0 60px;
        border-bottom: 1px solid rgba(255, 255, 255, 0.1);

        .footer-grid {
            display: grid;
            grid-template-columns: 1fr;
            gap: 3rem;
            margin-bottom: 4rem;

            @media (min-width: 768px) {
                grid-template-columns: repeat(2, 1fr);
            }

            @media (min-width: 1024px) {
                grid-template-columns: repeat(5, 1fr);
            }
        }
    }

    .footer-brand {
        .logo {
            display: flex;
            align-items: center;
            gap: 10px;
            text-decoration: none;
            margin-bottom: 1.5rem;

            img {
                height: 32px;
                width: auto;
            }

            .logo-text {
                font-size: 1.5rem;
                font-weight: 700;
                color: white;
            }
        }

        .brand-description {
            color: #9ca3af;
            line-height: 1.6;
            margin-bottom: 1.5rem;
        }

        .social-links {
            display: flex;
            gap: 1rem;

            .social-link {
                width: 40px;
                height: 40px;
                border-radius: 50%;
                background: rgba(255, 255, 255, 0.1);
                display: flex;
                align-items: center;
                justify-content: center;
                color: #e5e7eb;
                text-decoration: none;
                transition: all 0.3s ease;

                &:hover {
                    background: var(--primaryColor);
                    transform: translateY(-2px);
                }

                i {
                    font-size: 1.25rem;
                }
            }
        }
    }

    .footer-column {
        .column-title {
            font-size: 1.125rem;
            font-weight: 600;
            color: white;
            margin-bottom: 1.5rem;
        }

        .footer-links {
            list-style: none;
            padding: 0;
            margin: 0;

            li {
                margin-bottom: 0.75rem;

                a {
                    color: #9ca3af;
                    text-decoration: none;
                    transition: color 0.3s ease;
                    font-size: 0.875rem;

                    &:hover {
                        color: white;
                    }
                }
            }
        }
    }

    .footer-newsletter {
        @media (min-width: 768px) and (max-width: 1023px) {
            grid-column: span 2;
        }

        .newsletter-description {
            color: #9ca3af;
            margin-bottom: 1.5rem;
            font-size: 0.875rem;
        }

        .newsletter-form {
            .input-group {
                display: flex;
                gap: 0.5rem;
                margin-bottom: 0.75rem;

                input {
                    flex: 1;
                    padding: 0.75rem 1rem;
                    background: rgba(255, 255, 255, 0.1);
                    border: 1px solid rgba(255, 255, 255, 0.2);
                    border-radius: 8px;
                    color: white;
                    font-size: 0.875rem;

                    &::placeholder {
                        color: #9ca3af;
                    }

                    &:focus {
                        outline: none;
                        border-color: var(--primaryColor);
                    }
                }

                button {
                    padding: 0.75rem 1.5rem;
                    border-radius: 8px;
                }
            }

            .newsletter-note {
                font-size: 0.75rem;
                color: #6b7280;
            }
        }
    }

    .app-downloads {
        padding-top: 3rem;
        border-top: 1px solid rgba(255, 255, 255, 0.1);
        text-align: center;

        h4 {
            font-size: 1.125rem;
            font-weight: 600;
            color: white;
            margin-bottom: 1.5rem;
        }

        .download-buttons {
            display: flex;
            gap: 1rem;
            justify-content: center;
            flex-wrap: wrap;

            .app-button {
                display: flex;
                align-items: center;
                gap: 0.75rem;
                padding: 0.75rem 1.5rem;
                background: rgba(255, 255, 255, 0.1);
                border-radius: 12px;
                text-decoration: none;
                transition: all 0.3s ease;
                min-width: 180px;

                &:hover {
                    background: rgba(255, 255, 255, 0.2);
                    transform: translateY(-2px);
                }

                i {
                    font-size: 1.5rem;
                    color: white;
                }

                .app-info {
                    display: flex;
                    flex-direction: column;
                    align-items: flex-start;

                    .app-label {
                        font-size: 0.75rem;
                        color: #9ca3af;
                    }

                    .app-store {
                        font-size: 1rem;
                        font-weight: 600;
                        color: white;
                    }
                }
            }
        }
    }

    .footer-bottom {
        padding: 2rem 0;

        .bottom-content {
            display: flex;
            flex-direction: column;
            gap: 1.5rem;
            align-items: center;

            @media (min-width: 768px) {
                flex-direction: row;
                justify-content: space-between;
            }
        }

        .copyright {
            color: #9ca3af;
            font-size: 0.875rem;
        }

        .legal-links {
            display: flex;
            gap: 1.5rem;
            flex-wrap: wrap;
            justify-content: center;

            a {
                color: #9ca3af;
                text-decoration: none;
                font-size: 0.875rem;
                transition: color 0.3s ease;

                &:hover {
                    color: white;
                }
            }
        }

        .language-selector {
            display: flex;
            align-items: center;
            gap: 0.5rem;

            i {
                color: #9ca3af;
            }

            select {
                background: transparent;
                border: none;
                color: white;
                font-size: 0.875rem;
                cursor: pointer;

                option {
                    background: #1a1a2e;
                    color: white;
                }

                &:focus {
                    outline: none;
                }
            }
        }
    }

    .back-to-top {
        position: fixed;
        bottom: 2rem;
        right: 2rem;
        width: 50px;
        height: 50px;
        background: var(--primaryColor);
        color: white;
        border: none;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        opacity: 0;
        visibility: hidden;
        transform: translateY(20px);
        transition: all 0.3s ease;
        z-index: 1000;
        box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);

        &.visible {
            opacity: 1;
            visibility: visible;
            transform: translateY(0);
        }

        &:hover {
            background: var(--secondaryColor);
            transform: translateY(-2px);
        }

        i {
            font-size: 1.25rem;
        }
    }
}
</style>