<template>
    <div class="currency-selector">
        <!-- Show error/empty state if no currencies -->
        <div v-if="currencies.length === 0" class="empty-state">
            <i class="ri-error-warning-line"></i> No currencies available
        </div>

        <!-- Single Selection Mode -->
        <div v-else-if="!multiple" class="single-selector">
            <Multiselect v-model="internalValue" :options="formattedCurrencies" :multiple="false"
                :searchable="enableSearch" :close-on-select="true" :show-labels="false" :placeholder="placeholder"
                :custom-label="currencyLabel">
                <template #option="{ option }">
                    <div class="currency-option">
                        <span class="currency-flag">{{ getFlagEmoji(option.code) }}</span>
                        <div class="currency-details">
                            <span class="currency-code">{{ option.code }}</span>
                            <span class="currency-name">{{ option.name }}</span>
                        </div>
                        <span class="currency-symbol">{{ option.symbol }}</span>
                    </div>
                </template>
                <template #singleLabel="{ option }">
                    <div class="selected-currency">
                        <span class="currency-flag">{{ getFlagEmoji(option.code) }}</span>
                        <span class="currency-code">{{ option.code }}</span>
                        <span class="currency-name">- {{ option.name }}</span>
                    </div>
                </template>
            </Multiselect>
        </div>

        <!-- Multiple Selection Mode -->
        <div v-else class="multi-selector">
            <Multiselect v-model="internalMultipleValue" :options="filteredCurrencies" :multiple="true"
                :searchable="enableSearch" :close-on-select="false" :clear-on-select="false" :preserve-search="true"
                :show-labels="false" :placeholder="placeholder || 'Select currencies...'" :custom-label="currencyLabel"
                @select="handleAddCurrency" @remove="handleRemoveCurrency" @input="handleMultipleInput">
                <template #option="{ option }">
                    <div class="currency-option">
                        <span class="currency-flag">{{ getFlagEmoji(option.code) }}</span>
                        <div class="currency-details">
                            <span class="currency-code">{{ option.code }}</span>
                            <span class="currency-name">{{ option.name }}</span>
                        </div>
                        <span class="currency-symbol">{{ option.symbol }}</span>
                    </div>
                </template>
                <template #tag="{ option, remove }">
                    <div class="currency-tag">
                        <span class="tag-flag">{{ getFlagEmoji(option.code) }}</span>
                        <span class="tag-code">{{ option.code }}</span>
                        <button class="tag-remove" @click.prevent="() => remove(option)">
                            <i class="ri-close-line"></i>
                        </button>
                    </div>
                </template>
            </Multiselect>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, watch, PropType } from 'vue'
import Multiselect from 'vue-multiselect'

export interface CurrencyOption {
    id: number
    code: string
    name: string
    symbol: string
}

