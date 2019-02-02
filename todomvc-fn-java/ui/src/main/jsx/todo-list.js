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

/**
 * Todo list for managing todo list items
 */
export default class TodoList extends React.Component {

  constructor(props) {
    super(props);
    this.state = { loading: true };
  }

   componentWillUpdate(nextProps, nextState) {
        nextState.loading = false;
   }

  render() {
    if(this.state.loading) {
        return <div class="loading">Loading items...</div>
    }
    return (
      <ul>
        {this.props.items.map(item => (
          <li class="items" key={item.id}>
             <input class="itemActive" itemId={item.id} name="isDone" type="checkbox" checked={!item.active}
                     onChange={this.handleActiveChange.bind(this)} />
             <span class="itemDescription">{item.description}</span>
             <a class="itemRemove" itemId={item.id} href="#" onClick={this.handleRemove.bind(this)} >&#x2613;</a>
          </li>
        ))}
      </ul>
    );
  }

  handleActiveChange(e) {
    var itemId = e.target.getAttribute('itemId');
    var item = this.props.items.find ( item => { return item.id === itemId })
    item.active = !item.active;
    console.log("Changed active state of "+item.description + " to " + item.active);
    this.props.onActiveChange(item);
  }

  handleRemove(e) {
      var itemId = e.target.getAttribute('itemId');
      var item = this.props.items.find ( item => { return item.id === itemId })
      console.log("Removing item " + item.description);
      this.props.onRemove(item);
  }



}