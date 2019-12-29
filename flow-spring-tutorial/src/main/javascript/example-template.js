import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';

class ExampleTemplateElement extends PolymerElement {

  static get template() {
    return html`
        <style>
            :host { display: inline-block; cursor: pointer; animation: blinker 1s linear infinite;  }
            @keyframes blinker { 50% { opacity: 0; }}
        </style>
        <span id="message">[[message]]</span>
    `;
  }

  static get is() {
      return 'example-template';
  }
}
customElements.define(ExampleTemplateElement.is, ExampleTemplateElement);
