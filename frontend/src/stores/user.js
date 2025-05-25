import { defineStore } from 'pinia'

export const useUserStore = defineStore("user", {
    state: () => ({
        isLoggedIn: false,
        userId: null,
        accessToken: null,
        profile: null,
    }),
    getters: {
        getIsLoggedIn: (state) => {
            return state.isLoggedIn;
        },

        getUserId: (state) => {
            return state.userId;
        },
        
        getAccessToken: (state) => {
            return state.accessToken
        },

        getProfile: (state) => {
            return state.profile
        }
    },
    actions: {
        login(accessToken, userId) {
            this.isLoggedIn = true;
            this.userId = userId;
            this.accessToken = accessToken;
            this.profile = null;
        },

        logout(){
            this.isLoggedIn = false;
            this.userId = null;
            this.accessToken = null;
            this.profile = null;
        },

        setNewAccessToken(newAccessToken) {
            this.accessToken = newAccessToken;
        },

        setProfile(profile) {
            this.profile = profile;
        }
    },
        persist: {
        storage: window.sessionStorage,
    }
});
