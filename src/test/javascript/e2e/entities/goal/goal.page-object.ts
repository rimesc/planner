import { element, by, ElementFinder } from 'protractor';

export class GoalComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-goal div table .btn-danger'));
    title = element.all(by.css('jhi-goal div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
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

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setSummaryInput(summary) {
        await this.summaryInput.sendKeys(summary);
    }

    async getSummaryInput() {
        return this.summaryInput.getAttribute('value');
    }

    async setCreatedAtInput(createdAt) {
        await this.createdAtInput.sendKeys(createdAt);
    }

    async getCreatedAtInput() {
        return this.createdAtInput.getAttribute('value');
    }

    async setCompletedAtInput(completedAt) {
        await this.completedAtInput.sendKeys(completedAt);
    }

    async getCompletedAtInput() {
        return this.completedAtInput.getAttribute('value');
    }

    async setOrderInput(order) {
        await this.orderInput.sendKeys(order);
    }

    async getOrderInput() {
        return this.orderInput.getAttribute('value');
    }

    async setVisibilitySelect(visibility) {
        await this.visibilitySelect.sendKeys(visibility);
    }

    async getVisibilitySelect() {
        return this.visibilitySelect.element(by.css('option:checked')).getText();
    }

    async visibilitySelectLastOption() {
        await this.visibilitySelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async ownerSelectLastOption() {
        await this.ownerSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async ownerSelectOption(option) {
        await this.ownerSelect.sendKeys(option);
    }

    getOwnerSelect(): ElementFinder {
        return this.ownerSelect;
    }

    async getOwnerSelectedOption() {
        return this.ownerSelect.element(by.css('option:checked')).getText();
    }

    async tagSelectLastOption() {
        await this.tagSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async tagSelectOption(option) {
        await this.tagSelect.sendKeys(option);
    }

    getTagSelect(): ElementFinder {
        return this.tagSelect;
    }

    async getTagSelectedOption() {
        return this.tagSelect.element(by.css('option:checked')).getText();
    }

    async themeSelectLastOption() {
        await this.themeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async themeSelectOption(option) {
        await this.themeSelect.sendKeys(option);
    }

    getThemeSelect(): ElementFinder {
        return this.themeSelect;
    }

    async getThemeSelectedOption() {
        return this.themeSelect.element(by.css('option:checked')).getText();
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class GoalDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-goal-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-goal'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
