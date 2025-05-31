import privateAPI from '@/api/privateAPI';
import publicAPI from '@/api/publicAPI';

// 댓글 목록 가져오기
export const getComments = async (recipeId, page) => {
    return await publicAPI.get(`/comment?recipeId=${recipeId}&page=${page}`);
};

// 댓글 저장
export const commentInsert = async (content, recipeId, parentCommentId = null) => {
    return await privateAPI.post(`/comment`, {
        content: content,
        recipeId: recipeId,
        parentCommentId: parentCommentId,
    });
};

// 댓글 수정
export const commentUpdate = async (content, commentId) => {
    return await privateAPI.patch(`/comment`, {
        commentId: commentId,
        content: content,
    });
};

// 댓글 삭제
export const commentDelete = async (commentId) => {
    return await privateAPI.delete(`/comment`, { params: { commentId } });
};

// 리플 목록 가져오기
export const getCommentReplies = async (parentCommentId, page, size) => {
    return await publicAPI.get(`/comment/replies?parentCommentId=${parentCommentId}&page=${page}&size=${size}`);
};

// 유저가 작성한 댓글 가져오기
export const getCommentUser = async () => {
    return await privateAPI.get(`/comment/user`);
};

// 리플 총 갯수 가져요기
export const getRepliesCount = async (recipeId) => {
    return await publicAPI.get(`comment/replies/count?recipeId=${recipeId}`);
};
