<script setup>
import ReportModal from '@/components/ui/ReportModal.vue';
import { ReportType } from '@/constans/reportType';
import { commentDelete, commentInsert, commentUpdate, getCommentReplies } from '@/services/commentService';
import { useUserStore } from '@/stores/user';
import { commonInputHangle } from '@/utils/commonFunction';
import { errorMessages } from '@/utils/messages/errorMessages';
import { onBeforeUnmount, onMounted, reactive, ref } from 'vue';
import { CommentStatus } from '../../../constans/commentStatus';
import dayjs from '@/plugin/dayjs';

// 부모로 받은 데이터
const props = defineProps({
    commentsData: Object,
    recipeId: String,
    requesterId: Number,
    recipeWriterId: Number,
    repliesCountData: Number,
});

// 유저 스토어
const userStore = useUserStore();
const isLoggedIn = userStore.getIsLoggedIn;
const userId = userStore.getUserId;

// 부모 이벤트
const emit = defineEmits(['refreshComments', 'changePage', 'openReportModal', 'refreshReply']);

// inputFiled
const formValues = reactive({
    content: '',
    editContent: '',
});

const eidtCommentMap = reactive({}); // 보낼 댓글 수정 값
const activeEditCommentMap = reactive({}); // 보낼 댓글 수정 활성화
const activeCommentMenuMap = reactive({}); // 댓글 메뉴 활성화
const commentMentItemRefs = ref({}); // 외부 클릭 시 댓글 메뉴 닫히기

const replyContentMap = reactive({}); // 보낼 답글 값
const activeReplyInputMap = reactive({}); // 보낼 답글 활성화
const activeReplyMenuMap = reactive({}); // 답글 메뉴 활성화
const replyItemRefs = ref({}); // 외부 클릭 시 답글 메뉴 닫히기

const editReplyMap = reactive({}); // 보낼 답글 수정 값
const activeEditReplyMap = reactive({}); // 보낼 답글 수정 활성화

const replyListMap = reactive({}); // 답글 리스트
const replyPageMap = reactive({}); // 답글 페이지
const replySizeMap = reactive({}); // 답글 사이즈
const replyTotalMap = reactive({}); // 답글 총 페이지

function relativeTime(date) {
    return dayjs(date).fromNow();
}

// 댓글 메뉴 토글
const toggleCommentMenu = (event, commentId) => {
    closeReplyMenu(event);
    event.stopPropagation();
    activeCommentMenuMap[commentId] = !activeCommentMenuMap[commentId];
    closeCommentMenu(event);
};

// 외부 클릭 시 댓글 메뉴 닫기
const closeCommentMenu = (event) => {
    for (const commentId in activeCommentMenuMap) {
        const refElement = commentMentItemRefs.value[commentId];
        if (activeCommentMenuMap[commentId] && refElement && !refElement.contains(event.target)) {
            activeCommentMenuMap[commentId] = false;
        }
    }
};

const setCommentMenuRef = (el, commentId) => {
    if (el) {
        commentMentItemRefs.value[commentId] = el;
    } else {
        delete commentMentItemRefs.value[commentId];
    }
};

onMounted(() => {
    document.addEventListener('click', closeCommentMenu);
    document.addEventListener('click', closeReplyMenu);
});
onBeforeUnmount(() => {
    document.removeEventListener('click', closeCommentMenu);
    document.removeEventListener('click', closeReplyMenu);
});

// 댓글 입력
const handleCommentInsert = async () => {
    const content = formValues.content.trim();

    if (!content) return alert('댓글을 입력해주세요.');

    try {
        await commentInsert(content, props?.recipeId);
        formValues.content = ''; // 입력 필드 초기화
        emit('refreshComments');
        emit('refreshReply');
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }
    }
};

// 댓글 수정 입력
const handleEditCommentUpdate = async (content, commentId) => {
    const tirmContent = content.trim();
    if (!tirmContent) return alert('댓글을 입력해주세요.');

    try {
        await commentUpdate(tirmContent, commentId);
        emit('refreshComments');
        activeEditCommentMap[commentId] = false;
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }
    }
};

