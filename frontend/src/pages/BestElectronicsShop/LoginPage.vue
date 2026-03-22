<template>
  <div class="login-page">
    <div class="login-container">
      <!-- Left side: Branding / Background -->
      <div class="login-brand">
        <div class="brand-content">
          <h1 class="brand-title">EasyERPShop</h1>
          <p class="brand-tagline">Complete business management solution</p>
        </div>
      </div>

      <!-- Right side: Login Form -->
      <div class="login-form-container">
        <div class="form-card">
          <div class="form-header">
            <h2>Welcome back</h2>
            <p class="form-subtitle">Sign in to your account</p>
          </div>

          <form @submit.prevent="loginUser">
            <div class="form-group">
              <label for="username">Email or Phone</label>
              <input id="username" v-model="username" type="text" placeholder="admin@example.com or +1234567890"
                :class="{ 'error-input': errorMsg }" />
            </div>

            <div class="form-group">
              <label for="password">Password</label>
              <input id="password" v-model="password" type="password" placeholder="Enter your password"
                :class="{ 'error-input': errorMsg }" />
            </div>

            <!-- Error message -->
            <div v-if="errorMsg" class="error-message">
              <i class="ri-error-warning-line"></i> {{ errorMsg }}
            </div>

            <button type="submit" class="btn btn-primary btn-block">
              Sign in
            </button>
          </form>

          <div class="form-footer">
            <p>
              Don’t have an account?
              <router-link to="/signup" class="signup-link">Create an account</router-link>
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/userStore";

const router = useRouter();
const userStore = useUserStore();

const username = ref("");
const password = ref("");
const errorMsg = ref("");

const loginUser = async () => {
  errorMsg.value = "";

  try {
    await userStore.login({
      identifier: username.value,
      password: password.value,
    });
    router.push("/");
  } catch (error) {
    console.error(error);
    errorMsg.value = "Invalid username or password";
  }
};
</script>

<style lang="scss" scoped>
/* --------------------------------------------
   Login Page – Modern Enterprise Style
   Uses global design variables
-------------------------------------------- */
.login-page {
  min-height: 100vh;
  background: var(--color-background, #f8fafc);
  font-family: var(--font-sans, 'Inter', sans-serif);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1.5rem;
}

.login-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  max-width: 1200px;
  width: 100%;
  background: var(--color-surface, #ffffff);
  border-radius: var(--radius-xl, 1.5rem);
  box-shadow: var(--shadow-xl, 0 25px 50px -12px rgba(0, 0, 0, 0.25));
  overflow: hidden;
  min-height: 600px;
}

/* Left side – Branding */
.login-brand {
  background: linear-gradient(135deg, var(--color-primary, #4f46e5) 0%, var(--color-primary-dark, #3730a3) 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background-image: url('../../assets/img/login-bg.webp');
    background-size: cover;
    background-position: center;
    opacity: 0.15;
    mix-blend-mode: overlay;
  }

  .brand-content {
    position: relative;
    z-index: 1;
    text-align: center;
    max-width: 320px;
  }

  .brand-title {
    font-size: 2.5rem;
    font-weight: 700;
    margin-bottom: 1rem;
    letter-spacing: -0.02em;
    line-height: 1.2;
  }

  .brand-tagline {
    font-size: 1rem;
    opacity: 0.9;
    line-height: 1.6;
  }
}

/* Right side – Form */
.login-form-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  background: var(--color-surface, #ffffff);
}

.form-card {
  width: 100%;
  max-width: 380px;

  .form-header {
    margin-bottom: 2rem;
    text-align: center;

    h2 {
      font-size: 2rem;
      font-weight: 700;
      color: var(--color-text, #1e293b);
      margin-bottom: 0.5rem;
      letter-spacing: -0.02em;
    }

    .form-subtitle {
      color: var(--color-text-muted, #64748b);
      font-size: 0.9375rem;
    }
  }

  .form-group {
    margin-bottom: 1.5rem;

    label {
      display: block;
      margin-bottom: 0.5rem;
      font-weight: 500;
      color: var(--color-text, #1e293b);
      font-size: 0.875rem;
    }

    input {
      width: 100%;
      padding: 0.75rem 1rem;
      border: 1px solid var(--color-border, #e2e8f0);
      border-radius: var(--radius-md, 0.5rem);
      font-size: 0.9375rem;
      transition: var(--transition, all 0.2s ease);
      background: var(--color-surface, #ffffff);

      &:focus {
        outline: none;
        border-color: var(--color-primary, #4f46e5);
        box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
      }

      &.error-input {
        border-color: var(--color-danger, #ef4444);
        background-color: rgba(239, 68, 68, 0.02);
      }
    }
  }

  .error-message {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    background: rgba(239, 68, 68, 0.1);
    color: var(--color-danger, #ef4444);
    padding: 0.75rem 1rem;
    border-radius: var(--radius-md, 0.5rem);
    margin-bottom: 1.5rem;
    font-size: 0.875rem;
  }

  .btn-primary {
    width: 100%;
    padding: 0.875rem;
    font-weight: 600;
    font-size: 1rem;
    background: linear-gradient(135deg, var(--color-primary, #4f46e5) 0%, var(--color-primary-dark, #3730a3) 100%);
    color: white;
    border: none;
    border-radius: var(--radius-md, 0.5rem);
    cursor: pointer;
    transition: var(--transition, all 0.2s ease);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 10px 20px -5px var(--color-primary, #4f46e5);
    }

    &:active {
      transform: translateY(0);
    }
  }

  .form-footer {
    margin-top: 2rem;
    text-align: center;
    color: var(--color-text-muted, #64748b);
    font-size: 0.875rem;

    .signup-link {
      color: var(--color-primary, #4f46e5);
      font-weight: 500;
      text-decoration: none;

      &:hover {
        text-decoration: underline;
      }
    }
  }
}

/* Responsive Design */
@media (max-width: 768px) {
  .login-container {
    grid-template-columns: 1fr;
    min-height: auto;
  }

  .login-brand {
    display: none;
    /* Hide on small screens for cleaner form */
  }

  .login-form-container {
    padding: 2rem 1.5rem;
  }

  .form-card {
    max-width: 100%;
  }
}

@media (min-width: 1920px) {
  .login-container {
    max-width: 1400px;
  }

  .brand-title {
    font-size: 3rem;
  }

  .form-card {
    max-width: 420px;
  }
}
</style>
