import { HttpStatusCode } from "axios";


// 중복 체크 함수
export const checkDuplicate = async ({ value, validatorRef, errorMsgRef, apiCall, router }) => {
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
        if(e.response.data && e.response.data.status === HttpStatusCode.Conflict && e.response.data.message) {
            errorMsgRef.value = e.response.data.message;
        } else {
            alert('서버와의 통신이 원활하지 않습니다. 잠시 후 다시 시도해주세요.');       
            router.replace('/login');
        }
    }
};


// 한글 입력값 길이 제한 처리 함수
export const handleInputHangle = (e, maxLength, setValue) => {
    const value = e.target.value;
    setValue(value.length > maxLength ? value.slice(0, maxLength) : value);
};

