/*******************************************************************************
 *
 *    Copyright 2021 Adobe. All rights reserved.
 *    This file is licensed to you under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License. You may obtain a copy
 *    of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software distributed under
 *    the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
 *    OF ANY KIND, either express or implied. See the License for the specific language
 *    governing permissions and limitations under the License.
 *
 ******************************************************************************/
/* eslint-disable react/display-name */
import React from 'react';
import { useOrderRow } from '@magento/peregrine/lib/talons/OrderHistoryPage/useOrderRow';

import OrderRow from '../orderRow';
import render from '../../utils/test-utils';

jest.mock('@magento/peregrine/lib/talons/OrderHistoryPage/useOrderRow');

jest.mock('@magento/venia-ui/lib/classify');
jest.mock('@magento/venia-ui/lib/components/OrderHistoryPage/collapsedImageGallery', () => props => (
    <div componentName="CollapsedImageGallery" {...props} />
));
jest.mock('@magento/venia-ui/lib/components/OrderHistoryPage/orderProgressBar', () => props => (
    <div componentName="OrderProgressBar" {...props} />
));
jest.mock('../OrderDetails', () => props => <div componentName="Order Details" {...props} />);

const mockOrder = {
    billing_address: {
        city: 'Austin',
        country_code: 'US',
        firstname: 'Gooseton',
        lastname: 'Jr',
        postcode: '78759',
        region_id: 'TX',
        street: 'Goose Dr',
        telephone: '9123456789'
    },
    id: 2,
    invoices: [{ id: 1 }],
    items: [
        {
            id: '3',
            product_name: 'Product 3',
            product_sale_price: '$100.00',
            product_sku: 'VA03',
            selected_options: [
                {
                    label: 'Color',
                    value: 'Blue'
                }
            ],
            quantity_ordered: 1
        },
        {
            id: '4',
            product_name: 'Product 4',
            product_sale_price: '$100.00',
            product_sku: 'VP08',
            selected_options: [
                {
                    label: 'Color',
                    value: 'Black'
                }
            ],
            quantity_ordered: 1
        },
        {
            id: '5',
            product_name: 'Product 5',
            product_sale_price: '$100.00',
            product_sku: 'VSW09',
            selected_options: [
                {
                    label: 'Color',
                    value: 'Orange'
                }
            ],
            quantity_ordered: 1
        }
    ],
    number: '000000005',
    order_date: '2020-05-26 18:22:35',
    payment_methods: [
        {
            name: 'Braintree',
            type: 'Credit Card',
            additional_data: [
                {
                    name: 'card_type',
                    value: 'Visa'
                },
                {
                    name: 'last_four',
                    value: '1234'
                }
            ]
        }
    ],
    shipments: [
        {
            id: '1',
            tracking: [
                {
                    carrier: 'Fedex',
                    number: 'FEDEX5885541235452125'
                }
            ]
        }
    ],
    shipping_address: {
        city: 'Austin',
        country_code: 'US',
        firstname: 'Gooseton',
        lastname: 'Jr',
        postcode: '78759',
        region_id: 'TX',
        street: 'Goose Dr',
        telephone: '9123456789'
    },
    shipping_method: 'Free',
    status: 'Complete',
    total: {
        discounts: [
            {
                amount: {
                    currency: 'USD',
                    value: 123
                }
            }
        ],
        grand_total: {
            currency: 'USD',
            value: 1434
        },
        subtotal: {
            currency: 'USD',
            value: 1234
        },
        total_tax: {
            currency: 'USD',
            value: 34
        },
        total_shipping: {
            currency: 'USD',
            value: 12
        }
    }
};

const imagesData = [
    {
        id: 1094,
        sku: 'VA03',
        thumbnail: {
            url: 'https://master-7rqtwti-mfwmkrjfqvbjk.us-4.magentosite.cloud/media/catalog/product/cache/d3ba9f7bcd3b0724e976dc5144b29c7d/v/s/vsw01-rn_main_2.jpg'
        },
        url_key: 'valeria-two-layer-tank',
        url_suffix: '.html'
    },
    {
        id: 1103,
        sku: 'VP08',
        thumbnail: {
            url: 'https://master-7rqtwti-mfwmkrjfqvbjk.us-4.magentosite.cloud/media/catalog/product/cache/d3ba9f7bcd3b0724e976dc5144b29c7d/v/s/vsw01-rn_main_2.jpg'
        },
        url_key: 'chloe-silk-shell',
        url_suffix: '.html'
    },
    {
        id: 1108,
        sku: 'VSW09',
        thumbnail: {
            url: 'https://master-7rqtwti-mfwmkrjfqvbjk.us-4.magentosite.cloud/media/catalog/product/cache/d3ba9f7bcd3b0724e976dc5144b29c7d/v/s/vsw01-rn_main_2.jpg'
        },
        url_key: 'helena-cardigan',
        url_suffix: '.html'
    }
];

test('it renders collapsed order row', () => {
    useOrderRow.mockReturnValue({
        loading: false,
        imagesData,
        isOpen: false,
        handleContentToggle: jest.fn().mockName('handleContentToggle')
    });

    const tree = render(<OrderRow order={mockOrder} />);

    expect(tree.toJSON()).toMatchSnapshot();
});

test('it does not render order details if loading is true', () => {
    useOrderRow.mockReturnValue({
        loading: true,
        imagesData,
        isOpen: false,
        handleContentToggle: jest.fn().mockName('handleContentToggle')
    });

    const tree = render(<OrderRow order={mockOrder} />);

    expect(tree.toJSON()).toMatchSnapshot();
});

test('it renders open order row', () => {
    useOrderRow.mockReturnValue({
        loading: false,
        imagesData,
        isOpen: true,
        handleContentToggle: jest.fn().mockName('handleContentToggle')
    });

    const orderWithShipment = {
        ...mockOrder,
        shipments: [1]
    };
    const tree = render(<OrderRow order={orderWithShipment} />);

    expect(tree.toJSON()).toMatchSnapshot();
});

test('it renders shipped status', () => {
    useOrderRow.mockReturnValue({
        loading: false,
        imagesData,
        isOpen: false,
        handleContentToggle: jest.fn()
    });

    const orderWithInvoice = {
        ...mockOrder,
        invoices: [1]
    };
    const tree = render(<OrderRow order={orderWithInvoice} />);
    const { root } = tree;
    const orderProgressProps = root.findByProps({
        componentName: 'OrderProgressBar'
    }).props;

    expect(orderProgressProps.status).toBe('Delivered');
});

test('it renders delivered status', () => {
    useOrderRow.mockReturnValue({
        loading: false,
        imagesData,
        isOpen: false,
        handleContentToggle: jest.fn()
    });

    const completedOrder = {
        ...mockOrder,
        status: 'Complete'
    };
    const tree = render(<OrderRow order={completedOrder} />);
    const { root } = tree;
    const orderProgressProps = root.findByProps({
        componentName: 'OrderProgressBar'
    }).props;

    expect(orderProgressProps.status).toBe('Delivered');
});
