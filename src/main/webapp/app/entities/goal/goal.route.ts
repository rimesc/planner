import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Goal } from 'app/shared/model/goal.model';
import { GoalService } from './goal.service';
import { GoalComponent } from './goal.component';
import { GoalDetailComponent } from './goal-detail.component';
import { GoalUpdateComponent } from './goal-update.component';
import { GoalDeletePopupComponent } from './goal-delete-dialog.component';
import { IGoal } from 'app/shared/model/goal.model';

@Injectable({ providedIn: 'root' })
export class GoalResolve implements Resolve<IGoal> {
    constructor(private service: GoalService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IGoal> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Goal>) => response.ok),
                map((goal: HttpResponse<Goal>) => goal.body)
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

export const goalPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: GoalDeletePopupComponent,
        resolve: {
            goal: GoalResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'plannerApp.goal.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
