import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { INote, Note } from 'app/shared/model/note.model';
import { NoteService } from './note.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IGoal } from 'app/shared/model/goal.model';
import { GoalService } from 'app/entities/goal/goal.service';

@Component({
  selector: 'jhi-note-update',
  templateUrl: './note-update.component.html'
})
export class NoteUpdateComponent implements OnInit {
  isSaving = false;

  goals: IGoal[] = [];

  editForm = this.fb.group({
    id: [],
    markdown: [null, [Validators.required]],
    goalId: [null, Validators.required]
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected noteService: NoteService,
    protected goalService: GoalService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ note }) => {
      this.updateForm(note);

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
      goalId: note.goal != null ? note.goal.id : null
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
      goal: this.goals.find(g => g.id === this.editForm.get(['goalId'])!.value)
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

  trackById(index: number, item: IGoal): any {
    return item.id;
  }
}
