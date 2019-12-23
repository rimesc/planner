import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGoal } from 'app/shared/model/goal.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { GoalService } from './goal.service';
import { GoalDeleteDialogComponent } from './goal-delete-dialog.component';

@Component({
  selector: 'jhi-goal',
  templateUrl: './goal.component.html'
})
export class GoalComponent implements OnInit, OnDestroy {
  goals: IGoal[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected goalService: GoalService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.goals = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.goalService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IGoal[]>) => this.paginateGoals(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.goals = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInGoals();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IGoal): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInGoals(): void {
    this.eventSubscriber = this.eventManager.subscribe('goalListModification', () => this.reset());
  }

  delete(goal: IGoal): void {
    const modalRef = this.modalService.open(GoalDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.goal = goal;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
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
