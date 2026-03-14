<template>
  <div class="warehouse-setup">
    <div class="setup-section">
      <h2>Configure your warehouses</h2>
      <p class="section-subtitle">Add at least one warehouse to start operating.</p>

      <!-- Loading state for currencies -->
      <div v-if="loadingCurrencies" class="loading-state">
        <i class="ri-loader-4-line spin"></i> Loading currencies...
      </div>
      <div v-else-if="currencyError" class="error-state">{{ currencyError }}</div>

      <!-- Warehouse forms -->
      <div v-else>
        <div v-for="(warehouse, index) in warehouses" :key="index" class="warehouse-card">
          <div class="warehouse-card-header">
            <h4>Warehouse {{ index + 1 }}</h4>
            <button v-if="warehouses.length > 1" class="btn btn-text btn-sm" @click="removeWarehouse(index)">
              <i class="ri-close-line"></i>
            </button>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label :for="'name' + index">Name *</label>
              <input type="text" :id="'name' + index" v-model="warehouse.name" placeholder="Main Warehouse"
                @input="validateAll" />
              <div v-if="errors[`warehouse_${index}_name`]" class="error-message">
                {{ errors[`warehouse_${index}_name`] }}
              </div>
            </div>
          </div>

          <div class="form-group">
            <label :for="'city' + index">City *</label>
            <input type="text" :id="'city' + index" v-model="warehouse.city" placeholder="New York"
              @input="validateAll" />
            <div v-if="errors[`warehouse_${index}_city`]" class="error-message">
              {{ errors[`warehouse_${index}_city`] }}
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label :for="'country' + index">Country *</label>
              <input type="text" :id="'country' + index" v-model="warehouse.country" placeholder="USA"
                @input="validateAll" />
              <div v-if="errors[`warehouse_${index}_country`]" class="error-message">
                {{ errors[`warehouse_${index}_country`] }}
              </div>
            </div>
            <div class="form-group">
              <label :for="'zip' + index">Zip Code *</label>
              <input type="text" :id="'zip' + index" v-model="warehouse.zipCode" placeholder="10001"
                @input="validateAll" />
              <div v-if="errors[`warehouse_${index}_zip`]" class="error-message">
                {{ errors[`warehouse_${index}_zip`] }}
              </div>
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label :for="'currency' + index">Default Currency *</label>
              <select :id="'currency' + index" v-model="warehouse.currencyId" @change="validateAll">
                <option value="" disabled>Select currency</option>
                <option v-for="curr in companyCurrencies" :key="curr.id" :value="curr.currencyId">
                  {{ curr.currencyCode }} - {{ curr.currencyName }}
                </option>
              </select>
              <div v-if="errors[`warehouse_${index}_currency`]" class="error-message">
                {{ errors[`warehouse_${index}_currency`] }}
              </div>
            </div>
            <div class="form-group" v-if="index === 0">
              <label class="checkbox-label">
                <input type="checkbox" v-model="warehouse.isDefault" />
                <span class="checkmark"></span>
                Set as default warehouse for all operations
              </label>
            </div>
          </div>
        </div>

        <button class="btn btn-outline add-warehouse-btn" @click="addWarehouse">
          <i class="ri-add-line"></i> Add Another Warehouse
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed, watch } from 'vue'
import { useOnboardingStore } from '@/stores/onboardingStore'
import { useCompanyCurrencyStore } from '@/stores/companyCurrencyStore'
import { useWarehouseStore } from '@/stores/warehouseStore'
import { useWarehouseCurrencyStore } from '@/stores/warehouseCurrencyStore'
import { CurrencyStatus } from '@/enums/CurrencyStatus'

