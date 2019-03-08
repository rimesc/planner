/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { GoalComponentsPage, GoalDeleteDialog, GoalUpdatePage } from './goal.page-object';

const expect = chai.expect;

describe('Goal e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let goalUpdatePage: GoalUpdatePage;
    let goalComponentsPage: GoalComponentsPage;
    /*let goalDeleteDialog: GoalDeleteDialog;*/

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Goals', async () => {
        await navBarPage.goToEntity('goal');
        goalComponentsPage = new GoalComponentsPage();
        await browser.wait(ec.visibilityOf(goalComponentsPage.title), 5000);
        expect(await goalComponentsPage.getTitle()).to.eq('plannerApp.goal.home.title');
    });

    it('should load create Goal page', async () => {
        await goalComponentsPage.clickOnCreateButton();
        goalUpdatePage = new GoalUpdatePage();
        expect(await goalUpdatePage.getPageTitle()).to.eq('plannerApp.goal.home.createOrEditLabel');
        await goalUpdatePage.cancel();
    });

    /* it('should create and save Goals', async () => {
        const nbButtonsBeforeCreate = await goalComponentsPage.countDeleteButtons();

        await goalComponentsPage.clickOnCreateButton();
        await promise.all([
            goalUpdatePage.setSummaryInput('summary'),
            goalUpdatePage.setCreatedInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            goalUpdatePage.setCompletedInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            goalUpdatePage.setOrderInput('5'),
            goalUpdatePage.visibilitySelectLastOption(),
            goalUpdatePage.ownerSelectLastOption(),
            // goalUpdatePage.tagSelectLastOption(),
            goalUpdatePage.themeSelectLastOption(),
        ]);
        expect(await goalUpdatePage.getSummaryInput()).to.eq('summary');
        expect(await goalUpdatePage.getCreatedInput()).to.contain('2001-01-01T02:30');
        expect(await goalUpdatePage.getCompletedInput()).to.contain('2001-01-01T02:30');
        expect(await goalUpdatePage.getOrderInput()).to.eq('5');
        await goalUpdatePage.save();
        expect(await goalUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await goalComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });*/

    /* it('should delete last Goal', async () => {
        const nbButtonsBeforeDelete = await goalComponentsPage.countDeleteButtons();
        await goalComponentsPage.clickOnLastDeleteButton();

        goalDeleteDialog = new GoalDeleteDialog();
        expect(await goalDeleteDialog.getDialogTitle())
            .to.eq('plannerApp.goal.delete.question');
        await goalDeleteDialog.clickOnConfirmButton();

        expect(await goalComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
