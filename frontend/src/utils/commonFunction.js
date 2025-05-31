import { HttpStatusCode } from 'axios';
import { verifyMailAuthCode } from '@/services/mailService';
import { getUserProfile } from '@/services/userService';
import { useUserStore } from '@/stores/user';
import { errorMessages } from './messages/errorMessages';

const userStore = useUserStore();

// 중복 체크 함수
export const commonCheckDuplicate = async ({ value, validatorRef, errorMsgRef, successMsgRef, apiCall }) => {
    const isValid = await validatorRef.value?.validate();

    if (!value || isValid?.[0]) {
        errorMsgRef.value = '';
        successMsgRef.value = false;
        return;
    }

    try {
        const res = await apiCall(value);
        errorMsgRef.value = '';
        successMsgRef.value = true;
    } catch (e) {
        if (e.response.data && e.response.data.status === HttpStatusCode.BadRequest && e.response.data.message) {
            errorMsgRef.value = e.response.data.message;
            successMsgRef.value = false;
        } else {
            alert(errorMessages.BADREQUEST);
            window.location.reload();
        }
    }
};

// 메일 인증 코드 검증
export const commonVerifyMailAuthCode = async (email, authCodeValue, isAuthCodeRequest, setResultState) => {
    try {
        const res = await verifyMailAuthCode(email, authCodeValue.value);

        if (res.data.result) {
            setResultState(res.data.result, '');
        } else {
            setResultState(res.data.result, res?.data.message);
        }
    } catch (e) {
        if (e.response && e.response.data.status === HttpStatusCode.Gone) {
            alert(e.response.data.message);
        } else {
            alert(errorMessages.badRequest);
        }

        isAuthCodeRequest.value = false;
        authCodeValue.value = '';
        setResultState(false, '');
    }
};

// 한글 입력값 길이 제한 처리 함수
export const commonInputHangle = (e, maxLength, setValue) => {
    const value = e.target.value;
    setValue(value.length > maxLength ? value.slice(0, maxLength) : value);
};

// 로그인 체크
export const loginCheck = (router) => {
    const proceed = confirm('로그인이 필요한 서비스입니다. 로그인 페이지로 이동할까요?');
    if (proceed) {
        router.push({ name: 'login' });
    }
};

// 랜덤 UUID 추출출
export const generateId = () => crypto.randomUUID();

// 프로필 로드
export const loadProfile = async (isHostProfileStatus) => {
    const res = await getUserProfile();
    console.log(res);
    userStore.setProfile({
        ...res.data,
        isHostProfile: isHostProfileStatus,
    });
};
