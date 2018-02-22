<script>
    function creat(that){
        var cityObj = document.getElementById('showcity')
        var hosObj = document.getElementById('showhos')
        var depObj = document.getElementById('showdep')
        hosObj.style.maxHeight = cityObj.style.maxHeight = depObj.style.maxHeight = ''
        hosObj.style.height = cityObj.style.height = depObj.style.height = ''
        that.cityH = cityObj.offsetHeight
        that.hosH = hosObj.offsetHeight
        that.depH = depObj.offsetHeight
        cityObj.style.height = (that.cityH > 58 ? 58 : that.cityH) + 'px'
        hosObj.style.height = (that.hosH > 58 ? 58 : that.hosH) + 'px'
        depObj.style.height = (that.depH > 58 ? 58 : that.depH) + 'px'
        that.s_more_city = that.cityH > 58 ? true : false
        that.s_more_hos = that.hosH > 58 ? true : false
        that.s_more_dep = that.depH > 58 ? true : false
    }
    export default {
        async asyncData ({ app, store, params, error }) {
            // 得到所有合作医院
            let q_citys = await store.dispatch('remote/get_coohospitals')
            // 得到所有参数
            let query = {}

            params && params.id && params.id.replace(/([^(?|#)=&]+)(=([^&]*))?/g, ($0, $1, $2, $3) => {
                query[$1] = $3
            })
            // 得到所有医院所在城市及当前选中id
            let q_citys_id = query.c || q_citys[0]['key']
            if (!query.c && query.h) {
                let q_city_item = store.getters['remote/get_coohos_item'](query.h) || {}
                q_citys_id = String(q_city_item.distCode).slice(0, 2) || q_citys[0]['key']
            }
            // 得到当前城市医院及选中id
            let q_hoss = store.getters['remote/get_coohoss'](q_citys_id)
            let q_hoss_id = query.h || q_hoss[0]['id']

            // 得到当前医院科室及选中id
            let { departs: q_deps } = await store.dispatch('remote/get_coodeparts', q_hoss_id)
            let q_deps_id = query.d || (q_deps && q_deps.length ? q_deps[0]['id'] : '')

            let pageSize = query.s || 8
            let pageNumber = query.n || 1
            // 查询条件下医生
            let { pager } = await app.$axios.$get('kangxin/gainspecialspage', {
                params: {
                    pageNumber,
                    pageSize,
                    hosid: q_hoss_id,
                    depid: q_deps_id
                }
            }).catch(err => {
                error({ statusCode: 400, message: err })
            })

            return {
                pageNumber,
                pageSize,
                totalCount: 0,
                pageCount: 0,
                list: [],
                ...pager,
                hosid: q_hoss_id,
                depid: q_deps_id,
                cityid: q_citys_id,
                q_citys,
                q_hoss,
                q_deps,
                cityH: 0,
                hosH: 0,
                depH: 0,
                s_more_city: false,
                s_more_hos: false,
                s_more_dep: false
            }
        },
        
        mounted (){
            window.onresize = init => {
                creat(this)
            }
            window.onresize()
        },
        methods: {
            stringfyQueryString (obj) {
                if (!obj) return '';
                var pairs = [];
                for (var key in obj) {
                    var value = obj[key];
                    if (value instanceof Array) {
                        for (var i = 0; i < value.length; ++i) {
                            pairs.push(encodeURIComponent(key + '[' + i + ']') + '=' + encodeURIComponent(value[i]));
                        }
                        continue;
                    }
                    pairs.push(encodeURIComponent(key) + '=' + encodeURIComponent(obj[key]));
                }
                return pairs.join('&');
            },
            handleSizeChange (pageSize) {
                let { hosid, depid, cityid } = this
                this.$router.replace({
                    path: '/remote/' + this.stringfyQueryString({
                        n: 1,
                        s: pageSize,
                        c: cityid,
                        h: hosid,
                        d: depid
                    })
                })
            },
            handleCurrentChange (pageNumber) {
                let { hosid, depid, cityid, pageSize } = this
                this.$router.replace({
                    path: '/remote/' + this.stringfyQueryString({
                        n: pageNumber,
                        s: pageSize,
                        c: cityid,
                        h: hosid,
                        d: depid
                    })
                })
            },
            goToDetail (item) {
                let { q_hoss, hosid, q_deps, depid } = this
                let depObj = q_deps.filter((item)=>{
                    if (item.id == depid) {
                        return item
                    }
                })
                let hosObj = q_hoss.filter((item)=>{
                    if (item.id == hosid){
                        return item
                    }
                })
                this.$router.push({ path: '/remote/docdetail', query: { docid: item.specialId , hosname: hosObj[0].displayName, depname: depObj[0].displayName } } )
            },
            findMore (id){
                var item = document.getElementById(id)
                item.style.height = ''
                switch (id){
                    case 'showcity':
                        this.s_more_city = false
                        break
                    case 'showhos' :
                        this.s_more_hos = false
                        break
                    case 'showdep' :
                        this.s_more_dep = false
                        break
                }
            },
            stopLess (id){
                var item = document.getElementById(id)
                switch (id){
                    case 'showcity':
                        this.s_more_city = true
                        item.style.height = (this.cityH > 58 ? 58 : this.cityH) + 'px'
                        break
                    case 'showhos' :
                        this.s_more_hos = true
                        item.style.height = (this.hosH > 58 ? 58 : this.hosH) + 'px'
                        break
                    case 'showdep' :
                        this.s_more_dep = true
                        item.style.height = (this.depH > 58 ? 58 : this.depH) + 'px'
                        break
                }
            }
        }
    }
</script>

<template>
    <article>
        <section class="querywhere">
            <dl>
                <dd class="clearfix">
                    <label class="fleft">开通城市</label>
                    <i class="fright" @click="findMore('showcity')" v-if="s_more_city">更多<em class="iconfont icon-moreunfold"></em></i>
                    <i class="fright stop" v-if="!s_more_city && cityH > 58" @click="stopLess('showcity')">收起<em class="iconfont icon-less"></em></i>
                    <span id="showcity" class="ohidden" style="max-height: 58px">
                        <nuxt-link v-for="item in q_citys" :key="item.key" :class="{'selected': item.key == cityid}" :to="`/remote/n=1&s=${pageSize}&c=${item.key}`" replace>{{item.name}}</nuxt-link>
                    </span>
                </dd>
                <dd class="clearfix hos-opt">
                    <label class="fleft">当地合作医院</label>
                    <i class="fright" @click="findMore('showhos')" v-if="s_more_hos">更多<em class="iconfont icon-moreunfold"></em></i>
                    <i class="fright stop" v-if="!s_more_hos && hosH > 58" @click="stopLess('showhos')">收起<em class="iconfont icon-less"></em></i>
                    <span id="showhos" class="ohidden" style="max-height: 58px">

                        <nuxt-link v-for="item in q_hoss" :key="item.id" :to="`/remote/n=1&s=${pageSize}&c=${cityid}&h=${item.id}`" replace :class="{'selected': item.id == hosid}">{{item.displayName}}</nuxt-link>
                    </span>
                </dd>
                <dd class="clearfix dep-opt">
                    <label class="fleft">选择科室</label>
                    <i class="fright" @click="findMore('showdep')" v-if="s_more_dep">更多<em class="iconfont icon-moreunfold"></em></i>
                    <i class="fright stop" v-if="!s_more_dep && depH > 58" @click="stopLess('showdep')">收起<em class="iconfont icon-less"></em></i>
                    <span id="showdep" class="ohidden" style="max-height: 58px">
                        <nuxt-link v-for="item in q_deps" :key="item.id" :to="`/remote/n=1&s=${pageSize}&c=${cityid}&h=${hosid}&d=${item.id}`" replace :class="{'selected': item.id == depid}">{{item.displayName}}</nuxt-link>
                    </span>
                </dd>
            </dl>
        </section>
        <div class="queryall">
            <p style="color: #a6a6a6;background: #f9f9f9;padding: 15px;"><span class="big20" style="color: #333;">相关专家</span>  根据您的筛选，我们为您匹配来自全国各三甲医院顶级专家</p>
            <section class="queryresult clearfix">
                <el-col :xs="{span: 12}" :sm="{span: 8}" :md="{span: 6}" :lg="{span: 4}" v-for="o in list" :key="o.specialId">
                    <el-card style="margin: 1rem;" class="tcenter" @click.native="goToDetail(o)">
                        <div class="image relative">
                            <img :src="`${o.listSpecialPicture}?x-oss-process=image/resize,m_fill,h_100,w_100`" class="w100 h100">
                        </div>
                        <div>
                            <p class="whitespace big16 stronger">{{o.hosName}}</p>
                            <p class="whitespace">{{o.specialName}}&emsp;{{o.depName}}</p>
                            <p class="whitespace">{{o.duty}}</p>
                        </div>
                        <div class="tleft goodat">
                            擅长：{{o.specialty}}
                        </div>
                    </el-card>
                </el-col>
            </section>
            <footer class="pagination tcenter">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageNumber" :page-sizes="[8, 16, 32, 64, 128, 256]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="totalCount">
                </el-pagination>
            </footer>
        </div>
    </article>
</template>

<style lang="less">
    .querywhere {
        a {
            color: #333;
            display: inline-block;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            width: 6em;
            padding: 0 1.5em;
        }
        .hos-opt a {
            width: 10em;
        }
        .dep-opt a {
            width: 8em;
        }
        .selected {
            color: #0086d1;
        }
        label {
            display: inline-block;
            width: 8em;
            color: #999;
            border-right: 1px solid #ccc;
        }
        span {
            margin-left: 9em;
            margin-right: 6em;
            display: block;
        }
        dd {
            padding: 1em 0;
            line-height: 1.6;
            & + dd {
                border-top: 1px dashed #eee;
            }
        }
        dl {
            margin: 1rem;
            border: 1px solid #ccc;
            padding: 0 1rem;
        }
        i {
            display: inline-block;
            color: #999;
            cursor: pointer;
            z-index: 100;
        }
    }
    .queryall {
        margin:1rem; border: 1px solid #ccc;
    }
    .queryresult {
        p {
            line-height: 1.8;
        }
        .goodat {
            height: 3em;
            margin-top: 1em;
            line-height: 1.5;
            color: #aaa;
            overflow: hidden;
        }
        .image {
            width: 70%;
            margin: 0 auto 1rem;
            height: 0;
            padding-top: 70%;
            img {
                object-fit: cover;
                position: absolute;
                top: 0;
                left: 0;
                border-radius: 50%;
            }
        }
        .el-card:hover {
            box-shadow: rgb(1, 134, 209) 0 2px 0 0;
            border-color: rgb(1, 134, 209);
            cursor: pointer;
        }
    }
    .pagination {
        padding: 1rem 0;
    }
</style>
