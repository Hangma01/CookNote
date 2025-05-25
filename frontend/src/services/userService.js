import privateAPI from '@/api/privateAPI'
import publicAPI from '@/api/publicAPI'

// 유저 프로파일 (비로그인)
export const getHostProfile = async (hostId) => {
    return await publicAPI.get(`/user/profile/host?hostId=${hostId}`)
}

// 유저 프로파일 (게스트)
export const getHostProfileLoggedIn = async (hostId) => {
    return await privateAPI.get(`/user/profile/host?hostId=${hostId}`)
}

// 유저 프로파일 (본인)
export const getUserProfile = async () => {
    return await privateAPI.get(`/user/profile`)
}

// 유저 팔로우 정보 가져오기
export const getUserFollow = async () => {
    return await privateAPI.get(`/user/follow`)
}

// 유저 팔로우 하기
export const userFollow = async (followId) => {
    return await privateAPI.post(`/user/follow`, followId, {
  headers: {
    'Content-Type': 'application/json',
  }})
}

// 유저 팔로우 취소
export const userCancleFollow = async (followId) => {
    return await privateAPI.delete(`/user/follow`, { params: { followId } })
}
