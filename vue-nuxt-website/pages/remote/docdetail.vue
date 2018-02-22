<template>
    <article class="docdetail">
        <div style="border:1px solid #eee; margin: 10px; padding: 15px;" class="relative">
            <div class="image absolute">
                <img :src="`${special.listSpecialPicture}?x-oss-process=image/resize,m_fill,h_100,w_100`" class="w100 h100">
            </div>
            <div class="doc">
                <p class="big20 gray">{{special.specialName}} <span class="big14 graycolor">&nbsp;{{special.duty}}</span></p>
                <p style="color:#0086d1;">{{special.hosName}} &nbsp;<span>{{special.depName}}</span></p>
                <p class="insta"><span class="graycolor">擅长 : </span>{{special.specialty}}</p>
                <p class="insta marginb"><span class="graycolor">简介 : </span>{{special.profile}}</p>
            </div>
        </div>
        <div class="remote" v-if="hosname">
            <p class="big20 gray remote-title">远程门诊预约</p>
            <el-container class="remote-left">
                <el-aside width="70%">
                    <div v-if="times.length" class="hastime">
                        <dl v-for="(item,ind) in times" :key="ind">
                            <dt class="big18 graycolor">{{item[0].scheduleDate.split('-').slice(1).join('/')}}（{{item[0].week}}）</dt>
                            <el-tooltip placement="right-start" effect="light" v-for="(d,inx) in item" :key="inx" >
                                <div slot="content" class="masktext">
                                    <p class="big20" style="color:#CA5840; margin: 15px 200px 20px 0;">点击即可预约</p>
                                    <p class="big16">医院：{{special.hosName}}</p>
                                    <p class="big16">科室：{{special.depName}}</p>
                                    <p class="big16">医生：{{special.specialName}}</p>
                                    <p class="big16">时间：{{d.scheduleDate}} {{d.week}}</p>
                                    <p class="big16" style="margin-left: 45px;">{{d.startTime < '12:00' ? '上午': '下午'}} {{d.startTime}}</p>
                                </div>
                                <dd :class="{'open': d.hasAppoint == 0}" @click="goToBook(d)">{{d.startTime}}</dd>
                            </el-tooltip>
                        </dl>
                    </div>
                    <div class="doctime tcenter" v-else>
                        专家近期没有出诊安排
                    </div>
                    <div class="remote-detail tcenter">
                        <el-row>
                            <el-col :span="8" class="tleft"><div><i class="iconfont icon-didian"></i>门诊地点 : <span class="gray">{{hosname}}</span></div></el-col>
                            <el-col :span="8"><div><i class="iconfont icon-keshi"></i>选择科室 : <span class="gray">{{depname}}</span></div></el-col>
                            <el-col :span="8"><div><i class="iconfont icon-qian"></i>门诊费用 : <span style="color: #0086d1;">{{special.vedioAmount}}元</span></div></el-col>
                        </el-row>
                    </div>
                </el-aside>
                <el-main class="remote-right">
                    <p style="color:#0086d1;">提示 : </p>
                    <p>1. 为避免错过预约时间，请您提前到您所在地已开通服务的医院等候。</p>
                    <p>2. 您可以预约两周内的时间。</p>
                    <p>3. 远程门诊时间以专家变动为准。</p>
                    <p>4. 因专家职业特殊性，延迟难以避免，若超过30分钟，患者可以取消订单，费用全额返还。</p>
                </el-main>
            </el-container>
        </div>
    </article>
