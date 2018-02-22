<script>
    import EAuth from '~/components/EAuth.vue'
    import EFooter from '~/components/EFooter.vue'
    export default {
        components: {
            EAuth, EFooter
        },
        beforeCreate () {
            if (process.server) return
            (function () {
                function o () {
                    document.documentElement.style.fontSize = parseInt((document.documentElement.clientWidth > 414 ? 414 : document.documentElement.clientWidth) * 13 / 375) + "px"
                }
                var e = null;
                window.addEventListener("resize", function () {
                    clearTimeout(e), e = setTimeout(o, 100)
                }, !1), o()
            })();
        }
    }
</script>

<template>
    <div>
        <e-auth></e-auth>
        <div class="w1366">
            <nuxt/>
        </div>
        <e-footer></e-footer>
    </div>
</template>

<style>
    .button--green {
        display: inline-block;
        border-radius: 4px;
        border: 1px solid #3b8070;
        color: #3b8070;
        text-decoration: none;
        padding: 10px 30px;
    }

    .button--green:hover {
        color: #fff;
        background-color: #3b8070;
    }

    .button--grey {
        display: inline-block;
        border-radius: 4px;
        border: 1px solid #35495e;
        color: #35495e;
        text-decoration: none;
        padding: 10px 30px;
        margin-left: 15px;
    }

    .button--grey:hover {
        color: #fff;
        background-color: #35495e;
    }
</style>
