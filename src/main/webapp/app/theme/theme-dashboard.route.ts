import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ThemeResolve } from '../entities/theme/theme.route';
import { ThemeDashboardComponent } from './theme-dashboard.component';
import { GoalDetailComponent } from '../entities/goal/goal-detail.component';
import { GoalResolve } from '../entities/goal/goal.route';

export const themeRoute: Routes = [
  {
    path: 'theme/:id',
    component: ThemeDashboardComponent,
    resolve: {
      theme: ThemeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'plannerApp.theme.home.title'
    },
    canActivate: [UserRouteAccessService],
    children: [
      {
        path: 'goal/:id',
        component: GoalDetailComponent,
        resolve: {
          goal: GoalResolve
        },
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'plannerApp.theme.home.title'
        }
      }
    ]
  }
];
