import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'theme',
                loadChildren: './theme/theme.module#PlannerThemeModule'
            },
            {
                path: 'goal',
                loadChildren: './goal/goal.module#PlannerGoalModule'
            },
            {
                path: 'task',
                loadChildren: './task/task.module#PlannerTaskModule'
            },
            {
                path: 'note',
                loadChildren: './note/note.module#PlannerNoteModule'
            },
            {
                path: 'tag',
                loadChildren: './tag/tag.module#PlannerTagModule'
            },
            {
                path: 'goal',
                loadChildren: './goal/goal.module#PlannerGoalModule'
            },
            {
                path: 'task',
                loadChildren: './task/task.module#PlannerTaskModule'
            },
            {
                path: 'note',
                loadChildren: './note/note.module#PlannerNoteModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PlannerEntityModule {}
