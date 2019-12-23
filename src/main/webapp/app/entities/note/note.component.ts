import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { INote } from 'app/shared/model/note.model';
import { NoteService } from './note.service';
import { NoteDeleteDialogComponent } from './note-delete-dialog.component';

@Component({
  selector: 'jhi-note',
  templateUrl: './note.component.html'
})
export class NoteComponent implements OnInit, OnDestroy {
  notes?: INote[];
  eventSubscriber?: Subscription;

  constructor(
    protected noteService: NoteService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.noteService.query().subscribe((res: HttpResponse<INote[]>) => {
      this.notes = res.body ? res.body : [];
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInNotes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: INote): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInNotes(): void {
    this.eventSubscriber = this.eventManager.subscribe('noteListModification', () => this.loadAll());
  }

  delete(note: INote): void {
    const modalRef = this.modalService.open(NoteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.note = note;
  }
}
