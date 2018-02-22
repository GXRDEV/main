<template>
    <div class="home">
		<el-carousel :interval="5000" arrow="never" height="285px" id="el-carousel">
			<el-carousel-item class="tcenter">
				<nuxt-link to="/datas/aa" class="inlineBlock">
					<img src="~assets/img/home/text2.png" class="fleft" style="margin-top: 37.5px">
					<img src="~assets/img/home/person1.png" class="fright" style="margin-right: 120px">
				</nuxt-link>
			</el-carousel-item>
			<el-carousel-item class="tcenter">
				<nuxt-link to="/datas" class="inlineBlock">
					<img src="~assets/img/home/text1.png" alt="" class="fleft" style="height: 226px; margin-top: 40px">
					<img src="~assets/img/home/person2.png" alt="" class="fright" style="margin: 40px 115px 0 0;width:268px;">
				</nuxt-link>
			</el-carousel-item>
		</el-carousel>
		<div class="ohidden" style="margin: 30px">
			<div class="fleft left-main">
				<div class="remote box-border">
					<p class="title whitespace">
						<span class="big20">远程门诊</span>&emsp;在当地医院做检查，在当地医生的配合下，看全国知名专家。
					</p>
					<div class="ohidden">
						<div class="remote-left fleft">
							<div style="width: 80%; margin: 1.5em auto 0">
								<p class="big16">合作医院</p>
								<el-select v-model="hosId" placeholder="请选择" :filterable="true" @change="getDep">
									<el-option v-for="(item, ind) in hos" :key="ind" :label="item.displayName" :value="item.id"></el-option>
								</el-select>
								<p class="big16">选择科室</p>
								<el-select v-model="depId" placeholder="请选择">
									<el-option v-for="(item, ind) in dep" :key="ind" :label="item.displayName" :value="item.id"></el-option>
								</el-select>
								<div class="tcenter" style="margin-top: 1rem">
									<nuxt-link :to="`/remote/h=${hosId}&d=${depId}`" style="background: #FAA821; padding: 0.8em 10%; border-radius: 0.2em" class="fff inlineBlock">快速预约</nuxt-link>
								</div>
							</div>
						</div>
						<div class="remote-right fright">
							<p class="tright">
								<nuxt-link to="/remote" class="mainColor">全部<i class="el-icon-arrow-right"></i></nuxt-link>
							</p>
							<nuxt-link to="404" v-for="(item, ind) in exp" :key="ind" class="exp-info fleft">
								<img :src="item.listSpecialPicture" class="fleft">
								<div class="fleft" style="width: 55%">
									<p class="mainColor big16">{{item.specialName}}</p>
									<p class="small12 whitespace">{{item.duty}}<span v-if="item.profession">/{{item.profession}}</span></p>
									<p class="small12 whitespace">{{item.hosName}}<span v-if="item.depName">/{{item.depName}}</span></p>
								</div>
							</nuxt-link>
						</div>
					</div>
				</div>
				<div class="online box-border ohidden">
					<p class="title whitespace">
						<span class="big20">在线问诊</span>&emsp;足不出户看医生（佰医汇平台的问诊费由医生自行设定，平台不收取任何额外费用）。
					</p>
					<div class="depName ohidden cursor">
						<ul class="inlineBlock" style="width: 90%; overflow: hidden">
							<li class="inlineBlock" v-for="(depItem, depInd) in depList" :key="depInd" @click="getOnline(depItem.id, depInd)" :class="{'choose mainColor': depInd == ind}">
								<span>{{depItem.displayName}}</span>
							</li>
						</ul>
						<nuxt-link to="404" class="mainColor inlineBlock tright" style="vertical-align: top; line-height: 40px; width: 9%;">
							全部<i class="el-icon-arrow-right"></i>
						</nuxt-link>
					</div>
					<div style="clear: both">
						<div class="no-exp big16" v-if="!onlineList.length">提示：暂无相关专家</div>
						<template v-else>
							<el-row :gutter="20" style="padding:0 20px 20px">
								<el-col
									:xs="{span: 12}" :sm="{span: 12}" :md="{span: 8}" :lg="{span: 6}"
									v-for="(onlineItem, onlineInd) in onlineList" :key="onlineInd" style="margin-top: 20px">
									<el-card :body-style="{ padding: '0px' }" @click.native="expInit(onlineItem)" class="tcenter onlineExp">
										<img :src="onlineItem.listSpecialPicture" alt="">
										<p style="padding-bottom: 6px">{{onlineItem.specialName}}<span v-if="onlineItem.depName">&ensp;{{onlineItem.depName}}</span></p>
										<p>{{onlineItem.duty}}<span v-if="onlineItem.profession">&ensp;{{onlineItem.profession}}</span></p>
										<p class="specialty tleft">擅长：{{onlineItem.specialty || '暂无'}}</p>
										<div class="ohidden ask">
											<div class="fleft tcenter cursor"
												v-for="(askItem, askInd) in askExp" :key="askInd"
												@click.stop="expInit(onlineItem, askItem.name)"
												:class="{'open mainColor': askInd == 0 && onlineItem.openAsk || askInd == 1 && onlineItem.openTel}">
												<i :class="askItem.icon" class="inlineBlock"></i>
												<p>{{askItem.title}}</p>
											</div>
										</div>
									</el-card>
								</el-col>
							</el-row>
						</template>
					</div>
				</div>
			</div>
			<div class="fright right-main">
				<nuxt-link to="404">
					<img src="~assets/img/home/enter.png" alt="" class="w100">
				</nuxt-link>
				<div class="vedio box-border">
					<p class="big20 vedio-head">名医对对碰<img src="~assets/img/home/sina.png" alt=""></p>
					<div v-for="(vedioItem, vedioInd) in vedio" :key="vedioInd" @click="jump(vedioItem.accessUrl)" class="cursor">
						<img :src="vedioItem.backImag" alt="">
						<p class="big18 whitespace">{{vedioItem.title}}</p>
						<p class="big16 whitespace">{{vedioItem.duty}}&ensp;{{vedioItem.docName}}</p>
					</div>
				</div>
			</div>
		</div>
    </div>
