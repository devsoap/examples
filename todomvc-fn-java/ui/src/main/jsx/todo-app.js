/*
 * Copyright 2019 Devsoap Inc.
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
import React from 'react';
import ReactDOM from 'react-dom';
import TodoList from './todo-list.js'

/**
 * Todo application main application view
 */
class TodoApp extends React.Component {

  constructor(props) {
    super(props);
    this.state = { items: [], filteredItems: [], text: '', filter: 'all' };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleActiveChange = this.handleActiveChange.bind(this);
    this.handleRemove = this.handleRemove.bind(this);
    this.handleFilterChange = this.handleFilterChange.bind(this);
  }

  componentDidMount() {
      fetch("todomvc/items")
        .then(result => { return result.json() })
        .then(json => { this.setState({items: json}) })
        .catch(ex => { console.log('parsing failed', ex) });
  }

  componentWillUpdate(nextProps, nextState) {
        if(nextState.filter === 'all') {
            nextState.filteredItems = nextState.items;
        } else if(nextState.filter === 'active') {
           nextState.filteredItems = nextState.items.filter(item => item.active);
        } else if(nextState.filter === 'completed') {
           nextState.filteredItems = nextState.items.filter(item => !item.active);
        }
  }

  render() {
    return (
      <div class="container">
        <h3>todos</h3>
        <div class="inner-container">
            <header class="itemInput">
                <form onSubmit={this.handleSubmit}>
                  <input
                    id="new-todo"
                    onChange={this.handleChange}
                    value={this.state.text}
                    placeholder="What needs to be done?"
                  />
                </form>
            </header>
            <section class="itemList">
                <TodoList items={this.state.filteredItems} onActiveChange={this.handleActiveChange} onRemove={this.handleRemove} />
            </section>
            <footer class="itemControls">
                <span class="itemsCompleted">{this.state.items.filter(item => item.active).length} items left</span>
                <span class="activeStateFilter">
                    <span filter="all" class={this.state.filter === 'all' ? "stateFilter active" : "stateFilter"} onClick={this.handleFilterChange}>All</span>
                    <span filter="active" class={this.state.filter === 'active' ? "stateFilter active" : "stateFilter"} onClick={this.handleFilterChange}>Active</span>
                    <span filter="completed" class={this.state.filter === 'completed' ? "stateFilter active" : "stateFilter"} onClick={this.handleFilterChange}>Completed</span>
                </span>
            </footer>
        </div>
        <span class="howto">
            To add a new item write the description and press ENTER<br/>
            To delete an item press the X button<br/>
            To filter the list select All | Active | Completed
        </span>
      </div>
    );
  }

  handleChange(e) {
    this.setState({ text: e.target.value });
  }

  handleSubmit(e) {
    e.preventDefault();
    if (!this.state.text.length) {
      return;
    }

    const newItem = {
      description: this.state.text,
    };

    fetch('todomvc/items', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newItem)
    }).then(result => {
       return result.json();
    }).then(json => {
       this.setState( state => ({ items: state.items.concat(json), text: ''}) );
    }).catch(ex => {
      console.log('parsing failed', ex);
    });
  }

  handleActiveChange(newItem) {
    this.setState( state => ({
        text: '',
        items: state.items.map(oldItem => {
            if(oldItem.id === newItem.id) {
                fetch('todomvc/items', {
                      method: 'PUT',
                      headers: { 'Content-Type': 'application/json' },
                      body: JSON.stringify(newItem)
                }).then(result => {
                  return result.json();
               }).then(json => {
                  return json;
               })
            }
            return oldItem;
        })
    }));
  }

  handleRemove(item) {
    fetch('todomvc/items', {
          method: 'DELETE',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(item)
    }).then( result => {
        this.setState(state => ({
            items: state.items.filter(oldItem => oldItem.id != item.id)
        }))
    });
  }

  handleFilterChange(e) {
    var filter = e.target.getAttribute("filter");
    this.setState( state => ({
        filter: filter
    }));
  }

}

ReactDOM.render(<TodoApp />, document.getElementById("todoapp"));