import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { INote } from 'app/shared/model/note.model';
import { AccountService } from 'app/core';
import { NoteService } from './note.service';

@Component({
    selector: 'jhi-note',
    templateUrl: './note.component.html'
})
export class NoteComponent implements OnInit, OnDestroy {
    notes: INote[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected noteService: NoteService,
        protected jhiAlertService: JhiAlertService,
        protected dataUtils: JhiDataUtils,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.noteService
            .query()
            .pipe(
                filter((res: HttpResponse<INote[]>) => res.ok),
                map((res: HttpResponse<INote[]>) => res.body)
            )
            .subscribe(
                (res: INote[]) => {
                    this.notes = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInNotes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: INote) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInNotes() {
        this.eventSubscriber = this.eventManager.subscribe('noteListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
