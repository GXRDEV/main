<template>
  <section class="orderdetail">
    <el-row class="title common">
      <el-col :span="6"><div><i class="iconfont icon-contacts big16"></i>收货人 : {{userName}}</div></el-col>
      <el-col :span="8"><div><i class="iconfont icon-shijian big16"></i>订单时间 : {{createTime}}</div></el-col>
      <el-col :span="6"><div><i class="iconfont icon-dianhua big16"></i>联系方式 : {{telphone}}</div></el-col>
    </el-row>
    <el-row class="subtitle common">
      <el-col :span="4"><div>药品展示</div></el-col>
      <el-col :span="8"><div>药品名称</div></el-col>
      <el-col :span="4"><div>发货数量</div></el-col>
      <el-col :span="4"><div>药品金额</div></el-col>
      <el-col :span="4"><div>操作</div></el-col>
    </el-row>
    <div class="list">
      <el-row class="listdetail common" v-for="(item,ind) in orderArr" :key="ind">
        <el-col :span="4">
          <div>
            <img v-if="item.picUrl" :src="item.picUrl">
            <span v-else>暂无图片</span>
          </div>
        </el-col>
        <el-col :span="8"><div>{{item.drugName || '暂无名称'}}</div></el-col>
        <el-col :span="4"><div>{{item.number}}</div></el-col>
        <el-col :span="4"><div>{{item.totalMoney}}</div></el-col>
        <el-col :span="4"><div><span class="finddetail" @click="findDetail(item)">查看药品详情</span></div></el-col>
      </el-row>
    </div>
    <footer>
      <el-row v-if="isAdmin">
        <el-col v-if="!shopName && status!=50" :span="24"><div style="text-align: center;"><span class="sendcommon tcenter senddrug" @click="disDialog = true">分配药店</span></div></el-col>
      </el-row>
      <el-row v-else>
        <template v-if="status == 10">
          <el-col :span="12"><div style="text-align: right;padding-right:20px;"><span class="sendcommon tcenter senddrug" @click="sendDialog = true">统一发货</span></div></el-col>
          <el-col :span="12"><div style="text-align:left;padding-left: 20px;"><span class="sendcommon tcenter nosend" @click="refDialog = true">拒绝发货</span></div></el-col>
        </template>
      </el-row>
    </footer>
    <el-dialog
      title="药品详情"
      :visible.sync="dialogVisible"
      width="620px" class="dialog-content">
      <div class="drugpic tcenter" v-if="pic.picUrl">
        <img :src="pic.picUrl">
      </div>
      <div style="padding-top: 15px;">
        <p class="content-title">药品名称</p>
        <p>通用名称: {{detail.drugName}}</p>
      </div>
      <div>
        <p class="content-title">药品类别</p>
        <p>{{detail.drugType}}</p>
      </div>
      <div>
        <p class="content-title">药品编码</p>
        <p>{{detail.drugCode}}</p>
      </div>
      <div>
        <p class="content-title">药品内码</p>
        <p>{{detail.drugInnerCode}}</p>
      </div>
      <div>
        <p class="content-title">药品价格</p>
        <p>{{detail.price}}元/件</p>
      </div>
      <div>
        <p class="content-title">药品成份</p>
        <p>{{detail.drugComponent}}</p>
      </div>
      <div>
        <p class="content-title">性状</p>
        <p>{{detail.drugXZ}}</p>
      </div>
      <div>
        <p class="content-title">主治功能</p>
        <p>{{detail.drugFunction}}</p>
      </div>
      <div>
        <p class="content-title">药品规格</p>
        <p>{{detail.drugSpec}}</p>
      </div>
      <div>
        <p class="content-title">用法用量</p>
        <p>{{detail.drugUseMethod}}</p>
      </div>
      <div>
        <p class="content-title">不良反应</p>
        <p>{{detail.drugUnEffect}}</p>
      </div>
      <div>
        <p class="content-title">禁忌</p>
        <p>{{detail.drugAvoid}}</p>
      </div>
      <div>
        <p class="content-title">注意事项</p>
        <p>{{detail.drugAttention}}</p>
      </div>
      <div>
        <p class="content-title">批准文号</p>
        <p>{{detail.drugLiceNum}}</p>
      </div>
      <div v-if="detail.drugPcs">
        <p class="content-title">包装单位</p>
        <p>{{detail.drugPcs}}</p>
      </div>
      <div>
        <p class="content-title">生产厂家</p>
        <p>企业名称: {{detail.drugManFactory}}</p>
      </div>
    </el-dialog>
    <!--分配药店弹框-->
    <el-dialog width="500px" :visible.sync="disDialog" center class="disdialog">
      <el-form :model="disForm">
        <el-form-item label="收货地址" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="disForm.address" disabled></el-input>
        </el-form-item>
        <el-form-item label="分配店铺" :label-width="formLabelWidth">
          <el-select v-model="disForm.remark" placeholder="请选择分配药店">
            <el-option v-for="(s,ind) in shops" :key="ind" :label="s.shopAddress+'('+ s.shopName+')'" :value="s.shopAccountUuid"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="disCommit">确认</el-button>
      </div>
    </el-dialog>
    <!--发货弹框-->
    <el-dialog width="500px" :visible.sync="sendDialog" center class="disdialog">
      <el-form :model="sendForm">
        <el-form-item label="收货地址" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="sendForm.address" disabled></el-input>
        </el-form-item>
        <el-form-item label="快递单号" :label-width="formLabelWidth">
          <el-input v-model="sendForm.remark" auto-complete="off" placeholder="请输入快递单号"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="sendCommit">确认</el-button>
      </div>
    </el-dialog>
    <ref-dialog :form="refForm" v-model="refDialog"></ref-dialog>
  </section>
