import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Theme } from 'app/shared/model/theme.model';
import { ThemeService } from '../entities/theme/theme.service';
import { ThemeResolve } from '../entities/theme/theme.route';
import { ThemeDashboardComponent } from './theme-dashboard.component';
import { GoalDetailComponent, GoalResolve } from '../entities/goal';
import { ITheme } from 'app/shared/model/theme.model';

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
