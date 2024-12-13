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
import React from 'react';
import { FormattedMessage, useIntl } from 'react-intl';
import { string, number, shape, func, arrayOf, oneOf } from 'prop-types';
import { Trash2 as DeleteIcon } from 'react-feather';

import Price from '@magento/venia-ui/lib/components/Price';
import { useItem } from '@magento/peregrine/lib/talons/MiniCart/useItem';

import ProductOptions from '@magento/venia-ui/lib/components/LegacyMiniCart/productOptions';
import Image from '@magento/venia-ui/lib/components//Image';
import Icon from '@magento/venia-ui/lib/components//Icon';
import { useStyle } from '@magento/venia-ui/lib/classify';
import configuredVariant from '@magento/peregrine/lib/util/configuredVariant';

import defaultClasses from '@magento/venia-ui/lib/components/MiniCart/ProductList/item.css';

const Item = props => {
    const {
        classes: propClasses,
        product,
        id,
        quantity,
        configurable_options,
        handleRemoveItem,
        prices,
        configurableThumbnailSource
    } = props;

    const { formatMessage } = useIntl();
    const classes = useStyle(defaultClasses, propClasses);
    const stockStatusText =
        product.stock_status === 'OUT_OF_STOCK'
            ? formatMessage({
                  id: 'productList.outOfStock',
                  defaultMessage: 'Out-of-stock'
              })
            : '';

    const { isDeleting, removeItem } = useItem({
        id: parseInt(id),
        handleRemoveItem
    });

    const rootClass = isDeleting ? classes.root_disabled : classes.root;
    const configured_variant = configuredVariant(configurable_options, product);

    return (
        <div className={rootClass}>
            <div className={classes.thumbnailContainer}>
                <Image
                    alt={product.name}
                    classes={{
                        root: classes.thumbnail
                    }}
                    width={100}
                    resource={
                        configurableThumbnailSource === 'itself' && configured_variant
                            ? configured_variant.thumbnail.url
                            : product.thumbnail.url
                    }
                />
            </div>
            <div className={classes.name}>{product.name}</div>
            <ProductOptions
                options={configurable_options}
                classes={{
                    options: classes.options
                }}
            />
            <span className={classes.quantity}>
                <FormattedMessage id={'productList.quantity'} defaultMessage="Qty : {qty}" values={{ qty: quantity }} />
            </span>
            <span className={classes.price}>
                <Price currencyCode={prices.price.currency} value={prices.price.value} />
                <FormattedMessage id={'productList.each'} defaultMessage={' ea.'} />
            </span>
            <span className={classes.stockStatus}>{stockStatusText}</span>
            <button onClick={removeItem} type="button" className={classes.deleteButton} disabled={isDeleting}>
                <Icon
                    size={16}
                    src={DeleteIcon}
                    classes={{
                        icon: classes.editIcon
                    }}
                />
            </button>
        </div>
    );
};

export default Item;

Item.propTypes = {
    classes: shape({
        root: string,
        thumbnail: string,
        name: string,
        options: string,
        quantity: string,
        price: string,
        editButton: string,
        editIcon: string
    }),
    product: shape({
        name: string,
        thumbnail: shape({
            url: string
        })
    }),
    id: string,
    quantity: number,
    configurable_options: arrayOf(
        shape({
            id: number,
            option_label: string,
            value_id: number,
            value_label: string
        })
    ),
    handleRemoveItem: func,
    prices: shape({
        price: shape({
            value: number,
            currency: string
        })
    }),
    configured_variant: shape({
        thumbnail: shape({
            url: string
        })
    }),
    configurableThumbnailSource: oneOf(['parent', 'itself'])
};