export default defineComponent({
    name: 'CurrencySelector',
    components: { Multiselect },
    props: {
        modelValue: {
            type: [String, Array] as PropType<string | string[]>,
            default: ''
        },
        multiple: {
            type: Boolean,
            default: false
        },
        enableSearch: {
            type: Boolean,
            default: true
        },
        placeholder: {
            type: String,
            default: 'Select currency...'
        },
        popularCurrencies: {
            type: Array as () => string[],
            default: () => ['USD', 'EUR', 'GBP', 'JPY', 'CAD', 'AUD', 'INR', 'CNY']
        },
        currencies: {
            type: Array as PropType<CurrencyOption[]>,
            required: true
        }
    },
    emits: ['update:modelValue', 'change'],
    setup(props, { emit }) {
        const formattedCurrencies = computed(() => {
            const popular = props.currencies.filter(c =>
                props.popularCurrencies.includes(c.code)
            )
            const others = props.currencies.filter(c =>
                !props.popularCurrencies.includes(c.code)
            ).sort((a, b) => a.code.localeCompare(b.code))
            return [...popular, ...others]
        })

        const internalValue = ref<CurrencyOption | null>(null)
        const internalMultipleValue = ref<CurrencyOption[]>([])

        const filteredCurrencies = computed(() => {
            const selectedCodes = internalMultipleValue.value.map(c => c.code)
            return formattedCurrencies.value.filter(currency =>
                !selectedCodes.includes(currency.code)
            )
        })

        const initializeValues = () => {
            if (!props.multiple && typeof props.modelValue === 'string') {
                const currency = formattedCurrencies.value.find(c => c.code === props.modelValue)
                internalValue.value = currency || null
            } else if (props.multiple && Array.isArray(props.modelValue)) {
                const uniqueCodes = [...new Set(props.modelValue)]
                internalMultipleValue.value = formattedCurrencies.value.filter(c =>
                    uniqueCodes.includes(c.code)
                )
            }
        }

        initializeValues()

        watch(internalValue, (newVal) => {
            if (!props.multiple) {
                const code = newVal ? newVal.code : ''
                emit('update:modelValue', code)
                emit('change', code)
            }
        })

        const getFlagEmoji = (currencyCode: string): string => {
            const flagMap: Record<string, string> = {
                USD: '🇺🇸', EUR: '🇪🇺', GBP: '🇬🇧', JPY: '🇯🇵',
                AUD: '🇦🇺', CAD: '🇨🇦', CHF: '🇨🇭', CNY: '🇨🇳',
                INR: '🇮🇳', SGD: '🇸🇬', HKD: '🇭🇰', KRW: '🇰🇷',
                MXN: '🇲🇽', BRL: '🇧🇷', RUB: '🇷🇺', ZAR: '🇿🇦',
                AED: '🇦🇪', SAR: '🇸🇦', TRY: '🇹🇷', THB: '🇹🇭',
                MYR: '🇲🇾', IDR: '🇮🇩', PHP: '🇵🇭', VND: '🇻🇳',
                BDT: '🇧🇩', PKR: '🇵🇰', EGP: '🇪🇬', NGN: '🇳🇬',
                KES: '🇰🇪'
            }
            return flagMap[currencyCode] || '🏳️'
        }

        const currencyLabel = (currency: CurrencyOption) => {
            return `${currency.code} - ${currency.name}`
        }

        const handleAddCurrency = (selected: CurrencyOption) => {
            const alreadySelected = internalMultipleValue.value.some(c => c.code === selected.code)
            if (!alreadySelected) {
                internalMultipleValue.value = [...internalMultipleValue.value, selected]
                updateModelValue()
            }
        }

        const handleRemoveCurrency = (removed: CurrencyOption) => {
            internalMultipleValue.value = internalMultipleValue.value.filter(c => c.code !== removed.code)
            updateModelValue()
        }

        const handleMultipleInput = (value: CurrencyOption[]) => {
            internalMultipleValue.value = value
            updateModelValue()
        }

        const updateModelValue = () => {
            const uniqueCodes = [...new Set(internalMultipleValue.value.map(c => c.code))]
            emit('update:modelValue', uniqueCodes)
            emit('change', uniqueCodes)
        }

        watch(() => props.modelValue, () => {
            const currentCodes = internalMultipleValue.value.map(c => c.code)
            const newCodes = Array.isArray(props.modelValue) ? props.modelValue : []
            if (JSON.stringify([...currentCodes].sort()) !== JSON.stringify([...newCodes].sort())) {
                initializeValues()
            }
        }, { deep: true })

        watch(() => props.currencies, () => {
            initializeValues()
        }, { deep: true })

        return {
            formattedCurrencies,
            filteredCurrencies,
            internalValue,
            internalMultipleValue,
            getFlagEmoji,
            currencyLabel,
            handleAddCurrency,
            handleRemoveCurrency,
            handleMultipleInput
        }
    }
})
</script>

<style lang="scss">
@import "vue-multiselect/dist/vue-multiselect.css";

