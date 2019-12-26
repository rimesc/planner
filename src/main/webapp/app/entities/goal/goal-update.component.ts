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
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag/tag.service';
import { ITheme } from 'app/shared/model/theme.model';
import { ThemeService } from 'app/entities/theme/theme.service';

type SelectableEntity = ITag | ITheme;

@Component({
  selector: 'jhi-goal-update',
  templateUrl: './goal-update.component.html'
})
export class GoalUpdateComponent implements OnInit {
  isSaving = false;

  tags: ITag[] = [];

  themes: ITheme[] = [];

  editForm = this.fb.group({
    id: [],
    summary: [null, [Validators.required, Validators.maxLength(128)]],
    order: [null, [Validators.required]],
    completedAt: [],
    tags: [],
    themeId: [null, Validators.required]
  });

  constructor(
    protected goalService: GoalService,
    protected tagService: TagService,
    protected themeService: ThemeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ goal }) => {
      this.updateForm(goal);

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
      order: goal.order,
      completedAt: goal.completedAt != null ? goal.completedAt.format(DATE_TIME_FORMAT) : null,
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
      order: this.editForm.get(['order'])!.value,
      completedAt:
        this.editForm.get(['completedAt'])!.value != null ? moment(this.editForm.get(['completedAt'])!.value, DATE_TIME_FORMAT) : undefined,
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
