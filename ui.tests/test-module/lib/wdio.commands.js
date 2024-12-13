/*
 *  Copyright 2020 Adobe Systems Incorporated
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
const fs = require('fs');
const path = require('path');
const request = require('request-promise');
const url = require('url');
const config = require('./config');
const commons = require('./commons');
const errors = require('request-promise/errors');

const AEM_SITES_PATH = '/sites.html';

browser.addCommand('AEMLogin', function (username, password) {
    // Check presence of local sign-in Accordion
    if ($('[class*="Accordion"] form').isExisting()) {
        try {
            $('#username').setValue(username);
        } catch (e) {
            // Form field not interactable, not visible
            // Need to open the Accordion
            $('[class*="Accordion"] button').click();
            browser.pause(500);
        }
    }

    $('#username').setValue(username);
    $('#password').setValue(password);

    $('form [type="submit"]').click();

    $('coral-shell-content').waitForExist(5000);
});

browser.addCommand('AEMForceLogout', function () {
    browser.url('/');

    if (browser.getTitle() != 'AEM Sign In') {
        browser.url('/system/sling/logout.html');
    }

    $('form[name="login"]').waitForExist();
});

/**
 * Close all additional windows which might have been opened by a test.
 */
browser.addCommand('CloseOtherWindows', function () {
    const handles = browser.getWindowHandles();

    if (handles.length <= 1) return;

    // Close all windows except for the first
    for (let i = 1; i < handles.length; i++) {
        browser.switchToWindow(handles[i]);
        browser.closeWindow();
    }

    // Switch back to the first window
    browser.switchToWindow(handles[0]);
});

// Returns file handle to use for file upload component,
// depending on test context (local, Docker or Cloud)
browser.addCommand('getFileHandleForUpload', function (filePath) {
    return browser.call(() => {
        return fileHandle(filePath);
    });
});

browser.addCommand('AEMPathExists', function (baseUrl, path) {
    let options = commons.getAuthenticatedRequestOptions(browser);
    Object.assign(options, {
        method: 'GET',
        uri: url.resolve(baseUrl, path)
    });

    return request(options)
        .then(function () {
            return true;
        })
        .catch(errors.StatusCodeError, function (reason) {
            if (reason.statusCode == 404) {
                return false;
            }
        });
});

browser.addCommand('AEMEditorLoaded', function () {
    // Check for two conditions in the browse JavaScript context.
    // Taken from https://git.corp.adobe.com/CQ/selenium-it-base/blob/45815ecf4c509aefcc9872ad1be4d978875ee81d/src/main/java/com/adobe/qe/selenium/pageobject/EditorPage.java#L191-L197
    browser.waitUntil(() => {
        // eslint-disable-next-line
        return browser.execute(() => window && window.Granite && window.Granite.author && true);
    });

    browser.waitUntil(() => {
        return browser.execute(() => {
            // eslint-disable-next-line
            if (window && window.Granite && window.Granite.author && true) {
                // eslint-disable-next-line
                var ns = window.Granite.author;
                return ns.pageInfo && ns.pageInfo !== null;
            }
            return false;
        });
    });
});

/**
 * Delete a page at the given path.
 */
browser.addCommand('AEMDeletePage', function (path) {
    let options = commons.getAuthenticatedRequestOptions(browser);
    Object.assign(options, {
        formData: {
            cmd: 'deletePage',
            path,
            force: 'true',
            _charset_: 'utf-8'
        }
    });

    return request.post(url.resolve(config.aem.author.base_url, '/bin/wcmcommand'), options);
});

browser.addCommand('AEMSitesSetView', function (type) {
    if (!Object.values(commons.AEMSitesViewTypes).includes(type)) {
        throw new Error(`View type ${type} is not supported`);
    }

    browser.url(AEM_SITES_PATH);

    browser.setCookies({
        name: 'cq-sites-pages-pages',
        value: type
    });

    browser.refresh();
});

/**
 * Create a new page.
 */
browser.addCommand('AEMCreatePage', function (pageOptions) {
    let options = commons.getAuthenticatedRequestOptions(browser);
    const { title, name, parent, template } = pageOptions;
    Object.assign(options, {
        formData: {
            './jcr:title': title,
            pageName: name,
            parentPath: parent,
            template,
            _charset_: 'utf-8'
        }
    });

    return request.post(
        url.resolve(config.aem.author.base_url, '/libs/wcm/core/content/sites/createpagewizard/_jcr_content'),
        options
    );
});