// 댓글 수정
const handleCommentEdit = async (commentId, content) => {
    eidtCommentMap[commentId] = content;
    activeEditCommentMap[commentId] = true;
};

// 댓글 수정 취소
const handleCommentEditCancle = async (commentId) => {
    activeEditCommentMap[commentId] = false;
};

// 댓글 삭제
const handleCommentEditDelete = async (commentId) => {
    try {
        await commentDelete(commentId);
        emit('refreshComments');
        emit('refreshReply');
        activeEditCommentMap[commentId] = false;
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }
    }
};

// 답글 메뉴 토글
const toggleReplyMenu = (event, commentId) => {
    closeCommentMenu(event);
    event.stopPropagation();
    activeReplyMenuMap[commentId] = !activeReplyMenuMap[commentId];
    closeReplyMenu(event);
};

// 외부 클릭 시 답글 메뉴 닫기
const closeReplyMenu = (event) => {
    for (const commentId in activeReplyMenuMap) {
        const refElement = replyItemRefs.value[commentId];
        if (activeReplyMenuMap[commentId] && refElement && !refElement.contains(event.target)) {
            activeReplyMenuMap[commentId] = false;
        }
    }
};

const setReplyMenuRef = (el, commentId) => {
    if (el) {
        replyItemRefs.value[commentId] = el;
    } else {
        delete replyItemRefs.value[commentId];
    }
};

// 답글 버튼 클릭
const handleReplyBtn = async (commentId) => {
    activeReplyInputMap[commentId] = !activeReplyInputMap[commentId];
    if (activeReplyInputMap[commentId] && !replyListMap[commentId]) {
        await loadReplies(commentId, 0);
    }
};

// 답글 가져오기
const loadReplies = async (commentId, page = 0, size = 5) => {
    try {
        const res = await getCommentReplies(commentId, page, size); // page 파라미터 넘김

        if (!replyListMap[commentId]) {
            replyListMap[commentId] = [];
        }

        // 기존 데이터 + 새 데이터
        replyListMap[commentId] = [...res.data.content];

        // 현재 페이지, 전체 페이지 저장
        replyPageMap[commentId] = res.data.page.number;
        replySizeMap[commentId] = res.data.page.size;
        replyTotalMap[commentId] = res.data.page.totalPages;
    } catch (e) {
        delete replyListMap[commentId];
    }
};

// 답글 더보기 클릭
const handleLoadMoreReplies = (commentId) => {
    const nextSize = (replySizeMap[commentId] || 5) + 5;
    loadReplies(commentId, 0, nextSize);
};

// 답글 저장
const handleReplyInsert = async (parentCommentId) => {
    const content = replyContentMap[parentCommentId]?.trim();
    if (!content) return alert('답글을 입력해주세요.');

    try {
        await commentInsert(content, props.recipeId, parentCommentId);
        replyContentMap[parentCommentId] = '';
        emit('refreshReply');

        loadReplies(parentCommentId, replyPageMap[parentCommentId], replySizeMap[parentCommentId] || 5);
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }
    }
};

// 답글 수정 입력
const handleEditReplyUpdate = async (content, commentId, parentCommentId) => {
    const tirmContent = content.trim();
    if (!tirmContent) return alert('답글을 입력해주세요.');

    try {
        await commentUpdate(tirmContent, commentId);
        loadReplies(parentCommentId, replyPageMap[parentCommentId]);
        activeEditReplyMap[commentId] = false;
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }
    }
};

// 답글 수정
const handleReplyEdit = async (commentId, content) => {
    editReplyMap[commentId] = content;
    activeEditReplyMap[commentId] = true;
};

// 답글 취소
const handleReplyEditCancle = async (commentId) => {
    activeEditReplyMap[commentId] = false;
};

// 답글 삭제
const handleReplyEditDelete = async (commentId, parentCommentId) => {
    try {
        await commentDelete(commentId);
        loadReplies(parentCommentId, replyPageMap[parentCommentId]);
        activeEditReplyMap[commentId] = false;
        emit('refreshReply');
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.BADREQUEST);
        }
    }
};

// 페이지 전환
const goToPage = (page) => {
    emit('changePage', page);
};

