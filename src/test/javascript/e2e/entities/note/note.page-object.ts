import { element, by, ElementFinder } from 'protractor';

export class NoteComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-note div table .btn-danger'));
  title = element.all(by.css('jhi-note div h2#page-heading span')).first();

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class NoteUpdatePage {
  pageTitle = element(by.id('jhi-note-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  markdownInput = element(by.id('field_markdown'));
  goalSelect = element(by.id('field_goal'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setMarkdownInput(markdown: string): Promise<void> {
    await this.markdownInput.sendKeys(markdown);
  }

  async getMarkdownInput(): Promise<string> {
    return await this.markdownInput.getAttribute('value');
  }

  async goalSelectLastOption(): Promise<void> {
    await this.goalSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async goalSelectOption(option: string): Promise<void> {
    await this.goalSelect.sendKeys(option);
  }

  getGoalSelect(): ElementFinder {
    return this.goalSelect;
  }

  async getGoalSelectedOption(): Promise<string> {
    return await this.goalSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class NoteDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-note-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-note'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
