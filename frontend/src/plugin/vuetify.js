import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'
import { createVuetify } from 'vuetify'

// 필요한 컴포넌트만 import
import { VTextField, VTextarea } from 'vuetify/components'

export default createVuetify({
    components: {
        VTextField,
        VTextarea,
    }
})