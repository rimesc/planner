import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'theme',
        loadChildren: () => import('./theme/theme.module').then(m => m.PlannerThemeModule)
      },
      {
        path: 'goal',
        loadChildren: () => import('./goal/goal.module').then(m => m.PlannerGoalModule)
      },
      {
        path: 'task',
        loadChildren: () => import('./task/task.module').then(m => m.PlannerTaskModule)
      },
      {
        path: 'note',
        loadChildren: () => import('./note/note.module').then(m => m.PlannerNoteModule)
      },
      {
        path: 'tag',
        loadChildren: () => import('./tag/tag.module').then(m => m.PlannerTagModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PlannerEntityModule {}
