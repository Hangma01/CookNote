import { HttpStatusCode } from "axios";
import { verifyMailAuthCode } from '@/services/mailService';

// 중복 체크 함수
export const commonCheckDuplicate = async ({ value, validatorRef, errorMsgRef, apiCall, router }) => {
  const isValid = await validatorRef.value?.validate();

  if (!value || isValid?.[0]) {
    errorMsgRef.value = '';
    return;
  }

  try {
    const res = await apiCall(value);
    errorMsgRef.value = ''
  } catch (e) {
    console.log(e)
    if (e.response.data && e.response.data.status === HttpStatusCode.Conflict && e.response.data.message) {
      errorMsgRef.value = e.response.data.message;
    } else {
      alert('서버와의 통신이 원활하지 않습니다. 잠시 후 다시 시도해주세요.');
      router.replace('/login');
    }
  }
};

// 메일 인증 코드 검증
export const commonVerifyMailAuthCode = async (
  email, authCodeValue, isAuthCodeRequest, setResultState
) => {
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
    authCodeValue.value = ''
    setResultState(false, '');
  }
};

// 한글 입력값 길이 제한 처리 함수
export const commonInputHangle = (e, maxLength, setValue) => {
  const value = e.target.value;
  setValue(value.length > maxLength ? value.slice(0, maxLength) : value);
};

export const generateId = () => crypto.randomUUID();