.currency-selector {
    // Design variables (inherit from global)
    --input-height: 48px;
    --tag-bg: rgba(79, 70, 229, 0.1);
    --tag-color: var(--color-primary, #4f46e5);
    --option-hover-bg: var(--color-background, #f8fafc);

    .multiselect {
        min-height: var(--input-height);
        font-family: inherit;

        .multiselect__tags {
            border: 1px solid var(--color-border, #e2e8f0);
            border-radius: var(--radius-md, 0.5rem);
            padding: 8px 40px 8px 12px;
            min-height: var(--input-height);
            background: var(--color-surface, #ffffff);
            transition: var(--transition, all 0.2s ease);

            &:hover {
                border-color: var(--color-primary, #4f46e5);
            }
        }

        .multiselect__input {
            border: none;
            padding: 0;
            margin: 0;
            font-size: 0.875rem;
            color: var(--color-text, #1e293b);
            background: transparent;

            &::placeholder {
                color: var(--color-text-muted, #94a3b8);
            }
        }

        .multiselect__select {
            height: var(--input-height);
            padding: 0;

            &:before {
                border-color: var(--color-text-muted, #64748b) transparent transparent;
                opacity: 1;
            }
        }

        .multiselect__content-wrapper {
            border: 1px solid var(--color-border, #e2e8f0);
            border-radius: var(--radius-md, 0.5rem);
            box-shadow: var(--shadow-lg, 0 10px 15px -3px rgba(0, 0, 0, 0.1));
            margin-top: 4px;
            z-index: 50;
            background: var(--color-surface, #ffffff);
        }

        .multiselect__option {
            padding: 0.75rem 1rem;
            min-height: auto;
            font-size: 0.875rem;
            color: var(--color-text, #1e293b);
            transition: var(--transition, all 0.2s ease);

            &:hover {
                background: var(--option-hover-bg);
            }

            &--selected {
                background: rgba(79, 70, 229, 0.1);
                color: var(--color-primary, #4f46e5);
                font-weight: 500;

                &:after {
                    color: var(--color-primary, #4f46e5);
                }
            }

            &--highlight {
                background: var(--color-primary, #4f46e5);
                color: white;

                &:after {
                    color: white;
                }
            }
        }
    }

    .currency-option {
        display: flex;
        align-items: center;
        gap: 0.75rem;

        .currency-flag {
            width: 24px;
            height: 24px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1rem;
            flex-shrink: 0;
        }

        .currency-details {
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 0.125rem;

            .currency-code {
                font-weight: 600;
                color: var(--color-text, #1e293b);
                font-size: 0.875rem;
            }

            .currency-name {
                font-size: 0.75rem;
                color: var(--color-text-muted, #64748b);
            }
        }

        .currency-symbol {
            font-size: 0.875rem;
            font-weight: 500;
            color: var(--color-text, #1e293b);
            margin-left: auto;
            flex-shrink: 0;
        }
    }

    .selected-currency {
        display: flex;
        align-items: center;
        gap: 0.5rem;

        .currency-flag {
            font-size: 1rem;
        }

        .currency-code {
            font-weight: 600;
            color: var(--color-text, #1e293b);
            font-size: 0.875rem;
        }

        .currency-name {
            font-size: 0.75rem;
            color: var(--color-text-muted, #64748b);
        }
    }

    .currency-tag {
        display: inline-flex;
        align-items: center;
        gap: 0.375rem;
        background: var(--tag-bg);
        border: 1px solid rgba(79, 70, 229, 0.3);
        border-radius: 2rem;
        padding: 0.25rem 0.5rem 0.25rem 0.375rem;
        margin: 0.125rem 0.25rem 0.125rem 0;
        font-size: 0.75rem;
        font-weight: 500;
        color: var(--tag-color);

        .tag-flag {
            font-size: 0.875rem;
        }

        .tag-code {
            font-weight: 600;
        }

        .tag-remove {
            background: none;
            border: none;
            width: 1.125rem;
            height: 1.125rem;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 0;
            cursor: pointer;
            color: var(--tag-color);
            opacity: 0.7;
            transition: var(--transition, all 0.2s ease);

            &:hover {
                opacity: 1;
                background: rgba(255, 255, 255, 0.3);
            }

            i {
                font-size: 0.75rem;
            }
        }
    }

    .multiselect__single {
        padding: 0;
        margin: 0;
        background: transparent;
        line-height: 1.4;
    }

    .multiselect__placeholder {
        padding: 0;
        margin: 0;
        color: var(--color-text-muted, #94a3b8);
        font-size: 0.875rem;
    }

    .empty-state {
        display: flex;
        align-items: center;
        gap: 0.5rem;
        padding: 0.75rem 1rem;
        background: rgba(239, 68, 68, 0.1);
        border: 1px solid var(--color-danger, #ef4444);
        border-radius: var(--radius-md, 0.5rem);
        color: var(--color-danger, #ef4444);
        font-size: 0.875rem;
    }
}
</style>