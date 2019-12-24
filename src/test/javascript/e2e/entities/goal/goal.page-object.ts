import { element, by, ElementFinder } from 'protractor';

export class GoalComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-goal div table .btn-danger'));
  title = element.all(by.css('jhi-goal div h2#page-heading span')).first();

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

export class GoalUpdatePage {
  pageTitle = element(by.id('jhi-goal-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  summaryInput = element(by.id('field_summary'));
  createdAtInput = element(by.id('field_createdAt'));
  completedAtInput = element(by.id('field_completedAt'));
  orderInput = element(by.id('field_order'));
  visibilitySelect = element(by.id('field_visibility'));
  ownerSelect = element(by.id('field_owner'));
  tagSelect = element(by.id('field_tag'));
  themeSelect = element(by.id('field_theme'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setSummaryInput(summary: string): Promise<void> {
    await this.summaryInput.sendKeys(summary);
  }

  async getSummaryInput(): Promise<string> {
    return await this.summaryInput.getAttribute('value');
  }

  async setCreatedAtInput(createdAt: string): Promise<void> {
    await this.createdAtInput.sendKeys(createdAt);
  }

  async getCreatedAtInput(): Promise<string> {
    return await this.createdAtInput.getAttribute('value');
  }

  async setCompletedAtInput(completedAt: string): Promise<void> {
    await this.completedAtInput.sendKeys(completedAt);
  }

  async getCompletedAtInput(): Promise<string> {
    return await this.completedAtInput.getAttribute('value');
  }

  async setOrderInput(order: string): Promise<void> {
    await this.orderInput.sendKeys(order);
  }

  async getOrderInput(): Promise<string> {
    return await this.orderInput.getAttribute('value');
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

  async tagSelectLastOption(): Promise<void> {
    await this.tagSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async tagSelectOption(option: string): Promise<void> {
    await this.tagSelect.sendKeys(option);
  }

  getTagSelect(): ElementFinder {
    return this.tagSelect;
  }

  async getTagSelectedOption(): Promise<string> {
    return await this.tagSelect.element(by.css('option:checked')).getText();
  }

  async themeSelectLastOption(): Promise<void> {
    await this.themeSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async themeSelectOption(option: string): Promise<void> {
    await this.themeSelect.sendKeys(option);
  }

  getThemeSelect(): ElementFinder {
    return this.themeSelect;
  }

  async getThemeSelectedOption(): Promise<string> {
    return await this.themeSelect.element(by.css('option:checked')).getText();
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

export class GoalDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-goal-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-goal'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
