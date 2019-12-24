import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IGoal } from 'app/shared/model/goal.model';
import { ITheme } from 'app/shared/model/theme.model';
import { GoalService } from 'app/entities/goal/goal.service';

@Component({
  selector: 'jhi-theme-detail',
  templateUrl: './theme-dashboard.component.html',
  styleUrls: ['./theme-dashboard.component.scss']
})
export class ThemeDashboardComponent implements OnInit {
  theme: ITheme | null = null;
  goals: IGoal[] = [];

  constructor(
    protected dataUtils: JhiDataUtils,
    protected activatedRoute: ActivatedRoute,
    private jhiAlertService: JhiAlertService,
    private goalService: GoalService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ theme }) => {
      this.theme = theme;
      this.loadGoals();
    });
  }

  loadGoals(): void {
    this.goalService
      .query({
        'themeId.equals': this.theme!.id,
        sort: ['order,asc']
      })
      .subscribe(
        (res: HttpResponse<IGoal[]>) => (this.goals = res.body ? res.body : []),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  protected onError(errorMessage: string): void {
    this.jhiAlertService.error(errorMessage);
  }
}
