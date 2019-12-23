import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGoal } from 'app/shared/model/goal.model';
import { GoalService } from './goal.service';

@Component({
    selector: 'jhi-goal-delete-dialog',
    templateUrl: './goal-delete-dialog.component.html'
})
export class GoalDeleteDialogComponent {
    goal: IGoal;

    constructor(protected goalService: GoalService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.goalService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'goalListModification',
                content: 'Deleted an goal'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-goal-delete-popup',
    template: ''
})
export class GoalDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ goal }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(GoalDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.goal = goal;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/goal', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/goal', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
