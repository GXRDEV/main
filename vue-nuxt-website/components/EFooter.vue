<template>
    <footer class="e-footer">
        <ul class="w1366 clearfix">
            <li>
                <img src="~assets/img/whitelogo.png" />
            </li>
            <li>
                <div>
                    <h2>关于我们</h2>
                    <a target="_blank" href="#app">微信公众号</a>
                    <a target="_blank" href="http://weibo.com/zhuanjiahao1">新浪微博</a>
                    <a target="_blank" href="#home_statichtm/aboutus">关于我们</a>
                    <a target="_blank" href="#app">佰医汇app</a>
                </div>
            </li>
            <li>
                <div>
                    <h2>帮助</h2>
                    <a href="#home_statichtm/feedback">建议与反馈</a>
                    <a href="#home_statichtm/contactus">联系我们</a>
                    <a href="#home_statichtm/agreement">用户协议</a>
                </div>
            </li>
            <li class="lastli big18 ohidden">
                <div>
                    <a class="whitespace" href="hosin/index.html" target="_blank">
                        <i class="iconfont">&#xe608;</i>医院合作
                    </a>
                    <p class="kefu whitespace">
                        <i class="iconfont">&#xe605;</i>客服电话
                    </p>
                    <a class="whitespace" href="tel:400-631-9377">400-631-9377</a>
                </div>
            </li>
        </ul>
        <div class="copyrightdiv">
            <p>
                <span>版权所有：北京佰医汇医疗信息技术有限公司</span>
                <span>京ICP备15015320号</span>
            </p>
            <p>
                <span>服务声明：专家咨询建议仅供参考_不能作为诊断及医疗的依据</span>
            </p>
            <p>
                <a target="_blank" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=11010802017313">
                    京公网安备 11010802017313号
                </a>
            </p>
        </div>
    </footer>
</template>

<style lang="less">
    .e-footer {
        background: #0186d1;
        color: #fff;
        line-height: 2em;
        padding-top: 1px;
        a {
            color: #fff;
            display: block;
        }
        h2 {
            font-weight: 400;
            font-size: 1.286rem;
        }
        ul {
            padding: 3em 0 1em;
        }
        li {
            width: 25%;
            float: left;
            text-align: center;
            position: relative;
            &:first-child {
                padding: 0 1rem;
            }
        }
        img {
            max-width: 100%;
            object-fit: contain;
        }
        li:after {
            content: "";
            width: 1px;
            height: 6em;
            background: #fff;
            position: absolute;
            right: 0;
            top: 1em;
        }
        li:last-child:after {
            width: 0;
        }
        li div {
            display: inline-block;
            margin: 0 auto;
            text-align: left;
        }
        .lastli {
            padding: 0 1rem;
            i {
                font-size: 1.857rem;
                margin-right: 0.6rem;
            }
            .kefu {
                padding-top: 2em;
            }
        }
        .copyrightdiv {
            padding: 0.14rem 0 1.5rem;
            p {
                line-height: 2.4em;
                font-size: 0.857rem;
                color: #cecaca;
                text-align: center;
            }
            a {
                display: inline-block;
                text-indent: 2em;
                background: url(~assets/img/g-an.png) no-repeat 0 center;
                color: #aaa;
            }
            span {
                margin-right: 1em;
            }
        }
    }
</style>
