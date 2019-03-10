import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { LoginModalService, AccountService, Account } from 'app/core';
import { ThemeService } from 'app/entities/theme';
import { ITheme } from 'app/shared/model/theme.model';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    themes: ITheme[];
    maxCards: number;

    constructor(
        private accountService: AccountService,
        private loginModalService: LoginModalService,
        private themeService: ThemeService,
        private jhiAlertService: JhiAlertService,
        protected dataUtils: JhiDataUtils,
        private eventManager: JhiEventManager
    ) {
        this.themes = [];
        this.maxCards = 6; // ideally a multiple of 6 as there are 1, 2 or 3 columns depending on screen size
    }

    ngOnInit() {
        this.accountService.identity().then((account: Account) => {
            this.account = account;
            this.loadThemes();
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.accountService.identity().then(account => {
                this.account = account;
                this.loadThemes();
            });
        });
    }

    isAuthenticated() {
        return this.accountService.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    loadThemes() {
        this.themeService
            .query({
                page: 0,
                size: 6,
                sort: ['id,asc']
            })
            .subscribe((res: HttpResponse<ITheme[]>) => (this.themes = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
