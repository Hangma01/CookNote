<script setup>
import { getSanction } from '@/services/userService';
import { errorMessages } from '@/utils/messages/errorMessages';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

// 화면 전환
const router = useRouter();

// 레시피 데이터
const sanctionData = ref(null);

// 페이징
const currentPage = ref(0);
const currentPageGroup = ref(0);

const dialog = ref(false);
const selectedReason = ref('');

const showReason = (reason) => {
    selectedReason.value = reason;
    dialog.value = true;
};

// 데이터 가져오기
const loadSanction = async (page = 0) => {
    try {
        const res = await getSanction(page);

        sanctionData.value = res.data;
        currentPage.value = page;
        currentPageGroup.value = Math.floor(page / 10);
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }

        router.push({ name: 'mainPage' });
    }
};

// 페이지 그룹 시작 번호 (0부터)
const pageGroupStart = () => currentPageGroup.value * 10;

// 페이지 그룹 끝 번호 (총 페이지 수보다 크지 않게)
const pageGroupEnd = () => {
    if (!sanctionData.value) return 0;
    return Math.min(pageGroupStart() + 10, sanctionData.value.page.totalPages);
};

// 페이지 이동
const goToPage = (page) => {
    loadSanction(page);
    window.scrollTo(0, 0);
};

// 이전 10페이지 그룹
const prevPageGroup = () => {
    if (currentPageGroup.value > 0) {
        const newPage = (currentPageGroup.value - 1) * 10;
        loadSanction(newPage);
        window.scrollTo(0, 0);
    }
};

// 다음 10페이지 그룹
const nextPageGroup = () => {
    if (sanctionData.value && (currentPageGroup.value + 1) * 10 < sanctionData.value.page.totalPages) {
        const newPage = (currentPageGroup.value + 1) * 10;
        loadSanction(newPage);
        window.scrollTo(0, 0);
    }
};

onMounted(async () => {
    loadSanction();
});
</script>
<template>
    <div class="sanction-container">
        <div class="sanction-title">
            <p>제재 내역 {{ sanctionData?.page.totalElements }}</p>
        </div>

        <div class="line"></div>

        <div v-if="sanctionData?.content.length > 0">
            <table class="sanction-table">
                <thead>
                    <tr>
                        <th>신고 종류</th>
                        <th>대상 내용</th>
                        <th>제재일</th>
                        <th>신고사유</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(item, index) in sanctionData.content" :key="index">
                        <td>{{ item.reportType }}</td>
                        <td>{{ (item.reportType === 'RECIPE' ? '[제목]' : '[내용]') + item.targetContent }}</td>
                        <td>{{ item.reportProcessedAt }}</td>
                        <td>
                            <button @click="showReason(item.categoryReportReasons)" class="cateogrt-reaason">사유보기</button>
                        </td>
                    </tr>
                </tbody>
            </table>

            <div class="pagination" v-if="sanctionData?.page?.totalPages > 1">
                <!-- 이전 10개 페이지 그룹 버튼 -->
                <button v-if="sanctionData.page.totalPages > 10 && currentPageGroup > 0" @click="prevPageGroup">&lt;&lt;</button>

                <!-- 현재 페이지 그룹에 해당하는 페이지 버튼들 -->
                <button
                    v-for="n in pageGroupEnd() - pageGroupStart()"
                    :key="n + pageGroupStart()"
                    :class="{
                        active: sanctionData.page.number === n + pageGroupStart() - 1,
                    }"
                    @click="goToPage(n + pageGroupStart() - 1)"
                >
                    {{ n + pageGroupStart() }}
                </button>

                <!-- 다음 10개 페이지 그룹 버튼 -->
                <button v-if="sanctionData.page.totalPages > 10 && (currentPageGroup + 1) * 10 < sanctionData.page.totalPages" @click="nextPageGroup">
                    &gt;&gt;
                </button>
            </div>
        </div>

        <div v-else class="non-sanction">
            <p>제제당한 내역이 없습니다.</p>
        </div>
    </div>

    <v-dialog v-model="dialog" max-width="500">
        <v-card class="sanction-dialog">
            <v-card-title class="headline"> 신고 사유 </v-card-title>

            <v-divider></v-divider>

            <v-card-text>
                <div v-for="(item, index) in selectedReason" :key="index" class="reason-item">{{ index + 1 }}. {{ item.type }}</div>
            </v-card-text>

            <v-card-actions>
                <v-spacer />
                <v-btn text color="primary" @click="dialog = false">닫기</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>
<style lang="scss" scoped>
.headline {
    padding: 1.5rem 1.5rem 1rem 1.5rem;
    font-size: 1.4rem;
    font-weight: 700;
}

v-card-text {
    padding: 0 1.5rem 1.5rem 1.5rem;
    max-height: 300px;
    overflow-y: auto;
}

.reason-item {
    font-size: 1rem;
    padding-bottom: 0.5rem;
    margin-bottom: 0.5rem;
    color: #444;
}

.sanction-container {
    margin-bottom: 10rem;

    .sanction-title {
        margin-top: 2rem;
        font-weight: 500;
        margin-bottom: 1rem;
        padding-left: 0.2rem;
    }

    .line {
        border-bottom: 2px solid;
        color: #c09370;
    }

    .sanction-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 2rem;
        font-size: 0.8rem;

        .cateogrt-reaason {
            color: rgb(30, 30, 240);
        }

        th,
        td {
            padding: 0.75rem 1rem;
            border: 1px solid #ddd;
            text-align: center;
        }

        th {
            background-color: #f8f8f8;
            font-weight: bold;
            color: #333;
        }

        th:nth-child(1),
        td:nth-child(1) {
            width: 12%;
        }
        th:nth-child(2),
        td:nth-child(2) {
            width: 50%;
        }
        th:nth-child(3),
        td:nth-child(3) {
            width: 26%;
        }
        th:nth-child(4),
        td:nth-child(4) {
            width: 12%;
        }

        tbody tr:nth-child(even) {
            background-color: #fcfcfc;
        }

        tbody tr:hover {
            background-color: #f1f1f1;
        }

        .status.approved {
            font-weight: bold;
        }
        .status.rejected {
            font-weight: bold;
        }
    }

    .pagination {
        margin-top: 3rem;
        text-align: center;

        button {
            margin: 0 0.25rem;
            padding: 0.3rem 0.6rem;
            border: 1px solid #ccc;
            background-color: white;
            cursor: pointer;

            &.active {
                background-color: #a89d94;
                color: white;
                font-weight: bold;
            }

            &:disabled {
                cursor: not-allowed;
                opacity: 0.5;
            }
        }
    }

    .non-sanction {
        height: 30rem;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        font-size: 1.5rem;
        color: #333333;
    }
}
</style>