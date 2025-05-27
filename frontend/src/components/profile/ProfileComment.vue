<script setup>
import { commentDelete, getCommentUser } from '@/services/commentService';
import { onBeforeUnmount } from 'vue';
import { onMounted, ref } from 'vue';
import { reactive } from 'vue';
import { errorMessages } from 'vue/compiler-sfc';
import ProfileRecipeCard from './ProfileRecipeCard.vue';


const activeCommentMenuMap = reactive({});           // 게시글 메뉴 활성화
const commentItemRefs = ref({});                     // 외부 클릭 시 게시글 메뉴 닫히기

// 레시피 데이터
const commentData = ref(null);

// 메뉴 토글
const toggleCommentMenu = (event, commentId) => {
    event.stopPropagation();
    activeCommentMenuMap[commentId] = !activeCommentMenuMap[commentId];
    closeCommentMenu(event)
};

// 외부 클릭 시 메뉴 닫기
const closeCommentMenu = (event) => {
  for (const commentId in activeCommentMenuMap) {
    const refElement = commentItemRefs.value[commentId];
    
    if (activeCommentMenuMap[commentId] && refElement && !refElement.contains(event.target)) {
      activeCommentMenuMap[commentId] = false;
    }
  }
};

const setCommentMenuRef = (el, commentId) => {
  if (el) {
    commentItemRefs.value[commentId] = el;
  } else {
    delete commentItemRefs.value[commentId];
  }
};

// 댓글 삭제하기
const handleCommentDelete =  async (commentId) => {
    try {
        await commentDelete(commentId);
        loadCommentUser();
    } catch (e) {
        alert('댓글을 삭제하지 못했습니다.')
    }

}

// 데이터 가져오기
const loadCommentUser = async (page = 0) => {
    try {
        const res = await getCommentUser(page); // page 파라미터 넘김
        commentData.value = res.data

    } catch (e) {        
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message) 
        } else {
            alert(errorMessages.BADREQUEST)
        }

        router.push({ name : 'mainPage'})
    }
};

const goToPage = (page) => {
    loadCommentUser(page)
};

onMounted(async () => {
    try {
        loadCommentUser();
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message) 
        } else {
            alert(errorMessages.BADREQUEST)
        }

        window.location.reload()
    }
    
    document.addEventListener('click', closeCommentMenu);
})

onBeforeUnmount(() => {
    document.removeEventListener('click', closeCommentMenu);
});
</script>

<template>
    <div class="like-title">
        <p>댓글 수 {{ commentData?.page?.totalElements }}</p>
    </div>
    <div class="line"></div>

    <div>
        <ul>
            <li 
                v-for="(item) in commentData?.content"
                :key="item.commentId"
            >
                <div class="recipe-content">
                    <ProfileRecipeCard :recipeData = "item" />

                    <div class="menu-wrap">
                        <div class="menu-icon" @click="toggleCommentMenu($event, item.commentId)">
                            <font-awesome-icon :icon="['fas', 'ellipsis-vertical']" />
                        </div>
                    

                        <div class="menu"
                            v-if="activeCommentMenuMap[item.commentId]" 
                            :ref="el => setCommentMenuRef(el, item.commentId)">
                            <div>
                                <button @click="handleCommentDelete(item.commentId)">
                                    삭제
                                </button>
                            </div>
                        </div>
                    </div>

                </div>
                
                <div class="comment-content">
                    <div class="content">
                        <p>{{ item.privateAdmin ? '관리자에 의해 삭제되었습니다.' : item.commentContent }}</p>
                    </div>

                    <div class="content-date">
                        <span>{{ item.commentCreateAt }}</span>
                    </div>
                </div>
            </li>
        </ul>



        <div class="pagination" v-if="commentData?.page?.totalPages > 1">
            <button
                v-for="n in commentData?.page?.totalPages"
                :key="n"
                :class="{ active: commentData?.page?.number === n - 1 }"
                @click="goToPage(n - 1)"
                >
                {{ n }}
            </button>
        </div>
    </div>
</template>


<style lang="scss" scoped>
.like-title {
    margin-top: 2rem;
    font-weight: 500;
    margin-bottom: 1rem;
    padding-left: 0.2rem;
}

.line {
    border-bottom: 2px solid;
    color: #c09370;
}



.recipe-content {
    display: flex;
    justify-content: space-between;
    margin-top: 2rem;
    padding-bottom: 2rem;


    .menu-wrap {
        color: rgb(124, 124, 124);
        position: relative;

        .menu-icon {
            cursor: pointer;
            width: 1.5rem;
            text-align: center;
        }

        .menu {
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

.comment-content {
    border-bottom: 1px solid rgb(224, 224, 224);  
    padding-bottom: 2rem;  

    .content {
        font-size: 0.9rem;
        white-space: pre-wrap; 
    }

    .content-date {
        margin-top: 2rem;
        font-size: 0.8rem;
        color: #999999;
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
</style>