// 댓글 제한 250자 (한글)
const handleContentInput = (e) => commonInputHangle(e, 250, (value) => (formValues.content = value));

// 댓글 수정 제한 250자 (한글)
const handleEditContentInput = (e, commentId) => commonInputHangle(e, 250, (value) => (eidtCommentMap[commentId] = value));

// 답글 제한 250자 (한글)
const handleReplyInput = (e, commentId) => commonInputHangle(e, 250, (value) => (replyContentMap[commentId] = value));

// 답글 수정 제한 250자 (한글)
const handleEditReplyInput = (e, commentId) => commonInputHangle(e, 250, (value) => (editReplyMap[commentId] = value));
</script>

<template>
    <div class="recipe-detail-comment-section">
        <div class="section-title">
            <p>
                댓글
                {{ commentsData.page?.totalElements + (props?.repliesCountData || 0) }}
            </p>
        </div>

        <div class="comment-wrap">
            <!-- 댓글 입력창 -->
            <div class="comment-input-filed">
                <v-textarea
                    v-model="formValues.content"
                    @input="handleContentInput"
                    :placeholder="isLoggedIn ? '답글은 250자 이내로 작성해주세요.' : '로그인 후 답글 작성이 가능합니다.'"
                    rows="4"
                    no-resize
                    variant="outlined"
                    density="compact"
                    hide-details="true"
                    class="comment-text"
                    :disabled="!isLoggedIn"
                />
                <button class="comment-insert-btn" @click="handleCommentInsert(formValues.content)" :disabled="!isLoggedIn">등록</button>
            </div>

            <!-- 댓글 목록 -->
            <ul v-for="item in commentsData.content" :key="item.commentId" class="seq-item">
                <li class="comment-item">
                    <div class="comment-info-wrap">
                        <div class="comment-info">
                            <div>
                                <router-link
                                    :to="
                                        item.writerId === userId
                                            ? { name: 'profileRecipe' }
                                            : {
                                                  name: 'profileHost',
                                                  params: {
                                                      hostId: item.writerId,
                                                  },
                                              }
                                    "
                                >
                                    <img :src="item.profileImage" class="writer-profile-image" />
                                </router-link>
                            </div>

                            <span class="writer-nickname">{{ item.nickname }}</span>

                            <div class="is-author-box" v-if="recipeWriterId === item.writerId">
                                <div class="is-author">
                                    <span>주인장</span>
                                </div>
                            </div>

                            <!-- <div class="is-self-box">
                                <div class="is-self">
                                    <span v-if="userId === item.writerId">본인</span>
                                </div>
                            </div> -->
                        </div>

                        <div class="comment-menu-btn" v-if="isLoggedIn === true">
                            <div @click="toggleCommentMenu($event, item.commentId)" class="comment-menu-icon">
                                <font-awesome-icon :icon="['fas', 'ellipsis-vertical']" />
                            </div>

                            <div
                                class="comment-menu"
                                v-if="activeCommentMenuMap[item.commentId]"
                                :ref="(el) => setCommentMenuRef(el, item.commentId)"
                            >
                                <div v-if="item.writerId !== userId">
                                    <button @click="$emit('openReportModal', ReportType.COMMENT, item.commentId, item.writerId)">신고</button>
                                </div>

                                <div v-if="item.writerId === userId">
                                    <button @click="handleCommentEdit(item.commentId, item.content)">수정</button>
                                </div>

                                <div v-if="item.writerId === userId">
                                    <button @click="handleCommentEditDelete(item.commentId)">삭제</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="content" v-if="!activeEditCommentMap[item.commentId]">
                        <span>{{ item.content }}</span>
                    </div>

                    <!-- 댓글 수정 -->
                    <div class="edit-wrap" v-if="activeEditCommentMap[item.commentId]">
                        <v-textarea
                            v-model="eidtCommentMap[item.commentId]"
                            @input="handleEditContentInput($event, item.commentId)"
                            placeholder="댓글은 250자 이내로 작성해주세요."
                            rows="3"
                            no-resize
                            variant="outlined"
                            density="compact"
                            hide-details="true"
                            class="comment-edit-input"
                        />

                        <div class="edit-btn-box">
                            <button type="button" class="edit-btn edit-btn-cancle" @click="handleCommentEditCancle(item.commentId)">취소</button>
                            <button type="button" class="edit-btn" @click="handleEditCommentUpdate(eidtCommentMap[item.commentId], item.commentId)">
                                수정
                            </button>
                        </div>
                    </div>

                    <div class="comment-info-base">
                        <div class="create-date">
                            <span>{{ relativeTime(item.createAt) }}</span>
                        </div>
                    </div>

                    <!-- 답글 버튼 -->
                    <div class="reply-wrap">
                        <button class="reply-show-btn" @click="handleReplyBtn(item.commentId)">
                            답글
                            <span v-if="item.replyCount > 0">{{ item.replyCount }}</span>
                        </button>
                    </div>

                    <!-- 답글 입력창 -->
                    <div v-if="activeReplyInputMap[item.commentId]">
                        <div class="reply-input-wrap">
                            <v-textarea
                                v-model="replyContentMap[item.commentId]"
                                @input="handleReplyInput($event, item.commentId)"
                                :placeholder="isLoggedIn ? '답글은 250자 이내로 작성해주세요.' : '로그인 후 답글 작성이 가능합니다.'"
                                rows="3"
                                no-resize
                                variant="outlined"
                                density="compact"
                                hide-details
                                class="reply-input-field"
                                :disabled="!isLoggedIn"
                            />

                            <button @click="handleReplyInsert(item.commentId)" class="reply-insert-btn" :disabled="!isLoggedIn">등록</button>
                        </div>

                        <!-- 답글 목록 -->
                        <ul>
                            <div class="reply-list" v-if="replyListMap[item.commentId] && replyListMap[item.commentId].length > 0">
                                <li v-for="reply in replyListMap[item.commentId]" :key="reply.commentId" class="reply-item">
                                    <div class="reply-line-icon"></div>
                                    <div class="reply-info-box">
                                        <div class="reply-info-wrap">
                                            <div class="reply-info">
                                                <router-link
                                                    :to="
                                                        reply.writerId === userId
                                                            ? {
                                                                  name: 'profileRecipe',
                                                              }
                                                            : {
                                                                  name: 'profileHost',
                                                                  params: {
                                                                      hostId: reply.writerId,
                                                                  },
                                                              }
                                                    "
                                                >
                                                    <img :src="reply.profileImage" class="writer-profile-image" />
                                                </router-link>
                                                <span class="writer-nickname">{{ reply.nickname }}</span>
                                                <div class="is-author-box" v-if="recipeWriterId === reply.writerId">
                                                    <div class="is-author">
                                                        <span>주인장</span>
                                                    </div>
                                                </div>

                                                <!-- <div class="is-self-box">
                                                    <div class="is-self">
                                                        <span v-if="userId === reply.writerId">본인</span>
                                                    </div>
                                                </div> -->
                                            </div>

                                            <div class="comment-menu-btn" v-if="isLoggedIn === true">
                                                <div class="comment-menu-icon" @click="toggleReplyMenu($event, reply.commentId)">
                                                    <font-awesome-icon :icon="['fas', 'ellipsis-vertical']" />
                                                </div>

                                                <div
                                                    class="comment-menu"
                                                    v-if="activeReplyMenuMap[reply.commentId]"
                                                    :ref="(el) => setReplyMenuRef(el, reply.commentId)"
                                                >
                                                    <div v-if="reply.writerId !== userId">
                                                        <button
                                                            @click="$emit('openReportModal', ReportType.COMMENT, reply.commentId, reply.writerId)"
                                                        >
                                                            신고
                                                        </button>
                                                    </div>

                                                    <div v-if="reply.writerId === userId">
                                                        <button @click="handleReplyEdit(reply.commentId, reply.content)">수정</button>
                                                    </div>

                                                    <div v-if="reply.writerId === userId">
                                                        <button @click="handleReplyEditDelete(reply.commentId, item.commentId)">삭제</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="content" v-if="!activeEditReplyMap[reply.commentId]">
                                            <span>{{ reply.content }}</span>
                                        </div>

                                        <!-- 답글 수정 -->
                                        <div class="edit-wrap" v-if="activeEditReplyMap[reply.commentId]">
                                            <v-textarea
                                                v-model="editReplyMap[reply.commentId]"
                                                @input="handleEditReplyInput($event, editReplyMap[reply.commentId])"
                                                placeholder="댓글은 250자 이내로 작성해주세요."
                                                rows="2"
                                                no-resize
                                                variant="outlined"
                                                density="compact"
                                                hide-details="true"
                                                class="comment-edit-input"
                                            />

                                            <div class="edit-btn-box">
                                                <button
                                                    type="button"
                                                    class="edit-btn edit-btn-cancle"
                                                    @click="handleReplyEditCancle(reply.commentId)"
                                                >
                                                    취소
                                                </button>
                                                <button
                                                    type="button"
                                                    class="edit-btn"
                                                    @click="handleEditReplyUpdate(editReplyMap[reply.commentId], reply.commentId, item.commentId)"
                                                >
                                                    수정
                                                </button>
                                            </div>
                                        </div>

                                        <div class="comment-info-base">
                                            <span>{{ relativeTime(reply.createAt) }}</span>
                                        </div>
                                    </div>
                                </li>

                                <!-- 더보기 버튼 -->
                                <li v-if="replyPageMap[item.commentId] + 1 < replyTotalMap[item.commentId]" class="reply-show-more">
                                    <button @click="handleLoadMoreReplies(item.commentId)">더보기</button>
                                </li>
                            </div>
                        </ul>
                    </div>
                </li>
            </ul>

            <div class="pagination" v-if="commentsData?.page?.totalPages > 1">
                <button
                    v-for="n in commentsData?.page?.totalPages"
                    :key="n"
                    :class="{ active: commentsData?.page?.number === n - 1 }"
                    @click="goToPage(n - 1)"
                >
                    {{ n }}
                </button>
            </div>
        </div>
    </div>
