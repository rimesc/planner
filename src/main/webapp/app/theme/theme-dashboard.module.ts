import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PlannerSharedModule } from 'app/shared/shared.module';
import { ThemeDashboardComponent } from './theme-dashboard.component';
import { PlannerGoalModule } from '../entities/goal/goal.module';
import { themeRoute } from './theme-dashboard.route';

@NgModule({
  imports: [PlannerSharedModule, PlannerGoalModule, RouterModule.forChild(themeRoute)],
  declarations: [ThemeDashboardComponent],
  entryComponents: [ThemeDashboardComponent]
})
export class PlannerThemeDashboardModule {}
