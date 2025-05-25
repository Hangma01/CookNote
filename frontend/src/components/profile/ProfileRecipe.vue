<script setup>
import { computed, onBeforeUnmount, onMounted, reactive, ref, watch } from 'vue';
import ProfileRecipeCard from './ProfileRecipeCard.vue';
import { errorMessages } from '@/utils/messages/errorMessages';
import { useRouter } from 'vue-router';
import { deleteRecipe, getRecipePrivate, getRecipePublic } from '@/services/recipeService';
import { useUserStore } from '@/stores/user';
import { getUserProfile } from '@/services/userService';

// 화면 전환
const router = useRouter()

// 유저 스토어
const userStore = useUserStore();
const userProfile = computed(() => userStore.getProfile);

const activeTab = ref('PUBLIC')

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

// 공개 레시피 가져오기
const loadRecipPublic = async (page = 0) => {
  try {
    const res = await getRecipePublic(page); // page 파라미터 넘김

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

// 비공개 레시피 가져오기
const loadRecipPrivate = async (page = 0) => {
  try {
    const res = await getRecipePrivate(page); // page 파라미터 넘김
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


// 레시피 삭제
const handleRecipeDelete = async(recipeId) => {
    const proceed = confirm("레시피를 정말 삭제하시겠습니까?");
    if (proceed) {
        try {
            await deleteRecipe(recipeId)
        
            const res = await getUserProfile()

            userStore.setProfile({
                              ...res.data
                            , isHostProfile: false
            })

            if (activeTab.value === 'PUBLIC') {
                loadRecipPublic();
            } else if (activeTab.value === 'PRIVATE') {
                loadRecipPrivate();
            }
             

        } catch (e) {
            if (e.response && e.response?.data?.message) {
                alert(e.response.data.message)  
            } else {
                alert(errorMessages.BADREQUEST)
            }
            router.push({ name : 'mainPage'})
        }
    }
}

const goToPage = (page) => {
    if (activeTab.value === 'PUBLIC') {
        loadRecipPublic(page);
    } else if (activeTab.value === 'PRIVATE') {
        loadRecipPrivate(page);
    }
};

onMounted(async () => {
    try {
        if (activeTab.value === 'PUBLIC') {
            loadRecipPublic();
        } else if (activeTab.value === 'PRIVATE') {
            loadRecipPrivate();
        } 
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

watch(activeTab, (newTab) => {
  if (newTab === 'PUBLIC') {
    loadRecipPublic()
  } else if (newTab === 'PRIVATE') {
    loadRecipPrivate() 
  }
})
</script>

<template>
    <ul class="nav">
        <li 
            :class="['nav-item', { selected: activeTab === 'PUBLIC'}]"
            @click="activeTab = 'PUBLIC'"
        >
            공개({{ userProfile?.recipePublicCount || 0 }})
        </li>

        <li
            :class="['nav-item', { selected: activeTab === 'PRIVATE'}]"
            @click="activeTab = 'PRIVATE'"
        >
            비공개({{ userProfile?.recipePrivateCount || 0 }})
        </li>
    </ul>

    <div>
        <ul>
            <li 
                class="recipe-content"
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
                        <div v-if="item.privateAdmin !== true" >
                            <router-link :to="{name : 'recipeEdit', params: { recipeId: item.recipeId } }">
                                수정
                            </router-link>
                        </div>

                        <div>
                            <button @click="handleRecipeDelete(item.recipeId)">
                                삭제
                            </button>
                        </div>
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
</template>


<style lang="scss" scoped>
.nav {
    display: flex;
    border-bottom: 1px solid #ccc;
    margin-top: 2rem;

    .nav-item {
        padding: 0.7rem 2rem;
        cursor: pointer;
        position: relative;
        margin-bottom: -1px; // 하단 border 없애기 위해 올려줌
;
    }

    .nav-item.selected {
        background-color: #fff;
        border: 1px solid #ccc;
        border-bottom: none;
        border-radius: 0.5rem 0.5rem 0 0;
    }
}

.recipe-content {
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
</style>