import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { TaskComponentsPage, TaskDeleteDialog, TaskUpdatePage } from './task.page-object';

const expect = chai.expect;

describe('Task e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let taskComponentsPage: TaskComponentsPage;
  let taskUpdatePage: TaskUpdatePage;
  /*let taskDeleteDialog: TaskDeleteDialog;*/

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Tasks', async () => {
    await navBarPage.goToEntity('task');
    taskComponentsPage = new TaskComponentsPage();
    await browser.wait(ec.visibilityOf(taskComponentsPage.title), 5000);
    expect(await taskComponentsPage.getTitle()).to.eq('plannerApp.task.home.title');
  });

  it('should load create Task page', async () => {
    await taskComponentsPage.clickOnCreateButton();
    taskUpdatePage = new TaskUpdatePage();
    expect(await taskUpdatePage.getPageTitle()).to.eq('plannerApp.task.home.createOrEditLabel');
    await taskUpdatePage.cancel();
  });

  /* it('should create and save Tasks', async () => {
    const nbButtonsBeforeCreate = await taskComponentsPage.countDeleteButtons();

    await taskComponentsPage.clickOnCreateButton();
    await promise.all([
      taskUpdatePage.setSummaryInput('summary'),
      taskUpdatePage.setCreatedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      taskUpdatePage.setCompletedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      taskUpdatePage.ownerSelectLastOption(),
      taskUpdatePage.goalSelectLastOption()
    ]);
    expect(await taskUpdatePage.getSummaryInput()).to.eq('summary', 'Expected Summary value to be equals to summary');
    expect(await taskUpdatePage.getCreatedAtInput()).to.contain('2001-01-01T02:30', 'Expected createdAt value to be equals to 2000-12-31');
    expect(await taskUpdatePage.getCompletedAtInput()).to.contain('2001-01-01T02:30', 'Expected completedAt value to be equals to 2000-12-31');
    await taskUpdatePage.save();
    expect(await taskUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await taskComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });*/

  /* it('should delete last Task', async () => {
    const nbButtonsBeforeDelete = await taskComponentsPage.countDeleteButtons();
    await taskComponentsPage.clickOnLastDeleteButton();

    taskDeleteDialog = new TaskDeleteDialog();
    expect(await taskDeleteDialog.getDialogTitle()).to.eq('plannerApp.task.delete.question');
    await taskDeleteDialog.clickOnConfirmButton();

    expect(await taskComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });*/

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
