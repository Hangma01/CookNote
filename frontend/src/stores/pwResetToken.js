import { defineStore } from 'pinia'

export const usePwResetTokenStore = defineStore("pwResetToken", {
  state: () => ({
    pwResetToken: null,
  }),
  getters: {
    getPwResetToken: (state) => {
      return state.pwResetToken
    },
  },
  actions: {
    setPwResetToken(pwResetToken){
      this.pwResetToken = pwResetToken
    },

    deletePwResetToken() {
      this.pwResetToken = null
    }
  },
  persist: {
  storage: window.sessionStorage,
  }
});