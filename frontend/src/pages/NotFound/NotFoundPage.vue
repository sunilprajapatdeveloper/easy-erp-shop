<template>
    <div class="notfound-container d-flex flex-column align-items-center justify-content-center text-center">
        <h1 class="display-1 fw-bold text-danger">{{ code }}</h1>
        <h2 class="fs-24 fw-semibold mb-3">{{ title }}</h2>
        <p class="fs-16 text-muted mb-4">{{ message }}</p>
        <router-link :to="safeLink" class="btn style-five">
            {{ linkText }}
        </router-link>
    </div>
</template>

<script setup lang="ts">
import { useRoute } from "vue-router";
import { computed } from "vue";

const route = useRoute();

// Numeric error code
const code = computed(() => {
    const raw = route.query.code;
    const str = Array.isArray(raw) ? raw[0] : raw;
    return str ? parseInt(str as string, 10) || 404 : 404;
});

// Title and message
const title = computed(() => {
    const raw = route.query.title;
    return Array.isArray(raw) ? raw[0] : raw ?? "Page Not Found";
});

const message = computed(() => {
    const raw = route.query.message;
    return Array.isArray(raw) ? raw[0] : raw ?? "The page you are looking for might have been removed, had its name changed, or is temporarily unavailable.";
});

// Ensure link is always a string (RouterLink-safe)
const safeLink = computed<string>(() => {
    const raw = route.query.link;
    if (!raw) return "/"; // fallback
    return Array.isArray(raw) ? String(raw[0]) : String(raw); // ensure string
});

// Button text
const linkText = computed(() => {
    const raw = route.query.buttonName;
    return Array.isArray(raw) ? raw[0] : raw ?? "Go to Dashboard";
});
</script>

<style scoped>
.notfound-container {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100vh;
    background-color: #f5f5f5;
    padding: 0 20px;
    z-index: 9999;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.display-1 {
    font-size: 8rem;
}

.fs-24 {
    font-size: 1.5rem;
}

.fs-16 {
    font-size: 1rem;
}

.btn.style-five {
    padding: 0.75rem 2rem;
    font-size: 1rem;
}
</style>
