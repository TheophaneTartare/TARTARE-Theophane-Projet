import { readFileSync } from 'fs';
import { join } from 'path';

const homeHtml = readFileSync(join(process.cwd(), 'public/home.html'), 'utf-8');

export default class Builder {
  #request;
  #response;
  #status;
  #type;

  constructor(request, response, status, type) {
    this.#request = request;
    this.#response = response;
    this.#status = status;
    this.#type = type;
  }

  get response() {
    return this.#response;
  }

  get request() {
    return this.#request;
  }

  handleRequest(){ 
    this.buildResponse();
  }
 
  buildResponse() {

    this.#response.writeHead(200, { 'Content-Type': 'text/html' });
    this.#response.write(homeHtml);
    this.#response.end();
  }
}