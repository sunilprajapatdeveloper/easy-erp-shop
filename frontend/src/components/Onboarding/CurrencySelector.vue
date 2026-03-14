<template>
    <div class="currency-selector">
        <!-- Single Selection Mode -->
        <div v-if="!multiple" class="single-selector">
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
        // Sort currencies: popular first, then alphabetically
        const formattedCurrencies = computed(() => {
            const popular = props.currencies.filter(c =>
                props.popularCurrencies.includes(c.code)
            )
            const others = props.currencies.filter(c =>
                !props.popularCurrencies.includes(c.code)
            ).sort((a, b) => a.code.localeCompare(b.code))
            return [...popular, ...others]
        })

        // Internal values for Multiselect
        const internalValue = ref<CurrencyOption | null>(null)
        const internalMultipleValue = ref<CurrencyOption[]>([])

        // Filter currencies to exclude already selected ones
        const filteredCurrencies = computed(() => {
            const selectedCodes = internalMultipleValue.value.map(c => c.code)
            return formattedCurrencies.value.filter(currency =>
                !selectedCodes.includes(currency.code)
            )
        })

        // Initialize values from modelValue
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
                'USD': '🇺🇸', 'EUR': '🇪🇺', 'GBP': '🇬🇧', 'JPY': '🇯🇵',
                'AUD': '🇦🇺', 'CAD': '🇨🇦', 'CHF': '🇨🇭', 'CNY': '🇨🇳',
                'INR': '🇮🇳', 'SGD': '🇸🇬', 'HKD': '🇭🇰', 'KRW': '🇰🇷',
                'MXN': '🇲🇽', 'BRL': '🇧🇷', 'RUB': '🇷🇺', 'ZAR': '🇿🇦',
                'AED': '🇦🇪', 'SAR': '🇸🇦', 'TRY': '🇹🇷', 'THB': '🇹🇭',
                'MYR': '🇲🇾', 'IDR': '🇮🇩', 'PHP': '🇵🇭', 'VND': '🇻🇳',
                'BDT': '🇧🇩', 'PKR': '🇵🇰', 'EGP': '🇪🇬', 'NGN': '🇳🇬',
                'KES': '🇰🇪'
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

        // Watch for external modelValue changes
        watch(() => props.modelValue, () => {
            const currentCodes = internalMultipleValue.value.map(c => c.code)
            const newCodes = Array.isArray(props.modelValue) ? props.modelValue : []

            if (JSON.stringify([...currentCodes].sort()) !== JSON.stringify([...newCodes].sort())) {
                initializeValues()
            }
        }, { deep: true })

        // Watch for currencies changes to re-initialize when options become available
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
    .multiselect {
        min-height: 46px;
        font-family: inherit;

        .multiselect__tags {
            border: 2px solid rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            padding: 8px 40px 8px 12px;
            min-height: 46px;
            background: white;

            &:hover {
                border-color: rgba(0, 0, 0, 0.2);
            }
        }

        .multiselect__input {
            border: none;
            padding: 0;
            margin: 0;
            font-size: 14px;
            color: var(--textColor);

            &::placeholder {
                color: rgba(0, 0, 0, 0.4);
            }
        }

        .multiselect__select {
            height: 46px;
            padding: 0;

            &:before {
                border-color: var(--textColor) transparent transparent;
                opacity: 0.5;
            }
        }

        .multiselect__content-wrapper {
            border: 1px solid rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            margin-top: 4px;
            z-index: 50;
        }

        .multiselect__option {
            padding: 12px 16px;
            min-height: auto;

            &:hover {
                background: rgba(0, 0, 0, 0.02);
            }

            &--selected {
                background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
                color: var(--primaryColor);
                font-weight: 500;

                &:after {
                    color: var(--primaryColor);
                }
            }

            &--highlight {
                background: var(--primaryColor);
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
        gap: 12px;

        .currency-flag {
            width: 24px;
            height: 24px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 16px;
            flex-shrink: 0;
        }

        .currency-details {
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 2px;

            .currency-code {
                font-weight: 600;
                color: var(--titleColor);
                font-size: 14px;
            }

            .currency-name {
                font-size: 12px;
                color: var(--textColor);
                opacity: 0.8;
            }
        }

        .currency-symbol {
            font-size: 14px;
            font-weight: 500;
            color: var(--textColor);
            margin-left: auto;
            flex-shrink: 0;
        }
    }

    .selected-currency {
        display: flex;
        align-items: center;
        gap: 8px;

        .currency-flag {
            font-size: 16px;
        }

        .currency-code {
            font-weight: 600;
            color: var(--titleColor);
        }

        .currency-name {
            font-size: 13px;
            color: var(--textColor);
            opacity: 0.8;
        }
    }

    .currency-tag {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
        border: 1px solid rgba(102, 126, 234, 0.3);
        border-radius: 16px;
        padding: 4px 8px 4px 6px;
        margin: 2px 4px 2px 0;
        font-size: 13px;
        font-weight: 500;
        color: var(--primaryColor);

        .tag-flag {
            font-size: 14px;
        }

        .tag-code {
            font-weight: 600;
        }

        .tag-remove {
            background: none;
            border: none;
            width: 16px;
            height: 16px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 0;
            cursor: pointer;
            color: var(--primaryColor);
            opacity: 0.7;
            margin-left: 2px;

            &:hover {
                opacity: 1;
                background: rgba(255, 255, 255, 0.3);
            }

            i {
                font-size: 12px;
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
        color: rgba(0, 0, 0, 0.4);
        font-size: 14px;
    }
}
</style>