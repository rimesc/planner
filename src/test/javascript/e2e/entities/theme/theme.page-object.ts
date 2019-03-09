import { element, by, ElementFinder } from 'protractor';

export class ThemeComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-theme div table .btn-danger'));
    title = element.all(by.css('jhi-theme div h2#page-heading span')).first();

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

export class ThemeUpdatePage {
    pageTitle = element(by.id('jhi-theme-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    descriptionInput = element(by.id('field_description'));
    avatarInput = element(by.id('file_avatar'));
    createdInput = element(by.id('field_created'));
    visibilitySelect = element(by.id('field_visibility'));
    ownerSelect = element(by.id('field_owner'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    async setDescriptionInput(description) {
        await this.descriptionInput.sendKeys(description);
    }

    async getDescriptionInput() {
        return this.descriptionInput.getAttribute('value');
    }

    async setAvatarInput(avatar) {
        await this.avatarInput.sendKeys(avatar);
    }

    async getAvatarInput() {
        return this.avatarInput.getAttribute('value');
    }

    async setCreatedInput(created) {
        await this.createdInput.sendKeys(created);
    }

    async getCreatedInput() {
        return this.createdInput.getAttribute('value');
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

export class ThemeDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-theme-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-theme'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
