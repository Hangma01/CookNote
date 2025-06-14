import '@mdi/font/css/materialdesignicons.css';
import 'vuetify/styles';
import { createVuetify } from 'vuetify';

// 필요한 컴포넌트만 import
import {
    VBtn,
    VCard,
    VCardActions,
    VCardText,
    VDialog,
    VDivider,
    VForm,
    VRadio,
    VRadioGroup,
    VSelect,
    VSnackbar,
    VSpacer,
    VTextField,
    VTextarea,
    VOverlay,
    VProgressCircular,
    VCardTitle,
} from 'vuetify/components';

export default createVuetify({
    components: {
        VTextField,
        VTextarea,
        VBtn,
        VForm,
        VRadioGroup,
        VRadio,
        VSelect,
        VDialog,
        VCard,
        VCardText,
        VCardActions,
        VSpacer,
        VDivider,
        VSnackbar,
        VOverlay,
        VProgressCircular,
        VCardTitle,
    },
});
