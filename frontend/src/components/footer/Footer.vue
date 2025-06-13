<script setup>
</script>


<template>
    <footer class="footer-container">
        <div class="contents">
            <div class="customer-center">
                <div class="customer-center-wrap">
                    <strong class="customer-center-title">고객센터</strong>
                    <div class="call">
                        <font-awesome-icon :icon="['fas', 'phone-volume']" class="phone-icon" />
                        <span>010-9920-2102</span>
                    </div>
                    <p class="operating-hour-title">운영시간</p>
                    <p class="operating-hour">
                        전화문의 - 10:00 ~ 12:00, 13:00 ~ 17:00 / 주말·공휴일 휴무
                        <br />
                        1:1 문의 - 09:00 ~ 12:00, 13:00 ~ 17:30 / 주말·공휴일 휴무
                    </p>
                </div>

                <div class="service-policy">
                    <p class="service-policy-nav">
                        <a href="/policies/terms" target="_blank">
                            <span class="underline">이용약관</span>
                        </a>
                        <a href="/policies/privacy" target="_blank">
                            <span class="underline">개인정보처리방침</span>
                        </a>
                    </p>
                </div>

                <div class="address-wrap">
                    <address>
                        <p class="address-title">(주)쿡노트</p>
                        <div class="address-box">
                            <p>상호 : (주)쿡노트</p>
                            <p>대표자 : 안효준</p>
                            <p>개인정보관리책임자 : 안효준</p>
                        </div>
                        <div class="address-box">
                            <p>전화 : 010-9920-2102</p>
                            <p>팩스 : 010-9920-2102</p>
                        </div>
                        <div class="address-box">
                            <p>주소 : (13487) 경기도 하남시 미사강변서로 167</p>
                            <a href="mailto:hyojun.ahn@ta9.co.kr" class="email">
                                <p>이메일 : hyojun.ahn@ta9.co.kr</p>
                            </a>
                        </div>
                    </address>
                    <div class="logo">
                        <p class="copyright-notice">© CookNote Crop. All rights are reserved.</p>
                    </div>
                </div>
            </div>
        </div>
    </footer>
</template>


<style lang="scss" scoped>
.footer-container {
    border-top: 1px solid rgb(158, 158, 158);

    .contents {
        .customer-center {
            .customer-center-wrap {
                padding: 1.8rem 0.9rem;
                width: 70rem;
                margin: auto;
                .customer-center-title {
                    font-family: 'Noto Serif KR', serif, Helvetica, 'Helvetica Neue', Arial;
                    font-size: 1.2rem;
                }

                .call {
                    width: 11rem;
                    margin-top: 1.5rem;
                    display: flex;
                    align-items: center;
                    height: 3rem;
                    padding: 0 1.5rem;
                    border-radius: 0.3rem;
                    border: 1px solid rgb(117, 87, 75);
                    background-color: rgb(255, 255, 255);
                    font-size: 0.95rem;
                    font-weight: 500;
                    gap: 0.4rem;
                    margin-bottom: 1.5rem;

                    .phone-icon {
                        padding-top: 0.1rem;
                        font-size: 0.79rem;
                    }
                }

                .operating-hour-title {
                    font-weight: normal;
                    font-size: 1.05rem;
                    font-weight: 700;
                    font-family: 'Noto Serif KR', serif, Helvetica, 'Helvetica Neue', Arial;
                }

                .operating-hour {
                    margin-top: 0.4rem;
                    font-weight: normal;
                    font-family: 'Noto Sans KR', Helvetica, 'Helvetica Neue', Arial, 'sans-serif';
                    font-size: 0.92rem;
                    font-weight: 500;
                }
            }

            .service-policy {
                background-color: rgb(238, 238, 238);
                .service-policy-nav {
                    width: 70rem;
                    margin: auto;
                    padding: 0.6rem 0.9rem;
                    font-size: 0.9rem;
                    font-weight: 500;

                    .underline {
                        text-decoration: underline;
                        cursor: pointer;
                        margin-right: 2rem;
                    }
                }
            }

            .address-wrap {
                padding: 1.8rem 0.9rem;
                width: 70rem;
                margin: auto;
                display: flex;
                justify-content: space-between;
                gap: 0.1rem;

                address {
                    font-style: normal;
                }

                .email {
                    color: rgb(117, 117, 117);
                }

                .address-title {
                    margin-bottom: 0.6rem;
                    font-weight: 500;
                    font-size: 0.95rem;
                }

                .address-box {
                    margin-bottom: 0.55rem;
                    font-size: 0.9rem;
                    display: flex;
                    gap: 2.5rem;
                    color: rgb(117, 117, 117);
                    font-weight: 500;
                    line-height: 19px;
                    letter-spacing: -0.04rem;
                }

                .logo {
                    align-self: flex-end;
                    margin-bottom: 0.55rem;
                    font-weight: 500;
                    font-size: 0.9rem;
                }
                .copyright-notice {
                    color: rgb(117, 117, 117);
                }
            }
        }
    }
}
</style>