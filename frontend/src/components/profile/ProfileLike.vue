<script setup>
import { onBeforeUnmount, onMounted, reactive, ref } from 'vue';
import { getRecipeLike, recipeLikeDelete } from '@/services/recipeService';
import { errorMessages } from '@/utils/messages/errorMessages';
import ProfileRecipeCard from './ProfileRecipeCard.vue';


const activeRecipeMenuMap = reactive({});           // 게시글 메뉴 활성화
const recipeItemRefs = ref({});                     // 외부 클릭 시 게시글 메뉴 닫히기

// 레시피 데이터
const recipeData = ref(null);

// 메뉴 토글
const toggleRecipeMenu = (event, recipeId) => {
    event.stopPropagation();
    activeRecipeMenuMap[recipeId] = !activeRecipeMenuMap[recipeId];
    closeRecipeMenu(event)
};

// 외부 클릭 시 메뉴 닫기
const closeRecipeMenu = (event) => {
  for (const recipeId in activeRecipeMenuMap) {
    const refElement = recipeItemRefs.value[recipeId];
    
    if (activeRecipeMenuMap[recipeId] && refElement && !refElement.contains(event.target)) {
      activeRecipeMenuMap[recipeId] = false;
    }
  }
};

const setRecipeMenuRef = (el, recipeId) => {
  if (el) {
    recipeItemRefs.value[recipeId] = el;
  } else {
    delete recipeItemRefs.value[recipeId];
  }
};

// 데이터 가져오기
const loadRecipLike = async (page = 0) => {
    try {
        const res = await getRecipeLike(page); // page 파라미터 넘김

        recipeData.value = res.data

    } catch (e) {        
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message) 
        } else {
            alert(errorMessages.BADREQUEST)
        }

        router.push({ name : 'mainPage'})
    }
};


// 좋아요 삭제하기
const handleLikeDelete = async (recipeId) => {

    const proceed = confirm("좋아요를 정말 삭제하시겠습니까?");

    if(proceed) {
        try {
            await recipeLikeDelete(recipeId);
            loadRecipLike();
        } catch (e) {
            alert('좋아요를 삭제하지 못했습니다.')
        }
    } else {
        activeRecipeMenuMap[recipeId] = !activeRecipeMenuMap[recipeId];
    }
}

const goToPage = (page) => {
    loadRecipLike(page)
};

onMounted(async () => {
    try {
        loadRecipLike();
    } catch (e) {
        if (e.response && e.response?.data?.message) {
            alert(e.response.data.message) 
        } else {
            alert(errorMessages.BADREQUEST)
        }

        router.push({ name : 'mainPage'})
    }
    
    document.addEventListener('click', closeRecipeMenu);
})

onBeforeUnmount(() => {
    document.removeEventListener('click', closeRecipeMenu);
});

</script>

<template>
    <div class="like-title">
        <p>좋아요 수 {{ recipeData?.page?.totalElements }}</p>
    </div>
    <div class="line"></div>

    <div v-if="recipeData?.content.length > 0">
        <ul>
            <li 
                class="recipe-like-content"
                v-for="(item) in recipeData?.content"
                :key="item.recipeId"
            >
                <ProfileRecipeCard :recipeData = "item" />

                <div class="menu-wrap">
                <div class="menu-icon" @click="toggleRecipeMenu($event, item.recipeId)">
                    <font-awesome-icon :icon="['fas', 'ellipsis-vertical']" />
                </div>
                

                <div class="menu"
                        v-if="activeRecipeMenuMap[item.recipeId]" 
                        :ref="el => setRecipeMenuRef(el, item.recipeId)">
                        <button @click="handleLikeDelete(item.recipeId)" class="btn">
                            삭제
                        </button>
                    </div>
                </div>
            </li>
        </ul>

        <div class="pagination" v-if="recipeData?.page?.totalPages > 1">
            <button
                v-for="n in recipeData?.page?.totalPages"
                :key="n"
                :class="{ active: recipeData?.page?.number === n - 1 }"
                @click="goToPage(n - 1)"
                >
                {{ n }}
            </button>
        </div>
    </div>

    <div v-else class="non-bookmark">
        <p>좋아요한 레시피가 없습니다.</p>
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



.recipe-like-content {
    display: flex;
    justify-content: space-between;
    margin-top: 2rem;
    padding-bottom: 2rem;
    border-bottom: 1px solid rgb(224, 224, 224);    



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

            .btn {
                width: 100%;
            }
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

.non-bookmark {
    height: 30rem;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    font-size: 1.5rem;
    color: #333333;
}
</style>