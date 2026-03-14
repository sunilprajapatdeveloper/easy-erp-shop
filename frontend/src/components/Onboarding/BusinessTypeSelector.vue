<template>
    <div class="business-type-selector">
        <div class="selector-grid">
            <div v-for="type in businessTypes" :key="type.value" class="type-card"
                :class="{ selected: modelValue === type.value }" @click="selectType(type.value)">
                <div class="type-icon">
                    <i :class="type.icon"></i>
                </div>
                <h4>{{ type.label }}</h4>
                <p>{{ type.description }}</p>
            </div>
        </div>

        <div v-if="showCustomInput" class="custom-type-input">
            <label>Specify your business type</label>
            <input type="text" v-model="customType" placeholder="e.g., Restaurant, Manufacturing, etc."
                @input="handleCustomInput" />
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch } from 'vue'

interface BusinessType {
    value: string
    label: string
    description: string
    icon: string
}

export default defineComponent({
    name: 'BusinessTypeSelector',
    props: {
        modelValue: {
            type: String,
            default: ''
        }
    },
    emits: ['update:modelValue', 'change'],
    setup(props, { emit }) {
        const customType = ref('')
        const showCustomInput = ref(false)

        const businessTypes: BusinessType[] = [
            {
                value: 'sole_proprietorship',
                label: 'Sole Proprietorship',
                description: 'Owned and run by one individual',
                icon: 'ri-user-line'
            },
            {
                value: 'partnership',
                label: 'Partnership',
                description: 'Two or more people share ownership',
                icon: 'ri-group-line'
            },
            {
                value: 'llc',
                label: 'LLC (Limited Liability)',
                description: 'Hybrid structure providing liability protection',
                icon: 'ri-building-line'
            },
            {
                value: 'corporation',
                label: 'Corporation',
                description: 'Independent legal entity owned by shareholders',
                icon: 'ri-bank-line'
            },
            {
                value: 'retail_store',
                label: 'Retail Store',
                description: 'Physical or online store selling to consumers',
                icon: 'ri-store-line'
            },
            {
                value: 'wholesale',
                label: 'Wholesale',
                description: 'Selling products in bulk to retailers',
                icon: 'ri-truck-line'
            },
            {
                value: 'ecommerce',
                label: 'E-commerce',
                description: 'Online business selling products/services',
                icon: 'ri-shopping-cart-line'
            },
            {
                value: 'service_based',
                label: 'Service Based',
                description: 'Business providing services to clients',
                icon: 'ri-service-line'
            },
            {
                value: 'manufacturing',
                label: 'Manufacturing',
                description: 'Producing goods from raw materials',
                icon: 'ri-factory-line'
            },
            {
                value: 'restaurant',
                label: 'Restaurant/Food',
                description: 'Food service establishment',
                icon: 'ri-restaurant-line'
            },
            {
                value: 'franchise',
                label: 'Franchise',
                description: 'Operating under established brand',
                icon: 'ri-global-line'
            },
            {
                value: 'custom',
                label: 'Other/ Custom',
                description: 'Specify your business type',
                icon: 'ri-edit-line'
            }
        ]

        const selectType = (typeValue: string) => {
            if (typeValue === 'custom') {
                showCustomInput.value = true
                emit('update:modelValue', 'custom')
            } else {
                showCustomInput.value = false
                emit('update:modelValue', typeValue)
                emit('change', typeValue)
            }
        }

        const handleCustomInput = () => {
            emit('update:modelValue', customType.value)
            emit('change', customType.value)
        }

        watch(() => props.modelValue, (newValue) => {
            if (newValue && !businessTypes.find(t => t.value === newValue) && newValue !== 'custom') {
                customType.value = newValue
                showCustomInput.value = true
            }
        })

        return {
            businessTypes,
            customType,
            showCustomInput,
            selectType,
            handleCustomInput
        }
    }
})
</script>

<style lang="scss" scoped>
.business-type-selector {
    .selector-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
        gap: 16px;
        margin-bottom: 24px;

        .type-card {
            background: white;
            border: 2px solid rgba(0, 0, 0, 0.08);
            border-radius: 12px;
            padding: 20px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s ease;
            display: flex;
            flex-direction: column;
            align-items: center;
            height: 100%;

            &:hover {
                border-color: var(--primaryColor);
                transform: translateY(-2px);
                box-shadow: 0 8px 24px rgba(102, 126, 234, 0.1);
            }

            &.selected {
                border-color: var(--primaryColor);
                background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
                box-shadow: 0 8px 24px rgba(102, 126, 234, 0.15);

                .type-icon {
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    color: white;
                }
            }

            .type-icon {
                width: 60px;
                height: 60px;
                border-radius: 50%;
                background: rgba(0, 0, 0, 0.03);
                display: flex;
                align-items: center;
                justify-content: center;
                margin-bottom: 16px;
                font-size: 24px;
                color: var(--textColor);
                transition: all 0.3s ease;
            }

            h4 {
                font-size: 16px;
                font-weight: 600;
                margin-bottom: 8px;
                color: var(--titleColor);
                line-height: 1.3;
            }

            p {
                font-size: 14px;
                color: var(--textColor);
                opacity: 0.8;
                line-height: 1.4;
                margin: 0;
            }
        }
    }

    .custom-type-input {
        margin-top: 16px;

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: var(--titleColor);
        }

        input {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s ease;

            &:focus {
                outline: none;
                border-color: var(--primaryColor);
                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            }

            &::placeholder {
                color: rgba(0, 0, 0, 0.4);
            }
        }
    }
}

@media (max-width: 768px) {
    .business-type-selector {
        .selector-grid {
            grid-template-columns: repeat(2, 1fr);
            gap: 12px;

            .type-card {
                padding: 16px;

                .type-icon {
                    width: 50px;
                    height: 50px;
                    font-size: 20px;
                    margin-bottom: 12px;
                }

                h4 {
                    font-size: 14px;
                }

                p {
                    font-size: 12px;
                }
            }
        }
    }
}
</style>