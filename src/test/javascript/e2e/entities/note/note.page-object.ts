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
  htmlInput = element(by.id('field_html'));
  createdAtInput = element(by.id('field_createdAt'));
  editedAtInput = element(by.id('field_editedAt'));
  visibilitySelect = element(by.id('field_visibility'));
  ownerSelect = element(by.id('field_owner'));
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

  async setHtmlInput(html: string): Promise<void> {
    await this.htmlInput.sendKeys(html);
  }

  async getHtmlInput(): Promise<string> {
    return await this.htmlInput.getAttribute('value');
  }

  async setCreatedAtInput(createdAt: string): Promise<void> {
    await this.createdAtInput.sendKeys(createdAt);
  }

  async getCreatedAtInput(): Promise<string> {
    return await this.createdAtInput.getAttribute('value');
  }

  async setEditedAtInput(editedAt: string): Promise<void> {
    await this.editedAtInput.sendKeys(editedAt);
  }

  async getEditedAtInput(): Promise<string> {
    return await this.editedAtInput.getAttribute('value');
  }

  async setVisibilitySelect(visibility: string): Promise<void> {
    await this.visibilitySelect.sendKeys(visibility);
  }

  async getVisibilitySelect(): Promise<string> {
    return await this.visibilitySelect.element(by.css('option:checked')).getText();
  }

  async visibilitySelectLastOption(): Promise<void> {
    await this.visibilitySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async ownerSelectLastOption(): Promise<void> {
    await this.ownerSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async ownerSelectOption(option: string): Promise<void> {
    await this.ownerSelect.sendKeys(option);
  }

  getOwnerSelect(): ElementFinder {
    return this.ownerSelect;
  }

  async getOwnerSelectedOption(): Promise<string> {
    return await this.ownerSelect.element(by.css('option:checked')).getText();
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
