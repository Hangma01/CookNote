import { defineStore } from 'pinia'

export const useUserStore = defineStore("user", {
  state: () => ({
    isLoggedIn: false,
  }),
  getters: {
    getIsLoggedIn: (state) => {
      return state.isLoggedIn;
    },
  },
  actions: {
    login() {
      this.isLoggedIn = true;
    },

    logout(){
      this.isLoggedIn = false;
    },
  },
  persist: {
  storage: window.sessionStorage,
  }
});
