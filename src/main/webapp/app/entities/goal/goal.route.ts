import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IGoal, Goal } from 'app/shared/model/goal.model';
import { GoalService } from './goal.service';
import { GoalComponent } from './goal.component';
import { GoalDetailComponent } from './goal-detail.component';
import { GoalUpdateComponent } from './goal-update.component';

@Injectable({ providedIn: 'root' })
export class GoalResolve implements Resolve<IGoal> {
  constructor(private service: GoalService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGoal> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((goal: HttpResponse<Goal>) => {
          if (goal.body) {
            return of(goal.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Goal());
  }
}

export const goalRoute: Routes = [
  {
    path: '',
    component: GoalComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'plannerApp.goal.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: GoalDetailComponent,
    resolve: {
      goal: GoalResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'plannerApp.goal.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: GoalUpdateComponent,
    resolve: {
      goal: GoalResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'plannerApp.goal.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: GoalUpdateComponent,
    resolve: {
      goal: GoalResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'plannerApp.goal.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
