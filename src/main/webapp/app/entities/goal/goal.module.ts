import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PlannerSharedModule } from 'app/shared/shared.module';
import { GoalComponent } from './goal.component';
import { GoalDetailComponent } from './goal-detail.component';
import { GoalUpdateComponent } from './goal-update.component';
import { GoalDeleteDialogComponent } from './goal-delete-dialog.component';
import { goalRoute } from './goal.route';

@NgModule({
  imports: [PlannerSharedModule, RouterModule.forChild(goalRoute)],
  declarations: [GoalComponent, GoalDetailComponent, GoalUpdateComponent, GoalDeleteDialogComponent],
  entryComponents: [GoalDeleteDialogComponent]
})
export class PlannerGoalModule {}
