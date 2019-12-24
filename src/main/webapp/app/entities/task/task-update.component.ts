import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ITask, Task } from 'app/shared/model/task.model';
import { TaskService } from './task.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IGoal } from 'app/shared/model/goal.model';
import { GoalService } from 'app/entities/goal/goal.service';

type SelectableEntity = IUser | IGoal;

@Component({
  selector: 'jhi-task-update',
  templateUrl: './task-update.component.html'
})
export class TaskUpdateComponent implements OnInit {
  isSaving = false;

  users: IUser[] = [];

  goals: IGoal[] = [];

  editForm = this.fb.group({
    id: [],
    summary: [null, [Validators.required, Validators.maxLength(128)]],
    createdAt: [null, [Validators.required]],
    completedAt: [],
    ownerId: [null, Validators.required],
    goalId: [null, Validators.required]
  });

  constructor(
    protected taskService: TaskService,
    protected userService: UserService,
    protected goalService: GoalService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ task }) => {
      this.updateForm(task);

      this.userService
        .query()
        .pipe(
          map((res: HttpResponse<IUser[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IUser[]) => (this.users = resBody));

      this.goalService
        .query()
        .pipe(
          map((res: HttpResponse<IGoal[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IGoal[]) => (this.goals = resBody));
    });
  }

  updateForm(task: ITask): void {
    this.editForm.patchValue({
      id: task.id,
      summary: task.summary,
      createdAt: task.createdAt != null ? task.createdAt.format(DATE_TIME_FORMAT) : null,
      completedAt: task.completedAt != null ? task.completedAt.format(DATE_TIME_FORMAT) : null,
      ownerId: task.ownerId,
      goalId: task.goalId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const task = this.createFromForm();
    if (task.id !== undefined) {
      this.subscribeToSaveResponse(this.taskService.update(task));
    } else {
      this.subscribeToSaveResponse(this.taskService.create(task));
    }
  }

  private createFromForm(): ITask {
    return {
      ...new Task(),
      id: this.editForm.get(['id'])!.value,
      summary: this.editForm.get(['summary'])!.value,
      createdAt:
        this.editForm.get(['createdAt'])!.value != null ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      completedAt:
        this.editForm.get(['completedAt'])!.value != null ? moment(this.editForm.get(['completedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      ownerId: this.editForm.get(['ownerId'])!.value,
      goalId: this.editForm.get(['goalId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITask>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