</template>
<script>
	function names(that, params){
		return that.$axios.$get('/kangxin/gainspecialdatas', params)
	}
	export default {
		async asyncData ( { app, query, error  } ){
			let onlineList = []
			let hos = await app.$axios.$get('kangxin/gaincoohospitals')
			let exp = await names(app, { params: { pageSize: 4, pageNo: 1}})
			let depList = await app.$axios.$get('kangxin/gainStandDeps')
			let vedio = await app.$axios.$post('kangxin/gaindoctrforums')
			depList && (onlineList = await names( app, { params: { sdepid: depList.sdeps[0].id, pageSize: 4, pageNo: 1, stype: 'online' }}))
			return {
				hos: hos.hospitals,
				dep: [],
				hosId: '',
				depId: '',
				ind: 0,
				exp: exp.pager.list,
				depList: depList.sdeps.slice(0, 10),
				onlineList: onlineList.pager.list || [],
				askExp: [
					{
						name: 'pic',
						title: '图文问诊',
						icon: 'el-icon-picture'
					},
					{
						name: 'tel',
						title: '电话问诊',
						icon: 'iconfont icon-guanjiaowangtubiao38'
					}
				],
				vedio: vedio.docforums
			}
		},
		mounted () {
			var carousel = document.getElementById('el-carousel')
			carousel.style.width = document.body.offsetWidth+'px'
			carousel.style.left = document.body.offsetWidth > 1200 ? -(document.body.offsetWidth-1260)/2+'px' : 0
			window.onresize = function init (){
				carousel.style.width = document.body.offsetWidth+'px'
				carousel.style.left = document.body.offsetWidth > 1200 ?  -(document.body.offsetWidth-1260)/2+'px': 0
			}
		},
		methods: {
			getDep (){
				this.depId = ''
				this.$axios.$get('kangxin/gainhosdeparts?hosid='+this.hosId).then( d => {
					this.dep = d.departs
				})
			},
			getOnline (id, depInd){
				this.ind = depInd
				names(this, {params: {
					sdepid: id,
					pageSize: 4,
					pageNumber: 1,
					stype: 'online'
				}}).then( d => {
					this.onlineList = d.pager.list
				})
			},
			expInit (item, type){
				!type && this.$router.push('404')
			},
			jump (url){
				window.open(url)
			}
		}
	}
