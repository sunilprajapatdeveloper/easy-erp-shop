import { ref, computed, onUnmounted } from "vue";
import { Client, IMessage } from "@stomp/stompjs";
import { useToast } from "vue-toastification";
import { useBarcodeScannerStore } from "@/stores/barcodeScannerStore";
import { useUserStore } from "@/stores/userStore";
import type {
  BarcodeScanResponse,
  BarcodeScanRequest,
  ScannerRegistrationResponse,
  ScannerRegistrationRequest,
  ScannerStatusUpdateRequest,
  ScannerDisconnectRequest,
} from "@/types/barcodeScanner";

interface ScannerConfig {
  companyId: number;
  warehouseId: number;
  userId: number;
  scannerId?: string;
}

export function useBarcodeScanner() {
  const toast = useToast();
  const scannerStore = useBarcodeScannerStore();
  const userStore = useUserStore();

  const stompClient = ref<Client | null>(null);
  const isScanning = ref(false);
  const isConnecting = ref(false);
  const connectionError = ref<string | null>(null);

  const scannerType = ref<"MOBILE" | "PHYSICAL">("MOBILE");

  const connectWebSocket = (config: ScannerConfig): Promise<boolean> => {
    return new Promise((resolve) => {
      if (isConnecting.value) {
        resolve(false);
        return;
      }

      isConnecting.value = true;
      connectionError.value = null;

      try {
        // Use native WebSocket with wss protocol
        const wsUrl = `wss://liberal-tick-quiet.ngrok-free.app/ws-scanner`;
        console.log("Connecting via native WebSocket:", wsUrl);

        const socket = new WebSocket(wsUrl);

        stompClient.value = new Client({
          webSocketFactory: () => socket,
          reconnectDelay: 5000,
          heartbeatIncoming: 4000,
          heartbeatOutgoing: 4000,
          connectionTimeout: 15000,
          debug: (str) => {
            if (
              str.includes("ERROR") ||
              str.includes("Close") ||
              str.includes("Failed")
            ) {
              console.error("STOMP Error:", str);
            } else {
              console.log("STOMP Debug:", str);
            }
          },
          onConnect: (frame) => {
            console.log("Native WebSocket connected successfully", frame);
            scannerStore.setConnectionStatus(true);
            isConnecting.value = false;
            connectionError.value = null;

            // Subscribe to user-specific queues
            const subscriptions = [
              `/user/queue/scanner/response`,
              `/user/queue/scanner/registration`,
              `/user/queue/scanner/status`,
              `/user/queue/scanner/disconnect`,
            ];

            subscriptions.forEach((destination) => {
              stompClient.value?.subscribe(destination, (message: IMessage) => {
                console.log(
                  `Received message on ${destination}:`,
                  message.body
                );
                handleIncomingMessage(destination, message);
              });
            });

            // Subscribe to broadcast channel
            const broadcastDestination = `/topic/scanner/${config.warehouseId}/${config.userId}`;
            stompClient.value?.subscribe(
              broadcastDestination,
              (message: IMessage) => {
                console.log("Received broadcast scan:", message.body);
                handleBroadcastScan(JSON.parse(message.body));
              }
            );

            toast.success("Scanner connected successfully");
            resolve(true);
          },
          onStompError: (frame) => {
            console.error("Native WebSocket STOMP error:", frame);
            connectionError.value = "Native WebSocket connection failed";
            toast.error("Scanner connection failed");
            scannerStore.setConnectionStatus(false);
            isConnecting.value = false;
            resolve(false);
          },
          onWebSocketError: (event) => {
            console.error("Native WebSocket error:", event);
            connectionError.value = "Native WebSocket error";
            toast.error("WebSocket connection failed");
            scannerStore.setConnectionStatus(false);
            isConnecting.value = false;
            resolve(false);
          },
          onDisconnect: () => {
            console.log("WebSocket disconnected");
            scannerStore.setConnectionStatus(false);
            isConnecting.value = false;
          },
        });

        stompClient.value.activate();
      } catch (error) {
        console.error("Failed to initialize native WebSocket:", error);
        connectionError.value = "Failed to initialize native connection";
        toast.error("Failed to connect scanner");
        scannerStore.setConnectionStatus(false);
        isConnecting.value = false;
        resolve(false);
      }
    });
  };

  const handleIncomingMessage = (destination: string, message: IMessage) => {
    try {
      console.log(`Processing message from ${destination}:`, message.body);
      const data = JSON.parse(message.body);

      switch (destination) {
        case "/user/queue/scanner/response":
          handleScanResponse(data);
          break;
        case "/user/queue/scanner/registration":
          handleRegistrationResponse(data);
          break;
        case "/user/queue/scanner/status":
          console.log("Scanner status updated:", data);
          break;
        case "/user/queue/scanner/disconnect":
          console.log("Scanner disconnected:", data);
          handleScannerDisconnected(data);
          break;
      }
    } catch (error) {
      console.error("Error processing incoming message:", error);
    }
  };

  const handleScanResponse = (response: BarcodeScanResponse) => {
    console.log("Scan response received:", response);
    scannerStore.setLastScan(response);
    isScanning.value = false;

    if (response.success) {
      toast.success(`Scanned: ${response.productName}`);
      window.dispatchEvent(
        new CustomEvent("barcode-scanned", {
          detail: response,
        })
      );
    } else {
      toast.error(`Scan failed: ${response.errorMessage}`);
    }
  };

  const handleRegistrationResponse = (
    response: ScannerRegistrationResponse
  ) => {
    console.log("Registration response received:", response);

    if (response.status === "REGISTERED") {
      toast.success(`Scanner registered: ${response.scannerId}`);
      window.dispatchEvent(
        new CustomEvent("scanner-registered", {
          detail: response,
        })
      );
    } else {
      console.error("Registration failed:", response.message);
      toast.error(`Scanner registration failed: ${response.message}`);
    }
  };

  const handleScannerDisconnected = (response: any) => {
    console.log("Scanner disconnected:", response);
    toast.info(`Scanner disconnected: ${response.scannerId}`);
    scannerStore.setConnectionStatus(false);
  };

  const handleBroadcastScan = (response: BarcodeScanResponse) => {
    console.log("Broadcast scan received:", response);
    if (response.success) {
      toast.info(`Scan from ${response.scannerId}: ${response.productName}`);
      window.dispatchEvent(
        new CustomEvent("barcode-scanned-broadcast", {
          detail: response,
        })
      );
    }
  };

  const sendBarcodeScan = (
    barcode: string,
    scannerId: string,
    config: ScannerConfig
  ) => {
    if (!scannerStore.isConnected || !stompClient.value?.connected) {
      toast.error("Scanner not connected");
      return false;
    }

    isScanning.value = true;

    const scanRequest: BarcodeScanRequest = {
      scannerId,
      barcode,
      companyId: config.companyId,
    };

    console.log("Sending barcode scan:", scanRequest);

    try {
      stompClient.value.publish({
        destination: "/app/scanner/scan",
        body: JSON.stringify(scanRequest),
        headers: {
          companyId: config.companyId.toString(),
        },
      });
      return true;
    } catch (error) {
      console.error("Failed to send scan:", error);
      toast.error("Failed to send scan");
      isScanning.value = false;
      return false;
    }
  };

  const sendBarcodeScanRest = async (
    barcode: string,
    scannerId: string,
    config: ScannerConfig
  ) => {
    isScanning.value = true;

    try {
      const scanRequest: BarcodeScanRequest = {
        scannerId,
        barcode,
        companyId: config.companyId,
      };

      console.log("Sending REST barcode scan:", scanRequest);
      return await scannerStore.processScan(scanRequest);
    } finally {
      isScanning.value = false;
    }
  };

  const registerScannerViaWebSocket = (registrationData: {
    scannerName: string;
    scannerType: "MOBILE" | "USB" | "BLUETOOTH" | "WIFI";
    warehouseId: number;
    assignedUserId: number;
    companyId: number;
    ipAddress?: string;
    macAddress?: string;
  }) => {
    if (!stompClient.value?.connected) {
      toast.error("WebSocket not connected");
      return false;
    }

    const registrationRequest: ScannerRegistrationRequest = {
      scannerName: registrationData.scannerName,
      scannerType: registrationData.scannerType,
      warehouseId: registrationData.warehouseId,
      assignedUserId: registrationData.assignedUserId,
      companyId: registrationData.companyId,
      ipAddress: registrationData.ipAddress,
      macAddress: registrationData.macAddress,
    };

    console.log("Sending scanner registration:", registrationRequest);

    try {
      stompClient.value.publish({
        destination: "/app/scanner/register",
        body: JSON.stringify(registrationRequest),
        headers: {
          companyId: registrationData.companyId.toString(),
        },
      });
      return true;
    } catch (error) {
      console.error("Failed to register scanner:", error);
      toast.error("Failed to register scanner");
      return false;
    }
  };

  const updateScannerStatusViaWebSocket = (
    scannerId: string,
    status: string
  ) => {
    if (!stompClient.value?.connected) {
      toast.error("WebSocket not connected");
      return false;
    }

    const statusUpdate: ScannerStatusUpdateRequest = {
      scannerId,
      status,
    };

    try {
      stompClient.value.publish({
        destination: "/app/scanner/status",
        body: JSON.stringify(statusUpdate),
        headers: {
          companyId: userStore.currentUser?.companyId.toString() || "",
        },
      });
      return true;
    } catch (error) {
      console.error("Failed to update scanner status:", error);
      return false;
    }
  };

  const disconnectScannerViaWebSocket = (scannerId: string) => {
    if (!stompClient.value?.connected) {
      toast.error("WebSocket not connected");
      return false;
    }

    const disconnectRequest: ScannerDisconnectRequest = {
      scannerId,
    };

    try {
      stompClient.value.publish({
        destination: "/app/scanner/disconnect",
        body: JSON.stringify(disconnectRequest),
        headers: {
          companyId: userStore.currentUser?.companyId.toString() || "",
        },
      });
      return true;
    } catch (error) {
      console.error("Failed to disconnect scanner:", error);
      return false;
    }
  };

  const disconnect = () => {
    if (stompClient.value) {
      console.log("Disconnecting WebSocket...");
      stompClient.value.deactivate();
      scannerStore.setConnectionStatus(false);
    }
  };

  const getScannerConfig = () => {
    const user = userStore.currentUser;
    if (!user) {
      throw new Error("User not authenticated");
    }

    return {
      companyId: user.companyId,
      userId: user.id,
      warehouseId: user.defaultWarehouseId || 1,
    };
  };

  onUnmounted(() => {
    disconnect();
  });

  return {
    isConnected: computed(() => scannerStore.isConnected),
    isConnecting: computed(() => isConnecting.value),
    isScanning,
    lastScan: computed(() => scannerStore.lastScan),
    connectionError: computed(() => connectionError.value),
    scannerType,
    connectWebSocket,
    sendBarcodeScan,
    sendBarcodeScanRest,
    registerScannerViaWebSocket,
    updateScannerStatusViaWebSocket,
    disconnectScannerViaWebSocket,
    disconnect,
    getScannerConfig,
  };
}
