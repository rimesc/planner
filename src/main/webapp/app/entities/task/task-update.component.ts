import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ITask } from 'app/shared/model/task.model';
import { TaskService } from './task.service';
import { IUser, UserService } from 'app/core';
import { IGoal } from 'app/shared/model/goal.model';
import { GoalService } from 'app/entities/goal';

@Component({
    selector: 'jhi-task-update',
    templateUrl: './task-update.component.html'
})
export class TaskUpdateComponent implements OnInit {
    task: ITask;
    isSaving: boolean;

    users: IUser[];

    goals: IGoal[];
    created: string;
    completed: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected taskService: TaskService,
        protected userService: UserService,
        protected goalService: GoalService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ task }) => {
            this.task = task;
            this.created = this.task.created != null ? this.task.created.format(DATE_TIME_FORMAT) : null;
            this.completed = this.task.completed != null ? this.task.completed.format(DATE_TIME_FORMAT) : null;
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.goalService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IGoal[]>) => mayBeOk.ok),
                map((response: HttpResponse<IGoal[]>) => response.body)
            )
            .subscribe((res: IGoal[]) => (this.goals = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.task.created = this.created != null ? moment(this.created, DATE_TIME_FORMAT) : null;
        this.task.completed = this.completed != null ? moment(this.completed, DATE_TIME_FORMAT) : null;
        if (this.task.id !== undefined) {
            this.subscribeToSaveResponse(this.taskService.update(this.task));
        } else {
            this.subscribeToSaveResponse(this.taskService.create(this.task));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ITask>>) {
        result.subscribe((res: HttpResponse<ITask>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackGoalById(index: number, item: IGoal) {
        return item.id;
    }
}
