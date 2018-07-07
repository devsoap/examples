/*
 * Copyright 2018 Devsoap Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

import React from 'react';
import PropTypes from 'prop-types';

import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import { withStyles } from '@material-ui/core/styles';

/**
 * Currency selection component
 */
class CurrencySelect extends React.Component {

    constructor() {
        super();

        this.state = {
            value: 'EUR'
        }

        this.currencies = [
             ['EUR', '€ (EUR)'],
             ['SEK', 'kr (KRONOR)'],
             ['USD', '$ (US DOLLAR)']
        ];
    }

    componentWillMount() {
        this.setState(Object.assign(this.state, {value: this.props.value}))
    }

    handleChange(event) {
        this.setState(Object.assign(this.state, {value: event.target.value}))
        this.props.onChange(event);
    };

    render() {
        const { classes } = this.props;


        return (
           <div>
                <InputLabel htmlFor={this.props.id + "-input"}>{this.props.label}</InputLabel>
                <Select className={classes.select} required id={this.props.id + "-input"} value={this.state.value} onChange={this.handleChange.bind(this)}>
                      { this.currencies.map(function(currency) {
                            return <MenuItem key={currency[0]} value={currency[0]}>{currency[1]}</MenuItem>
                        })
                      }
                </Select>
            </div>
        );
    }
}

CurrencySelect.propTypes = {
  classes: PropTypes.object.isRequired,
  value: PropTypes.string.isRequired,
};

const styles = theme => ({});
export default withStyles(styles)(CurrencySelect);