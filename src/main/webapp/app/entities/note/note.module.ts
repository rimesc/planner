import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PlannerSharedModule } from 'app/shared/shared.module';
import { NoteComponent } from './note.component';
import { NoteDetailComponent } from './note-detail.component';
import { NoteUpdateComponent } from './note-update.component';
import { NoteDeleteDialogComponent } from './note-delete-dialog.component';
import { noteRoute } from './note.route';

@NgModule({
  imports: [PlannerSharedModule, RouterModule.forChild(noteRoute)],
  declarations: [NoteComponent, NoteDetailComponent, NoteUpdateComponent, NoteDeleteDialogComponent],
  entryComponents: [NoteDeleteDialogComponent]
})
export class PlannerNoteModule {}
