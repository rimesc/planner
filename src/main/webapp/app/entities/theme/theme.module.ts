import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { PlannerSharedModule } from 'app/shared';
import {
    ThemeComponent,
    ThemeDetailComponent,
    ThemeUpdateComponent,
    ThemeDeletePopupComponent,
    ThemeDeleteDialogComponent,
    themeRoute,
    themePopupRoute
} from './';

const ENTITY_STATES = [...themeRoute, ...themePopupRoute];

@NgModule({
    imports: [PlannerSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ThemeComponent, ThemeDetailComponent, ThemeUpdateComponent, ThemeDeleteDialogComponent, ThemeDeletePopupComponent],
    entryComponents: [ThemeComponent, ThemeUpdateComponent, ThemeDeleteDialogComponent, ThemeDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PlannerThemeModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
