import { element, by, ElementFinder } from 'protractor';

export class NoteComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-note div table .btn-danger'));
    title = element.all(by.css('jhi-note div h2#page-heading span')).first();

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

export class NoteUpdatePage {
    pageTitle = element(by.id('jhi-note-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    markdownInput = element(by.id('field_markdown'));
    htmlInput = element(by.id('field_html'));
    createdInput = element(by.id('field_created'));
    editedInput = element(by.id('field_edited'));
    visibilitySelect = element(by.id('field_visibility'));
    ownerSelect = element(by.id('field_owner'));
    goalSelect = element(by.id('field_goal'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setMarkdownInput(markdown) {
        await this.markdownInput.sendKeys(markdown);
    }

    async getMarkdownInput() {
        return this.markdownInput.getAttribute('value');
    }

    async setHtmlInput(html) {
        await this.htmlInput.sendKeys(html);
    }

    async getHtmlInput() {
        return this.htmlInput.getAttribute('value');
    }

    async setCreatedInput(created) {
        await this.createdInput.sendKeys(created);
    }

    async getCreatedInput() {
        return this.createdInput.getAttribute('value');
    }

    async setEditedInput(edited) {
        await this.editedInput.sendKeys(edited);
    }

    async getEditedInput() {
        return this.editedInput.getAttribute('value');
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

    async goalSelectLastOption() {
        await this.goalSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async goalSelectOption(option) {
        await this.goalSelect.sendKeys(option);
    }

    getGoalSelect(): ElementFinder {
        return this.goalSelect;
    }

    async getGoalSelectedOption() {
        return this.goalSelect.element(by.css('option:checked')).getText();
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

export class NoteDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-note-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-note'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