browser.addCommand('AEMSitesSetPageTitle', function (parentPath, name, title) {
    let originalTitle = '';

    // Navigate to page parent path
    browser.url(path.posix.join(AEM_SITES_PATH, parentPath));
    // Select sample page in the list
    $(
        `[data-foundation-collection-item-id="${path.posix.join(parentPath, name)}"] [type="checkbox"]`
    ).waitForClickable();
    browser.pause(1000); // Avoid action bar not appearing after clicking checkbox
    $(`[data-foundation-collection-item-id="${path.posix.join(parentPath, name)}"] [type="checkbox"]`).click();
    // Access page properties form
    $('[data-foundation-collection-action*="properties"]').click();
    // Store original title
    originalTitle = $('[name="./jcr:title"]').getValue();
    // Modify title
    $('[name="./jcr:title"]').setValue(title);
    // Submit
    $('[type="submit"]').click();
    // Wait until we get redirected to the Sites console
    $(`[data-foundation-collection-item-id="${path.posix.join(parentPath, name)}"] [type="checkbox"]`).waitForExist();

    return originalTitle;
});

browser.addCommand(
    'waitAndClick',
    function (args) {
        this.waitForDisplayed();
        this.click(args);
    },
    true
);

/**
 * Opens the side panel in the AEM Sites editor if closed.
 */
browser.addCommand('EditorOpenSidePanel', function () {
    const toggleButton = $('button[title="Toggle Side Panel"]');
    toggleButton.waitForDisplayed();
    const sidePanel = $('#SidePanel.sidepanel-opened');
    if (!sidePanel.isDisplayed()) {
        toggleButton.click();
    }
});

browser.addCommand('GraniteSelectPath', function (pathField, title) {
    expect(pathField).toBeDisplayed();

    // Open picker dialog
    const pickerButton = pathField.$('button[aria-label="Open Selection Dialog"]');
    pickerButton.waitForEnabled();
    pickerButton.click();
    expect($('input[aria-label="Type to search ..."]')).toBeDisplayed();

    // Select node
    const node = $(`div[title="${title}"]`);
    expect(node).toExist();
    if (config.aem.type === 'classic') {
        node.parentElement().parentElement().$('coral-columnview-item-thumbnail').moveTo({ xOffset: 1, yOffset: 1 });
        node.parentElement().parentElement().$('coral-columnview-item-thumbnail').click();
    } else {
        node.parentElement().parentElement().$('coral-checkbox').click();
    }

    // Submit dialog
    const submitButton = $('coral-button-label=Select').parentElement();
    submitButton.waitForEnabled();
    submitButton.click();
});

browser.addCommand('CIFSelectCategory', function (categoryField, categoryName) {
    expect(categoryField).toBeDisplayed();

    // Open picker dialog
    const pickerButton = categoryField.$('button[aria-label="Open Picker"]');
    pickerButton.waitForEnabled();
    pickerButton.click();
    expect($('h3=Add Category')).toBeDisplayed();

    // Select category
    const categoryRow = $(`//div[contains(text(),"${categoryName}")]`);
    expect(categoryRow).toBeClickable();
    categoryRow.click();

    // Submit dialog
    const submitButton = $('span=Add').parentElement();
    submitButton.waitForEnabled();
    submitButton.click();
});

browser.addCommand('CIFSelectProduct', function (productField, productName) {
    expect(productField).toBeDisplayed();

    // Open picker dialog
    const pickerButton = productField.$('button[aria-label="Open Picker"]');
    pickerButton.waitForEnabled();
    pickerButton.click();
    expect($('h3=Add Product')).toBeDisplayed();

    // Select product
    const productRow = $(`//div[contains(text(),"${productName}")]`);
    expect(productRow).toBeClickable();
    productRow.click();

    // Submit dialog
    const submitButton = $('span=Add').parentElement();
    submitButton.waitForEnabled();
    submitButton.click();
});

async function fileHandle(filePath) {
    if (config.upload_url) {
        return fileHandleByUploadUrl(config.upload_url, filePath);
    }
    if (config.shared_folder) {
        return fileHandleBySharedFolder(config.shared_folder, filePath);
    }
    return filePath;
}

function fileHandleBySharedFolder(sharedFolderPath, filePath) {
    const sharedFilePath = path.join(sharedFolderPath, path.basename(filePath));
    fs.copyFileSync(filePath, sharedFilePath);
    return sharedFilePath;
}

function fileHandleByUploadUrl(uploadUrl, filePath) {
    return request.post(uploadUrl, {
        formData: {
            data: {
                value: fs.createReadStream(filePath),
                options: {
                    filename: path.basename(filePath)
                }
            }
        }
    });
}
