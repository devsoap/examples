
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
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
import MenuItem from '@material-ui/core/MenuItem';
import TextField from '@material-ui/core/TextField';
import Card from '@material-ui/core/Card';
import Button from '@material-ui/core/Button';
import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
import { withStyles } from '@material-ui/core/styles';
import Select from '@material-ui/core/Select';
import Modal from '@material-ui/core/Modal';

import CurrencySelect from './currency-select.js';

/**
 * Main application view
 */
class AppView extends React.Component {

    constructor() {
        super();
        this.state = {
            fromAmount: 0,
            toAmount: 0,
            fromCurrency: 'EUR',
            toCurrency: 'SEK'
        };
    }

    render() {
         const { classes } = this.props;
         return (
             <form className={classes.root} noValidate autoComplete="off">

                <FormControl className={classes.formControl} fullWidth>
                    <TextField className={classes.textField} required id="amount" label="Amount:"
                                value={this.fromAmount} onBlur={this.onFromAmountChange.bind(this)} />
                </FormControl>

                <CurrencySelect label="From:" id="from-currency" className={classes.formControl} fullWidth
                                value={this.state.fromCurrency} onChange={this.onFromCurrencyChange.bind(this)} />
                <CurrencySelect label="To:" id="to-currency" className={classes.formControl} fullWidth
                                value={this.state.toCurrency} onChange={this.onToCurrencyChange.bind(this)} />

                 <FormControl className={classes.formControl} fullWidth>
                    <TextField className={classes.textField} required id="amount-converted" label="ConvertedAmount:"
                                value={this.state.toAmount} />
                </FormControl>
             </form>
         );
    }

    onFromCurrencyChange (event) {
        this.setState(Object.assign(this.state, { fromCurrency : event.target.value}))
        this.convert();
    }

    onToCurrencyChange (event) {
        this.setState(Object.assign(this.state, { toCurrency : event.target.value}))
        this.convert();
    }

    onFromAmountChange(event) {
        this.setState(Object.assign(this.state, { fromAmount: event.target.value}))
        this.convert();
    }

    convert() {
        fetch('convert/' + this.state.fromCurrency + '/' + this.state.toCurrency + "/" + this.state.fromAmount).then(result => {
            return result.json();
        }).then(data => {
            this.setState(Object.assign(this.state, {toAmount:data.toAmount}));
        });
    }
};

/**
 * View styles
 */
const styles = theme => ({
  root: {
    display: 'flex',
    flexWrap: 'wrap',
  },
  formControl: {
    margin: theme.spacing.unit,
    minWidth: 120,
  },
  selectEmpty: {
    marginTop: theme.spacing.unit * 2,
  },
  button: {
    margin: theme.spacing.unit,
    minWidth: 120
  },
});

AppView.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(AppView);