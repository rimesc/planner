import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { PlannerSharedModule } from 'app/shared';
import { ThemeDashboardComponent, themeRoute } from './';
import { PlannerGoalModule } from '../entities/goal/goal.module';

const THEME_ROUTES = [...themeRoute];

@NgModule({
  imports: [PlannerSharedModule, PlannerGoalModule, RouterModule.forChild(THEME_ROUTES)],
  declarations: [ThemeDashboardComponent],
  entryComponents: [ThemeDashboardComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PlannerThemeDashboardModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
