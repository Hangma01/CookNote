const checkIdReg = /^[a-z0-9]{5,20}$/;
const checkPwReg = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~!@#$%^&*_\-+=`|\\(){}[\]:;"'<>,.?])\S{10,20}$/;
const checkUsernameReg = /^[a-zA-Z가-힣]{1,20}$/
const checkNicknameReg = /^(?!\s*$).{1,15}$/
const checkEmailReg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;


export const required  = (v) => {
    return !!v || '필수 정보입니다.'
}

export const loginIdRule = (v) => {
    if(!v) {
        return '아이디는 필수 정보입니다.'
    }
    
    if(!checkIdReg.test(v)) {
        return '5~20자의 영문 소문자, 숫자만 사용 가능합니다.'
    }

    return true
}

export const userPwRule = (v) => {
    if(!v) {
        return '비밀번호는 필수 정보입니다.'
    }
    
    if(!checkPwReg.test(v)) {
        return '8~16자의 영문 대문자, 소문자, 숫자, 특수기호를 사용해주세요.'
    }

    return true
}

export const userUsernameRule = (v) => {
    if(!v) {
        return '이름은 필수 정보입니다.'
    }
    
    if(!checkUsernameReg.test(v)) {
        return '한글, 영문 대/소문자를 사용해주세요. (공백 사용 불가)'
    }

    return true
}

export const userNicknameRule = (v) => {
    if(!v) {
        return '닉네임은 필수 정보입니다.'
    }
    
    if(!checkNicknameReg.test(v)) {
        return '1~15자 이내로 작성성해주세요.'
    }

    return true
}

export const userEmailRule = (v) => {
    if(!v) {
        return '이메일은 필수 정보입니다.'
    }
    
    if(!checkEmailReg.test(v)) {
        return '이메일 주소가 정확한지 확인해 주세요.'
    }

    return true
}