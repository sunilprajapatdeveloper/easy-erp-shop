<template>
  <div class="login-wrapper d-flex flex-wrap overflow-hidden">
    <div class="login-bg"></div>
    <div class="login-form-wrap">
      <div class="login-form">
        <router-link to="#" class="logo">
          EasyERPShop
          <!-- <img src="../../assets" alt="Image" class="d-block mx-auto" /> -->
        </router-link>
        <form @submit.prevent="signupUser">
          <div class="form-group mb-15">
            <label class="d-block fs-14 text-black mb-2">Full Name</label>
            <input v-model="name" type="text" class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-title"
              placeholder="John Doe" />
          </div>

          <div class="form-group mb-15">
            <label class="d-block fs-14 text-black mb-2">Email or Phone Number</label>
            <input v-model="username" type="text" class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-title"
              placeholder="example@gmail.com or 9876543210" />
          </div>

          <div class="form-group mb-15 position-relative">
            <label class="d-block fs-14 text-black mb-2">Password</label>
            <input v-model="password" type="password" class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black"
              placeholder="Enter password" />
          </div>

          <div class="form-group mb-20 position-relative">
            <label class="d-block fs-14 text-black mb-2">Confirm Password</label>
            <input v-model="confirmPassword" type="password"
              class="w-100 h-55 bg_ash border-0 rounded-1 fs-14 text-black" placeholder="Confirm password" />
          </div>

          <!-- Error/Success message display -->
          <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
          <p v-if="successMsg" class="text-success">{{ successMsg }}</p>

          <button class="btn style-one w-100 d-block">Sign Up</button>
        </form>

        <!-- Login Link -->
        <p class="mt-3">
          Already have an account?
          <router-link to="/login" class="text-primary text-decoration-none">Login here</router-link>
        </p>
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

const name = ref("");
const username = ref("");
const password = ref("");
const confirmPassword = ref("");
const errorMsg = ref("");
const successMsg = ref("");

const signupUser = async () => {
  errorMsg.value = "";
  successMsg.value = "";

  if (!name.value || !username.value || !password.value || !confirmPassword.value) {
    errorMsg.value = "All fields are required.";
    return;
  }

  if (password.value !== confirmPassword.value) {
    errorMsg.value = "Passwords do not match.";
    return;
  }

  try {
    const [first, ...last] = name.value.trim().split(" ");
    const userPayload = {
      email: username.value.includes("@") ? username.value : "",
      phone: !username.value.includes("@") ? username.value : "",
      password: password.value,
      firstname: first,
      lastname: last.join(" "),
      username: username.value,
      profile: "",
      roleId: 2,
    };

    const res = await userStore.addUser(userPayload);

    if (res?.status === 201 || res?.data?.id) {
      alert("User created successfully!");
      router.push("/login");
    } else {
      errorMsg.value = "Signup failed. Try again.";
    }
  } catch (error) {
    console.error("Signup failed:", error);
    errorMsg.value = error?.response?.data?.message || "Signup failed.";
  }
};
</script>

<style lang="scss" scoped>
.login-wrapper {
  .login-bg {
    background-image: url(../../assets/img/login-bg.webp);
    background-repeat: no-repeat;
    background-size: cover;
    background-position: bottom center;
    width: 50%;
  }

  .login-form-wrap {
    display: flex;
    flex-wrap: wrap;
    flex-direction: column;
    justify-content: center;
    width: 50%;
    background-color: var(--whiteColor);
    padding: 0 50px;

    .logo {
      display: block;
      margin-bottom: 20px;
      border-bottom: 1px solid #eef3fa;
      padding-bottom: 25px;
    }

    .login-form {
      border: 1px solid var(--ashColor);
      padding: 25px;
    }

    h5 {
      font-weight: 500;
      border-bottom: 1px solid #eef3fa;
      text-align: center;
      padding-bottom: 20px;
      margin: 0 0 20px;
    }
  }

  .login-credential-item {
    border-bottom: 1px solid #eef3fa;
  }
}

.form-group {
  #toggler {
    right: 20px;
    top: 65px;
    transform: translateY(-50%);
    cursor: pointer;
  }
}

@media only screen and (max-width: 991px) {
  .login-wrapper {
    padding: 50px 0;

    .login-bg {
      display: none;
    }

    .login-form-wrap {
      width: 100%;
      height: 100%;
      padding: 0 12px;

      .login-form {
        padding: 20px 10px;
      }
    }
  }
}

@media only screen and (min-width: 992px) {
  .login-wrapper {
    height: 100vh;

    .login-bg {
      height: 100vh;
    }
  }
}

@media only screen and (min-width: 1920px) {
  .login-wrapper {
    .login-bg {
      width: 61%;
    }

    .login-form-wrap {
      width: 39%;
      padding: 0 100px;

      .logo {
        margin-bottom: 30px;
        padding-bottom: 45px;
      }

      .login-form {
        padding: 25px 35px;
      }

      h5 {
        padding-bottom: 20px;
        margin: 0 0 20px;
      }
    }
  }
}
</style>