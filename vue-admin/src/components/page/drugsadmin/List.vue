<template>
  <section class="list">
    <el-table :data="tableData" stripe border size="medium" @row-click="redirect" style="width: 100%; cursor: pointer;">
      <el-table-column prop="userName" label="联系人" width="120"></el-table-column>
      <el-table-column prop="telphone" label="联系电话" width="160"></el-table-column>
      <el-table-column label="行政区划" min-width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.provinceName + scope.row.cityName + scope.row.countryName }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="address" label="地址" min-width="300"></el-table-column>
      <template>
        <el-table-column label="分配药房" width="200" v-if="isAdmin">
          <template slot-scope="scope">
            <span>{{ scope.row.shopName || '未分配' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="快递单号" width="200" v-else>
          <template slot-scope="scope">
            <span>{{ scope.row.trackingNumber || '未发货' }}</span>
          </template>
        </el-table-column>
      </template>
      <el-table-column label="订购数量" width="80" align="right">
        <template slot-scope="scope">
          <span>{{ scope.row.number }}件</span>
        </template>
      </el-table-column>
      <el-table-column label="付款金额" width="80" align="right">
        <template slot-scope="scope">
          <span>{{ scope.row.money }}元</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="150" align="center">
        <template slot-scope="scope">
          <template v-if="isAdmin">
            <span v-if="scope.row.shopName || scope.row.status == 50" :class="{'gray': scope.row.status > 20}">{{ $store.getters.getter_table_tag(scope.row.status) }}</span>
            <el-button v-else type="warning" plain @click.stop="disShop(scope.row)">分配</el-button>
          </template>
          <template v-else>
            <span v-if="scope.row.trackingNumber || scope.row.status == 30 || scope.row.status == 50" :class="{'gray': scope.row.status > 20}">{{ $store.getters.getter_table_tag(scope.row.status) }}</span>
            <template v-else>
              <el-button type="warning" plain @click.stop="sendDrug(scope.row)">发货</el-button>
              <el-button type="danger" plain @click.stop="refDrug(scope.row)">拒绝</el-button>
            </template>
          </template>
        </template>
      </el-table-column>
    </el-table>
    <footer class="flex acenter">
      <div class="flex_1 customarea">
        <el-radio v-for="(i, k) in tags" :key="k" v-model="query.tabFlag" :label="i.value">{{i.name}}</el-radio>
        <el-input placeholder="请输入内容" @change="filterData" class="search hidden-md-and-down" prefix-icon="el-icon-search" v-model="query.search"></el-input>
      </div>
      <el-pagination :current-page="query.pageNo" :page-size="query.pageSize" @current-change="pageChange" layout="prev, pager, next" :total="total" prev-text="上一页" next-text="下一页"></el-pagination>
    </footer>
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
    <ref-dialog :form="refForm" v-model="refDialog" :isList="true"></ref-dialog>
  </section>
</template>
<script>
  import { RefDialog } from './share'
  export default {
    components: {
        RefDialog
    },
    methods: {
      filterData() {
        this.query.pageNo = 1
        this.fillData()
      },
      pageChange(page) {
        this.query.pageNo = page
        this.fillData()
      },
      fillData() {
        this.$http.$get(this.$urls.DRUG_BIZ_ORDER_LIST, this.query).then(d => {
          this.tableData = d.list
          this.total = d.totalCount
        })
      },
      redirect(row) {
        this.$router.push({path: '/drugs/orderdetail/' + row.orderUuid, query: { status: row.status, shopName: row.shopName }})
      },
      // 获取药店列表
      getShops() {
        this.$http.$get(this.$urls.DRUG_BIZ_SHOP_LIST).then(d => {
          this.shops = d
        })
      },
      // 分配药店操作
      disShop(item) {
        this.disDialog = true
        this.disForm.address = item.provinceName + item.cityName + item.countryName + item.address
        this.disForm.orderUuid = item.orderUuid
      },
      // 分配确认
      disCommit() {
        if (!this.disForm.remark) return this.$notify({title: '警告', message: '请选择要分配的药店', type: 'warning'})
        this.$http.$post(this.$urls.DRUG_BIZ_ORDER_OPERATE, this.disForm).then(d => {
          this.disDialog = false
          this.fillData()
        })
      },
      // 发货操作
      sendDrug(item) {
        this.sendDialog = true
        this.sendForm.address = item.provinceName + item.cityName + item.countryName + item.address
        this.sendForm.orderUuid = item.orderUuid
      },
      // 发货确认
      sendCommit() {
        if (!this.sendForm.remark) return this.$notify({title: '警告', message: '请输入快递单号', type: 'warning'})
        this.$http.$post(this.$urls.DRUG_BIZ_ORDER_OPERATE, this.sendForm).then(d => {
          this.sendDialog = false
          this.fillData()
        })
      },
      // 拒绝操作
      refDrug(item) {
        this.refDialog = true
        this.refForm.orderUuid = item.orderUuid
      }
    },
    computed: {
      tags() {
        return this.$store.getters.table_tags
      },
      isAdmin() {
        return this.$store.state.user.role == 'drugs_1'
      }
    },
    created() {
      this.fillData()
      this.isAdmin && this.getShops()
    },
    watch: {
      ['query.tabFlag']() {
        this.filterData()
      }
    },
    data() {
      return {
        query: {
          pageSize: 10,
          pageNo: 1,
          search: '',
          tabFlag: ''
        },
        total: 0,
        tableData: [],
        disDialog: false,
        sendDialog: false,
        refDialog: false,
        formLabelWidth: '100px',
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
        }
      }
    }
  }
</script>

<style lang="less">
  .el-pagination {
    padding: 1em 0;
  }
  .list {
    .gray {
      color: #aaa;
    }
    .search {
      width: 150px;
      margin: 0 1em;
      .el-input__inner {
        text-align: left;
        height: 28px;
      }
      .el-input__prefix {
        min-width: 22px;
      }
      .el-input__icon {
        line-height: 28px;
      }
    }
    .customarea {
      .el-radio + .el-radio {
        margin-left: 1em;
      }
    }
    .el-button--small {
      padding: 5px 10px;
    }
    .el-button+.el-button {
      margin-left: 0;
    }
    .el-select {
      display: block;
    }
    .disdialog {
      font-size: 14px;
      color: #666;
      .el-dialog__body {
        padding: 25px 37px 30px 0;
      }
    }
    .dialog-footer .el-button--small {
      padding: 8px 52px;
    }
  }
</style>
