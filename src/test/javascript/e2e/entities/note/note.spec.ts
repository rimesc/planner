import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { NoteComponentsPage, NoteDeleteDialog, NoteUpdatePage } from './note.page-object';

const expect = chai.expect;

describe('Note e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let noteComponentsPage: NoteComponentsPage;
  let noteUpdatePage: NoteUpdatePage;
  /*let noteDeleteDialog: NoteDeleteDialog;*/

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Notes', async () => {
    await navBarPage.goToEntity('note');
    noteComponentsPage = new NoteComponentsPage();
    await browser.wait(ec.visibilityOf(noteComponentsPage.title), 5000);
    expect(await noteComponentsPage.getTitle()).to.eq('plannerApp.note.home.title');
  });

  it('should load create Note page', async () => {
    await noteComponentsPage.clickOnCreateButton();
    noteUpdatePage = new NoteUpdatePage();
    expect(await noteUpdatePage.getPageTitle()).to.eq('plannerApp.note.home.createOrEditLabel');
    await noteUpdatePage.cancel();
  });

  /* it('should create and save Notes', async () => {
    const nbButtonsBeforeCreate = await noteComponentsPage.countDeleteButtons();

    await noteComponentsPage.clickOnCreateButton();
    await promise.all([
      noteUpdatePage.setMarkdownInput('markdown'),
      noteUpdatePage.setHtmlInput('html'),
      noteUpdatePage.setCreatedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      noteUpdatePage.setEditedAtInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      noteUpdatePage.visibilitySelectLastOption(),
      noteUpdatePage.ownerSelectLastOption(),
      noteUpdatePage.goalSelectLastOption()
    ]);
    expect(await noteUpdatePage.getMarkdownInput()).to.eq('markdown', 'Expected Markdown value to be equals to markdown');
    expect(await noteUpdatePage.getHtmlInput()).to.eq('html', 'Expected Html value to be equals to html');
    expect(await noteUpdatePage.getCreatedAtInput()).to.contain('2001-01-01T02:30', 'Expected createdAt value to be equals to 2000-12-31');
    expect(await noteUpdatePage.getEditedAtInput()).to.contain('2001-01-01T02:30', 'Expected editedAt value to be equals to 2000-12-31');
    await noteUpdatePage.save();
    expect(await noteUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await noteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });*/

  /* it('should delete last Note', async () => {
    const nbButtonsBeforeDelete = await noteComponentsPage.countDeleteButtons();
    await noteComponentsPage.clickOnLastDeleteButton();

    noteDeleteDialog = new NoteDeleteDialog();
    expect(await noteDeleteDialog.getDialogTitle()).to.eq('plannerApp.note.delete.question');
    await noteDeleteDialog.clickOnConfirmButton();

    expect(await noteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });*/

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
