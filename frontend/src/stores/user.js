import { defineStore } from 'pinia'

export const useUserStore = defineStore("user", {
  state: () => ({
    isLoggedIn: false,
    accessToken: null,
  }),
  getters: {
    getIsLoggedIn: (state) => {
      return state.isLoggedIn;
    },
    
    getAccessToken: (state) => {
      return state.accessToken
    }
  },
  actions: {
    login(accessToken) {
      this.isLoggedIn = true;
      this.accessToken = accessToken;
    },

    logout(){
      this.isLoggedIn = false;
      this.accessToken = null
    },

    setNewAccessToken(newAccessToken) {
      this.accessToken = newAccessToken;
    }
  },
  persist: {
  storage: window.sessionStorage,
  }
});