export default defineComponent({
  name: 'WarehouseSetup',
  emits: ['validated'],
  setup(_, { emit }) {
    const onboardingStore = useOnboardingStore()
    const companyCurrencyStore = useCompanyCurrencyStore()
    const warehouseStore = useWarehouseStore()
    const warehouseCurrencyStore = useWarehouseCurrencyStore()

    const companyId = computed(() => onboardingStore.companyId)

    // Company currencies list
    const companyCurrencies = ref<any[]>([])
    const loadingCurrencies = ref(false)
    const currencyError = ref('')

    // Warehouse form data – start with one empty warehouse
    const warehouses = ref([
      {
        name: '',
        city: '',
        country: '',
        zipCode: '',
        currencyId: null as number | null,
        isDefault: true, // first warehouse is default by default
      },
    ])

    const errors = ref<Record<string, string>>({})

    // Validation function
    const validateAll = () => {
      const newErrors: Record<string, string> = {}
      warehouses.value.forEach((wh, idx) => {
        if (!wh.name.trim()) newErrors[`warehouse_${idx}_name`] = 'Name is required'
        if (!wh.city.trim()) newErrors[`warehouse_${idx}_city`] = 'City is required'
        if (!wh.country.trim()) newErrors[`warehouse_${idx}_country`] = 'Country is required'
        if (!wh.zipCode.trim()) newErrors[`warehouse_${idx}_zip`] = 'Zip Code is required'
        if (!wh.currencyId) newErrors[`warehouse_${idx}_currency`] = 'Currency is required'
      })
      errors.value = newErrors
      const isValid = Object.keys(newErrors).length === 0
      emit('validated', isValid)
      return isValid
    }

    // Add a new warehouse (default is false for additional)
    const addWarehouse = () => {
      warehouses.value.push({
        name: '',
        city: '',
        country: '',
        zipCode: '',
        currencyId: null,
        isDefault: false,
      })
    }

    // Remove a warehouse (cannot remove last one)
    const removeWarehouse = (index: number) => {
      if (warehouses.value.length > 1) {
        warehouses.value.splice(index, 1)
        validateAll()
      }
    }

    // Save warehouses – called by parent via template ref
    const saveWarehouses = async (): Promise<void> => {
      if (!validateAll()) return
      if (!companyId.value) {
        throw new Error('Company ID missing')
      }

      for (const wh of warehouses.value) {
        // Create warehouse
        const created = await warehouseStore.addWarehouse({
          name: wh.name,
          city: wh.city,
          country: wh.country,
          zipCode: wh.zipCode,
          currencyId: wh.currencyId!, // validated, so not null
          isDefault: wh.isDefault,
        })

        // Create warehouse-currency association (default currency)
        await warehouseCurrencyStore.create(created.id, {
          currencyId: wh.currencyId!,
          defaultCurrency: true,
          status: CurrencyStatus.ACTIVE, // assuming enum has ACTIVE
        })
      }
    }

    // Load company currencies on mount
    onMounted(async () => {
      if (!companyId.value) {
        currencyError.value = 'Company not found. Please go back.'
        return
      }
      loadingCurrencies.value = true
      try {
        await companyCurrencyStore.fetchAll(companyId.value)
        companyCurrencies.value = companyCurrencyStore.list
        if (companyCurrencies.value.length === 0) {
          currencyError.value = 'No currencies found for this company. Please set up currencies first.'
        }
      } catch (err: any) {
        currencyError.value = err.message || 'Failed to load currencies'
      } finally {
        loadingCurrencies.value = false
      }
    })

    // Re-validate on warehouse changes
    watch(warehouses, validateAll, { deep: true, immediate: true })

    return {
      warehouses,
      companyCurrencies,
      loadingCurrencies,
      currencyError,
      errors,
      addWarehouse,
      removeWarehouse,
      validateAll,
      saveWarehouses,
    }
  },
})
</script>

<style lang="scss" scoped>
.warehouse-setup {
  max-width: 800px;
  margin: 0 auto;

  .setup-section {
    background: white;
    border-radius: 16px;
    padding: 2rem;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.05);
  }

  h2 {
    font-size: 1.75rem;
    margin-bottom: 0.5rem;
    color: var(--titleColor);
  }

  .section-subtitle {
    color: var(--textColor);
    margin-bottom: 2rem;
    font-size: 1.1rem;
  }

  .loading-state,
  .error-state {
    text-align: center;
    padding: 2rem;
    color: #666;
  }

  .spin {
    animation: spin 1s linear infinite;
  }

  @keyframes spin {
    from {
      transform: rotate(0deg);
    }

    to {
      transform: rotate(360deg);
    }
  }

  .warehouse-card {
    background: #f9f9f9;
    border: 1px solid #eee;
    border-radius: 12px;
    padding: 1.5rem;
    margin-bottom: 1.5rem;

    &-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 1rem;

      h4 {
        margin: 0;
        font-size: 1.125rem;
        color: var(--titleColor);
      }

      .btn-text {
        color: #ff4444;

        &:hover {
          background: rgba(255, 68, 68, 0.1);
        }
      }
    }
  }

  .form-row {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 1rem;
  }

  .form-group {
    margin-bottom: 1rem;

    label {
      display: block;
      margin-bottom: 0.5rem;
      font-weight: 500;
      color: var(--titleColor);
    }

    input,
    select {
      width: 100%;
      padding: 0.75rem;
      border: 2px solid rgba(0, 0, 0, 0.1);
      border-radius: 8px;
      font-size: 1rem;
      transition: border-color 0.3s ease;

      &:focus {
        outline: none;
        border-color: var(--primaryColor);
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
      }
    }
  }

  .checkbox-label {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    cursor: pointer;

    input {
      display: none;
    }

    .checkmark {
      width: 20px;
      height: 20px;
      border: 2px solid rgba(0, 0, 0, 0.2);
      border-radius: 4px;
      position: relative;
    }

    input:checked+.checkmark {
      background: var(--primaryColor);
      border-color: var(--primaryColor);
    }

    input:checked+.checkmark::after {
      content: '';
      position: absolute;
      left: 5px;
      top: 2px;
      width: 6px;
      height: 10px;
      border: solid white;
      border-width: 0 2px 2px 0;
      transform: rotate(45deg);
    }
  }

  .error-message {
    color: #ff4444;
    font-size: 0.875rem;
    margin-top: 0.25rem;
  }

  .add-warehouse-btn {
    width: 100%;
    padding: 1rem;
    border: 2px dashed #ccc;
    background: transparent;
    color: #666;
    margin-top: 1rem;

    &:hover {
      border-color: var(--primaryColor);
      color: var(--primaryColor);
      background: rgba(102, 126, 234, 0.05);
    }
  }

  @media (max-width: 768px) {
    .form-row {
      grid-template-columns: 1fr;
    }
  }
}
</style>