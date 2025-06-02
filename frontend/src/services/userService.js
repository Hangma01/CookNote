import privateAPI from '@/api/privateAPI';
import publicAPI from '@/api/publicAPI';

// 유저 프로파일 (비로그인)
export const getHostProfile = async (hostId) => {
    return await publicAPI.get(`/user/profile/host?hostId=${hostId}`);
};

// 유저 프로파일 (게스트)
export const getHostProfileLoggedIn = async (hostId) => {
    return await privateAPI.get(`/user/profile/host?hostId=${hostId}`);
};

// 유저 프로파일 (본인)
export const getUserProfile = async () => {
    return await privateAPI.get(`/user/profile`);
};

// 유저 팔로우 정보 가져오기
export const getUserFollow = async () => {
    return await privateAPI.get(`/user/follow`);
};

// 유저 팔로우 하기
export const userAddFollow = async (followId) => {
    return await privateAPI.post(`/user/follow`, followId, {
        headers: {
            'Content-Type': 'application/json',
        },
    });
};

// 유저 팔로우 취소
export const userCancleFollow = async (followId) => {
    return await privateAPI.delete(`/user/follow`, { params: { followId } });
};

// 프로필 수정
export const userProfileUpdate = async (newNickname, newProfileImage) => {
    return await privateAPI.patch(`/user/edit`, { newNickname, newProfileImage });
};

// 프로필 수정 정보
export const getUserProfileEditInfo = async () => {
    return await privateAPI.get(`/user/edit`);
};

// 비밀번호 변경
export const userPwEdit = async (formValues) => {
    return await privateAPI.patch(`/user/edit/pw`, formValues);
};

// 회원탈퇴
export const userDelete = async () => {
    return await privateAPI.delete(`/user`);
};

// 유저가 팔로잉한 유저 리스트 최신순(레시피작성)
export const getFollowingLatestForRecipe = async () => {
    return await privateAPI.get(`/user/following/latest`);
};

// 신고하기
export const userReport = async (reportType, recipeId, commentId, selectedReasonId, reportedId) => {
    return await privateAPI.post(
        `/user/report`,
        { reportType: reportType, recipeId: recipeId, commentId: commentId, reportedId: reportedId, categoryReportReasonId: selectedReasonId },
        {
            headers: {
                Accept: 'application/json', // 응답 형식으로 JSON을 명시적으로 요청
            },
        }
    );
};

// 신고 중복 체크
export const userReportDuplicationCheck = async (reportType, recipeId, commentId) => {
    return await privateAPI.post(
        `/user/report/duplication`,
        { reportType: reportType, recipeId: recipeId, commentId: commentId },
        {
            headers: {
                Accept: 'application/json', // 응답 형식으로 JSON을 명시적으로 요청
            },
        }
    );
};

// 신고 내역 가져오기
export const getReports = async (page) => {
    return await privateAPI.get(`user/report`, {
        params: {
            page: page,
        },
    });
};

// 제재 내역 가져오기
export const getSanction = async (page) => {
    return await privateAPI.get(`user/report/sanction`, {
        params: {
            page: page,
        },
    });
};

// 쉐프 검색
export const getChefSearch = async (keyword, page) => {
    return await publicAPI.get('/user/search/chef', {
        params: {
            keyword: keyword || null,
            page: page,
        },
    });
};

// 쉐프 검색 - 로그인 시
export const getChefSearchLoggined = async (keyword, page) => {
    return await privateAPI.get('/user/search/chef', {
        params: {
            keyword: keyword || null,
            page: page,
        },
    });
};
