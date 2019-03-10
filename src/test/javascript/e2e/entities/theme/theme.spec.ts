/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ThemeComponentsPage, ThemeDeleteDialog, ThemeUpdatePage } from './theme.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Theme e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let themeUpdatePage: ThemeUpdatePage;
    let themeComponentsPage: ThemeComponentsPage;
    /*let themeDeleteDialog: ThemeDeleteDialog;*/
    const fileNameToUpload = 'logo-jhipster.png';
    const fileToUpload = '../../../../../main/webapp/content/images/' + fileNameToUpload;
    const absolutePath = path.resolve(__dirname, fileToUpload);

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Themes', async () => {
        await navBarPage.goToEntity('theme');
        themeComponentsPage = new ThemeComponentsPage();
        await browser.wait(ec.visibilityOf(themeComponentsPage.title), 5000);
        expect(await themeComponentsPage.getTitle()).to.eq('plannerApp.theme.home.title');
    });

    it('should load create Theme page', async () => {
        await themeComponentsPage.clickOnCreateButton();
        themeUpdatePage = new ThemeUpdatePage();
        expect(await themeUpdatePage.getPageTitle()).to.eq('plannerApp.theme.home.createOrEditLabel');
        await themeUpdatePage.cancel();
    });

    /* it('should create and save Themes', async () => {
        const nbButtonsBeforeCreate = await themeComponentsPage.countDeleteButtons();

        await themeComponentsPage.clickOnCreateButton();
        await promise.all([
            themeUpdatePage.setNameInput('name'),
            themeUpdatePage.setDescriptionInput('description'),
            themeUpdatePage.setAvatarInput(absolutePath),
            themeUpdatePage.setCreatedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            themeUpdatePage.visibilitySelectLastOption(),
            themeUpdatePage.ownerSelectLastOption(),
        ]);
        expect(await themeUpdatePage.getNameInput()).to.eq('name');
        expect(await themeUpdatePage.getDescriptionInput()).to.eq('description');
        expect(await themeUpdatePage.getAvatarInput()).to.endsWith(fileNameToUpload);
        expect(await themeUpdatePage.getCreatedAtInput()).to.contain('2001-01-01T02:30');
        await themeUpdatePage.save();
        expect(await themeUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await themeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });*/

    /* it('should delete last Theme', async () => {
        const nbButtonsBeforeDelete = await themeComponentsPage.countDeleteButtons();
        await themeComponentsPage.clickOnLastDeleteButton();

        themeDeleteDialog = new ThemeDeleteDialog();
        expect(await themeDeleteDialog.getDialogTitle())
            .to.eq('plannerApp.theme.delete.question');
        await themeDeleteDialog.clickOnConfirmButton();

        expect(await themeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
