import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { INote } from 'app/shared/model/note.model';
import { NoteService } from './note.service';
import { IUser, UserService } from 'app/core';
import { IGoal } from 'app/shared/model/goal.model';
import { GoalService } from 'app/entities/goal';

@Component({
    selector: 'jhi-note-update',
    templateUrl: './note-update.component.html'
})
export class NoteUpdateComponent implements OnInit {
    note: INote;
    isSaving: boolean;

    users: IUser[];

    goals: IGoal[];
    created: string;
    edited: string;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected noteService: NoteService,
        protected userService: UserService,
        protected goalService: GoalService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ note }) => {
            this.note = note;
            this.created = this.note.created != null ? this.note.created.format(DATE_TIME_FORMAT) : null;
            this.edited = this.note.edited != null ? this.note.edited.format(DATE_TIME_FORMAT) : null;
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

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.note.created = this.created != null ? moment(this.created, DATE_TIME_FORMAT) : null;
        this.note.edited = this.edited != null ? moment(this.edited, DATE_TIME_FORMAT) : null;
        if (this.note.id !== undefined) {
            this.subscribeToSaveResponse(this.noteService.update(this.note));
        } else {
            this.subscribeToSaveResponse(this.noteService.create(this.note));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<INote>>) {
        result.subscribe((res: HttpResponse<INote>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
