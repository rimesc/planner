import { NgModule } from '@angular/core';

import { PlannerSharedLibsModule, FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent } from './';
import { FooterComponent } from '../layouts';

@NgModule({
    imports: [PlannerSharedLibsModule],
    declarations: [FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent, FooterComponent],
    exports: [PlannerSharedLibsModule, FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent, FooterComponent]
})
export class PlannerSharedCommonModule {}
