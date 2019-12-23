import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IGoal, Goal } from 'app/shared/model/goal.model';
import { GoalService } from './goal.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag/tag.service';
import { ITheme } from 'app/shared/model/theme.model';
import { ThemeService } from 'app/entities/theme/theme.service';

type SelectableEntity = IUser | ITag | ITheme;

@Component({
  selector: 'jhi-goal-update',
  templateUrl: './goal-update.component.html'
})
export class GoalUpdateComponent implements OnInit {
  isSaving = false;

  users: IUser[] = [];

  tags: ITag[] = [];

  themes: ITheme[] = [];

  editForm = this.fb.group({
    id: [],
    summary: [null, [Validators.required, Validators.maxLength(128)]],
    created: [null, [Validators.required]],
    completed: [],
    order: [null, [Validators.required]],
    visibility: [null, [Validators.required]],
    ownerId: [],
    tags: [],
    themeId: []
  });

  constructor(
    protected goalService: GoalService,
    protected userService: UserService,
    protected tagService: TagService,
    protected themeService: ThemeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ goal }) => {
      this.updateForm(goal);

      this.userService
        .query()
        .pipe(
          map((res: HttpResponse<IUser[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IUser[]) => (this.users = resBody));

      this.tagService
        .query()
        .pipe(
          map((res: HttpResponse<ITag[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ITag[]) => (this.tags = resBody));

      this.themeService
        .query()
        .pipe(
          map((res: HttpResponse<ITheme[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ITheme[]) => (this.themes = resBody));
    });
  }

  updateForm(goal: IGoal): void {
    this.editForm.patchValue({
      id: goal.id,
      summary: goal.summary,
      created: goal.created != null ? goal.created.format(DATE_TIME_FORMAT) : null,
      completed: goal.completed != null ? goal.completed.format(DATE_TIME_FORMAT) : null,
      order: goal.order,
      visibility: goal.visibility,
      ownerId: goal.ownerId,
      tags: goal.tags,
      themeId: goal.themeId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const goal = this.createFromForm();
    if (goal.id !== undefined) {
      this.subscribeToSaveResponse(this.goalService.update(goal));
    } else {
      this.subscribeToSaveResponse(this.goalService.create(goal));
    }
  }

  private createFromForm(): IGoal {
    return {
      ...new Goal(),
      id: this.editForm.get(['id'])!.value,
      summary: this.editForm.get(['summary'])!.value,
      created: this.editForm.get(['created'])!.value != null ? moment(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      completed:
        this.editForm.get(['completed'])!.value != null ? moment(this.editForm.get(['completed'])!.value, DATE_TIME_FORMAT) : undefined,
      order: this.editForm.get(['order'])!.value,
      visibility: this.editForm.get(['visibility'])!.value,
      ownerId: this.editForm.get(['ownerId'])!.value,
      tags: this.editForm.get(['tags'])!.value,
      themeId: this.editForm.get(['themeId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGoal>>): void {
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

  getSelected(selectedVals: ITag[], option: ITag): ITag {
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
