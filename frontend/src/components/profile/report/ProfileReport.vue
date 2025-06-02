<script setup>
import { getReports } from '@/services/userService';
import { onMounted, ref } from 'vue';

// 레시피 데이터
const reportsData = ref(null);

// 페이징
const currentPage = ref(0);
const currentPageGroup = ref(0);

// 데이터 가져오기
const loadReports = async (page = 0) => {
    try {
        const res = await getReports(page);
        console.log(res.data);
        reportsData.value = res.data;
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
    if (!reportsData.value) return 0;
    return Math.min(pageGroupStart() + 10, reportsData.value.page.totalPages);
};

// 페이지 이동
const goToPage = (page) => {
    loadReports(page);
};

// 이전 10페이지 그룹
const prevPageGroup = () => {
    if (currentPageGroup.value > 0) {
        const newPage = (currentPageGroup.value - 1) * 10;
        loadReports(newPage);
    }
};

// 다음 10페이지 그룹
const nextPageGroup = () => {
    if (reportsData.value && (currentPageGroup.value + 1) * 10 < reportsData.value.page.totalPages) {
        const newPage = (currentPageGroup.value + 1) * 10;
        loadReports(newPage);
    }
};

onMounted(async () => {
    loadReports();
});
</script>
<template>
    <div class="report-cotainer">
        <div class="report-title">
            <p>신고 수 {{ reportsData?.page.totalElements }}</p>
        </div>

        <div class="line"></div>

        <div v-if="reportsData?.content.length > 0">
            <table class="report-table">
                <thead>
                    <tr>
                        <th>신고 종류</th>
                        <th>피신고자</th>
                        <th>신고 사유</th>
                        <th>신고일</th>
                        <th>처리 상태</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(item, index) in reportsData.content" :key="index">
                        <td>
                            {{ item.reportType === 'RECIPE' ? '레시피' : '댓글' }}
                        </td>
                        <td>{{ item.reportedNickname }}</td>
                        <td>{{ item.reportReason }}</td>
                        <td>{{ item.reportCreateAt }}</td>
                        <td :class="`status ${item.reportStatus.toLowerCase()}`">
                            {{ item.reportStatusLabel }}
                        </td>
                    </tr>
                </tbody>
            </table>

            <div class="pagination" v-if="reportsData?.page?.totalPages > 1">
                <!-- 이전 10개 페이지 그룹 버튼 -->
                <button v-if="reportsData.page.totalPages > 10 && currentPageGroup > 0" @click="prevPageGroup">&lt;&lt;</button>

                <!-- 현재 페이지 그룹에 해당하는 페이지 버튼들 -->
                <button
                    v-for="n in pageGroupEnd() - pageGroupStart()"
                    :key="n + pageGroupStart()"
                    :class="{
                        active: reportsData.page.number === n + pageGroupStart() - 1,
                    }"
                    @click="goToPage(n + pageGroupStart() - 1)"
                >
                    {{ n + pageGroupStart() }}
                </button>

                <!-- 다음 10개 페이지 그룹 버튼 -->
                <button v-if="reportsData.page.totalPages > 10 && (currentPageGroup + 1) * 10 < reportsData.page.totalPages" @click="nextPageGroup">
                    &gt;&gt;
                </button>
            </div>
        </div>

        <div v-else class="non-report">
            <p>신고한 내역이 없습니다.</p>
        </div>
    </div>
</template>
<style lang="scss" scoped>
.report-cotainer {
    margin-bottom: 10rem;

    .report-title {
        margin-top: 2rem;
        font-weight: 500;
        margin-bottom: 1rem;
        padding-left: 0.2rem;
    }

    .line {
        border-bottom: 2px solid;
        color: #c09370;
    }

    .report-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 2rem;
        font-size: 0.8rem;

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
            width: 12%;
        }
        th:nth-child(3),
        td:nth-child(3) {
            width: 39%;
        }
        th:nth-child(4),
        td:nth-child(4) {
            width: 21%;
        }
        th:nth-child(5),
        td:nth-child(5) {
            width: 13%;
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

    .non-report {
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