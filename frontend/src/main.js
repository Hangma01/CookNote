import { createApp } from 'vue'
import { createPinia } from 'pinia'

// Vutify
import vuetify from './plugin/vuetify'

// fontawesome
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

// fontawesome - 필요한 아이콘 라이브러리를 추가
import { faPenToSquare, faMagnifyingGlass, faLock } from '@fortawesome/free-solid-svg-icons';
import { faUser, faEnvelope } from '@fortawesome/free-regular-svg-icons';

// fontawesome - 사용할 아이콘을 라이브러리에 추가
library.add(
    faPenToSquare, faMagnifyingGlass, faLock,
    faUser, faEnvelope
);

import App from './App.vue'
import router from './router'

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(vuetify)
//  - Vue 컴포넌트로 FontAwesomeIcon을 전역에 등록록
app.component('font-awesome-icon', FontAwesomeIcon);
app.mount('#app')
