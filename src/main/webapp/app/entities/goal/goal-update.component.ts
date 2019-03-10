import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IGoal } from 'app/shared/model/goal.model';
import { GoalService } from './goal.service';
import { IUser, UserService } from 'app/core';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';
import { ITheme } from 'app/shared/model/theme.model';
import { ThemeService } from 'app/entities/theme';

@Component({
    selector: 'jhi-goal-update',
    templateUrl: './goal-update.component.html'
})
export class GoalUpdateComponent implements OnInit {
    goal: IGoal;
    isSaving: boolean;

    users: IUser[];

    tags: ITag[];

    themes: ITheme[];
    createdAt: string;
    completedAt: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected goalService: GoalService,
        protected userService: UserService,
        protected tagService: TagService,
        protected themeService: ThemeService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ goal }) => {
            this.goal = goal;
            this.createdAt = this.goal.createdAt != null ? this.goal.createdAt.format(DATE_TIME_FORMAT) : null;
            this.completedAt = this.goal.completedAt != null ? this.goal.completedAt.format(DATE_TIME_FORMAT) : null;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.tagService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ITag[]>) => mayBeOk.ok),
                map((response: HttpResponse<ITag[]>) => response.body)
            )
            .subscribe((res: ITag[]) => (this.tags = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.themeService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ITheme[]>) => mayBeOk.ok),
                map((response: HttpResponse<ITheme[]>) => response.body)
            )
            .subscribe((res: ITheme[]) => (this.themes = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.goal.createdAt = this.createdAt != null ? moment(this.createdAt, DATE_TIME_FORMAT) : null;
        this.goal.completedAt = this.completedAt != null ? moment(this.completedAt, DATE_TIME_FORMAT) : null;
        if (this.goal.id !== undefined) {
            this.subscribeToSaveResponse(this.goalService.update(this.goal));
        } else {
            this.subscribeToSaveResponse(this.goalService.create(this.goal));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IGoal>>) {
        result.subscribe((res: HttpResponse<IGoal>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackTagById(index: number, item: ITag) {
        return item.id;
    }

    trackThemeById(index: number, item: ITheme) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
