<script setup lang="ts">
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/userStore";

const router = useRouter();
const userStore = useUserStore();

onMounted(() => {
    userStore.logout();
    router.push("/login");
});
</script>