import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import AOS from "aos";
import BootstrapVueNext from "bootstrap-vue-next";
import VueApexCharts from "vue3-apexcharts";
import { createPinia } from "pinia";

import "aos/dist/aos.css";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue-next/dist/bootstrap-vue-next.css";
import "bootstrap/dist/css/bootstrap.min.css";
// import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import "swiper/css";
import "swiper/css/bundle";
import * as bootstrap from "bootstrap";

import "./assets/custom.scss";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
import "vue-multiselect/dist/vue-multiselect.css";
import Toast from "vue-toastification";

// Font Awesome
import { library } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import {
  faTrash,
  faEdit,
  faEye,
  faPlus,
  faBoxOpen,
} from "@fortawesome/free-solid-svg-icons";
import 'remixicon/fonts/remixicon.css'

// Add icons to library
library.add(faTrash, faEdit, faEye, faPlus, faBoxOpen);

(window as any).bootstrap = bootstrap;

const app = createApp(App);
const pinia = createPinia();

const options = {
  transition: "Vue-Toastification__bounce",
  maxToasts: 20,
  newestOnTop: true,
};

pinia.use(piniaPluginPersistedstate);

app.component("FontAwesomeIcon", FontAwesomeIcon);
app.use(router);
app.use(pinia);
app.use(BootstrapVueNext);
app.use(VueApexCharts as any);
app.use(Toast, options);

app.mixin({
  mounted() {
    AOS.init();
  },
});

app.mount("#app");