</template>


<style lang="scss" scoped>
.recipe-detail-comment-section {
    margin-top: 5rem;

    .section-title {
        font-size: 1.2rem;
        font-weight: bold;
        border-bottom: 2px solid rgb(147, 112, 98);
        padding-bottom: 1rem;
        margin-bottom: 0.7em;
    }

    .comment-wrap {
        text-align: right;

        .comment-input-filed {
            .comment-insert-btn {
                background-color: rgb(244, 240, 239);
                border-radius: 0.5rem;
                padding: 0.4rem 1rem;
                font-size: 0.9rem;
                margin-top: 0.5rem;
            }
        }

        .comment-item {
            padding-top: 0.5rem;
            padding-bottom: 0.8rem;
            border-bottom: 1px solid rgb(224, 224, 224);
            text-align: left;

            .comment-info-wrap {
                display: flex;
                align-items: center;
                justify-content: space-between;

                .comment-info {
                    display: flex;
                    justify-content: space-between;
                    gap: 5px;

                    .writer-profile-image {
                        width: 1.5rem;
                        height: 1.5rem;
                        border-radius: 100%;
                    }

                    .writer-nickname {
                        padding-top: 0.1rem;
                        font-size: 0.85rem;
                        margin-left: 0.3rem;
                        font-weight: 600;
                    }
                }
            }

            .content {
                padding-left: 0.3rem;
                font-size: 0.83rem;
                white-space: pre-wrap;
            }

            .edit-wrap {
                .comment-edit-input {
                    width: 100%;
                    border-bottom: 1px solid black;
                    outline: none;
                    font-size: 0.9rem;
                }

                .edit-btn-box {
                    margin-top: 0.5rem;
                    text-align: right;

                    .edit-btn {
                        border-radius: 0.5rem;
                        font-size: 0.8rem;
                        border: 2px solid rgb(224, 224, 224);
                        padding: 0.3rem 0.7rem;
                    }

                    .edit-btn-cancle {
                        margin-right: 1rem;
                    }
                }
            }

            .comment-info-base {
                padding-left: 0.3rem;
                margin-top: 0.2rem;
                display: flex;
                align-items: center;
                gap: 0.8rem;
                color: rgb(117, 117, 117);
                font-size: 0.7rem;

                .line {
                    border-left: 0.1rem solid rgb(117, 117, 117);
                    height: 0.7rem;
                }

                .report-btn {
                    width: 1.5rem;
                    min-width: 0;
                    height: 1.1rem;
                }
            }

            .reply-wrap {
                margin-top: 0.2rem;
                padding-left: 0.2rem;

                .reply-show-btn {
                    font-size: 0.7rem;
                    padding: 0.1rem 0.5rem;
                    border: 1px solid rgb(224, 224, 224);
                }
            }

            .reply-input-wrap {
                margin-top: 1rem;
                padding-top: 1rem;
                padding-bottom: 0.8rem;
                border-top: 1px solid rgb(224, 224, 224);
                padding-left: 3rem;
                padding-right: 3rem;
                text-align: right;

                .reply-input-field {
                    margin: auto;
                }

                .reply-insert-btn {
                    background-color: rgb(244, 240, 239);
                    border-radius: 0.5rem;
                    padding: 0.4rem 0.8rem;
                    font-size: 0.8rem;
                    margin-top: 0.5rem;
                }
            }

            .reply-item {
                padding-top: 0.5rem;
                padding-bottom: 0.8rem;
                border-bottom: 1px solid rgb(224, 224, 224);
                text-align: left;
                padding-left: 3rem;
                padding-right: 3rem;
                display: flex;

                .reply-info-box {
                    width: 100%;

                    .writer-nickname {
                        padding-top: 0.1rem;
                        font-size: 0.85rem;
                        font-weight: 600;
                        margin-left: 0.3rem;
                    }
                    .reply-info-wrap {
                        display: flex;
                        align-items: center;
                        justify-content: space-between;

                        .reply-info {
                            display: flex;
                            justify-content: space-between;
                            gap: 5px;

                            .writer-profile-image {
                                width: 1.5rem;
                                height: 1.5rem;
                                border-radius: 100%;
                            }
                        }
                    }
                }

                .reply-line-icon {
                    position: relative;
                    width: 1rem;
                    height: 1rem;
                    margin-right: 10px;
                    margin-top: 0.3rem;

                    &::before,
                    &::after {
                        content: '';
                        position: absolute;
                        background-color: #ccc;
                    }

                    &::before {
                        top: 0;
                        left: 50%;
                        width: 2px;
                        height: 10px;
                    }

                    &::after {
                        top: 63%;
                        left: 50%;
                        width: 10px;
                        height: 2px;
                    }
                }
            }

            .reply-item:last-child {
                border: none;
                padding-bottom: 0;
            }

            .reply-show-more {
                text-align: center;
                font-size: 0.9rem;
                padding-top: 0.9rem;
            }

            .is-author-box {
                padding-top: 0.2rem;

                .is-author {
                    font-size: 0.6rem;
                    font-weight: 600;
                    border: 1px solid #03c75a;
                    height: 1.2rem;
                    border-radius: 10rem;
                    padding: 0.1rem 0.2rem;
                    color: #03c75a;
                }
            }

            // .is-self-box {
            //     padding-top: 0.2rem;

            //     .is-self {
            //         font-size: 0.6rem;
            //         font-weight: 600;
            //         border: 1px solid orange;
            //         height: 1.2rem;
            //         border-radius: 10rem;
            //         padding: 0.1rem 0.5rem;
            //         color: orange;

            //     }
            // }
        }
        .pagination {
            margin-top: 1rem;
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
            }
        }
    }

    .comment-menu-btn {
        color: rgb(124, 124, 124);
        position: relative;

        .comment-menu-icon {
            cursor: pointer;
            width: 1.5rem;
            text-align: center;
        }

        .comment-menu {
            position: absolute;
            background-color: white;
            width: 5rem;
            top: 2rem;
            left: -1rem;
            text-align: center;
            border: 1px solid rgb(124, 124, 124);
            border-radius: 0.5rem;
            z-index: 100;
            padding-top: 0.2rem;
            padding-bottom: 0.2rem;
            color: black;
        }
    }
}
</style>