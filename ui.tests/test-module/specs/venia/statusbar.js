/*
 *  Copyright 2021 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

const config = require('../../lib/config');
const { OnboardingDialogHandler } = require('../../lib/commons');
const isCloud = process.env.AEM_VERSION !== 'classic';

describe('Catalog Page Status', function () {
    const editor_page = `${config.aem.author.base_url}/editor.html`;
    const product_page = '/content/venia/us/en/products/product-page';
    const specific_page_no_suffix = '/content/venia/us/en/products/category-page/shop-the-look';
    const specific_page_with_suffix = '/content/venia/us/en/products/category-page/shop-the-look.html/shop-the-look';

    let onboardingHdler;

    this.retries(2);

    before(() => {
        // Set window size to desktop
        browser.setWindowSize(1280, 960);

        // AEM Login
        browser.AEMForceLogout();
        browser.url(config.aem.author.base_url);
        browser.AEMLogin(config.aem.author.username, config.aem.author.password);

        // Enable helper to handle onboarding dialog popup
        onboardingHdler = new OnboardingDialogHandler(browser);
        onboardingHdler.enable();
    });

    after(function () {
        // Disable helper to handle onboarding dialog popup
        onboardingHdler.disable();
    });

    it('is shown on template pages', () => {
        browser.url(`${editor_page}${product_page}.html`);
        browser.AEMEditorLoaded();

        expect($('coral-alert-header=Venia Demo Store - Product page')).toBeDisplayed();

        if (isCloud) {
            // actions are not available on 6.5 in general
            expect($('a[data-status-action-id="open-template-page"]')).not.toBeDisplayed();
        }
    });

    it('is shown on specific pages', () => {
        browser.url(`${editor_page}${specific_page_no_suffix}.html`);
        browser.AEMEditorLoaded();

        expect($('coral-alert-header=Shop the look')).toBeDisplayed();

        if (isCloud) {
            // actions are not available on 6.5 in general
            expect($('a[data-status-action-id="open-template-page"]')).not.toBeDisplayed();
        }
    });

    if (isCloud) {
        // due to an issue in the editors getPageInfoLocation function, the status bar is not shown
        // on classic at all with a suffix. This is a known issue, fixed for CS but not yet back
        // ported to 6.5
        it('is shown on specific pages with suffix', () => {
            browser.url(`${editor_page}${specific_page_with_suffix}.html`);
            browser.AEMEditorLoaded();

            expect($('coral-alert-header=Shop the look')).toBeDisplayed();

            const openTemplatePageButton = $('a[data-status-action-id="open-template-page"]');
            expect(openTemplatePageButton).toBeClickable();
        });
    }
});
