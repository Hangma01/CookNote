<script setup>
import { commentInsert, getComments } from '@/services/commentService';
import { commonInputHangle } from '@/utils/commonFunction';
import { reactive, ref } from 'vue';

// 부모로 받은 데이터
const props = defineProps({ 
    commentsData: { 
        type: Object    
    },
    recipeId: {
        type: String
    }
})

// 부모 이벤트
const emit = defineEmits(['refreshComments', 'changePage']);

// inputFiled
const formValues = reactive({
    conent: '',
    reply: '',
})

// 댓글 입력
const handleContentInsert = async () => {
    if (!formValues.conent.trim()) {
        alert('댓글을 입력해주세요.');
        return;
    }

    try {
        const res = await commentInsert(formValues.conent.trim(), props?.recipeId);
        formValues.conent = ''; // 입력 필드 초기화
        emit('refreshComments');
    } catch(e) {
        console.log(e)
    }
} 

// 페이지 전환
const goToPage = (page) => {
  emit('changePage', page);
};


// 댓글 제한 250자 (한글)
const handleContentInput = (e) => commonInputHangle(e, 250, (value) => formValues.conent = value)

</script>

<template>
    <div class="recipe-detail-comment-section">
        <div class="section-title">
            <p>댓글</p>
        </div>

        <div class="comment-wrap">

            <div class="comment-input-filed">
                <v-textarea
                v-model="formValues.conent"
                @input="handleContentInput"
                placeholder="댓글은250자 이내로 작성해주세요."
                rows="4"
                no-resize
                variant="outlined"
                density="compact"
                hide-details=true
                class="comment-text"
                />
                <button class="comment-insert-btn" @click="handleContentInsert">
                    등록
                </button>
            </div>

            <ul
                v-for="(item) in commentsData.content"
                :key="item.id"
                class="seq-item"
            >   
                <li class="comment-item">
                    <div class="comment-info">
                        <div>
                            <img :src="item.profileImage" class="writer-profile-image"/>
                        </div>

                        <span class="writer-nickname">{{ item.nickname }}</span>
                    </div>

                    <div class="content">
                        <span>{{ item.content }}</span>
                    </div>

                    <div class="comment-info-base">
                        <div class="create-date">
                            <span>{{ item.createAt }}</span>
                        </div>
                        
                        <div class="line"></div>

                        <button class="report-btn">
                            신고
                        </button>
                    </div>
                    
                    <div class="reply-wrap">
                        <button class="reply-show-btn">
                            답글
                        </button>
                    </div>
                </li>
            </ul>

            <div class="pagination" v-if="commentsData.totalPages > 1">
                <button
                    v-for="n in commentsData.totalPages"
                    :key="n"
                    :class="{ active: commentsData.number === n - 1 }"
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
        margin-bottom: 0.7em
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
            .comment-info {
                display: flex;
                gap: 5px;

                .writer-profile-image {
                    width: 2rem;
                    
                }

                .writer-nickname {
                    padding-top: 0.3rem;
                    font-size: 0.8rem;
                    font-weight: 600;
                }
            }

            .content{
                padding-left: 0.3rem;
                font-size: 0.83rem;
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

            .reply-wrap{
                margin-top: 0.2rem;
                padding-left: 0.2rem;
                .reply-show-btn {
                    font-size: 0.7rem;
                    padding: 0.1rem 0.5rem;
                    border: 1px solid rgb(224, 224, 224);
                }
            }
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
}
</style>