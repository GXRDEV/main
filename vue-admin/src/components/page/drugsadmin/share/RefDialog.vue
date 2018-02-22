<template>
  <el-dialog width="500px" :visible.sync="currentValue" center :show-close="false" class="refdialog">
    <el-form>
      <el-form-item class="tcenter">
        <el-radio-group v-model="form.remark" size="medium">
          <el-radio label="对不起，您购买的药品库存不足，无法发货"></el-radio>
          <el-radio label="对不起，您所属区域暂不支持配送，无法发货"></el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="refCommit">确认</el-button>
      <el-button type="danger" @click="currentValue = false">取消</el-button>
      <p style="color: #999; margin-top: 25px;font-size: 11px;">请注意，订单拒绝后将无法恢复</p>
    </div>
  </el-dialog>
</template>
<script>
  export default {
    props: {
      form: Object,
      value: Boolean,
      isList: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        currentValue: false
      }
    },
    watch: {
      currentValue (val) {
        this.$emit('input', val)
      },
      value (val) {
        this.currentValue = val
      }
    },
    methods: {
      refCommit() {
        if (!this.form.remark) return this.$notify({title: '警告', message: '请选择拒绝理由', type: 'warning'})
        this.$http.$post(this.$urls.DRUG_BIZ_ORDER_OPERATE, this.form).then(d => {
          this.currentValue = false
          this.isList ? setTimeout(() => { this.$router.go(0) }, 1000) : this.$router.go(-1)
        })
      }
    }
  }
</script>
<style lang="less">
  	.refdialog {
		.el-dialog__body {
			padding: 0
		}
		.el-radio:nth-child(2) {
			margin-left: 12px;
			margin-top: 20px
		}
		.dialog-footer{
			button{
				padding: 8px 37px;
				&:first-child{
					margin-right: 30px
				}
			}
		}
  	}
</style>

