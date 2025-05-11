const checkUserIdReg = /^[a-z0-9]{5,20}$/;
const checkPasswordReg = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~!@#$%^&*_\-+=`|\\(){}[\]:;"'<>,.?])\S{8,16}$/;
const checkNameReg = /^[a-zA-Z가-힣]{1,20}$/
const checkNicknameReg = /^(?!\s)(.{1,15})(?<!\s)$/
const checkEmailReg = /^[^\s][a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{1,}[^\s]$/;

export const required = (v) => {
    return !!v || '필수 입력 정보입니다.'
}

export const findNameRule = (v) => {
    return !!v || '이름은 필수 입력 정보입니다.'
}

export const findUserIdRule = (v) => {
    return !!v || '아이디는 필수 입력 정보입니다.'
}

export const userIdRule = (v) => {
    if (!v) {
        return '아이디는 필수 정보입니다.'
    }

    if (!checkUserIdReg.test(v)) {
        return '5~20자의 영문 소문자, 숫자만 사용 가능합니다.'
    }

    return true
}

export const passwordRule = (v) => {
    if (!v) {
        return '비밀번호는 필수 정보입니다.'
    }

    if (!checkPasswordReg.test(v)) {
        return '8~16자의 영문 대문자, 소문자, 숫자, 특수기호를 사용해주세요.'
    }

    return true
}

export const nameRule = (v) => {
    if (!v) {
        return '이름은 필수 정보입니다.'
    }

    if (!checkNameReg.test(v)) {
        return '한글, 영문 대/소문자를 사용해주세요. (공백 사용 불가)'
    }

    return true
}

export const nicknameRule = (v) => {
    if (!v) {
        return '닉네임은 필수 정보입니다.'
    }

    if (!checkNicknameReg.test(v)) {
        return '1~15자 이내로 작성해주세요. (앞 뒤 공백 불가)'
    }

    return true
}

export const emailRule = (v) => {
    if (!v) {
        return '이메일은 필수 정보입니다.'
    }

    if (!checkEmailReg.test(v)) {
        return '이메일 주소가 정확한지 확인해 주세요.'
    }

    return true
}