</script>
<style lang="less" scoped>
	@import '~assets/css/var.less';
	.box-border{
		border: 1px solid #EBEBEB;
		box-sizing: border-box
	}
	.home{
		max-width: 1260px;
		margin: 0 auto
	}
	.left-main{
		width: 72%;
		.title{
			color: #a6a6a6;
			border-bottom: 1px solid #EBEBEB;
			padding: 15px;
			span{
				color: @main-font-color;
			}
		}
		.remote{
			.remote-left{
				width: 33%;
				p{
					padding: 0 0 10px 1px;
					color: #666;
					&:nth-of-type(2){
						padding-top: 1em
					}
				}
			}
			.remote-right{
				padding: 0.5em;
				width: 67%;
				border-left: 1px solid #EBEBEB;
				.exp-info{
					width: 50%;
					padding: 1em 0 1em 1em;
					box-sizing: border-box;
					color: #666;
					line-height: 1.9em;
					img{
						width: 40%;
						height: 80px;
						object-fit: cover;
						margin-right: 5%;
					}
				}
			}
		}
		.online{
			margin-top: 30px;
			.depName{
				clear: both;
				border-bottom: 1px solid #EBEBEB;
				height: 42px;
				li{
					line-height: 39.5px;
					span{
						padding: 0 1em;
						border-left: 1px dotted #eee
					}
					&:first-child{
						span{ border: none}
					}
				}
			}
			.no-exp{
				text-align: center;
				padding: 40px 0;
				color: #666;
			}
			.onlineExp{
				cursor: pointer;
				&:hover{
					border: 1px solid @main-color
				}
				img{
					width: 5.7em;
					height: 5.7em;
					object-fit: cover;
					border-radius: 50%;
					margin: 10px 0
				}
				.specialty{
					width: 85%;
					margin: 10px auto;
					color: #999;
					padding-top: 10px;
					border-top: 1px dashed #ddd;
					.setMultEllipsis();
					line-height: 1.3;
					min-height: 47px
				}
				.ask{
					border-top: 1px dashed #ddd;
					div{
						width: 50%;
						color: #ccc;
						padding: 10px 0;
						i{
							border: 1px solid #ccc;
							border-radius: 50%;
							font-size: 1.5em;
							height: 2.2em;
							width: 2.2em;
							line-height: 2.2em;
							box-sizing: border-box;
							margin-bottom: 5px;
						}
					}
					.open{
						i{ border: 1px solid @main-color }
						&:hover{
							padding: 8px 0;
							i{
								font-size: 1.6em;
							}
						}
					}
				}
			}
			.choose{ border-bottom: 2px solid @main-color}
		}
	}
	.right-main{
		width: 25%;
		.vedio{
			margin-top: 30px;
			.vedio-head{
				padding: 15px;
				img{
					vertical-align: middle;
				}
			}
			div{
				color: #666;
				border-top: 1px solid #EBEBEB;
				padding-bottom: 6px;
				img{
					width: 88%;
					margin: 10px 6%
				}
				p{
					padding: 0 1em;
					padding-bottom: 5px;
				}
			}
		}
	}
</style>
<style lang="less">
	.home {
		.el-select{ width: 100%}
		.el-carousel{
			position: relative;
			left: -6%;
			img{
				max-height: 285px
			}
			.el-carousel__item{
				background: url(~assets/img/home/bannerbg.png) no-repeat center center;
				a{ min-width: 1200px}
			}
		}
		.remote-left {
			.el-input__inner{
				height: 33px !important;
			}
		}
	}
</style>