</template>
<script>
    export default {
        async asyncData ({ app, store, query, error }){
            let depname = query.depname ? query.depname : ''
            let hosname = query.hosname ? query.hosname : ''
            let { special } = await app.$axios.$post('kangxin/gainspedetail?docid='+ query.docid ).catch(err => {
                error({ statusCode: 400, message: err })
            })
            return {
                special,
                hosname,
                depname,
                docid:'',
                times: []
            }
        },
        created (){
            this.docid = this.$route.query.docid
            this.getTimeList();
        },
        methods : {
            getTimeList (){
                for (var i = 0; i < 14; i++){
                    this.$axios.$post('wzjh/gainSpecialTimes?sid='+ this.docid +'&sdate='+ this.GetDateStr(i).toString()).then( d => {
                        if (d.times.length) {
                            let newTime = d.times.map(item =>{
                                return {
                                    ...item,
                                    week: this.getWeek(item.scheduleDate)
                                }
                            })
                            this.times.push(newTime)
                        }
                    })
                }
            },
            isNull(object){
                if(object == null || typeof object == "undefined"){
                    return true;
                }
                return false;
            },
            getWeek(dateString){
                let date;
                if(this.isNull(dateString)){
                    date = new Date();
                }else{
                    let dateArray = dateString.split("-");
                    date = new Date(dateArray[0], parseInt(dateArray[1] - 1), dateArray[2]);
                }
                return "周" + "日一二三四五六".charAt(date.getDay());
            },
            GetDateStr(count) {
                let dd = new Date(), y, m, d
                dd.setDate(dd.getDate() + count)
                y = dd.getFullYear();
                m = (dd.getMonth()+1) <10 ? "0" + (dd.getMonth()+1) : (dd.getMonth()+1)
                d = dd.getDate() < 10 ? "0" + dd.getDate() : dd.getDate();
                return ( y + "-" + m + "-" + d);

            },
            goToBook (item) {
                item.hasAppoint == 0 ? this.$router.push({path: '/404'}) : ''
            }
        }
    }
</script>
<style lang="less">
    .docdetail {
        .graycolor { color: #828282;}
        .gray { color: #333; }
        .marginb { margin-bottom: 0 !important;}
        .doc {
            margin-left: 10em;
            p{ margin-bottom: .7em; line-height: 1.5em;}
        }
        .insta { position: relative; margin-left: 3.6em; color: #2c2c2c;
            span { position: absolute; left: -3.6em; }
        }
        .el-container { margin:2em 0;}
        .el-main { padding: 0; padding-left: 2em;}
        .el-aside { border-right: 1px solid #eee; }
        .image {
            width: 7em;
            margin-left: 1em;
            height: 7em;
            img {
                object-fit: cover;
                position: absolute;
                top: 0;
                left: 0;
                border-radius: 50%;
            }
        }
        .remote {
            border:1px solid #eee; margin:10px; padding:0 20px;
            p { line-height: 1.6em; }
        }
        .remote-title { height: 2.5em; border-bottom: 1px solid #eee;line-height:2.5em !important; }
        .remote-right { color: #828282; }
        .doctime { border-bottom: 1px solid #eee; color: rgb(170, 170, 170); opacity: 0.6; padding: 2em 0 4em; margin-right: 20px;}
        .remote-detail {
            padding: 4em 0 2em; color: rgb(170, 170, 170);
            i { color: #0086d1; margin-right: .3em;}
        }
        .hastime {
            margin-right: 20px;
            border-bottom: 1px solid #eee;
            padding-bottom: 20px;
            dt {
                border-bottom: 2px solid transparent;
                line-height: 2em;
                width: 7em;
                text-align: left;
                margin: 0.5em 0;
                &:hover {
                    border-bottom: 2px solid #07C597;
                 }
            }
            dd {
                position: relative;
                border: 1px solid #D2D2D2;
                color: #A2A2A2;
                margin: 5px;
                line-height: 1.5em;
                display: inline-block;
                width: 7em;
                text-align: center;
                &.open {
                     border: 1px solid #07C597;
                     color: #07C597;
                     cursor: pointer;
                 }
                &.open:hover {
                    background-color: #07C597;
                    color: #fff;
                }
            }

        }
    }
    .el-tooltip__popper.is-light {
        border:1px solid #eee !important;
        box-shadow: 0 0 20px #ddd;
        padding-left: 20px;
        p {
            color: #828282;
            margin-bottom: 10px;
        }
    }
    .el-tooltip__popper .popper__arrow,
    .el-tooltip__popper .popper__arrow::after {
        border-color: #eee;
    }
</style>