</template>
<script>
  import { RefDialog } from './share'
  export default {
    name: 'orderdetail',
    components: {
      RefDialog
    },
    data() {
      return {
        id: this.$route.params.id,
        status: this.$route.query.status,
        shopName: this.$route.query.shopName,
        userName: '',
        telphone: '',
        createTime: '',
        orderArr: [],
        dialogVisible: false,
        disDialog: false,
        sendDialog: false,
        refDialog: false,
        shops: [],
        disForm: {
          address: '',
          remark: '',
          orderUuid: '',
          operateDesc: 'distribute'
        },
        sendForm: {
          address: '',
          remark: '',
          orderUuid: '',
          operateDesc: 'sendgood'
        },
        refForm: {
          remark: '',
          orderUuid: '',
          operateDesc: 'refulse'
        },
        detail: {},
        pic: {},
        formLabelWidth: '80px'
      }
    },
    computed: {
      isAdmin() {
        return this.$store.state.user.role == 'drugs_1'
      }
    },
    created() {
      this.$http.$get(this.$urls.DRUG_BIZ_ORDER_DETAIL + this.id, { orderUuid: this.id }).then(d => {
        this.orderArr = d.drugs || []
        this.userName = d.userName || ''
        this.telphone = d.telphone || ''
        this.createTime = d.createTime || ''
        this.disForm.address = this.sendForm.address = d.address
        this.disForm.orderUuid = this.sendForm.orderUuid = this.refForm.orderUuid = d.orderUuid
      })
      this.isAdmin && this.getShops()
    },
    methods: {
      findDetail(item) {
        this.dialogVisible = true
        this.$http.$get(this.$urls.DRUG_BIZ_DRUG_DETAIL + item.drugInnerCode, { drugInnerCode: item.drugInnerCode }).then(d => {
          this.detail = d || {}
          this.pic = d.pics[0]
        })
      },
      // 获取药店列表
      getShops() {
        this.$http.$get(this.$urls.DRUG_BIZ_SHOP_LIST).then(d => {
          this.shops = d
        })
      },
      // 分配确认
      disCommit() {
        if (!this.disForm.remark) return this.$notify({title: '警告', message: '请选择要分配的药店', type: 'warning'})
        this.$http.$post(this.$urls.DRUG_BIZ_ORDER_OPERATE, this.disForm).then(d => {
          this.disDialog = false
          this.$router.go(-1)
        })
      },
      // 发货确认
      sendCommit() {
        if (!this.sendForm.remark) return this.$notify({title: '警告', message: '请输入快递单号', type: 'warning'})
        this.$http.$post(this.$urls.DRUG_BIZ_ORDER_OPERATE, this.sendForm).then(d => {
          this.sendDialog = false
          this.$router.go(-1)
        })
      }
    }
  }
</script>
<style lang="less" scoped>
  @import '../../../assets/css/var';
  .orderdetail {
    .common { color: #666; font-size: 14px;}
    .title {
      height: 24px; margin-top:-8px;
      i {
        margin-right: 5px;
        color: @main-color;
      }
    }
    .subtitle {
      color: #222;
      height: 40px; line-height:40px;
      border-top: 3px solid #f5f5f5;
      border-bottom: 1px solid #f5f5f5;
      text-align: center;
    }
    .list {
      margin-bottom: 36px;
    }
    .listdetail {
      height: 100px;
      line-height: 100px;
      text-align: center;
      border-bottom: 1px solid #f5f5f5;
      img {
        height: 80px;
        max-width: 100%;
        vertical-align: middle;
      }
      .finddetail {
        display: inline-block;
        width: 100px;
        height: 25px;
        line-height: 25px;
        border: 1px solid @main-color;
        color: @main-color;
        cursor: pointer;
      }
    }
  }
  .sendcommon {
    display: inline-block;
    width: 130px;
    height: 30px;
    line-height:30px;
    border-radius: 30px;
    color: #fff;
    font-size: 14px;
    cursor: pointer;
  }
  .senddrug {
    background: #eca614;
  }
  .nosend {
    background: #fe6c6c;
  }
  .drugpic {
    width: 100%;
    height:300px;
    margin-top: 40px;
    img {
      height: 300px;
      width: auto;
      max-width: 100%;
    }
  }
  .dialog-content {
    font-size: 14px;
    color: #666;
    .content-title {
      color: #000;
      margin: 20px 0 10px;
    }
  }
</style>
<style lang="less">
  .orderdetail {
    .el-dialog {
      max-height: 70vh;
      overflow: auto;
    }
    .dialog-content .el-dialog__header {
      position: fixed;
      width: 620px;
      border-bottom:1px solid #eee;
      height: 60px;
      line-height: 60px;
      padding: 0 15px;
      font-size: 18px;
      color: #000;
      background-color: #fff;
    }
    .el-select {
      display: block;
    }
    .disdialog {
      font-size: 14px;
      color: #666;
      .el-dialog__body {
        padding: 25px 27px 30px 10px;
      }
    }
    .dialog-footer .el-button--small {
      padding: 8px 52px;
    }
  }
</style>
