import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ITheme, Theme } from 'app/shared/model/theme.model';
import { ThemeService } from './theme.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-theme-update',
  templateUrl: './theme-update.component.html'
})
export class ThemeUpdateComponent implements OnInit {
  isSaving = false;

  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(128)]],
    description: [null, [Validators.required, Validators.maxLength(512)]],
    shortName: [null, [Validators.required, Validators.maxLength(32)]],
    avatar: [null, []],
    avatarContentType: [],
    created: [null, [Validators.required]],
    visibility: [null, [Validators.required]],
    ownerId: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected themeService: ThemeService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ theme }) => {
      this.updateForm(theme);

      this.userService
        .query()
        .pipe(
          map((res: HttpResponse<IUser[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IUser[]) => (this.users = resBody));
    });
  }

  updateForm(theme: ITheme): void {
    this.editForm.patchValue({
      id: theme.id,
      name: theme.name,
      description: theme.description,
      shortName: theme.shortName,
      avatar: theme.avatar,
      avatarContentType: theme.avatarContentType,
      created: theme.created != null ? theme.created.format(DATE_TIME_FORMAT) : null,
      visibility: theme.visibility,
      ownerId: theme.ownerId
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

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const theme = this.createFromForm();
    if (theme.id !== undefined) {
      this.subscribeToSaveResponse(this.themeService.update(theme));
    } else {
      this.subscribeToSaveResponse(this.themeService.create(theme));
    }
  }

  private createFromForm(): ITheme {
    return {
      ...new Theme(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      shortName: this.editForm.get(['shortName'])!.value,
      avatarContentType: this.editForm.get(['avatarContentType'])!.value,
      avatar: this.editForm.get(['avatar'])!.value,
      created: this.editForm.get(['created'])!.value != null ? moment(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      visibility: this.editForm.get(['visibility'])!.value,
      ownerId: this.editForm.get(['ownerId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITheme>>): void {
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

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
