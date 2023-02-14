<template>
  <div class="nft-formsearch">
    <div class="nft-formsearch__content">
      <el-form
        ref="form"
        class="demo-ruleForm"
        autocomplete="off"
        :model="formValue"
      >
        <el-row>
          <el-col
            v-for="(item, key) in listForm"
            :key="key"
            :span="listForm.length > 4 ? 24 / listForm.length : 24 / 4 || 4"
          >
            <el-form-item
              :prop="item.prop"
              :label="(item.label && item.label + ':') || ''"
              :rules="[{ required: false, message: 'please input' }]"
              :label-width="item.label ? item.labelWidth || '80px' : '10px'"
            >
              <el-input
                size="small"
                v-if="item.type == 'input'"
                :model="formValue[item.prop]"
                width="100%"
                type="keyword"
                :placeholder="
                  item.placeholder ? item.placeholder : 'please input'
                "
                suffix-icon="el-icon-search"
              ></el-input>
              <el-select
                size="small"
                v-if="item.type == 'select'"
                :model="formValue[item.prop]"
                :placeholder="
                  item.placeholder ? item.placeholder : 'please select'
                "
              >
                <el-option
                  v-for="(li, index) in item.options"
                  :key="index"
                  :label="li.name"
                  :value="li.value"
                >
                </el-option>
              </el-select>
              <!-- v-model.number="numberValidateForm.age" -->
            </el-form-item>
          </el-col>
          <el-col :span="7" style="margin-left:22px">
            <el-form-item>
              <el-button
                size="small"
                type="primary"
                @click="
                  () => {
                    $emit('onSubmit', formValue);
                  }
                "
              >
                Search
              </el-button>
              <!-- <el-button
                size="small"
                :icon="RefreshLeft"
                @click="onReset"
              ></el-button> -->
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div
        v-if="listButton && listButton.length > 0"
        class="nft-formsearch__content--buttons"
      >
        <el-button
          size="small"
          v-for="(item, key) in listButton"
          :key="key"
          :type="item.type"
          :disabled="item.disabled"
          v-hasPermi="[`${item.hasPermi}`]"
          :style="
            item.color
              ? `backgroundColor:${item.color};border:1px solid ${item.color}`
              : ''
          "
          @click="
            () => {
              $emit(item.onClick);
            }
          "
        >
          {{ item.title }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { RefreshLeft } from "@element-plus/icons";
export default {
  name: "FormSearch",
  props: {
    listForm: { type: Array, default: () => [] },
    formValue: { type: Object, default: () => {} },
    listButton: { type: Array, default: () => [] },
  },
  data() {
    return {
      RefreshLeft,
      form: {
        keyword: "",
      },
    };
  },
  watch: {},
  methods: {
    //重置
    onReset() {
      if (this.$refs["form"]) {
        this.$refs["form"].resetFields();
        this.$emit("onResetForm");
      }
      // this.$ref["form"].resetFields();
    },
  },
  created() {},
  mounted() {},
};
</script>
<style scoped lang="less">
.nft-formsearch {
  // display: flex;
  // justify-content: space-between;
  .el-form-item__label {
    color: #434343 !important;
  }
  &__content {
    &--buttons {
      margin-bottom: 20px;
    }
  }
}
</style>
