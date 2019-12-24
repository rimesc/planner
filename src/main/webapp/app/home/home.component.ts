import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { ThemeService } from 'app/entities/theme/theme.service';
import { ITheme } from 'app/shared/model/theme.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  themes: ITheme[];
  maxCards: number;

  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private themeService: ThemeService,
    private jhiAlertService: JhiAlertService,
    protected dataUtils: JhiDataUtils
  ) {
    this.themes = [];
    this.maxCards = 6; // ideally a multiple of 6 as there are 1, 2 or 3 columns depending on screen size
  }

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      if (account) {
        this.loadThemes();
      }
    });
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  loadThemes(): void {
    this.themeService
      .query({
        page: 0,
        size: 6,
        sort: ['id,asc']
      })
      .subscribe(
        (res: HttpResponse<ITheme[]>) => (this.themes = res.body ? res.body : []),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  protected onError(errorMessage: string): void {
    this.jhiAlertService.error(errorMessage);
  }
}
