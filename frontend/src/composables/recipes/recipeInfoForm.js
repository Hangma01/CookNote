import { commonValues } from '@/utils/commonValues'
import { ref } from 'vue'

export const recipeInfoForm = () => {
  const title = ref('')         // 제목
  const description = ref('')   // 요리 소개
  const videoId = ref(null)     // 동영상 아이디
  const serving = ref(null)     // 인원수
  const duration = ref(null)    // 요리시간
  const level = ref(null)       // 난이도
  const categories = ref({      // 요리 종류, 목적 종류
    cuisine: null,
    purpose: null
  })

  const imageFile = ref(null);
  const url = ref('')

  const validation = () => {
    if (!title.value) return '제목을 입력하세요'
    else if (!description.value) return '요리소개를 입력하세요'
    else if (url.value && !videoId.value) return '유효한 유튜브 동영상 URL을 입력하세요.'
    else if (!categories.value.cuisine) return '카테고리 종류를 선택하세요'
    else if (!categories.value.purpose) return '카테고리 목적을 선택하세요'
    else if (!serving.value) return '인원수를 선택하세요'
    else if (!duration.value) return '요리시간을 선택하세요'
    else if (!level.value) return '난이도를 선택하세요'
    else return true
  }

  const getData = () => ({
    title: title.value,
    description: description.value,
    thumbnail: imageFile.value,
    videoId: videoId.value,
    serving: serving.value?.no,
    duration: duration.value?.no,
    level: level.value?.no,
        categories: {
        cuisine: categories.value.cuisine?.no ?? null,
        purpose: categories.value.purpose?.no ?? null
    },
  })

  const getYoutubeVideoId = () => {
  const regex = commonValues.YOUTUBE_VIDEO_ID_REGEX;
  const match = url.value.match(regex)
  return match ? match[1] : null
  }

  const handleGetVideoId = () => {
    videoId.value = getYoutubeVideoId()
  }

  return {
    title,
    description,
    videoId,
    serving,
    duration,
    level,
    categories,
    imageFile,
    url,
    getData,
    handleGetVideoId,
    validation,
  }
}
