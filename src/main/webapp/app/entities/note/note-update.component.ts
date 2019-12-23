import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { INote, Note } from 'app/shared/model/note.model';
import { NoteService } from './note.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IGoal } from 'app/shared/model/goal.model';
import { GoalService } from 'app/entities/goal/goal.service';

type SelectableEntity = IUser | IGoal;

@Component({
  selector: 'jhi-note-update',
  templateUrl: './note-update.component.html'
})
export class NoteUpdateComponent implements OnInit {
  isSaving = false;

  users: IUser[] = [];

  goals: IGoal[] = [];

  editForm = this.fb.group({
    id: [],
    markdown: [null, [Validators.required]],
    html: [null, [Validators.required]],
    created: [null, [Validators.required]],
    edited: [],
    visibility: [null, [Validators.required]],
    ownerId: [],
    goalId: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected noteService: NoteService,
    protected userService: UserService,
    protected goalService: GoalService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ note }) => {
      this.updateForm(note);

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

  updateForm(note: INote): void {
    this.editForm.patchValue({
      id: note.id,
      markdown: note.markdown,
      html: note.html,
      created: note.created != null ? note.created.format(DATE_TIME_FORMAT) : null,
      edited: note.edited != null ? note.edited.format(DATE_TIME_FORMAT) : null,
      visibility: note.visibility,
      ownerId: note.ownerId,
      goalId: note.goalId
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('plannerApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const note = this.createFromForm();
    if (note.id !== undefined) {
      this.subscribeToSaveResponse(this.noteService.update(note));
    } else {
      this.subscribeToSaveResponse(this.noteService.create(note));
    }
  }

  private createFromForm(): INote {
    return {
      ...new Note(),
      id: this.editForm.get(['id'])!.value,
      markdown: this.editForm.get(['markdown'])!.value,
      html: this.editForm.get(['html'])!.value,
      created: this.editForm.get(['created'])!.value != null ? moment(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      edited: this.editForm.get(['edited'])!.value != null ? moment(this.editForm.get(['edited'])!.value, DATE_TIME_FORMAT) : undefined,
      visibility: this.editForm.get(['visibility'])!.value,
      ownerId: this.editForm.get(['ownerId'])!.value,
      goalId: this.editForm.get(['goalId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INote>>): void {
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
