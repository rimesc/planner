import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { PlannerSharedModule } from 'app/shared';
import {
    GoalComponent,
    GoalDetailComponent,
    GoalUpdateComponent,
    GoalDeletePopupComponent,
    GoalDeleteDialogComponent,
    goalRoute,
    goalPopupRoute
} from './';

const ENTITY_STATES = [...goalRoute, ...goalPopupRoute];

@NgModule({
    imports: [PlannerSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [GoalComponent, GoalDetailComponent, GoalUpdateComponent, GoalDeleteDialogComponent, GoalDeletePopupComponent],
    entryComponents: [GoalComponent, GoalUpdateComponent, GoalDeleteDialogComponent, GoalDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PlannerGoalModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
