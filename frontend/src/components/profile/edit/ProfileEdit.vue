
<script setup>
import ImageUploader from '@/components/ui/ImageUploader.vue';
import { existsNickname } from '@/services/authService';
import { getUserProfile, getUserProfileEditInfo, userProfileUpdate } from '@/services/userService';
import { useUserStore } from '@/stores/user';
import { commonCheckDuplicate, commonInputHangle } from '@/utils/commonFunction';
import { errorMessages } from '@/utils/messages/errorMessages';
import { successMessage } from '@/utils/messages/successMessage';
import { nicknameRule } from '@/utils/rules';
import { onMounted, ref, watch } from 'vue';


// 유저 스토어
const userStore = useUserStore();


const imageFile = ref(null)
const originalProfileImage = ref(null)
const originalNickname = ref(null)
const userEditInfo = ref(null);
const errorMsgNicknameDuplicate = ref('')     // 닉네임 중복 시 에러 메시지
const isSuccessNicknameCheck = ref(false)     // 닉네임 인증 성공 메시지
const nickname = ref('')                        // 닉네임 입력
const ruleNicknameRef = ref(null)             // 닉네임 유효성 검사

// 닉네임 15자 제한 (한글)
const handleNicknameInput = (e) => commonInputHangle(e, 15, (value) => nickname.value = value)

// 닉네임 중복 체크
const handleExistsNickname = async () => {
    await commonCheckDuplicate({
        value: nickname.value,             
        validatorRef: ruleNicknameRef,          
        errorMsgRef: errorMsgNicknameDuplicate, 
        successMsgRef: isSuccessNicknameCheck,
        apiCall: existsNickname,
    });
};

// 프로필 수정 정보 가져오기
const loadUserInfo = async () => {
    const res = await getUserProfileEditInfo()
    userEditInfo.value = res.data
    nickname.value = userEditInfo.value.nickname
    imageFile.value = userEditInfo.value.profileImage
    originalProfileImage.value = userEditInfo.value.profileImage
    originalNickname.value = userEditInfo.value.nickname
}

// 프로필 수정 내용 저장
const handleSave = async () => {
    if(originalProfileImage.value === imageFile.value && originalNickname.value === nickname.value) {
        alert(errorMessages.USER_EDIT_REQUIRED_ERROR_MESSAGE)
    } else if(originalNickname.value !== nickname.value && !isSuccessNicknameCheck.value ) {
        alert(errorMessages.NICKNAME_DUPLICATE_CHECK_ERROR_MESSAGE)
    } else {
        const isFormVal =  await ruleNicknameRef.value.validate();
        try {
            if(isFormVal){
                await userProfileUpdate(nickname.value
                                      , imageFile.value);
                
                const res = await getUserProfile()
                alert(successMessage.USER_PROFILE_UPDATE_SUCCESS_MESSAGE)
                window.location.reload()
            }
        } catch (e) {
            console.log(e)
            if (e.response && e.response?.data?.message) {
                alert(e.response.data.message) 
            } else {
                alert(errorMessages.BADREQUEST)
            }
        }
    }
}

onMounted(() => {
    loadUserInfo();
})

watch(nickname, () => {
  isSuccessNicknameCheck.value = false;
});

</script>

<template>
    <div class="profile-edit-container">

        <div class="form-group image-box">
            <ImageUploader v-model="imageFile"/>
            <div class="camera-box">
                <font-awesome-icon :icon="['fas', 'camera']" class="camera-icon"/>
            </div>
        </div>

        <div class="form-group">
            <label>이름</label>
            <div class="readonly-field">
                <span>{{ userEditInfo?.name }}</span>
            </div>
        </div>

        <div class="form-group">
            <label>이메일</label>
            <div class="readonly-field">
                <span>{{ userEditInfo?.email }}</span>
            </div>
        </div>

        <div class="form-group">
            <label>닉네임</label>
            <div class="nickname">
                <v-text-field
                    v-model="nickname"
                    @input="handleNicknameInput"
                    type="text"
                    variant="outlined"
                    density="compact"
                    hide-details="auto"
                    ref="ruleNicknameRef"
                    :rules="[nicknameRule]"
                    :error-messages="errorMsgNicknameDuplicate"
                />

                <v-btn class="nickname-check-btn" @click="handleExistsNickname()">
                    중복확인
                </v-btn>
            </div>
            <div v-if="isSuccessNicknameCheck" class="success-message">
                <span>
                    사용 가능한 닉네임입니다.
                </span>
            </div>
        </div>

        <div class="form-group">
            <label>가입일</label>
            <div class="readonly-field">
                <span>{{ userEditInfo?.createAt }}</span>
            </div>
        </div>

        <div class="form-group">
            <label>최근 수정일</label>
            <div class="readonly-field">
                {{ userEditInfo?.updateAt }}
            </div>
        </div>
  </div>
    
  <div class="button-wrap">
    <button class="save-btn" @click="handleSave">수정</button>
  </div>
</template>


<style lang="scss" scoped>
.profile-edit-container {
    margin-top: 2rem;
    height: 47rem;
    border: 1px solid rgb(200,200,200);
    border-radius: 0.5rem;
    display: flex;
    padding: 1rem 2rem;
    flex-direction: column;
    gap: 1.5rem;

    .form-group {
        display: flex;
        flex-direction: column;

        label {
            font-weight: bold;
            margin-bottom: 0.4rem;
        }

        .nickname {
            display: flex;
            gap: 1rem
        }

        .success-message {
            padding-left: 1rem;
            padding-top: 0.3rem;
            font-size: 0.75rem;
            color: green;
        }

        .readonly-field {
            padding: 0.6rem 1rem;
            background-color: #f4f4f4;
            border-radius: 0.3rem;
            border: 1px solid #ddd;
        }

        .editable-field {
            padding: 0.6rem 1rem;
            border-radius: 0.3rem;
            border: 1px solid #ccc;
            font-size: 1rem;
        }
    }

    .image-box {
        align-self: center;
        width: 18rem;
        position: relative;
        
        .camera-box {
            position: absolute;
            width: 2.5rem;
            height: 2.5rem;
            border-radius: 100%;
            background-color: black;
            display: flex;
            align-items: center;
            justify-content: center;
            right: -1rem;
            top: -0.5rem;

            .camera-icon {
                font-size: 1.5rem;
                color: white;
            }
        }

    }
}

.button-wrap {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1rem;

  .save-btn {
    padding: 0.6rem 1.2rem;
    font-size: 1rem;
    border-radius: 0.3rem;
    cursor: pointer;
    border: none;
    background-color: #007bff;
    color: white;
  }
}
</style>