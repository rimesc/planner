import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IGoal } from 'app/shared/model/goal.model';
import { ITheme } from 'app/shared/model/theme.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { GoalService } from 'app/entities/goal/goal.service';

@Component({
  selector: 'jhi-theme-detail',
  templateUrl: './theme-dashboard.component.html',
  styleUrls: ['./theme-dashboard.component.scss']
})
export class ThemeDashboardComponent implements OnInit, OnDestroy {
  theme: ITheme | null;
  goals: IGoal[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected eventManager: JhiEventManager,
    protected parseLinks: JhiParseLinks,
    private goalService: GoalService
  ) {
    this.theme = null;
    this.goals = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
  }

  reset(): void {
    this.page = 0;
    this.goals = [];
    this.loadGoals();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadGoals();
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ theme }) => {
      this.theme = theme;
      this.loadGoals();
      this.registerChangeInGoals();
    });
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  registerChangeInGoals(): void {
    this.eventSubscriber = this.eventManager.subscribe('goalListModification', () => this.reset());
  }

  loadGoals(): void {
    this.goalService
      .query({
        'themeId.equals': this.theme!.id,
        page: this.page,
        size: this.itemsPerPage,
        sort: ['order,asc']
      })
      .subscribe((res: HttpResponse<IGoal[]>) => this.paginateGoals(res.body, res.headers));
  }

  protected paginateGoals(data: IGoal[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.goals.push(data[i]);
      }
    }
  }
}
